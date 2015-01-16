package youapp.services.standard;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.IConversationParticipantDao;
import youapp.dataaccess.dao.IMessageDao;
import youapp.dataaccess.dto.ConversationParticipantDto;
import youapp.dataaccess.dto.MessageDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.model.Conversation;
import youapp.model.Message;
import youapp.services.MessagingService;
import youapp.services.PersonService;

public class StandardMessagingService
    implements MessagingService
{
    /**
     * Logger.
     */
    static final Log log = LogFactory.getLog(StandardMessagingService.class);

    private IMessageDao messageDao;

    private IConversationParticipantDao conversationParticipantDao;

    private PersonService personService;

    @Autowired
    public void setConversationParticipantDao(IConversationParticipantDao conversationParticipantDao)
    {
        this.conversationParticipantDao = conversationParticipantDao;
    }

    @Autowired
    public void setMessageDao(IMessageDao messageDao)
    {
        this.messageDao = messageDao;
    }

    @Autowired
    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }

    @Override
    public Message getMessageById(Long id) throws InconsistentStateException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id must not be null.");
        }

        MessageDto messageDto = null;
        messageDto = messageDao.getById(id);
        return reassembleMessage(messageDto);
    }

    @Override
    public Conversation getConversationById(Long conversationGroupId, Long ownPersonId)
        throws InconsistentStateException
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation Group Id must not be null.");
        }
        if (ownPersonId == null)
        {
            throw new IllegalArgumentException("Own Person Id must not be null.");
        }

        List<MessageDto> messageDtos = messageDao.getByConversationGroupId(conversationGroupId);
        List<ConversationParticipantDto> conversationParticipantDtos =
            conversationParticipantDao.getByConversationGroupId(conversationGroupId);

        return reassembleConversation(messageDtos, conversationParticipantDtos, ownPersonId);
    }

    @Override
    public Conversation getConversationById(Long conversationGroupId, Long ownPersonId, int offset, int resultSize)
        throws InconsistentStateException
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation Group Id must not be null.");
        }
        if (ownPersonId == null)
        {
            throw new IllegalArgumentException("Own Person Id must not be null.");
        }

        List<MessageDto> messageDtos =
            messageDao.getByConversationGroupId(conversationGroupId, ownPersonId, offset, resultSize);
        List<ConversationParticipantDto> conversationParticipantDtos =
            conversationParticipantDao.getByConversationGroupId(conversationGroupId);

        return reassembleConversation(messageDtos, conversationParticipantDtos, ownPersonId);
    }

    @Override
    public Conversation getConversationById(Long conversationGroupId, Long ownPersonId, Timestamp startTimestamp,
        int offset, int resultSize) throws InconsistentStateException
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation Group Id must not be null.");
        }
        if (ownPersonId == null)
        {
            throw new IllegalArgumentException("Own Person Id must not be null.");
        }
        if (startTimestamp == null)
        {
            throw new IllegalArgumentException("Start Timestamp must not be null.");
        }

        List<MessageDto> messageDtos =
            messageDao.getByConversationGroupId(conversationGroupId, ownPersonId, startTimestamp, offset, resultSize);
        List<ConversationParticipantDto> conversationParticipantDtos =
            conversationParticipantDao.getByConversationGroupId(conversationGroupId);

        return reassembleConversation(messageDtos, conversationParticipantDtos, ownPersonId);
    }

    @Override
    public List<Conversation> getLastConversationsOfParticipant(Long personId) throws InconsistentStateException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person Id must not be null.");
        }

        List<Conversation> conversations = new LinkedList<Conversation>();

        List<ConversationParticipantDto> conversationParticipantDtos =
            conversationParticipantDao.getByPersonId(personId);
        for (ConversationParticipantDto conversationParticipantDto : conversationParticipantDtos)
        {
            // Get latest message of given conversationgroup
            List<MessageDto> messageDtoList =
                messageDao
                    .getByConversationGroupId(conversationParticipantDto.getConversationGroupId(), personId, 0, 1);

            if (messageDtoList.size() > 0)
            {
                List<ConversationParticipantDto> messageParticipantDtos =
                    conversationParticipantDao.getByConversationGroupId(conversationParticipantDto
                        .getConversationGroupId());

                conversations.add(reassembleConversation(messageDtoList, messageParticipantDtos, personId));
            }
        }

        // Sorts the conversations DESC by timestamp of the first message
        Comparator<Conversation> ConversationComperator = new Comparator<Conversation>()
        {
            @Override
            public int compare(Conversation o1, Conversation o2)
            {
                if ((o1 == null || o2 == null) || o1.getConversationMessages().size() == 0
                    || o2.getConversationMessages().size() == 0)
                    return 0;

                return o2.getConversationMessages().get(0).getTimestamp()
                    .compareTo(o1.getConversationMessages().get(0).getTimestamp());
            }
        };
        Collections.sort(conversations, ConversationComperator);

        return conversations;
    }

    @Override
    public Long createMessage(Message message, Long conversationGroupId)
        throws InconsistentModelException,
        DataAccessLayerException
    {
        if (message == null)
        {
            throw new IllegalArgumentException("Message must not be null.");
        }
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation Group Id must not be null.");
        }
        validateMessage(message);
        if (!personService.exists(message.getSenderId(), false)
            || !personService.isActive(message.getSenderId(), false))
        {
            throw new IllegalArgumentException("Sender with id " + message.getSenderId()
                + " does not exist or is not active.");
        }

        if (!conversationParticipantDao.exists(message.getSenderId(), conversationGroupId))
            return null;

        updateConverationParticipantsHaveNewMessage(message.getSenderId(), conversationGroupId);

        return messageDao.create(disassembleMessage(message, conversationGroupId));
    }

    @Override
    public Long createMessage(Message message, Set<Long> conversationParticipantIds)
        throws InconsistentModelException,
        DataAccessLayerException
    {
        if (message == null)
        {
            throw new IllegalArgumentException("Message must not be null.");
        }
        if ((conversationParticipantIds == null) || conversationParticipantIds.size() == 0)
        {
            throw new IllegalArgumentException("Conversation Group Id must not be null or empty.");
        }
        validateMessage(message);

        // Validate if sender and conversation participants are existent people
        // and are active
        if (!personService.exists(message.getSenderId(), false)
            || !personService.isActive(message.getSenderId(), false))
        {
            throw new IllegalArgumentException("Sender with id " + message.getSenderId()
                + " does not exist or is not active.");
        }
        for (Long conversationParticipantId : conversationParticipantIds)
        {
            if (!personService.exists(conversationParticipantId, false)
                || !personService.isActive(conversationParticipantId, false))
            {
                throw new IllegalArgumentException("Conversation participant with id " + conversationParticipantId
                    + " does not exist or is not active.");
            }
        }

        // Add sender id of message to conversation participants, if not
        // existing
        if (!conversationParticipantIds.contains(message.getSenderId()))
        {
            if (log.isDebugEnabled())
                log.debug("Add sender id of message to conversation participants, because it's not existing in conversation participants");
            conversationParticipantIds.add(message.getSenderId());
        }

        Long conversationGroupId;

        if (conversationParticipantDao.existsConversationGroup(conversationParticipantIds))
        {
            conversationGroupId = conversationParticipantDao.getConversationGroupId(conversationParticipantIds);
            updateConverationParticipantsHaveNewMessage(message.getSenderId(), conversationGroupId);
        }
        else
        {
            List<ConversationParticipantDto> conversationParticipantDtos = new LinkedList<ConversationParticipantDto>();
            for (Long conversationParticipantId : conversationParticipantIds)
            {
                ConversationParticipantDto conversationParticipantDto = new ConversationParticipantDto();
                conversationParticipantDto.setPersonId(conversationParticipantId);
                if (conversationParticipantId != message.getSenderId())
                {
                    conversationParticipantDto.setNewMessages(true);
                }
                else
                {
                    conversationParticipantDto.setNewMessages(false);
                }
                conversationParticipantDtos.add(conversationParticipantDto);
            }
            conversationGroupId = conversationParticipantDao.create(conversationParticipantDtos);
        }

        return messageDao.create(disassembleMessage(message, conversationGroupId));
    }

    private void updateConverationParticipantsHaveNewMessage(Long senderId, Long conversationGroupId)
    {
        List<ConversationParticipantDto> conversationParticipantDtos =
            conversationParticipantDao.getByConversationGroupId(conversationGroupId);
        for (ConversationParticipantDto conversationParticipantDto : conversationParticipantDtos)
        {
            if (conversationParticipantDto.getPersonId() != senderId)
            {
                conversationParticipantDto.setNewMessages(true);
                conversationParticipantDao.update(conversationParticipantDto);
            }
        }
    }

    @Override
    public boolean addConversationParticipant(Long personId, Long conversationGroupId)
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation Group Id must not be null.");
        }
        if (personId == null)
        {
            throw new IllegalArgumentException("Person Id must not be null.");
        }

        if (!conversationParticipantDao.existsConversationGroup(conversationGroupId))
            return false;

        // TODO send notification to added person

        ConversationParticipantDto conversationParticipantDto = new ConversationParticipantDto();
        conversationParticipantDto.setPersonId(personId);
        conversationParticipantDto.setConversationGroupId(conversationGroupId);
        conversationParticipantDto.setNewMessages(false);
        conversationParticipantDao.create(conversationParticipantDto);

        return true;
    }

    @Override
    public boolean clearConversationForParticipant(Long personId, Long conversationGroupId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person Id must not be null.");
        }
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Covnersation Group Id must not be null.");
        }

        if (!conversationParticipantDao.exists(personId, conversationGroupId))
            return false;

        ConversationParticipantDto conversationParticipantDto =
            conversationParticipantDao.getById(personId, conversationGroupId);
        conversationParticipantDto.setLastConversationClear(new Timestamp(System.currentTimeMillis()));
        conversationParticipantDao.update(conversationParticipantDto);

        return true;
    }

    @Override
    public void deleteMessage(Long messageId)
    {
        if (messageId == null)
        {
            throw new IllegalArgumentException("Message Id must not be null.");
        }

        messageDao.delete(messageId);
    }

    @Override
    public void deleteConversation(Long conversationGroupId)
    {
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Covnersation Group Id must not be null.");
        }

        if (!conversationParticipantDao.existsConversationGroup(conversationGroupId))
            return;

        conversationParticipantDao.deleteByConversationGroupId(conversationGroupId);
        messageDao.deleteByGroup(conversationGroupId);
    }

    private Message reassembleMessage(MessageDto messageDto) throws InconsistentStateException
    {
        if (messageDto == null)
        {
            throw new IllegalArgumentException("Message Dto must not be null.");
        }

        validateMessage(messageDto);

        // Reassemble.
        if (log.isDebugEnabled())
        {
            log.debug("Reassembling message dto: " + messageDto.toString());
        }

        Message message = new Message();
        message.setMessageId(messageDto.getId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Message Id: " + message.getMessageId());
        }
        message.setSenderId(messageDto.getSenderId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Sender id: " + message.getSenderId());
        }
        message.setText(messageDto.getText());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Text: " + message.getText());
        }
        message.setTimestamp(messageDto.getTimestamp());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Timestamp: " + message.getTimestamp());
        }

        return message;
    }

    private MessageDto disassembleMessage(Message message, Long conversationGroupId)
    {
        if (message == null)
        {
            throw new IllegalArgumentException("Message Dto must not be null.");
        }
        if (conversationGroupId == null)
        {
            throw new IllegalArgumentException("Conversation group id must not be null.");
        }

        // Reassemble.
        if (log.isDebugEnabled())
        {
            log.debug("Reassembling message: " + message.toString() + " with conversation group id: "
                + conversationGroupId);
        }

        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getMessageId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Message Id: " + messageDto.getId());
        }
        messageDto.setConversationGroupId(conversationGroupId);
        if (log.isDebugEnabled())
        {
            log.debug(">>> Conversation group Id: " + messageDto.getConversationGroupId());
        }
        messageDto.setSenderId(message.getSenderId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Sender id: " + messageDto.getSenderId());
        }
        messageDto.setText(message.getText());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Text: " + messageDto.getText());
        }
        messageDto.setTimestamp(message.getTimestamp());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Timestamp: " + messageDto.getTimestamp());
        }

        return messageDto;
    }

    private Conversation reassembleConversation(List<MessageDto> messageDtos,
        List<ConversationParticipantDto> conversationParticipantDtos, Long ownPersonId)
        throws InconsistentStateException
    {
        if (messageDtos == null)
        {
            throw new IllegalArgumentException("MessageDtos must not be null.");
        }
        if ((conversationParticipantDtos == null) || conversationParticipantDtos.size() == 0)
        {
            throw new IllegalArgumentException("Conversation Participant Dtos must not be null or empty.");
        }
        if (ownPersonId == null)
        {
            throw new IllegalArgumentException("Person Id must not be null.");
        }

        Conversation conversation = new Conversation();
        conversation.setConversationGroupId(conversationParticipantDtos.get(0).getConversationGroupId());

        Set<Long> conversationMembers = new HashSet<Long>();
        for (ConversationParticipantDto conversationParticipantDto : conversationParticipantDtos)
        {
            validate(conversationParticipantDto);
            if (conversation.getConversationGroupId() != conversationParticipantDto.getConversationGroupId())
            {
                throw new InconsistentStateException("Conversation Group Id is everywhere the same. Should be: "
                    + conversation.getConversationGroupId() + ", but is: "
                    + conversationParticipantDto.getConversationGroupId());
            }

            conversationMembers.add(conversationParticipantDto.getPersonId());
            if (conversationParticipantDto.getPersonId() == ownPersonId)
            {
                conversation.setNewMessages(conversationParticipantDto.hasNewMessages());
                conversation.setLastConversationClear(conversationParticipantDto.getLastConversationClear());
            }
        }
        // validate conversationMembers
        if (!conversationMembers.contains(ownPersonId))
        {
            throw new InconsistentStateException("There is no conversation member with the same id as the own Person.");
        }

        List<Message> messages = new LinkedList<Message>();
        for (MessageDto messageDto : messageDtos)
        {
            // Validation
            validateMessage(messageDto);
            if (conversation.getConversationGroupId() != messageDto.getConversationGroupId())
            {
                throw new InconsistentStateException("Conversation Group Id is everywhere the same. Should be: "
                    + conversation.getConversationGroupId() + ", but is: " + messageDto.getConversationGroupId());
            }
            if ((conversation.getLastConversationClear() != null)
                && messageDto.getTimestamp().before(conversation.getLastConversationClear()))
            {
                throw new InconsistentStateException("The message with id: " + messageDto.getId()
                    + " is created before the last conversation clear.");
            }

            messages.add(reassembleMessage(messageDto));
        }

        conversation.setConversationMembers(conversationMembers);
        conversation.setConversationMessages(messages);

        return conversation;
    }

    private void validateMessage(MessageDto messageDto) throws InconsistentStateException
    {
        if (messageDto == null)
        {
            throw new IllegalArgumentException("Message must not be null.");
        }
        if (messageDto.getId() == null)
        {
            throw new InconsistentStateException("Message Id must not be null.");
        }
        if (messageDto.getConversationGroupId() == null)
        {
            throw new InconsistentStateException("Conversation Group Id must not be null.");
        }
        if (messageDto.getSenderId() == null)
        {
            throw new InconsistentStateException("Sender Id must not be null.");
        }
        if (messageDto.getText() == null)
        {
            throw new InconsistentStateException("Text must not be null.");
        }
        if (messageDto.getTimestamp() == null)
        {
            throw new InconsistentStateException("Timestamp must not be null.");
        }
    }

    private void validateMessage(Message message) throws InconsistentModelException
    {
        if (message == null)
        {
            throw new IllegalArgumentException("Message must not be null.");
        }
        if (message.getMessageId() != null)
        {
            throw new InconsistentModelException("Message Id must be null.");
        }
        if (message.getSenderId() == null)
        {
            throw new InconsistentModelException("Sender Id must not be null.");
        }
        if (message.getText() == null)
        {
            throw new InconsistentModelException("Text must not be null.");
        }
        if (message.getTimestamp() == null)
        {
            throw new InconsistentModelException("Timestamp must not be null.");
        }
    }

    private void validate(ConversationParticipantDto conversationParticipantDto) throws InconsistentStateException
    {
        if (conversationParticipantDto == null)
        {
            throw new IllegalArgumentException("Conversation Participant must not be null.");
        }
        if (conversationParticipantDto.getPersonId() == null)
        {
            throw new InconsistentStateException("Person Id must not be null.");
        }
        if (conversationParticipantDto.getConversationGroupId() == null)
        {
            throw new InconsistentStateException("Conversation Group Id must not be null.");
        }
        if (conversationParticipantDto.hasNewMessages() == null)
        {
            throw new InconsistentStateException("Has new messages must not be null.");
        }
    }

/*	@Override
	public String getUnreadConversationAmount(long person_id)
			throws InconsistentStateException {
        if (person_id == 0)
        {
            throw new IllegalArgumentException("Person-ID must not be null.");
        }		
        if (person_id != 0)
        {
        	List<MessageDto> messageDtos =
                    messageDao.getUnreadConversationAmount(person_id);
        	String unreadMessages = messageDtos.get(0).toString();
            return unreadMessages;
        }
		return null;
		
	}
*/
}
