package youapp.model;

public class Mood
{
    private Integer id;

    private Integer rating;

    private String description;

    /**
     * @return the id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return the rating
     */
    public Integer getRating()
    {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(Integer rating)
    {
        this.rating = rating;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return id + " - " + rating + " : " + description;
    }

}
