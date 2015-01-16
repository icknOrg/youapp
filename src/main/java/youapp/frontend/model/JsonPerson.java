package youapp.frontend.model;

public class JsonPerson
{
    private Long id;

    private Long fbId;

    private String name;
    
    private String firstName;

    private String lastName;

    private String gender;

    private String nickName;

    private String pictureUrl;

    private String thumbnailUrl;

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
     * @return the fbId
     */
    public Long getFbId()
    {
        return fbId;
    }

    /**
     * @param fbId the fbId to set
     */
    public void setFbId(Long fbId)
    {
        this.fbId = fbId;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the firstName
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * @return the gender
     */
    public String getGender()
    {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    /**
     * @return the nickName
     */
    public String getNickName()
    {
        return nickName;
    }

    /**
     * @param nickName the nickName to set
     */
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    /**
     * @return the pictureUrl
     */
    public String getPictureUrl()
    {
        return pictureUrl;
    }

    /**
     * @param pictureUrl the pictureUrl to set
     */
    public void setPictureUrl(String pictureUrl)
    {
        this.pictureUrl = pictureUrl;
    }

    /**
     * @return the thumbnailUrl
     */
    public String getThumbnailUrl()
    {
        return thumbnailUrl;
    }

    /**
     * @param thumbnailUrl the thumbnailUrl to set
     */
    public void setThumbnailUrl(String thumbnailUrl)
    {
        this.thumbnailUrl = thumbnailUrl;
    }

}
