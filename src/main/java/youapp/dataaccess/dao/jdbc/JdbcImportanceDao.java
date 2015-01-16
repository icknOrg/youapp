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

import youapp.dataaccess.dao.IImportanceDao;
import youapp.dataaccess.dto.ImportanceDto;

@Repository
public class JdbcImportanceDao implements IImportanceDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate jdbcTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcImportanceInsert;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcImportanceInsert = new SimpleJdbcInsert(dataSource)
        									.withTableName("importance");
    }
	
	@Override
	public ImportanceDto getByWeight(Integer weight) {
		if (weight == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select * from importance where weight = ?";
    	return this.jdbcTemplate.queryForObject(sql, new ImportanceMapper(), weight);
	}
	
	@Override
	public List<ImportanceDto> getByDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select * from importance where description = ?";
    	return this.jdbcTemplate.query(sql, new ImportanceMapper(), description);
	}
	
	@Override
	public List<ImportanceDto> getAll() {
		String sql = "select * from importance";
    	return this.jdbcTemplate.query(sql, new ImportanceMapper());
	}
	
	@Override
	public void create(ImportanceDto importance) {
		if (importance == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (importance.getWeight() == null) {
			throw new IllegalArgumentException("Weight must not be null.");
		}
		
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("weight", importance.getWeight());
        parameters.put("description", importance.getDescription());
        
        // Execute and set id.
		this.jdbcImportanceInsert.execute(parameters);
	}
	
	@Override
	public void update(ImportanceDto importance) {
		if (importance == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (importance.getWeight() == null) {
			throw new IllegalArgumentException("Weight must not be null.");
		}
		String sql = "update importance set description = ? where weight = ?";
		this.jdbcTemplate.update(sql, importance.getDescription(), importance.getWeight());
	}

	@Override
	public void delete(ImportanceDto importance) {
		if (importance == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (importance.getWeight() == null) {
			throw new IllegalArgumentException("Weight must not be null.");
		}
		this.jdbcTemplate.update("delete from importance where weight = ?", importance.getWeight());
	}
	
	/**
	 * Maps a database row to an importance level data transfer object.
	 * @author neme
	 *
	 */
	private static final class ImportanceMapper implements RowMapper<ImportanceDto> {

	    public ImportanceDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	ImportanceDto importance = new ImportanceDto();
	    	importance.setWeight(rs.getInt("weight"));
	    	if (rs.wasNull()) {
	    		importance.setWeight(null);
	    	}
	    	importance.setDescription(rs.getString("description"));
	    	if (rs.wasNull()) {
	    		importance.setDescription(null);
	    	}
			return importance;
	    }        
	}

}
