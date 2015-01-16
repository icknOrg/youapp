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

import youapp.dataaccess.dao.IFacebookPageDao;
import youapp.dataaccess.dto.FacebookPageDto;
import youapp.dataaccess.dto.PersonFacebookPageDto;

public class JdbcFacebookPageDao implements IFacebookPageDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate jdbcPageTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcPageInsert;
	
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
        this.jdbcPageTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcPageInsert = new SimpleJdbcInsert(dataSource)
        								.withTableName("facebookpage")
        								.usingGeneratedKeyColumns("id_page");
        this.jdbcAssociationTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcAssociationInsert = new SimpleJdbcInsert(dataSource)
        								.withTableName("personfacebookpage");
    }
	
	@Override
	public FacebookPageDto getById(Long pageId, Boolean fbId) {
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		String sql = null;
		if (fbId) {
			sql = "select * from facebookpage where pid = ?";
		} else {
			sql = "select * from facebookpage where id_page = ?";
		}
    	return this.jdbcPageTemplate.queryForObject(sql, new PageMapper(), pageId);
	}

	@Override
	public List<FacebookPageDto> getByPerson(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		String sql = "select * from facebookpage where id_page in (select id_page from personfacebookpage where id_person = ?)";
		return this.jdbcPageTemplate.query(sql, new PageMapper(), personId);
	}

	@Override
	public List<FacebookPageDto> getByName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name must not be null.");
		}
		String sql = "select * from facebookpage where name = ?";
    	return this.jdbcPageTemplate.query(sql, new PageMapper(), name);
	}

	@Override
	public List<FacebookPageDto> getByCategory(String category) {
		if (category == null) {
			throw new IllegalArgumentException("Category must not be null.");
		}
		String sql = "select * from facebookpage where category = ?";
    	return this.jdbcPageTemplate.query(sql, new PageMapper(), category);
	}

	@Override
	public List<FacebookPageDto> getAll() {
		String sql = "select * from facebookgroup";
		return this.jdbcPageTemplate.query(sql, new PageMapper());
	}
	
	@Override
	public List<FacebookPageDto> getCommonPages(Long personIdA, Long personIdB) {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		String sql = "select * from facebookpage where id_page in (select id_page from personfacebookpage where id_person = ? and id_page in (select id_page from personfacebookpage where id_person = ?)) order by id_page asc";
		return this.jdbcPageTemplate.query(sql, new PageMapper(), personIdA, personIdB);
	}
	
	@Override
	public Integer getNumberOfPages(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		String sql = "select count(id_page) from personfacebookpage where id_person = ?";
		return this.jdbcPageTemplate.queryForInt(sql, personId);
	}

	@Override
	public Integer getNumberOfCommonPages(Long personIdA, Long personIdB) {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		String sql = "select count(id_page) from personfacebookpage where id_person = ? and id_page in (select id_page from personfacebookpage where id_person = ?)";
		return this.jdbcPageTemplate.queryForInt(sql, personIdA, personIdB);
	}
	
	@Override
	public Integer getPageFrequency(Long pageId) {
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		String sql = "select count(id_person) from personfacebookpage where id_page = ?";
		return this.jdbcPageTemplate.queryForInt(sql, pageId);
	}
	
	@Override
	public Long getAlternativeId(Long pageId, Boolean fbId) {
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (!exists(pageId, fbId)) {
			return null;
		}
		
		String sql = null;
		if (fbId) {
			sql = "select id_page from facebookpage where pid = ?";
		} else {
			sql = "select pid from facebookpage where id_page = ?";
		}
		return this.jdbcPageTemplate.queryForLong(sql, pageId);
	}

	@Override
	public Boolean exists(Long pageId, Boolean fbId) {
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		String sql = null;
		if (fbId) {
			sql = "select id_page from facebookpage where pid = ?";
		} else {
			sql = "select id_page from facebookpage where id_page = ?";
		}
		List<Long> ids = this.jdbcPageTemplate.query(sql, new MyLongMapper(), pageId);
		return (!(ids.isEmpty() || (ids.size() > 1)));
	}

	@Override
	public void addAssociation(Long personId, Long pageId, Boolean visible, String profileSection, Long createdTime) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		if (visible == null) {
			throw new IllegalArgumentException("Visible must not be null.");
		}
		if (existsAssociation(personId, pageId)) {
			return;
		}
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(5);
		parameters.put("id_person", personId);
		parameters.put("id_page", pageId);
		parameters.put("visible", visible);
		if (profileSection != null) {
			parameters.put("profile_section", profileSection);
		}
		if (createdTime != null) {
			parameters.put("created_time", createdTime);
		}
		// Execute update.
		jdbcAssociationInsert.execute(parameters);
	}

	@Override
	public boolean existsAssociation(Long personId, Long pageId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		String sql = "select * from personfacebookpage where id_person = ? and id_page = ?";
		List<PersonFacebookPageDto> pss = this.jdbcAssociationTemplate.query(sql, new PersonPageMapper(), personId, pageId);
		return (!(pss.isEmpty() || (pss.size() > 1)));
	}

	@Override
	public void removeAssociation(Long personId, Long pageId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		this.jdbcAssociationTemplate.update("delete from personfacebookpage where id_person = ? and id_page = ?", personId, pageId);
	}

	@Override
	public void removeAllAssociations(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		this.jdbcAssociationTemplate.update("delete from personfacebookpage where id_person = ?", personId);
	}

	@Override
	public Long create(FacebookPageDto page) {
		if (page == null) {
			throw new IllegalArgumentException("Page must not be null.");
		}
		if (page.getId() != null) {
			throw new IllegalArgumentException("Page id must not be set.");
		}
		if (exists(page.getpId(), true)) {
			FacebookPageDto p = getById(page.getpId(), true);
			return p.getId();
		}
		
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(7);
		parameters.put("pid", page.getpId());
		parameters.put("name", page.getName());
		parameters.put("category", page.getCategory());
		parameters.put("type", page.getType());
		parameters.put("likes", page.getLikes());
		parameters.put("website", page.getWebsite());
		parameters.put("founded", page.getFounded());
        
        // Execute and set id.
		Number newId = this.jdbcPageInsert.executeAndReturnKey(parameters);
		page.setId(newId.longValue());
		return newId.longValue();
	}

	@Override
	public void delete(Long pageId) {
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		this.jdbcPageTemplate.update("delete from facebookgroup where id_group = ?", pageId);
	}
	
	/**
	 * Maps a database row to a page data transfer object.
	 * @author Linda
	 *
	 */
	private static final class PageMapper implements RowMapper<FacebookPageDto> {

	    public FacebookPageDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	FacebookPageDto page = new FacebookPageDto();
	    	page.setId(rs.getLong("id_page"));
	    	if (rs.wasNull()) {
	    		page.setId(null);
	    	}
	    	page.setpId(rs.getLong("pid"));
	    	if (rs.wasNull()) {
	    		page.setpId(null);
	    	}
	    	page.setName(rs.getString("name"));
	    	if (rs.wasNull()) {
	    		page.setName(null);
	    	}
	    	page.setCategory(rs.getString("category"));
	    	if (rs.wasNull()) {
	    		page.setCategory(null);
	    	}
	    	page.setType(rs.getString("type"));
	    	if (rs.wasNull()) {
	    		page.setType(null);
	    	}
	    	page.setLikes(rs.getLong("likes"));
	    	if (rs.wasNull()) {
	    		page.setLikes(null);
	    	}
	    	page.setWebsite(rs.getString("website"));
	    	if (rs.wasNull()) {
	    		page.setWebsite(null);
	    	}
	    	page.setFounded(rs.getInt("founded"));
	    	if (rs.wasNull()) {
	    		page.setFounded(null);
	    	}
	    	return page;
	    }        
	}
	
	/**
	 * Maps a database row to a person page data transfer object.
	 * @author Linda
	 *
	 */
	private static final class PersonPageMapper implements RowMapper<PersonFacebookPageDto> {

		@Override
		public PersonFacebookPageDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			PersonFacebookPageDto pp = new PersonFacebookPageDto();
			pp.setPersonId(rs.getLong("id_person"));
			if (rs.wasNull()) {
				pp.setPersonId(null);
			}
			pp.setPageId(rs.getLong("id_page"));
			if (rs.wasNull()) {
				pp.setPageId(null);
			}
			pp.setVisible(rs.getBoolean("visible"));
			if (rs.wasNull()) {
				pp.setVisible(null);
			}
			pp.setProfileSection(rs.getString("profile_section"));
			if (rs.wasNull()) {
				pp.setProfileSection(null);
			}
			pp.setCreatedTime(rs.getLong("created_time"));
			if (rs.wasNull()) {
				pp.setCreatedTime(null);
			}
			return pp;
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
