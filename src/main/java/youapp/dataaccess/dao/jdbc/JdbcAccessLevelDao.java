package youapp.dataaccess.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import youapp.dataaccess.dao.IAccessLevelDao;
import youapp.dataaccess.dto.AccessLevelDto;

@Repository
public class JdbcAccessLevelDao implements IAccessLevelDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate jdbcTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcAccessLevelInsert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcAccessLevelInsert = new SimpleJdbcInsert(dataSource)
        								.withTableName("accesslevel")
        								.usingGeneratedKeyColumns("id_access_level");
    }
	
	@Override
	public AccessLevelDto getById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select * from accesslevel where id_access_level = ?";
    	return this.jdbcTemplate.queryForObject(sql, new AccessLevelMapper(), id);
	}

	@Override
	public AccessLevelDto getByDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select * from accesslevel where description = ?";
    	return this.jdbcTemplate.queryForObject(sql, new AccessLevelMapper(), description);
	}
	
	@Override
	public List<AccessLevelDto> getAll() {
		String sql = "select * from accesslevel";
    	return this.jdbcTemplate.query(sql, new AccessLevelMapper());
	}

	@Override
	public void create(AccessLevelDto accessLevel) {
		if (accessLevel == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (accessLevel.getId() != null) {
			throw new IllegalArgumentException("Id must be null.");
		}
		
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(3);
        parameters.put("description", accessLevel.getDescription());
        
        // Execute and set id.
		Number newId = this.jdbcAccessLevelInsert.executeAndReturnKey(parameters);
        accessLevel.setId(newId.intValue());
	}

	@Override
	public void update(AccessLevelDto accessLevel) {
		if (accessLevel == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (accessLevel.getId() == null) {
			throw new IllegalArgumentException("Id must not be null.");
		}
		String sql = "update accesslevel set description = ? where id_access_level = ?";
		this.jdbcTemplate.update(sql, accessLevel.getDescription(), accessLevel.getId());
	}
	
	@Override
	public void delete(AccessLevelDto accessLevel) {
		if (accessLevel == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (accessLevel.getId() == null) {
			throw new IllegalArgumentException("Id must not be null.");
		}
		this.jdbcTemplate.update("delete from accesslevel where id_access_level = ?", accessLevel.getId());
	}

	/**
	 * Maps a database row to a access level data transfer object.
	 * @author neme
	 *
	 */
	private static final class AccessLevelMapper implements RowMapper<AccessLevelDto> {

	    public AccessLevelDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	AccessLevelDto accLevel = new AccessLevelDto();
			accLevel.setId(rs.getInt("id_access_level"));
			if (rs.wasNull()) {
				accLevel.setId(null);
			}
			accLevel.setDescription(rs.getString("description"));
			if (rs.wasNull()) {
				accLevel.setDescription(null);
			}
			return accLevel;
	    }        
	}
	
}
