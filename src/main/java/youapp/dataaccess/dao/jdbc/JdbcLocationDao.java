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

import youapp.dataaccess.dao.ILocationDao;
import youapp.dataaccess.dto.LocationDto;
import youapp.model.Location;

@Repository
public class JdbcLocationDao implements ILocationDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once
	 * configured.
	 */
	private SimpleJdbcTemplate jdbcTemplate;

	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcLocationInsert;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
		this.jdbcLocationInsert = new SimpleJdbcInsert(dataSource)
				.withTableName("location");
	}

	@Override
	public LocationDto getByIdAndName(Integer locationId, String locationName) {
		if (locationId == null) {
			throw new IllegalArgumentException("location id must not be null.");
		}
		if (locationName == null) {
			throw new IllegalArgumentException("location name must not be null.");
		}
		
		String sql = "select * from location where id_location = ? and name = ?";
		return this.jdbcTemplate.queryForObject(sql, new LocationMapper(),
				locationId, locationName);
	}

	@Override
	public List<LocationDto> getByName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select * from location where name = ?";
		return this.jdbcTemplate.query(sql, new LocationMapper(), name);
	}

	@Override
	public boolean exists(Location location) {
		
		if (location.getLongitude() == null) {
			throw new IllegalArgumentException("Longitude must not be null.");
		}
		if (location.getLatitude() == null) {
			throw new IllegalArgumentException("Latitude must not be null.");
		}
		String sql = "select id_location from location where name = ? and id_location = ?";
		List<Long> ids = this.jdbcTemplate.query(sql, new MyLongMapper(), 
				location.getName(), location.getId());
		return (!(ids.isEmpty() || (ids.size() > 1)));
	}

	@Override
	public void create(LocationDto location) {
		if (location == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (location.getId() == null) {
			throw new IllegalArgumentException("location id must not be null.");
		}
		if (location.getName() == null) {
			throw new IllegalArgumentException("name must not be null.");
		}
		if (location.getLongitude() == null) {
			throw new IllegalArgumentException("longitude must not be null.");
		}
		if (location.getLatitude() == null) {
			throw new IllegalArgumentException("latitude must not be null.");
		}
		
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(4);
		parameters.put("id_location", location.getId());
		parameters.put("name", location.getName());
		parameters.put("longitude", location.getLongitude());
		parameters.put("latitude", location.getLatitude());

		// Execute update.
		jdbcLocationInsert.execute(parameters);
	}

	@Override
	public void update(LocationDto location) {
		if (location == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (location.getId() == null) {
			throw new IllegalArgumentException("location id must not be null.");
		}
		if (location.getName() == null) {
			throw new IllegalArgumentException("name must not be null.");
		}
		if (location.getLongitude() == null) {
			throw new IllegalArgumentException("longitude must not be null.");
		}
		if (location.getLatitude() == null) {
			throw new IllegalArgumentException("latitude must not be null.");
		}

		String sql = "update location set name = ?, longitude = ?, latitude = ? "
				+ "where id_location = ?";

		this.jdbcTemplate.update(sql, location.getName(), location.getLongitude(),
				location.getLatitude(), location.getId());
	}

	@Override
	public void delete(LocationDto location) {
		if (location == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (location.getId() == null) {
			throw new IllegalArgumentException("location id must not be null.");
		}
		this.jdbcTemplate.update(
				"delete from location where id_location = ?",
				location.getId());
	}

	/**
	 * Maps a database row to a location data transfer object.
	 * 
	 * @author neme
	 * 
	 */
	private static final class LocationMapper implements RowMapper<LocationDto> {

		public LocationDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			LocationDto location = new LocationDto();
			location.setId(rs.getInt("id_location"));
			if (rs.wasNull()) {
				location.setId(null);
			}
			location.setName(rs.getString("name"));
			if (rs.wasNull()) {
				location.setName(null);
			}
			location.setLongitude(rs.getBigDecimal("longitude"));
			if (rs.wasNull()) {
				location.setLongitude(null);
			}
			location.setLatitude(rs.getBigDecimal("latitude"));
			if (rs.wasNull()) {
				location.setLatitude(null);
			}
			return location;
		}
	}

	/**
	 * Maps a row containing an id to a Long.
	 * 
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
