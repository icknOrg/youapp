package youapp.dataaccess.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import youapp.dataaccess.dao.IReplyDao;
import youapp.dataaccess.dto.ReplyDto;
import youapp.dataaccess.dto.ReplyPairDto;

@Repository
public class JdbcReplyDao implements IReplyDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	/**
	 * Used for more complex database access. JdbcTemplate is thread safe once configured.
	 */
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcReplyInsert;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcReplyInsert = new SimpleJdbcInsert(dataSource)
        									.withTableName("reply");
    }
	
	@Override
	public ReplyDto getById(Long personId, Long questionId) {
		if (personId == null) {
    		throw new IllegalArgumentException("person id must not be null.");
    	}
		if (questionId == null) {
    		throw new IllegalArgumentException("question id must not be null.");
    	}
    	String sql = "select * from reply where id_person = ? and id_question = ?";
    	return this.simpleJdbcTemplate.queryForObject(sql, new ReplyMapper(), personId, questionId);
	}
	
	@Override
	public List<ReplyDto> getByIds(final Long personId, final List<Long> questionIds) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (questionIds == null) {
			throw new IllegalArgumentException("List of question ids must not be null.");
		}
		if (questionIds.isEmpty()) {
			throw new IllegalArgumentException("List of question ids must not be empty.");
		}
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// Prepare parameters / placeholders for question ids.
				StringBuilder builder = new StringBuilder();
				for (int i=0; i<questionIds.size(); i++) {
					if (questionIds.get(i) != null) {
						builder.append("?");
						if (i+1 < questionIds.size()) {
							builder.append(",");
						}
					}
				}
				// Prepare select string.
				String sql = String.format("select * from reply where id_person = ? and id_question in (%s)", builder.toString());
				PreparedStatement selectReplies = con.prepareStatement(sql);
				selectReplies.setLong(1, personId);
				int parameterIndex = 2;
				for (int i=0; i<questionIds.size(); i++) {
					if (questionIds.get(i) != null) {
						selectReplies.setLong(parameterIndex, questionIds.get(i));
						parameterIndex = parameterIndex + 1;
					}
				}
				return selectReplies;
			}
		};
		return this.jdbcTemplate.query(creator, new ReplyMapper());
	}
	
	@Override
	public List<ReplyDto> getByPerson(Long personId, Boolean skipped, Boolean inPrivate, Boolean critical) {
		if (personId == null) {
    		throw new IllegalArgumentException("person id must not be null.");
    	}
		String sql = "select * from reply where id_person = ?";
		if (skipped != null) {
			sql = skipped ? (sql + " and skipped = 1") : (sql + " and skipped = 0");
		}
		if (inPrivate != null) {
			sql = inPrivate ? (sql + " and private = 1") : (sql + " and private = 0");
		}
		if (critical != null) {
			sql = critical ? (sql + " and critical = 1") : (sql + " and critical = 0");
		}
    	return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), personId);
	}
	
	@Override
	public List<ReplyDto> getByPerson(Long personId, int offset, int resultSize) {
		if (personId == null) {
    		throw new IllegalArgumentException("person id must not be null.");
    	}
		if (offset < 0) {
			throw new IllegalArgumentException("Offset must be greater than or equals zero.");
		}
		if (resultSize <= 0) {
			throw new IllegalArgumentException("Result size must be greater than zero.");
		}
    	String sql = "select * from reply where id_person = ? limit ?,?";
    	return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), personId, offset, resultSize);
	}
	
	@Override
	public List<ReplyDto> getByQuestion(Long questionId) {
		if (questionId == null) {
    		throw new IllegalArgumentException("question id must not be null.");
    	}
    	String sql = "select * from reply where id_question = ?";
    	return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), questionId);
	}
	
	@Override
	public List<ReplyDto> getByQuestion(Long questionId, int offset, int resultSize) {
		if (questionId == null) {
    		throw new IllegalArgumentException("question id must not be null.");
    	}
		if (offset < 0) {
			throw new IllegalArgumentException("Offset must be greater than or equals zero.");
		}
		if (resultSize <= 0) {
			throw new IllegalArgumentException("Result size must be greater than zero.");
		}
    	String sql = "select * from reply where id_question = ? limit ?,?";
    	return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), questionId, offset, resultSize);
	}
	
	@Override
	public List<ReplyDto> getByDate(Date date) {
		if (date == null) {
    		throw new IllegalArgumentException("Parameter must not be null.");
    	}
		Calendar dateFilter = Calendar.getInstance();
		dateFilter.setTime(date);
		
    	String sql = "select * from reply where year(last_update) = ? and month(last_update) = ? and day(last_update) = ?";
    	return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), dateFilter.get(Calendar.YEAR),
    			(dateFilter.get(Calendar.MONTH) + 1), dateFilter.get(Calendar.DATE));
	}
	
	@Override
	public List<ReplyDto> getByDate(Date date, int offset, int resultSize) {
		if (date == null) {
    		throw new IllegalArgumentException("Parameter must not be null.");
    	}
		Calendar dateFilter = Calendar.getInstance();
		dateFilter.setTime(date);
		
    	String sql = "select * from reply where year(last_update) = ? and month(last_update) = ? and day(last_update) = ? limit ?,?";
    	return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), dateFilter.get(Calendar.YEAR),
    			(dateFilter.get(Calendar.MONTH) + 1), dateFilter.get(Calendar.DATE), offset, resultSize);
	}
	
	@Override
	public List<ReplyDto> getByDate(Long personId, Date date) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (date == null) {
    		throw new IllegalArgumentException("Date must not be null.");
    	}
		Calendar dateFilter = Calendar.getInstance();
		dateFilter.setTime(date);
		
    	String sql = "select * from reply where id_person = ? and year(last_update) = ? " +
    			"and month(last_update) = ? and day(last_update) = ?";
    	return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), personId, dateFilter.get(Calendar.YEAR),
    			(dateFilter.get(Calendar.MONTH) + 1), dateFilter.get(Calendar.DATE));
	}
	
	@Override
	public Long getNumberOfRepliesByQuestion(Long questionId, Boolean skipped, Boolean inPrivate, Boolean critical) {
		if (questionId == null) {
			throw new IllegalArgumentException("question id must not be null.");
		}
		String sql = "select count(id_person) from reply where id_question = ?";
		if (skipped != null) {
			sql = skipped ? (sql + " and skipped = 1") : (sql + " and skipped = 0");
		}
		if (inPrivate != null) {
			sql = inPrivate ? (sql + " and private = 1") : (sql + " and private = 0");
		}
		if (critical != null) {
			sql = critical ? (sql + " and critical = 1") : (sql + " and critical = 0");
		}
		return simpleJdbcTemplate.queryForLong(sql, questionId);
	}
	
	@Override
	public Long getNumberOfRepliesByPerson(Long personId, Boolean skipped, Boolean inPrivate, Boolean critical) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		String sql = "select count(id_question) from reply where id_person = ?";
		if (skipped != null) {
			sql = skipped ? (sql + " and skipped = 1") : (sql + " and skipped = 0");
		}
		if (inPrivate != null) {
			sql = inPrivate ? (sql + " and private = 1") : (sql + " and private = 0");
		}
		if (critical != null) {
			sql = critical ? (sql + " and critical = 1") : (sql + " and critical = 0");
		}
		return simpleJdbcTemplate.queryForLong(sql, personId);
	}
	
	@Override
	public List<ReplyPairDto> getCommonRepliesByPair(Long personIdA, Long personIdB) {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		
		String sql = "select * from reply where id_person = ? and skipped = 0 and id_question in (select id_question from reply where id_person = ? and skipped = 0) order by id_question asc";
		
		List<ReplyDto> repliesA = this.simpleJdbcTemplate.query(sql, new ReplyMapper(), personIdA, personIdB);
		List<ReplyDto> repliesB = this.simpleJdbcTemplate.query(sql, new ReplyMapper(), personIdB, personIdA);
		List<ReplyPairDto> replyPairs = new LinkedList<ReplyPairDto>();
		if (repliesA.size() != repliesB.size()) {
			return replyPairs;
		}
		Iterator<ReplyDto> itA = repliesA.iterator();
		Iterator<ReplyDto> itB = repliesB.iterator();
		while(itA.hasNext()) {
			if (itB.hasNext()) {
				ReplyDto replyA = itA.next();
				ReplyDto replyB = itB.next();
				if (replyA.getQuestionId() == replyB.getQuestionId()) {
					replyPairs.add(new ReplyPairDto(replyA, replyB));
				}
			}
		}
		return replyPairs;
	}
	
	@Override
	public List<ReplyDto> getMostRecentByDays(int days, boolean publicOnly) {
		if (days < 0) {
			throw new IllegalArgumentException("Parameter must be greater or equals zero.");
		}
		String sql = null;
		if (publicOnly) {
			sql = "select * from reply where private = ? and date_sub(curdate(), interval ? day) <= last_update";
			return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), false, days);
		} else {
			sql = "select * from reply where date_sub(curdate(), interval ? day) <= last_update";
			return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), days);
		}
	}
	
	@Override
	public List<ReplyDto> getMostRecentBySize(int resultSize, boolean publicOnly) {
		if (resultSize <= 0) {
			throw new IllegalArgumentException("Result size must be greater than zero.");
		}
		String sql = null;
		if (publicOnly) {
			sql = "select * from reply where private = ? order by last_update desc limit ?,?";
			return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), false, 0, resultSize);
		} else {
			sql = "select * from reply order by last_update desc limit ?,?";
			return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), 0, resultSize);
		}
	}
	
	@Override
	public List<ReplyDto> getAll(int offset, int resultSize) {
		String sql = "select * from reply limit ?,?";
    	return this.simpleJdbcTemplate.query(sql, new ReplyMapper(), offset, resultSize);
	}
	
	@Override
	public boolean exists(Long personId, Long questionId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (questionId == null) {
			throw new IllegalArgumentException("question id must not be null.");
		}
		String sql = "select id_person from reply where id_person = ? and id_question = ?";
		List<Long> ids = this.simpleJdbcTemplate.query(sql, new MyLongMapper(), personId, questionId);
		return (!(ids.isEmpty() || (ids.size() > 1)));
	}
	
	@Override
	public void create(ReplyDto reply) {
		if (reply == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (reply.getPersonId() == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (reply.getQuestionId() == null) {
			throw new IllegalArgumentException("question id must not be null.");
		}
		
		// Check whether reply already exsits.
		if (exists(reply.getPersonId(), reply.getQuestionId())) {
			return;
		}
		
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(17);
		parameters.put("id_person", reply.getPersonId());
		parameters.put("id_question", reply.getQuestionId());
		parameters.put("skipped", reply.getSkipped());
		parameters.put("private", reply.getInPrivate());
		parameters.put("critical", reply.getCritical());
		parameters.put("last_update", reply.getLastUpdate());
		parameters.put("importance", reply.getImportance());
		parameters.put("explanation", reply.getExplanation());
		parameters.put("answer_a", reply.getAnswerA());
		parameters.put("answer_b", reply.getAnswerB());
		parameters.put("answer_c", reply.getAnswerC());
		parameters.put("answer_d", reply.getAnswerD());
		parameters.put("answer_e", reply.getAnswerE());
		//TODO put different values for expected ..after implementation
		parameters.put("expected_a", reply.getAnswerA());
		parameters.put("expected_b", reply.getAnswerB());
		parameters.put("expected_c", reply.getAnswerC());
		parameters.put("expected_d", reply.getAnswerD());
		parameters.put("expected_e", reply.getAnswerE());
        
        // Execute.
        this.jdbcReplyInsert.execute(parameters);
	}
	
	@Override
	public void update(ReplyDto reply) {
		if (reply == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (reply.getPersonId() == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (reply.getQuestionId() == null) {
			throw new IllegalArgumentException("question id must not be null.");
		}
		
        String sql = "update reply set skipped = ?," +
        		" private = ?," +
        		" critical = ?," +
        		" last_update = ?," +
        		" importance = ?," +
        		" explanation = ?," + 
        		" answer_a = ?," +
        		" answer_b = ?," +
        		" answer_c = ?," +
        		" answer_d = ?," +
        		" answer_e = ?, " +
        		" expected_a = ?," +
        		" expected_b = ?," +
        		" expected_c = ?," +
        		" expected_d = ?," +
        		" expected_e = ?" +
        		" where id_person = ?" +
        		" and id_question = ?";
        
        this.simpleJdbcTemplate.update(sql, 
        		reply.getSkipped(),
        		reply.getInPrivate(),
        		reply.getCritical(),
        		reply.getLastUpdate(),
        		reply.getImportance(), 
        		reply.getExplanation(),
        		reply.getAnswerA(), 
        		reply.getAnswerB(),
        		reply.getAnswerC(),
        		reply.getAnswerD(),
        		reply.getAnswerE(),
        		//TODO put different values for expected ..after implementation
        		reply.getAnswerA(), 
        		reply.getAnswerB(),
        		reply.getAnswerC(),
        		reply.getAnswerD(),
        		reply.getAnswerE(),
        		reply.getPersonId(),
        		reply.getQuestionId());
	}
	
	@Override
	public void delete(Long personId, Long questionId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (questionId == null) {
			throw new IllegalArgumentException("question id must not be null.");
		}
		this.simpleJdbcTemplate.update("delete from reply where id_person = ? and id_question = ?", personId, questionId);
	}
	
	@Override
	public void deletePersonReplies(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		this.simpleJdbcTemplate.update("delete from reply where id_person = ?", personId);
	}

	@Override
	public void deleteQuestionReplies(Long questionId) {
		if (questionId == null) {
			throw new IllegalArgumentException("question id must not be null.");
		}
		this.simpleJdbcTemplate.update("delete from reply where id_question = ?", questionId);
	}
	
	/**
	 * Maps a database row to a reply data transfer object.
	 * @author neme
	 *
	 */
	
	//TODO add expected columns
	private static final class ReplyMapper implements RowMapper<ReplyDto> {

	    public ReplyDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	ReplyDto reply = new ReplyDto();
	    	reply.setPersonId(rs.getLong("id_person"));
	    	if (rs.wasNull()) {
	    		reply.setPersonId(null);
	    	}
	    	reply.setQuestionId(rs.getLong("id_question"));
	    	if (rs.wasNull()) {
	    		reply.setQuestionId(null);
	    	}
	    	reply.setSkipped(rs.getBoolean("skipped"));
	    	if (rs.wasNull()) {
	    		reply.setSkipped(null);
	    	}
	    	reply.setInPrivate(rs.getBoolean("private"));
	    	if (rs.wasNull()) {
	    		reply.setInPrivate(null);
	    	}
	    	reply.setCritical(rs.getBoolean("critical"));
	    	if (rs.wasNull()) {
	    		reply.setCritical(null);
	    	}
	    	reply.setLastUpdate(rs.getDate("last_update"));
	    	if (rs.wasNull()) {
	    		reply.setLastUpdate(null);
	    	}
	    	reply.setImportance(rs.getInt("importance"));
	    	if (rs.wasNull()) {
	    		reply.setImportance(null);
	    	}
	    	reply.setExplanation(rs.getString("explanation"));
	    	if (rs.wasNull()) {
	    		reply.setExplanation(null);
	    	}
	    	reply.setAnswerA(rs.getBoolean("answer_a"));
	    	if (rs.wasNull()) {
	    		reply.setAnswerA(null);
	    	}
	    	reply.setAnswerB(rs.getBoolean("answer_b"));
	    	if (rs.wasNull()) {
	    		reply.setAnswerB(null);
	    	}
	    	reply.setAnswerC(rs.getBoolean("answer_c"));
	    	if (rs.wasNull()) {
	    		reply.setAnswerC(null);
	    	}
	    	reply.setAnswerD(rs.getBoolean("answer_d"));
	    	if (rs.wasNull()) {
	    		reply.setAnswerD(null);
	    	}
	    	reply.setAnswerE(rs.getBoolean("answer_e"));
	    	if (rs.wasNull()) {
	    		reply.setAnswerE(null);
	    	}
			return reply;
	    }        
	}
	
	/**
	 * Maps a row containing an id to a Long.
	 * @author neme
	 *
	 */
	private static final class MyLongMapper implements RowMapper<Long> {

		@Override
		public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = rs.getLong(1);
			if (rs.wasNull()) {
				id = null;
			}
			return id;
		}
	}
	
}
