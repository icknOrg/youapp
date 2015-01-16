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

import youapp.dataaccess.dao.IEducationDao;
import youapp.dataaccess.dto.EducationDto;

@Repository
public class JdbcEducationDao implements IEducationDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate jdbcTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcEducationInsert;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcEducationInsert = new SimpleJdbcInsert(dataSource)
        								.withTableName("education")
        								.usingGeneratedKeyColumns("id_education");
    }
	
	@Override
	public EducationDto getById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select * from education where id_education = ?";
    	return this.jdbcTemplate.queryForObject(sql, new EducationMapper(), id);
	}

	@Override
	public List<EducationDto> getByName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select * from education where name = ?";
    	return this.jdbcTemplate.query(sql, new EducationMapper(), name);
	}
	
	@Override
	public List<EducationDto> getAll() {
		String sql = "select * from education";
    	return this.jdbcTemplate.query(sql, new EducationMapper());
	}
	
	@Override
	public void create(EducationDto education) {
		if (education == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (education.getId() != null) {
			throw new IllegalArgumentException("Id must be null.");
		}
		
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("name", education.getName());
        
        // Execute and set id.
		Number newId = this.jdbcEducationInsert.executeAndReturnKey(parameters);
        education.setId(newId.intValue());
	}
	
	@Override
	public void update(EducationDto education) {
		if (education == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (education.getId() == null) {
			throw new IllegalArgumentException("Id must not be null.");
		}
		String sql = "update education set name = ? where id_education = ?";
		this.jdbcTemplate.update(sql, education.getName(), education.getId());
	}
	
	@Override
	public void delete(EducationDto education) {
		if (education == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (education.getId() == null) {
			throw new IllegalArgumentException("Id must not be null.");
		}
		this.jdbcTemplate.update("delete from education where id_education = ?", education.getId());
	}

	/**
	 * Maps a database row to an education data transfer object.
	 * @author neme
	 *
	 */
	private static final class EducationMapper implements RowMapper<EducationDto> {

	    public EducationDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	EducationDto education = new EducationDto();
	    	education.setId(rs.getInt("id_education"));
	    	if (rs.wasNull()) {
	    		education.setId(null);
	    	}
	    	education.setName(rs.getString("name"));
	    	if (rs.wasNull()) {
	    		education.setName(null);
	    	}
			return education;
	    }        
	}
	
}
