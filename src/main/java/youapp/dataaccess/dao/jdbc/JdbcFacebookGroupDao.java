package youapp.dataaccess.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import youapp.dataaccess.dao.IFacebookGroupDao;
import youapp.dataaccess.dto.FacebookGroupDto;
import youapp.dataaccess.dto.PersonFacebookGroupDto;

public class JdbcFacebookGroupDao implements IFacebookGroupDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate jdbcGroupTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcGroupInsert;
	
	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate jdbcAssociationTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcAssociationInsert;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcGroupTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcGroupInsert = new SimpleJdbcInsert(dataSource)
        								.withTableName("facebookgroup")
        								.usingGeneratedKeyColumns("id_group");
        this.jdbcAssociationTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcAssociationInsert = new SimpleJdbcInsert(dataSource)
        								.withTableName("personfacebookgroup");
    }
	
	@Override
	public FacebookGroupDto getById(Long groupId, Boolean fbId) {
		if (groupId == null) {
			throw new IllegalArgumentException("Group id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		String sql = null;
		if (fbId) {
			sql = "select * from facebookgroup where gid = ?";
		} else {
			sql = "select * from facebookgroup where id_group = ?";
		}
    	return this.jdbcGroupTemplate.queryForObject(sql, new GroupMapper(), groupId);
	}
	
	@Override
	public List<FacebookGroupDto> getByPerson(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		String sql = "select * from facebookgroup where id_group in (select id_group from personfacebookgroup where id_person = ?)";
		return this.jdbcGroupTemplate.query(sql, new GroupMapper(), personId);
	}

	@Override
	public List<FacebookGroupDto> getByName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name must not be null.");
		}
		String sql = "select * from facebookgroup where name = ?";
    	return this.jdbcGroupTemplate.query(sql, new GroupMapper(), name);
	}

	@Override
	public List<FacebookGroupDto> getByNetworkId(Long networkId) {
		if (networkId == null) {
			throw new IllegalArgumentException("Network id must not be null.");
		}
		String sql = "select * from facebookgroup where nid = ?";
		return this.jdbcGroupTemplate.query(sql, new GroupMapper(), networkId);
	}

	@Override
	public List<FacebookGroupDto> getByCreator(Long creatorId) {
		if (creatorId == null) {
			throw new IllegalArgumentException("Creator id must not be null.");
		}
		String sql = "select * from facebookgroup where creator = ?";
		return this.jdbcGroupTemplate.query(sql, new GroupMapper(), creatorId);
	}

	@Override
	public List<FacebookGroupDto> getByType(String type) {
		if (type == null) {
			throw new IllegalArgumentException("Type must not be null.");
		}
		String sql = "select * from facebookgroup where type = ?";
		return this.jdbcGroupTemplate.query(sql, new GroupMapper(), type);
	}
	
	@Override
	public List<FacebookGroupDto> getAll() {
		String sql = "select * from facebookgroup";
		return this.jdbcGroupTemplate.query(sql, new GroupMapper());
	}
	
	@Override
	public List<FacebookGroupDto> getCommonGroups(Long personIdA, Long personIdB) {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must nut be null.");
		}
		String sql = "select * from facebookgroup where id_group in (select id_group from personfacebookgroup where id_person = ? and id_group in (select id_group from personfacebookgroup where id_person = ?)) order by id_group asc";
		return this.jdbcGroupTemplate.query(sql, new GroupMapper(), personIdA, personIdB);
	}
	
	@Override
	public Integer getNumberOfGroups(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		String sql = "select count(id_group) from personfacebookgroup where id_person = ?";
		return this.jdbcGroupTemplate.queryForInt(sql, personId);
	}

	@Override
	public Integer getNumberOfCommonGroups(Long personIdA, Long personIdB) {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		String sql = "select count(id_group) from personfacebookgroup where id_person = ? and id_group in (select id_group from personfacebookgroup where id_person = ?)";
		return this.jdbcGroupTemplate.queryForInt(sql, personIdA, personIdB);
	}
	
	@Override
	public Integer getGroupFrequency(Long groupId) {
		if (groupId == null) {
			throw new IllegalArgumentException("Goup id must not be null.");
		}
		String sql = "select count(id_person) from personfacebookgroup where id_group = ?";
		return this.jdbcGroupTemplate.queryForInt(sql, groupId);
	}

	@Override
	public Boolean exists(Long groupId, Boolean fbId) {
		if (groupId == null) {
			throw new IllegalArgumentException("Group id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indiecator must not be null.");
		}
		String sql = null;
		if (fbId) {
			sql = "select id_group from facebookgroup where gid = ?";
		} else {
			sql = "select id_group from facebookgroup where id_group = ?";
		}
		List<Long> ids = this.jdbcGroupTemplate.query(sql, new MyLongMapper(), groupId);
		return (!(ids.isEmpty() || (ids.size() > 1)));
	}

	@Override
	public List<FacebookGroupDto> getGroupRecommendation(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		// TODO: Return some groups that might be interesting for the person with the given id.
		return new LinkedList<FacebookGroupDto>();
	}

	@Override
	public void addAssociation(Long personId, Long groupId, Boolean visible) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (visible == null) {
			throw new IllegalArgumentException("Visible must not be null.");
		}
		if (existsAssociation(personId, groupId)) {
			return;
		}
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(3);
		parameters.put("id_person", personId);
		parameters.put("id_group", groupId);
		parameters.put("visible", visible);
		// Execute update.
		jdbcAssociationInsert.execute(parameters);
	}

	@Override
	public boolean existsAssociation(Long personId, Long groupId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (groupId == null) {
			throw new IllegalArgumentException("Group id must not be null.");
		}
		String sql = "select * from personfacebookgroup where id_person = ? and id_group = ?";
		List<PersonFacebookGroupDto> pss = this.jdbcAssociationTemplate.query(sql, new PersonGroupMapper(), personId, groupId);
		return (!(pss.isEmpty() || (pss.size() > 1)));
	}

	@Override
	public void removeAssociation(Long personId, Long groupId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (groupId == null) {
			throw new IllegalArgumentException("Group id must not be null.");
		}
		this.jdbcAssociationTemplate.update("delete from personfacebookgroup where id_person = ? and id_group = ?", personId, groupId);
	}

	@Override
	public void removeAllAssociations(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		this.jdbcAssociationTemplate.update("delete from personfacebookgroup where id_person = ?", personId);
	}

	@Override
	public Long create(FacebookGroupDto group) {
		if (group == null) {
			throw new IllegalArgumentException("Group must not be null.");
		}
		if (group.getId() != null) {
			throw new IllegalArgumentException("Group id must not be set.");
		}
		if (exists(group.getgId(), true)) {
			FacebookGroupDto g = getById(group.getgId(), true);
			return g.getId();
		}
		
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(9);
		parameters.put("name", group.getName());
		parameters.put("gid", group.getgId());
		parameters.put("nid", group.getnId());
		parameters.put("creator", group.getCreatorId());
		parameters.put("description", group.getDescription());
		parameters.put("type", group.getType());
		parameters.put("subtype", group.getSubtype());
		parameters.put("last_update", group.getLastUpdate());
		parameters.put("website", group.getWebsite());
        
        // Execute and set id.
		Number newId = this.jdbcGroupInsert.executeAndReturnKey(parameters);
		group.setId(newId.longValue());
		return newId.longValue();
	}

	@Override
	public void delete(Long groupId) {
		if (groupId == null) {
			throw new IllegalArgumentException("Group id must not be null.");
		}
		this.jdbcGroupTemplate.update("delete from facebookgroup where id_group = ?", groupId);
	}
	
	/**
	 * Maps a database row to a group data transfer object.
	 * @author Linda
	 *
	 */
	private static final class GroupMapper implements RowMapper<FacebookGroupDto> {

	    public FacebookGroupDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	FacebookGroupDto group = new FacebookGroupDto();
	    	group.setId(rs.getLong("id_group"));
	    	if (rs.wasNull()) {
	    		group.setId(null);
	    	}
	    	group.setName(rs.getString("name"));
	    	if (rs.wasNull()) {
	    		group.setName(null);
	    	}
	    	group.setgId(rs.getLong("gid"));
	    	if (rs.wasNull()) {
	    		group.setgId(null);
	    	}
	    	group.setnId(rs.getLong("nid"));
	    	if (rs.wasNull()) {
	    		group.setnId(null);
	    	}
	    	group.setCreatorId(rs.getLong("creator"));
	    	if (rs.wasNull()) {
	    		group.setCreatorId(null);
	    	}
	    	group.setDescription(rs.getString("description"));
	    	if (rs.wasNull()) {
	    		group.setDescription(null);
	    	}
	    	group.setType(rs.getString("type"));
	    	if (rs.wasNull()) {
	    		group.setType(null);
	    	}
	    	group.setSubtype(rs.getString("subtype"));
	    	if (rs.wasNull()) {
	    		group.setSubtype(null);
	    	}
	    	group.setLastUpdate(rs.getDate("last_update"));
	    	if (rs.wasNull()) {
	    		group.setLastUpdate(null);
	    	}
	    	group.setWebsite(rs.getString("website"));
	    	if (rs.wasNull()) {
	    		group.setWebsite(null);
	    	}
			return group;
	    }        
	}
	
	/**
	 * Maps a database row to a person group data transfer object.
	 * @author Linda
	 *
	 */
	private static final class PersonGroupMapper implements RowMapper<PersonFacebookGroupDto> {

		@Override
		public PersonFacebookGroupDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			PersonFacebookGroupDto pg = new PersonFacebookGroupDto();
			pg.setPersonId(rs.getLong("id_person"));
			if (rs.wasNull()) {
				pg.setPersonId(null);
			}
			pg.setGroupId(rs.getLong("id_group"));
			if (rs.wasNull()) {
				pg.setGroupId(null);
			}
			pg.setVisible(rs.getBoolean("visible"));
			if (rs.wasNull()) {
				pg.setVisible(null);
			}
			return pg;
		}
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
