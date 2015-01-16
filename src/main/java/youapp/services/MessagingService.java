package youapp.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.model.Conversation;
import youapp.model.Message;

public interface MessagingService
{
    public Message getMessageById(Long id) throws InconsistentStateException, DataAccessLayerException;

    public Conversation getConversationById(Long conversationGroupId, Long ownPersonId)
        throws InconsistentStateException,
        DataAccessLayerException;

    public Conversation getConversationById(Long conversationGroupId, Long ownPersonId, int offset, int resultSize)
        throws InconsistentStateException,
        DataAccessLayerException;

    public Conversation getConversationById(Long conversationGroupId, Long ownPersonId, Timestamp startTimestamp,
        int offset, int resultSize) throws InconsistentStateException, DataAccessLayerException;

    public List<Conversation> getLastConversationsOfParticipant(Long personId)
        throws InconsistentStateException,
        DataAccessLayerException;

    public Long createMessage(Message message, Long conversationGroupId)
        throws InconsistentModelException,
        DataAccessLayerException;

    public Long createMessage(Message message, Set<Long> conversationParticipantIds)
        throws InconsistentModelException,
        DataAccessLayerException;

    public boolean addConversationParticipant(Long personId, Long conversationGroupId) throws DataAccessLayerException;

    public boolean clearConversationForParticipant(Long personId, Long conversationGroupId)
        throws DataAccessLayerException;

    public void deleteMessage(Long messageId) throws DataAccessLayerException;

    public void deleteConversation(Long conversationGroupId) throws DataAccessLayerException;

/*
	public String getUnreadConversationAmount(long person_id)
	        throws InconsistentStateException,
	        DataAccessLayerException;    
*/    

}
