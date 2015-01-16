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

import youapp.dataaccess.dao.IMessageDao;
import youapp.dataaccess.dto.MessageDto;

public class JdbcMessageDao
    implements IMessageDao
{

    /**
     * Used for accessing the database. JdbcTemplate is thread safe once
     * configured.
     */
    private SimpleJdbcTemplate jdbcTemplate;

    /**
     * Used for simple inserts. JdbcTemplate is thread safe once configured.
     */
    private SimpleJdbcInsert jdbcMessageInsert;

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcMessageInsert =
            new SimpleJdbcInsert(dataSource).withTableName("message").usingGeneratedKeyColumns("id_message");
        ;
    }

    @Override
    public MessageDto getById(Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Message id must not be null.");
        }
        String sql = "select * from message where id_message = ?";
        return this.jdbcTemplate.queryForObject(sql, new MessageMapper(), id);
    }

    @Override
    public List<MessageDto> getByConversationGroupId(Long conversationGroupId)
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }
        String sql = "select * from message where conversation_group = ? order by timestamp asc";
        return this.jdbcTemplate.query(sql, new MessageMapper(), conversationGroupId);
    }

    @Override
    public List<MessageDto> getByConversationGroupId(Long conversationGroupId, Long personId)
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }
        String sql =
            "select * from message where conversation_group = ? and timestamp > (select last_conversation_clear from conversationparticipant where id_person = ? and id_conversation_group = ?) order by timestamp asc";
        return this.jdbcTemplate.query(sql, new MessageMapper(), conversationGroupId, personId, conversationGroupId);
    }

    @Override
    public List<MessageDto> getByConversationGroupId(Long conversationGroupId, Long personId, int offset, int resultSize)
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }
        String sql =
            "select * from message where conversation_group = ?  and timestamp > (select last_conversation_clear from conversationparticipant where id_person = ? and id_conversation_group = ?) order by timestamp desc limit ?, ?";
        return this.jdbcTemplate.query(sql, new MessageMapper(), conversationGroupId, personId, conversationGroupId,
            offset, resultSize);
    }

    @Override
    public List<MessageDto> getByConversationGroupId(Long conversationGroupId, Long personId, Timestamp startTimestamp,
        int offset, int resultSize)
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }
        if (startTimestamp == null)
        {
            throw new IllegalArgumentException("Timestamp must not be null.");
        }
        String sql =
            "select * from message where conversation_group = ? and timestamp <= ? and timestamp > (select last_conversation_clear from conversationparticipant where id_person = ? and id_conversation_group = ?) order by timestamp desc limit ?, ?";
        return this.jdbcTemplate.query(sql, new MessageMapper(), conversationGroupId, startTimestamp, personId,
            conversationGroupId, offset, resultSize);
    }

    @Override
    public boolean exists(Long senderId, Long conversationGroupId, String text, Timestamp timestamp)
    {
        if (senderId == null)
        {
            throw new IllegalArgumentException("Sender id must not be null.");
        }
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }
        if (text == null)
        {
            throw new IllegalArgumentException("Text must not be null.");
        }
        if (timestamp == null)
        {
            throw new IllegalArgumentException("Timestamp must not be null.");
        }
        String sql =
            "select count(*) from message where sender = ? and conversation_group = ? and text = ? and timestamp = ?";
        return this.jdbcTemplate.queryForInt(sql, senderId, conversationGroupId, text, timestamp) > 0;
    }

    @Override
    public Long create(MessageDto messageDto)
    {
        if (messageDto == null)
        {
            throw new IllegalArgumentException("Message dto must not be null.");
        }
        if (messageDto.getSenderId() == null)
        {
            throw new IllegalArgumentException("Sender id must not be null.");
        }
        if (messageDto.getConversationGroupId() == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }
        if (messageDto.getText() == null)
        {
            throw new IllegalArgumentException("Text must not be null.");
        }
        if (messageDto.getTimestamp() == null)
        {
            throw new IllegalArgumentException("Timestamp must not be null.");
        }

        // Prepare parameters.
        Map<String, Object> parameters = new HashMap<String, Object>(4);
        parameters.put("sender", messageDto.getSenderId());
        parameters.put("conversation_group", messageDto.getConversationGroupId());
        parameters.put("text", messageDto.getText());
        parameters.put("timestamp", messageDto.getTimestamp());

        // Execute update.
        Number newId = jdbcMessageInsert.executeAndReturnKey(parameters);
        messageDto.setId(newId.longValue());
        return messageDto.getId();
    }

    @Override
    public void update(MessageDto messageDto)
    {
        if (messageDto == null)
        {
            throw new IllegalArgumentException("Message dto must not be null.");
        }
        if (messageDto.getId() == null)
        {
            throw new IllegalArgumentException("Id must not be null.");
        }
        if (messageDto.getSenderId() == null)
        {
            throw new IllegalArgumentException("Sender id must not be null.");
        }
        if (messageDto.getConversationGroupId() == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }
        if (messageDto.getText() == null)
        {
            throw new IllegalArgumentException("Text must not be null.");
        }
        if (messageDto.getTimestamp() == null)
        {
            throw new IllegalArgumentException("Timestamp must not be null.");
        }

        String sql =
            "update message set sender = ?, conversation_group = ?, text = ?, timestamp = ? where id_message = ?";

        this.jdbcTemplate.update(sql, messageDto.getSenderId(), messageDto.getConversationGroupId(),
            messageDto.getText(), messageDto.getTimestamp(), messageDto.getId());
    }

    @Override
    public void delete(Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Message id must not be null.");
        }

        this.jdbcTemplate.update("delete from message where id_message = ?", id);
    }

    @Override
    public void deleteByGroup(Long conversationGroupId)
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }

        this.jdbcTemplate.update("delete from message where conversation_group = ?", conversationGroupId);
    }

    /**
     * Maps a database row to a picture data transfer object.
     * 
     * @author jonas.lauener
     * 
     */
    private static final class MessageMapper
        implements RowMapper<MessageDto>
    {

        public MessageDto mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            MessageDto messageDto = new MessageDto();
            messageDto.setId(rs.getLong("id_message"));
            if (rs.wasNull())
            {
                messageDto.setId(null);
            }
            messageDto.setSenderId(rs.getLong("sender"));
            if (rs.wasNull())
            {
                messageDto.setSenderId(null);
            }
            messageDto.setConversationGroupId(rs.getLong("conversation_group"));
            if (rs.wasNull())
            {
                messageDto.setConversationGroupId(null);
            }
            messageDto.setText(rs.getString("text"));
            if (rs.wasNull())
            {
                messageDto.setText(null);
            }
            messageDto.setTimestamp(rs.getTimestamp("timestamp"));
            if (rs.wasNull())
            {
                messageDto.setTimestamp(null);
            }

            return messageDto;
        }
    }

/*	@Override
	public List<MessageDto> getUnreadConversationAmount(Long ownPerson_Id) {
	
		if (ownPerson_Id == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }else{
        	String sql =
        		"select count(*) from conversationparticipant where sender = ? and new_messages = 1;";
           
        	return this.jdbcTemplate.query(sql, new MessageMapper(), ownPerson_Id);
        }
	}
*/
}
