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

import youapp.dataaccess.dao.IPictureDao;
import youapp.dataaccess.dto.PictureDto;

@Repository
public class JdbcPictureDao implements IPictureDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate jdbcTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcPictureInsert;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcPictureInsert = new SimpleJdbcInsert(dataSource)
											.withTableName("picture");
    }
	
	@Override
	public PictureDto getById(Long personId, Integer pictureId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (pictureId == null) {
			throw new IllegalArgumentException("picture id must not be null.");
		}
		String sql = "select * from picture where id_picture = ? and id_person = ?";
    	return this.jdbcTemplate.queryForObject(sql, new PictureMapper(), pictureId, personId);
	}

	@Override
	public List<PictureDto> getByPerson(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select * from picture where id_person = ?";
    	return this.jdbcTemplate.query(sql, new PictureMapper(), personId);
	}

    @Override
    public boolean exists(Long personId, Integer pictureId)
    {
        if (pictureId == null) {
            throw new IllegalArgumentException("picture id must not be null.");
        }
        if (personId == null)
        {
            throw new IllegalArgumentException("person id must not be null.");
        }
        String sql = "select id_picture from picture where id_person = ? and id_picture = ?";
        List<Long> ids = this.jdbcTemplate.query(sql, new MyLongMapper(), personId, pictureId);
        return (!(ids.isEmpty() || (ids.size() > 1)));
    }
	
	@Override
	public void create(PictureDto picture) {
		if (picture == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (picture.getPictureId() == null) {
			throw new IllegalArgumentException("picture id must not be null.");
		}
		if (picture.getPersonId() == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (picture.getPicture() == null) {
			throw new IllegalArgumentException("picture must not be null.");
		}
		
		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(8);
		parameters.put("id_picture", picture.getPictureId());
		parameters.put("id_person", picture.getPersonId());
		parameters.put("caption", picture.getCaption());
		parameters.put("picture", picture.getPicture());
		parameters.put("picture_format_name", picture.getPictureFormatName());
		parameters.put("mood", picture.getMood());
		parameters.put("thumbnail", picture.getThumbnail());
		parameters.put("thumbnail_format_name", picture.getThumbnailFormatName());
        
        // Execute update.
		jdbcPictureInsert.execute(parameters);
	}

	@Override
	public void update(PictureDto picture) {
		if (picture == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (picture.getPictureId() == null) {
			throw new IllegalArgumentException("picture id must not be null.");
		}
		if (picture.getPersonId() == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		if (picture.getPicture() == null) {
			throw new IllegalArgumentException("picture must not be null.");
		}
		
		String sql = "update picture set caption = ?, picture = ?, picture_format_name = ?, mood = ?, thumbnail = ?, thumbnail_format_name = ? " +
				"where id_picture = ? and id_person = ?";

		this.jdbcTemplate.update(sql, picture.getCaption(), picture.getPicture(), picture.getPictureFormatName(),
				picture.getMood(), picture.getThumbnail(), picture.getThumbnailFormatName(), picture.getPictureId(), picture.getPersonId());
	}
	
	@Override
	public void delete(Integer pictureId, Long personId) {
		if (pictureId == null) {
			throw new IllegalArgumentException("picture id must not be null.");
		}
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		this.jdbcTemplate.update("delete from picture where id_picture = ? and id_person = ?", pictureId, personId);
	}
	
	@Override
	public void deleteAll(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("person id must not be null.");
		}
		this.jdbcTemplate.update("delete from picture where id_person = ?", personId);
	}
	
	@Override
	public Integer getValidPictureId(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		String sql = "select max(id_picture) as id from picture where id_person = ?";
		return (this.jdbcTemplate.queryForInt(sql, personId)) + 1;
	}
	
	/**
	 * Maps a database row to a picture data transfer object.
	 * @author neme
	 *
	 */
	private static final class PictureMapper implements RowMapper<PictureDto> {

	    public PictureDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	PictureDto picture = new PictureDto();
	    	picture.setPictureId(rs.getInt("id_picture"));
	    	if (rs.wasNull()) {
	    		picture.setPictureId(null);
	    	}
	    	picture.setPersonId(rs.getLong("id_person"));
	    	if (rs.wasNull()) {
	    		picture.setPersonId(null);
	    	}
	    	picture.setCaption(rs.getString("caption"));
	    	if (rs.wasNull()) {
	    		picture.setCaption(null);
	    	}
	    	picture.setPicture(rs.getBlob("picture"));
	    	if (rs.wasNull()) {
	    		picture.setPicture(null);
	    	}
	    	picture.setPictureFormatName(rs.getString("picture_format_name"));
	    	if(rs.wasNull()){
	    	    picture.setPictureFormatName(null);
	    	}
	    	picture.setMood(rs.getInt("mood"));
	    	if (rs.wasNull()) {
                picture.setMood(null);
            }
	    	picture.setThumbnail(rs.getBlob("thumbnail"));
	    	if (rs.wasNull()) {
                picture.setThumbnail(null);
            }
            picture.setThumbnailFormatName(rs.getString("thumbnail_format_name"));
            if (rs.wasNull())
            {
                picture.setThumbnailFormatName(null);
            }
	    	return picture;
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
