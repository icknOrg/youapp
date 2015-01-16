package youapp.model;

import java.sql.Timestamp;

public class Message
{
    private Long messageId;

    private Long senderId;

    private String text;

    private Timestamp timestamp;

    /**
     * @return the messageId
     */
    public Long getMessageId()
    {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(Long messageId)
    {
        this.messageId = messageId;
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
}
