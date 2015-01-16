package youapp.dataaccess.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.util.StringUtils;

import youapp.dataaccess.dao.IConversationParticipantDao;
import youapp.dataaccess.dto.ConversationParticipantDto;

public class JdbcConversationParticipantDao
    implements IConversationParticipantDao
{

    /**
     * Used for accessing the database. JdbcTemplate is thread safe once
     * configured.
     */
    private SimpleJdbcTemplate jdbcTemplate;

    /**
     * Used for simple inserts. JdbcTemplate is thread safe once configured.
     */
    private SimpleJdbcInsert jdbcConversationParticipantInsert;

    /**
     * Used for simple inserts. JdbcTemplate is thread safe once configured.
     */
    private SimpleJdbcInsert jdbcConversationGroupInsert;

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcConversationParticipantInsert =
            new SimpleJdbcInsert(dataSource).withTableName("conversationparticipant");
        this.jdbcConversationGroupInsert =
            new SimpleJdbcInsert(dataSource).withTableName("conversationgroup").usingGeneratedKeyColumns(
                "id_conversation_group");
    }

    @Override
    public ConversationParticipantDto getById(Long personId, Long conversationGroupId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }

        String sql = "select * from conversationparticipant where id_person = ? and id_conversation_group = ?";
        return this.jdbcTemplate
            .queryForObject(sql, new ConversationParticipantMapper(), personId, conversationGroupId);
    }

    @Override
    public List<ConversationParticipantDto> getByConversationGroupId(Long conversationGroupId)
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }

        String sql = "select * from conversationparticipant where id_conversation_group = ?";
        return this.jdbcTemplate.query(sql, new ConversationParticipantMapper(), conversationGroupId);
    }

    @Override
    public List<ConversationParticipantDto> getByPersonId(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }

        String sql = "select * from conversationparticipant where id_person = ?";
        return this.jdbcTemplate.query(sql, new ConversationParticipantMapper(), personId);
    }

    @Override
    public Long getConversationGroupId(Set<Long> personIds)
    {
        if ((personIds == null) || personIds.size() == 0)
        {
            throw new IllegalArgumentException("Person id must not be null or empty.");
        }

        String sql =
            "select id_conversation_group as icg from conversationparticipant where id_person in ("
                + StringUtils.collectionToCommaDelimitedString(personIds)
                + ") group by id_conversation_group having (count(*) = ?) and ? = (select count(*) from conversationparticipant where id_conversation_group = icg)";
        try
        {
            return this.jdbcTemplate.queryForLong(sql, personIds.size(), personIds.size());
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    @Override
    public boolean exists(Long personId, Long conversationGroupId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }

        String sql = "select count(*) from conversationparticipant where id_person = ? and id_conversation_group = ?";
        return this.jdbcTemplate.queryForInt(sql, personId, conversationGroupId) > 0;
    }

    @Override
    public boolean existsConversationGroup(Long conversationGroupId)
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }

        String sql = "select count(*) from conversationparticipant id_conversation_group = ?";
        return this.jdbcTemplate.queryForInt(sql, conversationGroupId) > 0;
    }

    @Override
    public boolean existsConversationGroup(Set<Long> personIds)
    {
        if ((personIds == null) || personIds.size() == 0)
        {
            throw new IllegalArgumentException("Person id must not be null or empty.");
        }

        String sql =
            "select id_conversation_group as icg from conversationparticipant where id_person in ("
                + StringUtils.collectionToCommaDelimitedString(personIds)
                + ") group by id_conversation_group having (count(*) = ?) and ? = (select count(*) from conversationparticipant where id_conversation_group = icg)";
        try
        {
            return this.jdbcTemplate.queryForLong(sql, personIds.size(), personIds.size()) > 0;
        }
        catch (EmptyResultDataAccessException e)
        {
            return false;
        }
    }

    @Override
    public void create(ConversationParticipantDto conversationParticipantDto)
    {
        if (conversationParticipantDto == null)
        {
            throw new IllegalArgumentException("Conversation Participant Dto must not be null.");
        }
        if (conversationParticipantDto.getPersonId() == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (conversationParticipantDto.getConversationGroupId() == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }
        if (conversationParticipantDto.hasNewMessages() == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }

        if (conversationParticipantDto.getLastConversationClear() == null)
        {
            conversationParticipantDto.setLastConversationClear(new Timestamp(0));
        }

        // Prepare parameters.
        Map<String, Object> parameters = new HashMap<String, Object>(4);
        parameters.put("id_person", conversationParticipantDto.getPersonId());
        parameters.put("id_conversation_group", conversationParticipantDto.getConversationGroupId());
        parameters.put("new_messages", conversationParticipantDto.hasNewMessages());
        parameters.put("last_conversation_clear", conversationParticipantDto.getLastConversationClear());

        // Execute update.
        jdbcConversationParticipantInsert.execute(parameters);
    }

    @Override
    public Long create(List<ConversationParticipantDto> conversationParticipantDtos)
    {
        if ((conversationParticipantDtos == null) || conversationParticipantDtos.size() == 0)
        {
            throw new IllegalArgumentException("Conversation Participant Dtos must not be null or empty.");
        }

        Number newNumber = jdbcConversationGroupInsert.executeAndReturnKey(new HashMap<String, Object>());
        Long conversationGroupId = newNumber.longValue();

        for (ConversationParticipantDto conversationParticipantDto : conversationParticipantDtos)
        {
            if (conversationParticipantDto == null)
            {
                throw new IllegalArgumentException("Conversation Participant Dto must not be null.");
            }
            if (conversationParticipantDto.getPersonId() == null)
            {
                throw new IllegalArgumentException("Person id must not be null.");
            }
            if (conversationParticipantDto.getConversationGroupId() != null)
            {
                throw new IllegalArgumentException("Conversation group id must be null.");
            }
            if (conversationParticipantDto.hasNewMessages() == null)
            {
                throw new IllegalArgumentException("Conversation group id must not be null.");
            }
            if (conversationParticipantDto.getLastConversationClear() == null)
            {
                conversationParticipantDto.setLastConversationClear(new Timestamp(0));
            }
            // Prepare parameters.
            Map<String, Object> convParticipantParameters = new HashMap<String, Object>(4);
            convParticipantParameters.put("id_person", conversationParticipantDto.getPersonId());
            convParticipantParameters.put("id_conversation_group", conversationGroupId);
            convParticipantParameters.put("new_messages", conversationParticipantDto.hasNewMessages());
            convParticipantParameters.put("last_conversation_clear",
                conversationParticipantDto.getLastConversationClear());

            // Execute update.
            jdbcConversationParticipantInsert.execute(convParticipantParameters);
        }

        return conversationGroupId;
    }

    @Override
    public void update(ConversationParticipantDto conversationParticipantDto)
    {
        if (conversationParticipantDto == null)
        {
            throw new IllegalArgumentException("Conversation Participant Dto must not be null.");
        }
        if (conversationParticipantDto.getPersonId() == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (conversationParticipantDto.getConversationGroupId() == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }
        if (conversationParticipantDto.hasNewMessages() == null)
        {
            throw new IllegalArgumentException("New messages must not be null.");
        }
        if (conversationParticipantDto.getLastConversationClear() == null)
        {
            throw new IllegalArgumentException("Last conversation clear must not be null.");
        }

        String sql =
            "update conversationparticipant set new_messages = ?, last_conversation_clear = ?  where id_person = ? and id_conversation_group = ?";
        this.jdbcTemplate.update(sql, conversationParticipantDto.hasNewMessages(),
            conversationParticipantDto.getLastConversationClear(), conversationParticipantDto.getPersonId(),
            conversationParticipantDto.getConversationGroupId());
    }

    @Override
    public void delete(Long personId, Long conversationGroupId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }

        this.jdbcTemplate.update(
            "delete from conversationparticipant where id_person = ? and id_conversation_group = ?", personId,
            conversationGroupId);
    }

    @Override
    public void deleteByConversationGroupId(Long conversationGroupId)
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }

        this.jdbcTemplate.update("delete from conversationparticipant where id_conversation_group = ?",
            conversationGroupId);
    }

    /**
     * Maps a database row to a picture data transfer object.
     * 
     * @author jonas.lauener
     * 
     */
    private static final class ConversationParticipantMapper
        implements RowMapper<ConversationParticipantDto>
    {

        public ConversationParticipantDto mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            ConversationParticipantDto conversationParticipantDto = new ConversationParticipantDto();
            conversationParticipantDto.setPersonId(rs.getLong("id_person"));
            if (rs.wasNull())
            {
                conversationParticipantDto.setPersonId(null);
            }
            conversationParticipantDto.setConversationGroupId(rs.getLong("id_conversation_group"));
            if (rs.wasNull())
            {
                conversationParticipantDto.setConversationGroupId(null);
            }
            conversationParticipantDto.setNewMessages(rs.getBoolean("new_messages"));
            if (rs.wasNull())
            {
                conversationParticipantDto.setNewMessages(null);
            }
            conversationParticipantDto.setLastConversationClear(rs.getTimestamp("last_conversation_clear"));
            if (rs.wasNull())
            {
                conversationParticipantDto.setLastConversationClear(null);
            }

            return conversationParticipantDto;
        }
    }
}
