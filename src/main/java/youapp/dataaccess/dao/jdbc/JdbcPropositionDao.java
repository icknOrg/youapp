package youapp.dataaccess.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import youapp.dataaccess.dao.IPropositionDao;
import youapp.dataaccess.dto.PropositionDto;

public class JdbcPropositionDao implements IPropositionDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate jdbcTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcPropositionInsert;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcPropositionInsert = new SimpleJdbcInsert(dataSource)
        									.withTableName("proposition");
    }
	
	@Override
	public PropositionDto getById(Long personId, Long propositionId) {
		if (personId == null) {
    		throw new IllegalArgumentException("person id must not be null.");
    	}
		if (propositionId == null) {
    		throw new IllegalArgumentException("proposition id must not be null.");
    	}
    	String sql = "select * from proposition where id_person = ? and id_proposition = ?";
    	return this.jdbcTemplate.queryForObject(sql, new PropositionMapper(), personId, propositionId);
	}

	@Override
	public List<PropositionDto> getByPerson(Long personId) {
		if (personId == null) {
    		throw new IllegalArgumentException("person id must not be null.");
    	}
    	String sql = "select * from proposition where id_person = ?";
    	return this.jdbcTemplate.query(sql, new PropositionMapper(), personId);
	}

	@Override
	public List<PropositionDto> getByProposition(Long propositionId) {
		if (propositionId == null) {
    		throw new IllegalArgumentException("proposition id must not be null.");
    	}
    	String sql = "select * from proposition where id_proposition = ?";
    	return this.jdbcTemplate.query(sql, new PropositionMapper(), propositionId);
	}

	@Override
	public List<PropositionDto> getByDate(Date date) {
		if (date == null) {
    		throw new IllegalArgumentException("Parameter must not be null.");
    	}
		Calendar dateFilter = Calendar.getInstance();
		dateFilter.setTime(date);
		
    	String sql = "select * from proposition where year(created) = ? and month(created) = ? and day(created) = ?";
    	return this.jdbcTemplate.query(sql, new PropositionMapper(), dateFilter.get(Calendar.YEAR),
    				(dateFilter.get(Calendar.MONTH) + 1), dateFilter.get(Calendar.DATE));
	}

	@Override
	public List<PropositionDto> getByDate(Long personId, Date date) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (date == null) {
    		throw new IllegalArgumentException("Date must not be null.");
    	}
		Calendar dateFilter = Calendar.getInstance();
		dateFilter.setTime(date);
		
    	String sql = "select * from proposition where id_person = ? and year(created) = ? and month(created) = ? and day(created) = ?";
    	return this.jdbcTemplate.query(sql, new PropositionMapper(), personId, dateFilter.get(Calendar.YEAR),
    			(dateFilter.get(Calendar.MONTH) + 1), dateFilter.get(Calendar.DATE));
	}

	@Override
	public List<PropositionDto> getByDate(Date date, int offset, int resultSize) {
		if (date == null) {
    		throw new IllegalArgumentException("Parameter must not be null.");
    	}
		if (offset < 0) {
			throw new IllegalArgumentException("Offset must be greater or equals zero.");
		}
		if (resultSize <= 0) {
			throw new IllegalArgumentException("Result size must be greater than zero.");
		}
		Calendar dateFilter = Calendar.getInstance();
		dateFilter.setTime(date);
		
    	String sql = "select * from proposition where year(created) = ? and month(created) = ? and day(created) = ? limit ?,?";
    	return this.jdbcTemplate.query(sql, new PropositionMapper(), dateFilter.get(Calendar.YEAR),
    			(dateFilter.get(Calendar.MONTH) + 1), dateFilter.get(Calendar.DATE), offset, resultSize);
	}

	@Override
	public List<PropositionDto> getByDate(Long personId, Date date, int offset, int resultSize) {
		if (date == null) {
    		throw new IllegalArgumentException("Parameter must not be null.");
    	}
		if (offset < 0) {
			throw new IllegalArgumentException("Offset must be greater or equals zero.");
		}
		if (resultSize <= 0) {
			throw new IllegalArgumentException("Result size must be greater than zero.");
		}
		Calendar dateFilter = Calendar.getInstance();
		dateFilter.setTime(date);
		
    	String sql = "select * from proposition where id_person = ? and year(created) = ? and month(created) = ? and day(created) = ? limit ?,?";
    	return this.jdbcTemplate.query(sql, new PropositionMapper(), personId, dateFilter.get(Calendar.YEAR),
    			(dateFilter.get(Calendar.MONTH) + 1), dateFilter.get(Calendar.DATE), offset, resultSize);
	}
	
	@Override
	public List<PropositionDto> getMostRecent(int days) {
		if (days < 0) {
			throw new IllegalArgumentException("Parameter must be greater or equals zero.");
		}
		String sql = "select * from proposition where date_sub(curdate(), interval ? day) <= created";
		return this.jdbcTemplate.query(sql, new PropositionMapper(), days);
	}

	@Override
	public List<PropositionDto> getMostRecent(Long personId, int days) {
		if (days < 0) {
			throw new IllegalArgumentException("Parameter must be greater or equals zero.");
		}
		String sql = "select * from proposition where id_person = ? and date_sub(curdate(), interval ? day) <= created";
		return this.jdbcTemplate.query(sql, new PropositionMapper(), personId, days);
	}
	
	@Override
	public List<PropositionDto> getAll(int offset, int resultSize) {
		if (offset < 0) {
			throw new IllegalArgumentException("Offset must be greater or equals zero.");
		}
		if (resultSize <= 0) {
			throw new IllegalArgumentException("Result size must be greater than zero.");
		}
		String sql = "select * from proposition limit ?,?";
    	return this.jdbcTemplate.query(sql, new PropositionMapper(), offset, resultSize);
	}
	
	@Override
	public void create(PropositionDto proposition) {
		if (proposition == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (proposition.getPersonId() == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (proposition.getPropositionId() == null) {
			throw new IllegalArgumentException("proposition id must not be null.");
		}
		
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(3);
		parameters.put("id_person", proposition.getPersonId());
		parameters.put("id_proposition", proposition.getPropositionId());
		parameters.put("created", proposition.getCreated());
		
		// Execute.
        this.jdbcPropositionInsert.execute(parameters);
	}
	
	@Override
	public void update(PropositionDto proposition) {
		if (proposition == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (proposition.getPersonId() == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (proposition.getPropositionId() == null) {
			throw new IllegalArgumentException("proposition id must not be null.");
		}
		
        String sql = "update proposition set created = ? " +
        		"where id_person = ? and id_proposition = ?";
        
        this.jdbcTemplate.update(sql, proposition.getCreated(), proposition.getPersonId(),
        		proposition.getPropositionId());
	}

	@Override
	public void delete(PropositionDto proposition) {
		if (proposition == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (proposition.getPersonId() == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (proposition.getPropositionId() == null) {
			throw new IllegalArgumentException("proposition id must not be null.");
		}
		this.jdbcTemplate.update("delete from proposition where id_person = ? and id_proposition = ?", proposition.getPersonId(), proposition.getPropositionId());
	}
	
	/**
	 * Maps a database row to a proposition data transfer object.
	 * @author neme
	 *
	 */
	private static final class PropositionMapper implements RowMapper<PropositionDto> {

	    public PropositionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	PropositionDto proposition = new PropositionDto();
	    	proposition.setPersonId(rs.getLong("id_person"));
	    	if (rs.wasNull()) {
	    		proposition.setPersonId(null);
	    	}
	    	proposition.setPropositionId(rs.getLong("id_proposition"));
	    	if (rs.wasNull()) {
	    		proposition.setPropositionId(null);
	    	}
	    	proposition.setCreated(rs.getDate("created"));
	    	if (rs.wasNull()) {
	    		proposition.setCreated(null);
	    	}
			return proposition;
	    }        
	}

}
