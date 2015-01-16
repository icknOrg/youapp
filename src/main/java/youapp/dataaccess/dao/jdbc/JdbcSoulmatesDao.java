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

import youapp.dataaccess.dao.ISoulmatesDao;
import youapp.dataaccess.dto.SoulmatesDto;

public class JdbcSoulmatesDao
    implements ISoulmatesDao
{

    /**
     * Used for accessing the database. JdbcTemplate is thread safe once
     * configured.
     */
    private SimpleJdbcTemplate jdbcTemplate;

    /**
     * Used for simple inserts. JdbcTemplate is thread safe once configured.
     */
    private SimpleJdbcInsert jdbcSoulmatesInsert;

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcSoulmatesInsert = new SimpleJdbcInsert(dataSource).withTableName("soulmates");
    }

    @Override
    public SoulmatesDto getById(Long requesterId, Long requestedId)
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester id must not be null.");
        }
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested id must not be null.");
        }
        String sql = "select * from soulmates where id_requester = ? and id_requested = ?";
        return this.jdbcTemplate.queryForObject(sql, new SoulmatesMapper(), requesterId, requestedId);
    }

    @Override
    public SoulmatesDto getByIdAnyDirection(Long personAId, Long personBId)
    {
        if (personAId == null)
        {
            throw new IllegalArgumentException("Person A id must not be null.");
        }
        if (personBId == null)
        {
            throw new IllegalArgumentException("Person B id must not be null.");
        }
        String sql =
            "select * from soulmates where (id_requester = ? and id_requested = ?) or (id_requester = ? and id_requested = ?)";
        return this.jdbcTemplate.queryForObject(sql, new SoulmatesMapper(), personAId, personBId, personBId, personAId);
    }

    @Override
    public List<SoulmatesDto> getByRequester(Long requesterId)
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester id must not be null.");
        }
        String sql = "select * from soulmates where id_requester = ?";
        return this.jdbcTemplate.query(sql, new SoulmatesMapper(), requesterId);
    }

    @Override
    public List<SoulmatesDto> getByRequester(Long requesterId, Boolean requestPending)
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester id must not be null.");
        }
        String sql = "select * from soulmates where id_requester = ? and request_pending = ?";
        return this.jdbcTemplate.query(sql, new SoulmatesMapper(), requesterId, requestPending);
    }

    @Override
    public List<SoulmatesDto> getByRequested(Long requestedId)
    {
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested id must not be null.");
        }
        String sql = "select * from soulmates where id_requested = ?";
        return this.jdbcTemplate.query(sql, new SoulmatesMapper(), requestedId);
    }

    @Override
    public List<SoulmatesDto> getByRequested(Long requestedId, Boolean requestPending)
    {
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested id must not be null.");
        }
        String sql = "select * from soulmates where id_requested = ? and request_pending = ?";
        return this.jdbcTemplate.query(sql, new SoulmatesMapper(), requestedId, requestPending);
    }

    @Override
    public List<SoulmatesDto> getAll(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        String sql = "select * from soulmates where (id_requester = ? or id_requested = ?)";
        return this.jdbcTemplate.query(sql, new SoulmatesMapper(), personId, personId);
    }

    @Override
    public List<SoulmatesDto> getAll(Long personId, Boolean requestPending)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        String sql = "select * from soulmates where (id_requester = ? or id_requested = ?) and request_pending = ?";
        return this.jdbcTemplate.query(sql, new SoulmatesMapper(), personId, personId, requestPending);
    }

    @Override
    public int getNumberByRequester(Long requesterId)
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester id must not be null.");
        }
        String sql = "select count(*) from soulmates where id_requester = ?";
        return this.jdbcTemplate.queryForInt(sql, requesterId);
    }

    @Override
    public int getNumberByRequester(Long requesterId, Boolean requestPending)
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester id must not be null.");
        }
        String sql = "select count(*) from soulmates where id_requester = ? and request_pending = ?";
        return this.jdbcTemplate.queryForInt(sql, requesterId, requestPending);
    }

    @Override
    public int getNumberByRequested(Long requestedId)
    {
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested id must not be null.");
        }
        String sql = "select count(*) from soulmates where id_requested = ?";
        return this.jdbcTemplate.queryForInt(sql, requestedId);
    }

    @Override
    public int getNumberByRequested(Long requestedId, Boolean requestPending)
    {
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested id must not be null.");
        }
        String sql = "select count(*) from soulmates where id_requested = ? and request_pending = ?";
        return this.jdbcTemplate.queryForInt(sql, requestedId, requestPending);
    }

    @Override
    public int getNumber(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        String sql = "select count(*) from soulmates where (id_requester = ? or id_requested = ?)";
        return this.jdbcTemplate.queryForInt(sql, personId, personId);
    }

    @Override
    public int getNumber(Long personId, Boolean requestPending)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        String sql =
            "select count(*) from soulmates where (id_requester = ? or id_requested = ?) and request_pending = ?";
        return this.jdbcTemplate.queryForInt(sql, personId, personId, requestPending);
    }

    @Override
    public boolean exists(Long requesterId, Long requestedId)
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester id must not be null.");
        }
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested id must not be null.");
        }
        String sql = "select count(*) from soulmates where id_requester = ? and id_requested = ?";
        return this.jdbcTemplate.queryForInt(sql, requesterId, requestedId) > 0;
    }

    @Override
    public boolean existsAnyDirection(Long personAId, Long personBId)
    {
        if (personAId == null)
        {
            throw new IllegalArgumentException("Person A id must not be null.");
        }
        if (personBId == null)
        {
            throw new IllegalArgumentException("Person B id must not be null.");
        }
        String sql =
            "select count(*) from soulmates where (id_requester = ? and id_requested = ?) or (id_requester = ? and id_requested = ?)";
        return this.jdbcTemplate.queryForInt(sql, personAId, personBId, personBId, personAId) > 0;
    }

    @Override
    public boolean areSoulmates(Long personAId, Long personBId)
    {
        if (personAId == null)
        {
            throw new IllegalArgumentException("Person A id must not be null.");
        }
        if (personBId == null)
        {
            throw new IllegalArgumentException("Person B id must not be null.");
        }
        String sql =
            "select count(*) from soulmates where ((id_requester = ? and id_requested = ?) or (id_requester = ? and id_requested = ?)) and request_pending = 0";
        return this.jdbcTemplate.queryForInt(sql, personAId, personBId, personBId, personAId) > 0;
    }

    @Override
    public void create(SoulmatesDto soulmates)
    {
        if (soulmates == null)
        {
            throw new IllegalArgumentException("soulmates must not be null.");
        }
        if (soulmates.getRequesterId() == null)
        {
            throw new IllegalArgumentException("Requester id must not be null.");
        }
        if (soulmates.getRequestedId() == null)
        {
            throw new IllegalArgumentException("Requested id must not be null.");
        }
        if (soulmates.getRequestPending() == null)
        {
            throw new IllegalArgumentException("Requested pending must not be null.");
        }

        // Prepare parameters.
        Map<String, Object> parameters = new HashMap<String, Object>(5);
        parameters.put("id_requester", soulmates.getRequesterId());
        parameters.put("id_requested", soulmates.getRequestedId());
        parameters.put("request_pending", soulmates.getRequestPending());
        parameters.put("request_since", soulmates.getRequestSince());
        parameters.put("soulmates_since", soulmates.getSoulmatesSince());

        // Execute update.
        jdbcSoulmatesInsert.execute(parameters);
    }

    @Override
    public void update(SoulmatesDto soulmates)
    {
        if (soulmates == null)
        {
            throw new IllegalArgumentException("soulmates must not be null.");
        }
        if (soulmates.getRequesterId() == null)
        {
            throw new IllegalArgumentException("Requester id must not be null.");
        }
        if (soulmates.getRequestedId() == null)
        {
            throw new IllegalArgumentException("Requested id must not be null.");
        }
        if (soulmates.getRequestPending() == null)
        {
            throw new IllegalArgumentException("Requested pending must not be null.");
        }
        String sql =
            "update soulmates set request_pending = ?, request_since = ?, soulmates_since = ? "
                + "where id_requester = ? and id_requested = ?";
        this.jdbcTemplate.update(sql, soulmates.getRequestPending(), soulmates.getRequestSince(),
            soulmates.getSoulmatesSince(), soulmates.getRequesterId(), soulmates.getRequestedId());
    }

    @Override
    public void delete(Long requesterId, Long requestedId)
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester id must not be null.");
        }
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested id must not be null.");
        }
        String sql = "delete from soulmates where id_requester = ? and id_requested = ?";
        this.jdbcTemplate.update(sql, requesterId, requestedId);
    }

    @Override
    public void deleteAnyDirection(Long personAId, Long personBId)
    {
        if (personAId == null)
        {
            throw new IllegalArgumentException("Person A id must not be null.");
        }
        if (personBId == null)
        {
            throw new IllegalArgumentException("Person B id must not be null.");
        }
        String sql =
            "delete from soulmates where (id_requester = ? and id_requested = ?) or (id_requester = ? and id_requested = ?)";
        this.jdbcTemplate.update(sql, personAId, personBId, personBId, personAId);        
    }
    
    /**
     * Maps a database row to a soulmate data transfer object.
     * 
     * @author jolau
     * 
     */
    private static final class SoulmatesMapper
        implements RowMapper<SoulmatesDto>
    {

        public SoulmatesDto mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            SoulmatesDto soulmates = new SoulmatesDto();
            soulmates.setRequesterId(rs.getLong("id_requester"));
            if (rs.wasNull())
            {
                soulmates.setRequesterId(null);
            }
            soulmates.setRequestedId(rs.getLong("id_requested"));
            if (rs.wasNull())
            {
                soulmates.setRequestedId(null);
            }
            soulmates.setRequestPending(rs.getBoolean("request_pending"));
            if (rs.wasNull())
            {
                soulmates.setRequestPending(null);
            }
            soulmates.setRequestSince(rs.getDate("request_since"));
            if (rs.wasNull())
            {
                soulmates.setRequestSince(null);
            }
            soulmates.setSoulmatesSince(rs.getDate("soulmates_since"));
            if (rs.wasNull())
            {
                soulmates.setSoulmatesSince(null);
            }
            return soulmates;
        }
    }
}
