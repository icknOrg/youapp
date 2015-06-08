package youapp.model;

import java.io.File;
import java.sql.Timestamp;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class StatusUpdate
{
	private int id;
	
    private Person person;

    private Timestamp when;

    private Mood mood;

    private String description;
    
    /**
     * @return the id
     */
    public int getId()
    {
        return this.id;
    }

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
    public void setId(int id)
    {
        this.id = id;
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
    
    public boolean hasImage()
    {
    	ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	String pathToImagesDir = requestAttributes.getRequest().getRealPath("/images");
    	
    	String pathWithoutExtension = pathToImagesDir + "/" + this.getPerson().getId() + "/" + this.getId() + ".";
    	
    	String[] extensions = { "png", "jpg", "jpeg", "gif" };
    	
    	boolean match = false;
    	
    	for (String extension : extensions)
    	{
    		File f = new File(pathWithoutExtension + extension);
    		if (f.exists() && !f.isDirectory()) 
    			match = true;
    	}
    	
    	return match;
    }
    
    public String getImageURL()
    {
    	ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	String pathToImagesDir = requestAttributes.getRequest().getRealPath("/images");
    	
    	String pathWithoutExtension = pathToImagesDir + "/" + this.getPerson().getId() + "/" + this.getId() + ".";
    	
    	String[] extensions = { "png", "jpg", "jpeg", "gif" };
    	
    	boolean match = false;
    	String url = "";
    	
    	for (String extension : extensions)
    	{
    		File f = new File(pathWithoutExtension + extension);
    		if (f.exists() && !f.isDirectory()) { 
    			match = true;
    			url = "/images/" + this.getPerson().getId() + "/" + this.getId() + "." + extension;
    		}
    	}
    	
    	if (match)
    		return url;
    	else
    		return "false";
    }

    @Override
    public String toString()
    {
        return "Status update with description: " + description + " which has the mood: " + mood + " is written of "
            + person + " on " + when;
    }
}
