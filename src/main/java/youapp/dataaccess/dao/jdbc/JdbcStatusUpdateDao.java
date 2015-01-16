package youapp.dataaccess.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import youapp.dataaccess.dao.IStatusUpdateDao;
import youapp.dataaccess.dto.StatusUpdateDto;

public class JdbcStatusUpdateDao
    implements IStatusUpdateDao
{

    /**
     * Used for accessing the database. JdbcTemplate is thread safe once
     * configured.
     */
    private SimpleJdbcTemplate jdbcTemplate;

    /**
     * Used for simple inserts. JdbcTemplate is thread safe once configured.
     */
    private SimpleJdbcInsert jdbcStatusUpdateInsert;

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcStatusUpdateInsert = new SimpleJdbcInsert(dataSource).withTableName("statusupdate");
    }

    @Override
    public StatusUpdateDto getById(Long personId, Timestamp when)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (when == null)
        {
            throw new IllegalArgumentException("Date when must not be null.");
        }
        String sql = "select * from statusupdate where id_person = ? and id_when = ?";
        return this.jdbcTemplate.queryForObject(sql, new StatusUpdateMapper(), personId, when);
    }

    @Override
    public StatusUpdateDto getLastById(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        String sql = "select * from statusupdate where id_person = ? order by id_when desc limit 0, 1";
        return this.jdbcTemplate.queryForObject(sql, new StatusUpdateMapper(), personId);
    }
    
    @Override
    public List<StatusUpdateDto> getByPerson(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        String sql = "select * from statusupdate where id_person = ?";
        return this.jdbcTemplate.query(sql, new StatusUpdateMapper(), personId);
    }

    @Override
    public List<StatusUpdateDto> getByDate(Timestamp when)
    {
        if (when == null)
        {
            throw new IllegalArgumentException("Date when must not be null.");
        }
        String sql = "select * from statusupdate where id_when = ?";
        return this.jdbcTemplate.query(sql, new StatusUpdateMapper(), when);
    }

    @Override
    public List<StatusUpdateDto> getByPersonAndSoulmates(Long personId, Integer offset, Integer resultSize)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (offset == null)
        {
            throw new IllegalArgumentException("Offset must not be null.");
        }
        if (resultSize == null)
        {
            throw new IllegalArgumentException("Result size must not be null.");
        }
        String sql =
            "select * from statusupdate where id_person = ? or id_person in (select id_requested from soulmates where id_requester = ? and request_pending = 0 union select id_requester from soulmates where id_requested = ? and request_pending = 0) order by id_when desc limit ?, ?";
        return this.jdbcTemplate.query(sql, new StatusUpdateMapper(), personId, personId, personId, offset, resultSize);
    }

    @Override
    public List<StatusUpdateDto> getByPersonAndSoulmates(Long personId, Timestamp startDate, Integer offset,
        Integer resultSize)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (startDate == null)
        {
            throw new IllegalArgumentException("Start date id must not be null.");
        }
        if (offset == null)
        {
            throw new IllegalArgumentException("Offset must not be null.");
        }
        if (resultSize == null)
        {
            throw new IllegalArgumentException("Result size must not be null.");
        }
        String sql =
            "select * from statusupdate where id_person = ? or id_person in (select id_requested from soulmates where id_requester = ? and request_pending = 0 union select id_requester from soulmates where id_requested = ? and request_pending = 0) and id_when <= ? order by id_when desc limit ?, ?";
        return this.jdbcTemplate
            .query(sql, new StatusUpdateMapper(), personId, personId, personId, startDate, offset, resultSize);
    }

    @Override
    public boolean exists(Long personId, Timestamp when)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (when == null)
        {
            throw new IllegalArgumentException("Date when must not be null.");
        }
        String sql = "select count(*) from statusupdate where id_person = ? and id_when = ?";
        return this.jdbcTemplate.queryForInt(sql, personId, when) > 0;
    }

    @Override
    public boolean exists(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        String sql = "select count(*) from statusupdate where id_person = ?";
        return this.jdbcTemplate.queryForInt(sql, personId) > 0;
    }
    
    @Override
    public void create(StatusUpdateDto statusUpdate)
    {
        if (statusUpdate == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (statusUpdate.getPersonId() == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (statusUpdate.getWhen() == null)
        {
            throw new IllegalArgumentException("Date when must not be null.");
        }

        // Prepare parameters.
        Map<String, Object> parameters = new HashMap<String, Object>(8);
        parameters.put("id_person", statusUpdate.getPersonId());
        parameters.put("id_when", statusUpdate.getWhen());
        parameters.put("mood", statusUpdate.getMoodId());
        parameters.put("description", statusUpdate.getDescription());

        // Execute update.
        jdbcStatusUpdateInsert.execute(parameters);
    }

    @Override
    public void update(StatusUpdateDto statusUpdate)
    {
        if (statusUpdate == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (statusUpdate.getPersonId() == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (statusUpdate.getWhen() == null)
        {
            throw new IllegalArgumentException("Date when must not be null.");
        }

        String sql = "update statusupdate set mood = ?, description = ? where id_person = ? and id_when = ?";

        this.jdbcTemplate.update(sql, statusUpdate.getMoodId(), statusUpdate.getDescription(),
            statusUpdate.getPersonId(), statusUpdate.getWhen());
    }

    @Override
    public void delete(Long personId, Timestamp when)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (when == null)
        {
            throw new IllegalArgumentException("Date when must not be null.");
        }

        this.jdbcTemplate.update("delete from statusupdate where id_person = ? and id_when = ?", personId, when);
    }

    @Override
    public void deleteAll(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }

        this.jdbcTemplate.update("delete from statusupdate where id_person = ?", personId);
    }

    /**
     * Maps a database row to a picture data transfer object.
     * 
     * @author neme
     * 
     */
    private static final class StatusUpdateMapper
        implements RowMapper<StatusUpdateDto>
    {

        public StatusUpdateDto mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            StatusUpdateDto statusUpdate = new StatusUpdateDto();
            statusUpdate.setPersonId(rs.getLong("id_person"));
            if (rs.wasNull())
            {
                statusUpdate.setPersonId(null);
            }
            statusUpdate.setWhen(rs.getTimestamp("id_when"));
            if (rs.wasNull())
            {
                statusUpdate.setWhen(null);
            }
            statusUpdate.setMoodId(rs.getInt("mood"));
            if (rs.wasNull())
            {
                statusUpdate.setMoodId(null);
            }
            statusUpdate.setDescription(rs.getString("description"));
            if (rs.wasNull())
            {
                statusUpdate.setDescription(null);
            }
            return statusUpdate;
        }
    }

}
