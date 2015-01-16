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

import youapp.dataaccess.dao.IMoodDao;
import youapp.dataaccess.dto.MoodDto;

@Repository
public class JdbcMoodDao
    implements IMoodDao
{
    /**
     * Used for accessing the database. JdbcTemplate is thread safe once
     * configured.
     */
    private SimpleJdbcTemplate jdbcTemplate;

    /**
     * Used for simple inserts. JdbcTemplate is thread safe once configured.
     */
    private SimpleJdbcInsert jdbcMoodInsert;

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcMoodInsert =
            new SimpleJdbcInsert(dataSource).withTableName("mood").usingGeneratedKeyColumns("id_mood");
    }

    @Override
    public MoodDto getById(Integer id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        String sql = "select * from mood where id_mood = ?";
        return this.jdbcTemplate.queryForObject(sql, new MoodMapper(), id);
    }

    @Override
    public List<MoodDto> getByRating(Integer rating)
    {
        if (rating == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        String sql = "select * from mood where rating = ?";
        return this.jdbcTemplate.query(sql, new MoodMapper(), rating);
    }

    @Override
    public List<MoodDto> getByDescription(String description)
    {
        if (description == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        String sql = "select * from mood where description = ?";
        return this.jdbcTemplate.query(sql, new MoodMapper(), description);
    }

    @Override
    public List<MoodDto> getByRatingDescription(Integer rating, String description)
    {
        if (rating == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        String sql;
        if (description != null)
        {
            sql = "select * from mood where rating = ? and description = ?";
        }
        else
        {
            sql = "select * from mood where rating = ? and description is ?";
        }

        return this.jdbcTemplate.query(sql, new MoodMapper(), rating, description);
    }

    @Override
    public List<MoodDto> getAll()
    {
        String sql = "select * from mood";
        return this.jdbcTemplate.query(sql, new MoodMapper());
    }

    @Override
    public boolean exists(Integer rating, String description)
    {
        if (rating == null)
        {
            throw new IllegalArgumentException("Rating must not be null.");
        }

        String sql;
        if (description != null)
        {
            sql = "select id_mood from mood where rating = ? and description = ?";
        }
        else
        {
            sql = "select id_mood from mood where rating = ? and description is ?";
        }
        List<Long> ids = this.jdbcTemplate.query(sql, new MyLongMapper(), rating, description);
        return (!(ids.isEmpty() || (ids.size() > 1)));
    }

    @Override
    public Integer create(MoodDto mood)
    {
        if (mood == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        if (mood.getId() != null)
        {
            throw new IllegalArgumentException("Id must be null.");
        }

        // Prepare statement
        Map<String, Object> parameters = new HashMap<String, Object>(2);
        parameters.put("rating", mood.getRating());
        parameters.put("description", mood.getDescription());

        // Execute and set id.
        Number newId = this.jdbcMoodInsert.executeAndReturnKey(parameters);
        mood.setId(newId.intValue());
        return mood.getId();
    }

    @Override
    public void update(MoodDto mood)
    {
        if (mood == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        if (mood.getId() == null)
        {
            throw new IllegalArgumentException("Id must not be null.");
        }

        String sql = "update mood set rating = ?, description = ? where id_mood = ?";
        this.jdbcTemplate.update(sql, mood.getRating(), mood.getDescription(), mood.getId());
    }

    @Override
    public void delete(Integer id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id must not be null.");
        }

        this.jdbcTemplate.update("delete from mood where id_mood", id);
    }

    private static final class MoodMapper
        implements RowMapper<MoodDto>
    {

        @Override
        public MoodDto mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            MoodDto mood = new MoodDto();
            mood.setId(rs.getInt("id_mood"));
            if (rs.wasNull())
            {
                mood.setId(null);
            }

            mood.setRating(rs.getInt("rating"));
            if (rs.wasNull())
            {
                mood.setRating(null);
            }

            mood.setDescription(rs.getString("description"));
            if (rs.wasNull())
            {
                mood.setDescription(null);
            }

            return mood;
        }

    }

    /**
     * Maps a row containing an id to a Long.
     * 
     * @author neme
     * 
     */
    private static final class MyLongMapper
        implements RowMapper<Long>
    {

        @Override
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            Long id = rs.getLong(1);
            if (rs.wasNull())
            {
                id = null;
            }
            return id;
        }
    }

}
