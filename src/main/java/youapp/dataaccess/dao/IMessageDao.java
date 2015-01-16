package youapp.dataaccess.dao;

import java.sql.Timestamp;
import java.util.List;

import youapp.dataaccess.dto.MessageDto;

public interface IMessageDao
{
    public MessageDto getById(Long id);

    public List<MessageDto> getByConversationGroupId(Long conversationGroupId);
    
    public List<MessageDto> getByConversationGroupId(Long conversationGroupId, Long ownPersonId);

    public List<MessageDto> getByConversationGroupId(Long conversationGroupId, Long ownPersonId, int offset, int resultSize);

    public List<MessageDto> getByConversationGroupId(Long conversationGroupId, Long ownPersonId, Timestamp startTimestamp,
        int offset, int resultSize);

    public boolean exists(Long senderId, Long conversationGroupId, String text, Timestamp timestamp);

    public Long create(MessageDto messageDto);

    public void update(MessageDto messageDto);

    public void delete(Long id);

    public void deleteByGroup(Long conversationGroupId);
    
 // public List<MessageDto> getUnreadConversationAmount(Long ownPerson_Id);

}
