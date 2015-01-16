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

import youapp.dataaccess.dao.IPersonBlockedDao;
import youapp.dataaccess.dto.PersonBlockedDto;

public class JdbcPersonBlockedDao
    implements IPersonBlockedDao
{

    /**
     * Used for accessing the database. JdbcTemplate is thread safe once
     * configured.
     */
    private SimpleJdbcTemplate jdbcTemplate;

    /**
     * Used for simple inserts. JdbcTemplate is thread safe once configured.
     */
    private SimpleJdbcInsert jdbcPersonBlockedInsert;

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcPersonBlockedInsert = new SimpleJdbcInsert(dataSource).withTableName("personblocked");
    }

    @Override
    public PersonBlockedDto getById(Long blockerId, Long blockedId)
    {
        if (blockerId == null)
        {
            throw new IllegalArgumentException("Blocker id must not be null.");
        }
        if (blockedId == null)
        {
            throw new IllegalArgumentException("Blocked id must not be null.");
        }
        String sql = "select * from personblocked where id_blocker = ? and id_blocked = ?";
        return this.jdbcTemplate.queryForObject(sql, new PersonBlockedMapper(), blockerId, blockedId);
    }

    @Override
    public List<PersonBlockedDto> getByBlocker(Long blockerId)
    {
        if (blockerId == null)
        {
            throw new IllegalArgumentException("Blocker id must not be null.");
        }
        String sql = "select * from personblocked where id_blocker = ?";
        return this.jdbcTemplate.query(sql, new PersonBlockedMapper(), blockerId);
    }

    @Override
    public List<PersonBlockedDto> getByBlocked(Long blockedId)
    {
        if (blockedId == null)
        {
            throw new IllegalArgumentException("Blocked id must not be null.");
        }
        String sql = "select * from personblocked where id_blocked = ?";
        return this.jdbcTemplate.query(sql, new PersonBlockedMapper(), blockedId);
    }

    @Override
    public boolean exists(Long blockerId, Long blockedId)
    {
        if (blockerId == null)
        {
            throw new IllegalArgumentException("Blocker id must not be null.");
        }
        if (blockedId == null)
        {
            throw new IllegalArgumentException("Blocked id must not be null.");
        }
        String sql = "select count(*) from personblocked where id_blocker = ? and id_blocked = ?";
        return this.jdbcTemplate.queryForInt(sql, blockerId, blockedId) > 0;
    }

    @Override
    public boolean blockedAnyDirection(Long personAId, Long personBId)
    {
        if (personAId == null)
        {
            throw new IllegalArgumentException("Person A id must not be null.");
        }
        if (personBId == null)
        {
            throw new IllegalArgumentException("Person B id must not be null.");
        }
        String sql = "select count(*) from personblocked where (id_blocker = ? and id_blocked = ?) or (id_blocked = ? and id_blocker = ?)";
        return this.jdbcTemplate.queryForInt(sql, personAId, personBId, personAId, personBId) > 0;
    }

    @Override
    public void create(PersonBlockedDto personBlocked)
    {
        if (personBlocked == null)
        {
            throw new IllegalArgumentException("Blocker id must not be null.");
        }
        if (personBlocked.getBlockerId() == null)
        {
            throw new IllegalArgumentException("Blocker id must not be null.");
        }
        if (personBlocked.getBlockedId() == null)
        {
            throw new IllegalArgumentException("Blocked id must not be null.");
        }

        // Prepare parameters.
        Map<String, Object> parameters = new HashMap<String, Object>(3);
        parameters.put("id_blocker", personBlocked.getBlockerId());
        parameters.put("id_blocked", personBlocked.getBlockedId());
        parameters.put("since", personBlocked.getSince());

        // Execute update.
        jdbcPersonBlockedInsert.execute(parameters);
    }

    @Override
    public void delete(Long blockerId, Long blockedId)
    {
        if (blockerId == null)
        {
            throw new IllegalArgumentException("Blocker id must not be null.");
        }
        if (blockedId == null)
        {
            throw new IllegalArgumentException("Blocked id must not be null.");
        }
        String sql = "delete from personblocked where id_blocker = ? and id_blocked = ?";
        this.jdbcTemplate.update(sql, blockerId, blockedId);
    }

    /**
     * Maps a database row to a soulmate data transfer object.
     * 
     * @author jolau
     * 
     */
    private static final class PersonBlockedMapper
        implements RowMapper<PersonBlockedDto>
    {

        public PersonBlockedDto mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            PersonBlockedDto personBlockedDto = new PersonBlockedDto();
            personBlockedDto.setBlockerId(rs.getLong("id_blocker"));
            if (rs.wasNull())
            {
                personBlockedDto.setBlockerId(null);
            }
            personBlockedDto.setBlockedId(rs.getLong("id_blocked"));
            if (rs.wasNull())
            {
                personBlockedDto.setBlockedId(null);
            }
            personBlockedDto.setSince(rs.getDate("since"));
            if (rs.wasNull())
            {
                personBlockedDto.setSince(null);
            }
            return personBlockedDto;
        }
    }
}
