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

import youapp.dataaccess.dao.IFacebookFriendshipDao;

public class JdbcFacebookFriendshipDao implements IFacebookFriendshipDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate jdbcTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcFriendshipInsert;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcFriendshipInsert = new SimpleJdbcInsert(dataSource)
        										.withTableName("facebookfriendship");
    }
	
	@Override
	public List<Long> getAllFriendsIds(Long sourceId) {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		String sql = "select id_target from facebookfriendship where id_source = ?";
		return jdbcTemplate.query(sql, new MyLongMapper(), sourceId);
	}
	
	@Override
	public List<Long> getCommonFriends(Long sourceIdA, Long sourceIdB) {
		if (sourceIdA == null) {
			throw new IllegalArgumentException("Source id A must not be null.");
		}
		if (sourceIdB == null) {
			throw new IllegalArgumentException("Source id B must not be null.");
		}
		String sql = "select id_target from facebookfriendship where id_source = ? and id_target in (select id_target from facebookfriendship where id_source = ?) order by id_target asc";
		return jdbcTemplate.query(sql, new MyLongMapper(), sourceIdA, sourceIdB);
	}
	
	@Override
	public Integer getNumberOfFriends(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		String sql = "select count(id_target) from facebookfriendship where id_source = ?";
		return jdbcTemplate.queryForInt(sql, personId);
	}
	
	@Override
	public Integer getNumberOfCommonFriends(Long idPersonA, Long idPersonB) {
		if (idPersonA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (idPersonB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		String sql = "select count(id_target) from facebookfriendship where id_source = ? and id_target in (select id_target from facebookfriendship where id_source = ?)";
		return jdbcTemplate.queryForInt(sql, idPersonA, idPersonB);
	}

	@Override
	public Boolean areFriends(Long sourceId, Long targetId) {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (targetId == null) {
			throw new IllegalArgumentException("Target id must not be null.");
		}
		String sql = "select id_source from facebookfriendship where id_source = ? and id_target = ?";
		List<Long> ids = this.jdbcTemplate.query(sql, new MyLongMapper(), sourceId, targetId);
		return (!(ids.isEmpty() || (ids.size() > 1)));
	}

	@Override
	public void create(Long sourceId, Long targetId) {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (targetId == null) {
			throw new IllegalArgumentException("Target id must not be null.");
		}
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("id_source", sourceId);
		parameters.put("id_target", targetId);
		
		// Execute update.
		jdbcFriendshipInsert.execute(parameters);
	}

	@Override
	public void delete(Long sourceId, Long targetId) {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (targetId == null) {
			throw new IllegalArgumentException("Target id must not be null.");
		}
		this.jdbcTemplate.update("delete from facebookfriendship where id_source = ? and id_target = ?", sourceId, targetId);
	}
	
	/**
	 * Maps a row containing an id to a Long.
	 * @author Linda
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
