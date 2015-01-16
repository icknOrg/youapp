package youapp.model;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Picture {

	private Integer pictureId;
	
	private Long personId;
	
	private String caption;
	
	private BufferedImage picture;
	
	private String pictureFormatName;
	
	private BufferedImage thumbnail;
	
	private String thumbnailFormatName;
	
	private Mood mood;

	/**
	 * @return the pictureId
	 */
	public Integer getPictureId() {
		return pictureId;
	}

	/**
	 * @param pictureId the pictureId to set
	 */
	public void setPictureId(Integer pictureId) {
		if (pictureId == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		this.pictureId = pictureId;
	}

	/**
	 * @return the personId
	 */
	public Long getPersonId() {
		return personId;
	}

	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		this.personId = personId;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		// Caption might be set to null.
		this.caption = caption;
	}

	/**
	 * @return the picture
	 */
	public BufferedImage getPicture() {
		if (picture == null) {
			return null;
		}
		return deepCopy(picture);
	}
	
	/**
	 * Returns whether picture data is set for this picture or not.
	 * @return true, if picture data is set, false otherwise.
	 */
	public boolean pictureDataSet() {
		return (picture != null);
	}

	/**
	 * @param picture the picture to set
	 */
	public void setPicture(BufferedImage picture) {
		if (picture == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		this.picture = deepCopy(picture);
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
    public BufferedImage getThumbnail()
    {
        if (thumbnail == null) {
            return null;
        }
        return deepCopy(thumbnail);
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(BufferedImage thumbnail)
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
    public Mood getMood()
    {
        return mood;
    }

    /**
     * @param mood the mood to set
     */
    public void setMood(Mood mood)
    {
        this.mood = mood;
    }

    private BufferedImage deepCopy(BufferedImage bi) {
		if (bi == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}
