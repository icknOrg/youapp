package youapp.model;

import java.sql.Timestamp;

public class StatusUpdate
{
    private Person person;

    private Timestamp when;

    private Mood mood;

    private String description;

    /**
     * @return the person
     */
    public Person getPerson()
    {
        return person;
    }

    /**
     * @param person the person to set
     */
    public void setPerson(Person person)
    {
        this.person = person;
    }

    /**
     * @return the when
     */
    public Timestamp getWhen()
    {
        return when;
    }

    /**
     * @param when the when to set
     */
    public void setWhen(Timestamp when)
    {
        this.when = when;
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
        return "Status update with description: " + description + " which has the mood: " + mood + " is written of "
            + person + " on " + when;
    }
}
