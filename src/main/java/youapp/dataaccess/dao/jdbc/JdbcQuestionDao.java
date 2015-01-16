package youapp.dataaccess.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.stereotype.Repository;

import youapp.dataaccess.dao.IQuestionDao;
import youapp.dataaccess.dto.QuestionDto;

@Repository
public class JdbcQuestionDao implements IQuestionDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcQuestionInsert;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcQuestionInsert = new SimpleJdbcInsert(dataSource)
        								.withTableName("question")
        								.usingGeneratedKeyColumns("id_question");
    }
	
	@Override
	public QuestionDto getById(Long id) {
		if (id == null) {
    		throw new IllegalArgumentException("Parameter must not be null.");
    	}
    	String sql = "select * from question where id_question = ?";
    	return this.jdbcTemplate.queryForObject(sql, new QuestionMapper(), id);
	}

	@Override
	public List<QuestionDto> getByAuthor(Long personId) {
		if (personId == null) {
    		throw new IllegalArgumentException("Parameter must not be null.");
    	}
    	String sql = "select * from question where author = ?";
    	return this.jdbcTemplate.query(sql, new QuestionMapper(), personId);
	}
	
	@Override
	public List<QuestionDto> getByAuthor(Long personId, Integer offset, Integer resultSize) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (offset == null) {
			throw new IllegalArgumentException("Offset must not be null.");
		}
		if (resultSize == null) {
			throw new IllegalArgumentException("Result size must not be null.");
		}
		if (offset < 0) {
			throw new IllegalArgumentException("Offset must be greater or equal zero.");
		}
		if (resultSize <= 0) {
			throw new IllegalArgumentException("Result size must be greater than zero.");
		}
		
		String sql = "select * from question where author = ? limit ?,?";
		return this.jdbcTemplate.query(sql, new QuestionMapper(), personId, offset, resultSize);
	}

	@Override
	public List<QuestionDto> getByDate(Date date) {
		if (date == null) {
    		throw new IllegalArgumentException("Parameter must not be null.");
    	}
		Calendar dateFilter = Calendar.getInstance();
		dateFilter.setTime(date);
		
    	String sql = "select * from question where year(created) = ? and month(created) = ? and day(created) = ?";
    	return this.jdbcTemplate.query(sql, new QuestionMapper(), dateFilter.get(Calendar.YEAR), dateFilter.get(Calendar.MONTH) + 1, dateFilter.get(Calendar.DATE));
	}

	@Override
	public List<QuestionDto> getMostRecentByDays(int days) {
		if (days < 0) {
			throw new IllegalArgumentException("Parameter must be greater or equals zero.");
		}
		String sql = "select * from question where date_sub(curdate(), interval ? day) <= created";
		return this.jdbcTemplate.query(sql, new QuestionMapper(), days);
	}
	
	@Override
	public List<QuestionDto> getMostRecentBySize(Integer resultSize) {
		if (resultSize == null) {
			throw new IllegalArgumentException("Result size must not be null.");
		}
		if (resultSize <= 0) {
			throw new IllegalArgumentException("Result size must be greater than zero.");
		}
		String sql = "select * from question order by created desc limit ?,?";
		return this.jdbcTemplate.query(sql, new QuestionMapper(), 0, resultSize);
	}
	
	@Override
	public List<QuestionDto> getMatchingQuestionsByPair(Long idPersonA, Long idPersonB) {
		if (idPersonA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (idPersonB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		
		String sql = "select * from question where id_question in " +
				"(select distinct id_question from reply where id_person=? and id_question in (select id_question from reply where id_person=?))";
		return this.jdbcTemplate.query(sql, new QuestionMapper(), idPersonA, idPersonB);
	}
	
	@Override
	public int getNumberOfMatchingQuestionsByPair(Long idPersonA, Long idPersonB, Boolean skipped, Boolean inPrivate, Boolean critical) {
		if (idPersonA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (idPersonB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		
		String sqlA = "select count(id_question) from reply where id_person=?";
		String sqlB = "select id_question from reply where id_person=?";
		if (skipped != null) {
			if (skipped) {
				sqlA = sqlA + " and skipped = 1";
				sqlB = sqlB + " and skipped = 1";
			} else {
				sqlA = sqlA + " and skipped = 0";
				sqlB = sqlB + " and skipped = 0";
			}
		}
		if (inPrivate != null) {
			if (inPrivate) {
				sqlA = sqlA + " and private = 1";
				sqlB = sqlB + " and private = 1";
			} else {
				sqlA = sqlA + " and private = 0";
				sqlB = sqlB + " and private = 0";
			}
		}
		if (critical != null) {
			if (critical) {
				sqlA = sqlA + " and critical = 1";
				sqlB = sqlB + " and critical = 1";
			} else {
				sqlA = sqlA + " and critical = 0";
				sqlB = sqlB + " and critical = 0";
			}
		}
		
		String sql = sqlA + " and id_question in (" + sqlB + ")";
		return this.jdbcTemplate.queryForInt(sql, idPersonA, idPersonB);
	}
	
//	@Override
//	public List<QuestionDto> getAnsweredQuestionsByPerson(Long personId, Boolean includeSkipped, Boolean includePrivate, Boolean includeCritical) {
//		if (personId == null) {
//			throw new IllegalArgumentException("person id must not be null.");
//		}
//		if (includeSkipped == null) {
//			throw new IllegalArgumentException("Include skipped must not be null.");
//		}
//		if (includePrivate == null) {
//			throw new IllegalArgumentException("Include private must not be null.");
//		}
//		if (includeCritical == null) {
//			throw new IllegalArgumentException("Include critical must not be null.");
//		}
//		
//		// Build query string.
//		String sql = "select * from question where author = ? and id_question not in " +
//				"(select id_question from reply where id_person = ?";
//		if (!includeSkipped) {
//			sql = sql + " and skipped = 0";
//		}
//		if (!includePrivate) {
//			sql = sql + " and private = 0";
//		}
//		if (!includeCritical) {
//			sql = sql + " and critical = 0";
//		}
//		sql = sql + ")";
//		// Execute.
//		return this.jdbcTemplate.query(sql, new QuestionMapper(), personId, personId);
//	}
	
	@Override
	public List<QuestionDto> fetchNext(Long personId, int numberOfQuestions) {
		if (personId == null) {
    		throw new IllegalArgumentException("Parameter must not be null.");
    	}
		if (numberOfQuestions <= 0) {
			throw new IllegalArgumentException("Number of questions must be greater than zero.");
		}
		// XXX Think of a more sophisticated way of fetching questions (based on a questions ranking).
    	String sql = "select * from question where id_question not in " +
    			"(select id_question from reply where id_person = ?) " +
    			"limit ?";
    	return this.jdbcTemplate.query(sql, new QuestionMapper(), personId, numberOfQuestions);
	}
	
	@Override
	public Long create(QuestionDto question) {
		if (question == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (question.getId() != null) {
			throw new IllegalArgumentException("Id must be null.");
		}
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(8);
		parameters.put("created", question.getCreated());
		parameters.put("author", question.getAuthor());
		parameters.put("question", question.getQuestion());
		parameters.put("answer_a", question.getAnswerA());
		parameters.put("answer_b", question.getAnswerB());
		parameters.put("answer_c", question.getAnswerC());
		parameters.put("answer_d", question.getAnswerD());
		parameters.put("answer_e", question.getAnswerE());
		// XXX Think about a good solution for character weights.
        
        // Execute and set id.
        Number newId = this.jdbcQuestionInsert.executeAndReturnKey(parameters);
        question.setId(newId.longValue());
        return question.getId();
	}

	@Override
	public void update(QuestionDto question) {
		if (question == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (question.getId() == null) {
			throw new IllegalArgumentException("Id must not be null.");
		}
		
        String sql = "update question set created = ?, author = ?, question = ?, " +
        		"answer_a = ?, answer_b = ?, answer_c = ?, answer_d = ?, answer_e = ? " +
        		"where id_question = ?";
        
        this.jdbcTemplate.update(sql, question.getCreated(), question.getAuthor(), question.getQuestion(),
        		question.getAnswerA(), question.getAnswerB(), question.getAnswerC(), question.getAnswerD(),
        		question.getAnswerE(), question.getId());
	}
	
	@Override
	public void update(final List<QuestionDto> questions) {
		if (questions == null) {
			throw new IllegalArgumentException("questions must not be null.");
		}
		
        String sql = "update question set created = ?, author = ?, question = ?, " +
        		"answer_a = ?, answer_b = ?, answer_c = ?, answer_d = ?, answer_e = ? " +
        		"where id_question = ?";
        
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return questions.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				QuestionDto question = questions.get(index);
				ps.setDate(1, new java.sql.Date(question.getCreated().getTime()));
				ps.setLong(2, question.getAuthor());
				ps.setString(3, question.getQuestion());
				ps.setString(4, question.getAnswerA());
				ps.setString(5, question.getAnswerB());
				ps.setString(6, question.getAnswerC());
				ps.setString(7, question.getAnswerD());
				ps.setString(8, question.getAnswerE());
				ps.setLong(9, question.getId());
			}
        	
        });
        
	}
	
	@Override
	public void delete(Long questionId) {
		if (questionId == null) {
			throw new IllegalArgumentException("question id must not be null.");
		}
		this.jdbcTemplate.update("delete from question where id_question = ?", questionId);
	}
	
	/**
	 * Maps a database row to a question data transfer object.
	 * @author neme
	 *
	 */
	private static final class QuestionMapper implements RowMapper<QuestionDto> {

	    public QuestionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	QuestionDto question = new QuestionDto();
	    	question.setId(rs.getLong("id_question"));
	    	if (rs.wasNull()) {
	    		question.setId(null);
	    	}
	    	question.setCreated(rs.getDate("created"));
	    	if (rs.wasNull()) {
	    		question.setCreated(null);
	    	}
	    	question.setAuthor(rs.getLong("author"));
	    	if (rs.wasNull()) {
	    		question.setAuthor(null);
	    	}
	    	question.setQuestion(rs.getString("question"));
	    	if (rs.wasNull()) {
	    		question.setQuestion(null);
	    	}
	    	question.setAnswerA(rs.getString("answer_a"));
	    	if (rs.wasNull()) {
	    		question.setAnswerA(null);
	    	}
	    	question.setAnswerB(rs.getString("answer_b"));
	    	if (rs.wasNull()) {
	    		question.setAnswerB(null);
	    	}
	    	question.setAnswerC(rs.getString("answer_c"));
	    	if (rs.wasNull()) {
	    		question.setAnswerC(null);
	    	}
	    	question.setAnswerD(rs.getString("answer_d"));
	    	if (rs.wasNull()) {
	    		question.setAnswerD(null);
	    	}
	    	question.setAnswerE(rs.getString("answer_e"));
	    	if (rs.wasNull()) {
	    		question.setAnswerE(null);
	    	}
	    	// XXX Think about a good solution for character weights.
			return question;
	    }        
	}
	
}
