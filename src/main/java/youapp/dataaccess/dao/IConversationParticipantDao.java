package youapp.dataaccess.dao;

import java.util.List;
import java.util.Set;

import youapp.dataaccess.dto.ConversationParticipantDto;

public interface IConversationParticipantDao
{
    public ConversationParticipantDto getById(Long personId, Long conversationGroupId);

    public List<ConversationParticipantDto> getByConversationGroupId(Long conversationGroupId);

    public List<ConversationParticipantDto> getByPersonId(Long personId);

    public Long getConversationGroupId(Set<Long> personIds);
    
    public boolean exists(Long personId, Long conversationGroupId);

    public boolean existsConversationGroup(Set<Long> personIds);
    
    public boolean existsConversationGroup(Long conversationGroupId);

    public void create(ConversationParticipantDto conversationParticipantDto);

    public Long create(List<ConversationParticipantDto> conversationParticipantDtos);

    public void update(ConversationParticipantDto conversationParticipantDto);

    public void delete(Long personId, Long conversationGroupId);

    public void deleteByConversationGroupId(Long conversationGroupId);
}
