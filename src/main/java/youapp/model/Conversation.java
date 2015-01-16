package youapp.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public class Conversation
{
    private List<Message> conversationMessages;

    private Set<Long> conversationMembers;

    private Long conversationGroupId;

    private boolean newMessages;

    private Timestamp lastConversationClear;

    /**
     * @return the conversationMessages
     */
    public List<Message> getConversationMessages()
    {
        return conversationMessages;
    }

    /**
     * @param conversationMessages the conversationMessages to set
     */
    public void setConversationMessages(List<Message> conversationMessages)
    {
        this.conversationMessages = conversationMessages;
    }

    public Set<Long> getConversationMembers()
    {
        return conversationMembers;
    }

    public void setConversationMembers(Set<Long> conversationMembers)
    {
        this.conversationMembers = conversationMembers;
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

    /**
     * @return the newMessages
     */
    public boolean isNewMessages()
    {
        return newMessages;
    }

    /**
     * @param newMessages the newMessages to set
     */
    public void setNewMessages(boolean newMessages)
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
}
