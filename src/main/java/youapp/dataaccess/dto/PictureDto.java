package youapp.dataaccess.dto;

import java.sql.Blob;

/**
 * Data transfer object for table Person.
 * 
 * @author neme
 * 
 */
public class PictureDto
{

    private Integer pictureId;

    private Long personId;

    private String caption;

    private Blob picture;

    private String pictureFormatName;

    private Blob thumbnail;

    private String thumbnailFormatName;

    private Integer mood;

    /**
     * Returns this picture's id.
     * 
     * @return this picture's id.
     */
    public Integer getPictureId()
    {
        return pictureId;
    }

    /**
     * Sets this picture's id.
     * 
     * @param pictureId the id to set.
     */
    public void setPictureId(Integer pictureId)
    {
        this.pictureId = pictureId;
    }

    /**
     * Returns this picture's person id.
     * 
     * @return this picture's person id.
     */
    public Long getPersonId()
    {
        return personId;
    }

    /**
     * Sets this picture's person id.
     * 
     * @param personId the person id to set.
     */
    public void setPersonId(Long personId)
    {
        this.personId = personId;
    }

    /**
     * Returns this picture's caption.
     * 
     * @return this picture's caption.
     */
    public String getCaption()
    {
        return caption;
    }

    /**
     * Sets this picture's caption.
     * 
     * @param caption the caption to set.
     */
    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    /**
     * Returns this picture's image data.
     * 
     * @return this picture's image data.
     */
    public Blob getPicture()
    {
        return picture;
    }

    /**
     * Sets this picture's image data.
     * 
     * @param image the image data to set.
     */
    public void setPicture(Blob image)
    {
        this.picture = image;
    }

    /**
     * @return the pictureFormatName
     */
    public String getPictureFormatName()
    {
        return pictureFormatName;
    }

    /**
     * @param pictureFormatName the pictureFormatName to set
     */
    public void setPictureFormatName(String pictureFormatName)
    {
        this.pictureFormatName = pictureFormatName;
    }

    /**
     * @return the thumbnail
     */
    public Blob getThumbnail()
    {
        return thumbnail;
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(Blob thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    /**
     * @return the thumbnailFormatName
     */
    public String getThumbnailFormatName()
    {
        return thumbnailFormatName;
    }

    /**
     * @param thumbnailFormatName the thumbnailFormatName to set
     */
    public void setThumbnailFormatName(String thumbnailFormatName)
    {
        this.thumbnailFormatName = thumbnailFormatName;
    }

    /**
     * @return the mood
     */
    public Integer getMood()
    {
        return mood;
    }

    /**
     * @param mood the mood to set
     */
    public void setMood(Integer mood)
    {
        this.mood = mood;
    }

    @Override
    public String toString()
    {
        return pictureId + " - Owner: " + personId;
    }

}
