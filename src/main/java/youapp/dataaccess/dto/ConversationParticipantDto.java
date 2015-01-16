package youapp.dataaccess.dto;

import java.sql.Timestamp;

public class ConversationParticipantDto
{
    private Long personId;

    private Long conversationGroupId;

    private Boolean newMessages;

    private Timestamp lastConversationClear;

    /**
     * @return the personId
     */
    public Long getPersonId()
    {
        return personId;
    }

    /**
     * @param personId the personId to set
     */
    public void setPersonId(Long personId)
    {
        this.personId = personId;
    }

    /**
     * @return the conversationGroupId
     */
    public Long getConversationGroupId()
    {
        return conversationGroupId;
    }

    /**
     * @param conversationGroupId the conversationGroupId to set
     */
    public void setConversationGroupId(Long conversationGroupId)
    {
        this.conversationGroupId = conversationGroupId;
    }

    public Boolean hasNewMessages()
    {
        return newMessages;
    }

    /**
     * @param newMessages the newMessages to set
     */
    public void setNewMessages(Boolean newMessages)
    {
        this.newMessages = newMessages;
    }

    /**
     * @return the lastConversationClear
     */
    public Timestamp getLastConversationClear()
    {
        return lastConversationClear;
    }

    /**
     * @param lastConversationClear the lastConversationClear to set
     */
    public void setLastConversationClear(Timestamp lastConversationClear)
    {
        this.lastConversationClear = lastConversationClear;
    }

    @Override
    public String toString()
    {
        return "Conversation participant with person id: " + personId + " of conversation group with id: "
            + conversationGroupId + " has new messages: " + newMessages + " last conversation clear was: "
            + lastConversationClear;
    }
}
