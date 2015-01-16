package youapp.dataaccess.dto;

import java.sql.Timestamp;

public class MessageDto
{
    private Long id;

    private Long senderId;

    private Long conversationGroupId;

    private String text;

    private Timestamp timestamp;

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return the senderId
     */
    public Long getSenderId()
    {
        return senderId;
    }

    /**
     * @param senderId the senderId to set
     */
    public void setSenderId(Long senderId)
    {
        this.senderId = senderId;
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
     * @return the text
     */
    public String getText()
    {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text)
    {
        this.text = text;
    }

    /**
     * @return the timestamp
     */
    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        return "Message with id: " + id + " of sender with id: " + senderId + " to conversation group with id: "
            + conversationGroupId + "with text: \"" + text + " at: " + timestamp;
    }
}
