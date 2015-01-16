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

import youapp.dataaccess.dao.ITagDao;
import youapp.dataaccess.dto.PersonTagDto;
import youapp.dataaccess.dto.TagDto;
import youapp.model.Tag;

public class JdbcTagDao implements ITagDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate jdbcTagTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcTagInsert;
	
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
        this.jdbcTagTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcTagInsert = new SimpleJdbcInsert(dataSource)
        								.withTableName("tag")
        								.usingGeneratedKeyColumns("id_tag");
        this.jdbcAssociationTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcAssociationInsert = new SimpleJdbcInsert(dataSource)
        								.withTableName("persontag");
    }
	
	@Override
	public TagDto getById(Long tagId) {
		if (tagId == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select * from tag where id_tag = ?";
    	return this.jdbcTagTemplate.queryForObject(sql, new TagMapper(), tagId);
	}

	@Override
	public List<TagDto> getByNameAndCategory(String name, Integer category) {
		if (name == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (category == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}		
		String sql = "select * from tag where name = ? and category = ?";
    	return this.jdbcTagTemplate.query(sql, new TagMapper(), name, category);
	}

	@Override
	public List<TagDto> getByPerson(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}		
		String sql = "select * from tag where id_tag in (select id_tag from persontag where id_person = ?)";
    	return this.jdbcTagTemplate.query(sql, new TagMapper(), personId);
	}
	
	@Override
	public List<TagDto> getByCategory(Integer category) {
		if (category == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select * from tag where category = ?";
    	return this.jdbcTagTemplate.query(sql, new TagMapper(), category);
	}
	
	@Override
	public List<TagDto> getAll() {
		String sql = "select * from tag";
    	return this.jdbcTagTemplate.query(sql, new TagMapper());
	}
	
	@Override
	public boolean exists(String name, Integer category) {
		if (name == null) {
			throw new IllegalArgumentException("Name must not be null.");
		}
		if (category == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select id_tag from tag where name = ? and category = ?";
		List<Long> ids = this.jdbcTagTemplate.query(sql, new MyLongMapper(), name, category);
		return (!(ids.isEmpty() || (ids.size() > 1)));
	}
	
	@Override
	public void addAssociation(Long personId, Long tagId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (tagId == null) {
			throw new IllegalArgumentException("tag id must not be null.");
		}
		if (existsAssociation(personId, tagId)) {
			return;
		}
		
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("id_person", personId);
		parameters.put("id_tag", tagId);
		
		// Execute update.
		jdbcAssociationInsert.execute(parameters);
	}

	@Override
	public boolean existsAssociation(Long personId, Long tagId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (tagId == null) {
			throw new IllegalArgumentException("tag id must not be null.");
		}		
		String sql = "select * from persontag where id_person = ? and id_tag = ?";
		List<PersonTagDto> pss = this.jdbcAssociationTemplate.query(sql, new PersonTagMapper(), personId, tagId);
		return (!(pss.isEmpty() || (pss.size() > 1)));
	}

	@Override
	public void removeAssociation(Long personId, Long tagId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (tagId == null) {
			throw new IllegalArgumentException("tag id must not be null.");
		}
		this.jdbcAssociationTemplate.update("delete from persontag where id_person = ? and id_tag = ?", personId, tagId);
	}
	
	@Override
	public void removeAllAssociations(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		this.jdbcAssociationTemplate.update("delete from persontag where id_person = ?", personId);
	}

	@Override
	public Long create(TagDto tag) {
		if (tag == null) {
			throw new IllegalArgumentException("tag must not be null.");
		}
		if (tag.getId() != null) {
			throw new IllegalArgumentException("Id must be null.");
		}
		if (exists(tag.getName(), tag.getCategory())) {
			List<TagDto> tags = getByNameAndCategory(tag.getName(), tag.getCategory());
			if (!tags.isEmpty()) {
				TagDto existingTag = tags.get(0);
				tag.setId(existingTag.getId());
				return existingTag.getId();
			}
		}
		
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(2);
        parameters.put("name", tag.getName());
        parameters.put("category", tag.getCategory());
        
        // Execute and set id.
		Number newId = this.jdbcTagInsert.executeAndReturnKey(parameters);
		tag.setId(newId.longValue());
		return newId.longValue();
	}

	@Override
	public void delete(Long tagId) {
		if (tagId == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		this.jdbcTagTemplate.update("delete from tag where id_tag = ?", tagId);
	}
	
	/**
	 * Maps a database row to a tag data transfer object.
	 * @author neme
	 *
	 */
	private static final class TagMapper implements RowMapper<TagDto> {

	    public TagDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	TagDto tag = new TagDto();
	    	tag.setId(rs.getLong("id_tag"));
	    	if (rs.wasNull()) {
	    		tag.setId(null);
	    	}
	    	tag.setCategory(rs.getInt("category"));
	    	if (rs.wasNull()) {
	    		tag.setCategory(null);
	    	}
	    	tag.setName(rs.getString("name"));
	    	if (rs.wasNull()) {
	    		tag.setCategory(null);
	    	}
			return tag;
	    }        
	}
	
	/**
	 * Maps a database row to a person tag data transfer object.
	 * @author neme
	 *
	 */
	private static final class PersonTagMapper implements RowMapper<PersonTagDto> {

		@Override
		public PersonTagDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			PersonTagDto pt = new PersonTagDto();
			pt.setPersonId(rs.getLong("id_person"));
			if (rs.wasNull()) {
				pt.setPersonId(null);
			}
			pt.setTagId(rs.getLong("id_tag"));
			if (rs.wasNull()) {
				pt.setTagId(null);
			}
			return pt;
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
	
	
	
	@Override
	public Integer getNumberOfCommonTagsByCategory(Long personIdA, Long personIdB,Tag.Category category) {
		
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A parameter must not be null.");
		}
		
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B parameter must not be null.");
		}
		
		if (category == null) {
			throw new IllegalArgumentException("Tag category parameter must not be null.");		}	
		
		String sql = "SELECT count(persontag.id_tag) " +
					 "from persontag " +
					 "inner join tag on  persontag.id_tag = tag.id_tag " +
					 "where " +
					 "persontag.id_person=? " +
					 "and tag.category=? " +
					 "and persontag.id_tag IN " +
					 "(SELECT persontag.id_tag " +
					 "from persontag " +
					 "inner join tag on  persontag.id_tag = tag.id_tag " +
					 "where " +
					 "persontag.id_person=? " +
					 "and tag.category=?)";		
		
		
		return this.jdbcTagTemplate.queryForInt(sql, personIdA,category.id(),personIdB,category.id());	
		
	}	
	
	@Override
	public Integer getNumberOfTagsByCategory(Long personId,Tag.Category category) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		String sql = "SELECT count(persontag.id_tag) " +
					 "from persontag " +
					 "inner join tag on  persontag.id_tag = tag.id_tag " +
					 "where " +
					 "persontag.id_person=? " +
					 "and tag.category=? ";
		
		return this.jdbcTagTemplate.queryForInt(sql, personId,category.id());
	}

	
	
}
