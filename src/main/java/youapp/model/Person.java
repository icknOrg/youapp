package youapp.model;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.format.annotation.DateTimeFormat;

import youapp.model.Tag.Category;

public class Person
{

    private Long id;

    private Long fbId;

    private AccessLevel accessLevel;

    private String firstName;

    private String lastName;

    private String gender;

    private Boolean activated;

    private String nickName;

    private String description;

    private Date memberSince;

    private Date lastOnline;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date birthday;
    
    private Boolean useFBMatching;
    
    private Boolean useQuestionMatching;
    
    private Boolean useDistanceMatching;    
   
	private Boolean useSymptomsMatching;
    
    private Boolean useMedicationMatching;
    
    private Location location;

    private List<Picture> picturesNew = new LinkedList<Picture>();

    private List<Picture> picturesRemoved = new LinkedList<Picture>();

    private Map<Integer, Picture> picturesStored = new TreeMap<Integer, Picture>();

    private TagSet tags = new TagSet();

    private TagSet tagsRemoved = new TagSet();

    /**
     * Default cosntructor constructs an empty person.
     */
    public Person()
    {
        this.activated = false;
    }

    /**
     * Constructs an inactive person with all mandatory items.
     * 
     * @param fbId
     * @param accessLevel
     * @param firstName
     * @param lastName
     * @param gender
     */
    public Person(Long fbId, AccessLevel accessLevel, String firstName, String lastName, String gender)
    {
        if (fbId == null || accessLevel == null || firstName == null || lastName == null)
        {
            // Note: Gender may be null in facebook. That's the reason why its
            // not checked here.
            throw new IllegalArgumentException("Parameters must not be null.");
        }
        this.fbId = fbId;
        this.accessLevel = accessLevel;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.activated = false; // !
    }

    /**
     * Constructs an active person with all mandatory items. Other attributes
     * can be set afterwards.
     * 
     * @param fbId
     * @param accessLevel
     * @param firstName
     * @param lastName
     * @param gender
     * @param activated
     * @param nickName
     * @param birthday
     * @param location
     */
    public Person(Long fbId, AccessLevel accessLevel, String firstName, String lastName, String gender,
        String nickName, Date birthday, Location location)
    {
        if (fbId == null || accessLevel == null || firstName == null || lastName == null || gender == null
            || nickName == null || birthday == null || location == null)
        {
            throw new IllegalArgumentException("Parameters must not be null.");
        }
        if (!("M".equals(gender) || "F".equals(gender)))
        {
            throw new IllegalArgumentException("Gender must be either \"M\" or \"F\".");
        }
        this.fbId = fbId;
        this.accessLevel = accessLevel;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.nickName = nickName;
        this.birthday = birthday;
        this.location = location;
        this.activated = true; // !
        this.memberSince = new Date(System.currentTimeMillis());
        this.lastOnline = new Date(System.currentTimeMillis());
    }

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
        if (id == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
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
        if (fbId == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        this.fbId = fbId;
    }

    /**
     * @return the accessLevel
     */
    public AccessLevel getAccessLevel()
    {
        return accessLevel;
    }

    /**
     * @param accessLevel the accessLevel to set
     */
    public void setAccessLevel(AccessLevel accessLevel)
    {
        if (accessLevel == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        this.accessLevel = accessLevel;
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
        if (firstName == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
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
        if (lastName == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
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
        if (gender == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        if (!("M".equals(gender) || "F".equals(gender)))
        {
            throw new IllegalArgumentException("Gender must be either \"M\" or \"F\".");
        }
        this.gender = gender;
    }

    /**
     * @return the activated
     */
    public Boolean getActivated()
    {
        return activated;
    }

    /**
     * @param activated the activated to set
     */
    public void setActivated(Boolean activated)
    {
        if (activated == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        this.activated = activated;
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
        // Might be set to null in case of deletion.
        this.nickName = nickName;
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

    /**
     * @return the memberSince
     */
    public Date getMemberSince()
    {
        return memberSince;
    }

    /**
     * @param memberSince the memberSince to set
     */
    public void setMemberSince(Date memberSince)
    {
        // Might be set to null in case of deletion.
        this.memberSince = memberSince;
    }

    /**
     * @return the lastOnline
     */
    public Date getLastOnline()
    {
        return lastOnline;
    }

    /**
     * @param lastOnline the lastOnline to set
     */
    public void setLastOnline(Date lastOnline)
    {
        // Might be set to null in case of deletion.
        this.lastOnline = lastOnline;
    }

    /**
     * @return the birthday
     */
    public Date getBirthday()
    {
        return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(Date birthday)
    {
        // Might be set to null in case of deletion.
        this.birthday = birthday;
    }
    
    public int getAge(){
    	if(birthday != null){
    		return Years.yearsBetween(new DateTime(birthday), new DateTime()).getYears();
     	}else{
    		throw new IllegalStateException("Birthday must be set when calculating the age of a person.");
    	}    	
    }

    /**
     * @return the location
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Location location)
    {
        // Might be set to null in case of deletion.
        this.location = location;
    }
    
    public Boolean getUseFBMatching() {
		return useFBMatching;
	}

	public void setUseFBMatching(Boolean useFBMatching) {
		this.useFBMatching = useFBMatching;
	}

	public Boolean getUseQuestionMatching() {
		return useQuestionMatching;
	}

	public void setUseQuestionMatching(Boolean useQuestionMatching) {
		this.useQuestionMatching = useQuestionMatching;
	}

	public Boolean getUseDistanceMatching() {
		return useDistanceMatching;
	}

	public void setUseDistanceMatching(Boolean useDistanceMatching) {
		this.useDistanceMatching = useDistanceMatching;
	}

	public Boolean getUseSymptomsMatching() {
		return useSymptomsMatching;
	}

	public void setUseSymptomsMatching(Boolean useSymptomsMatching) {
		this.useSymptomsMatching = useSymptomsMatching;
	}

	public Boolean getUseMedicationMatching() {
		return useMedicationMatching;
	}

	public void setUseMedicationMatching(Boolean useMedicationMatching) {
		this.useMedicationMatching = useMedicationMatching;
	}


    /**
     * Returns all pictures associated with this person that have not been
     * stored yet.
     * 
     * @return all new pictures.
     */
    public Iterator<Picture> getNewPictures()
    {
        return picturesNew.iterator();
    }

    /**
     * Returns all pictures associated with this person that have been stored
     * already.
     * 
     * @return all stored pictures.
     */
    public Iterator<Picture> getStoredPictures()
    {
        return picturesStored.values().iterator();
    }

    /**
     * Returns all pictures associated with this person that have been removed.
     * 
     * @return all removed pictures.
     */
    public Iterator<Picture> getRemovedPictures()
    {
        return picturesRemoved.iterator();
    }

    /**
     * Returns the stored picture with the given id, if any.
     * 
     * @param id the id of the picture to be returned.
     * @return the stored picture or null, if no picture with the given id
     *         exists.
     */
    public Picture getPictureById(int id)
    {
        return picturesStored.get(id);
    }

    /**
     * Returns the main picture, if any. Might return null if no main picture is
     * set!
     * 
     * @return the main picture or null, if no main picture is present.
     */
    public Picture getMoodRatingPicture(Integer rating)
    {
        // Search through new pictures.
        for (Picture p : picturesNew)
        {
            if ((p.getMood() != null) && p.getMood().getRating() == rating)
            {
                return p;
            }
        }

        // Search through stored pictures.
        Picture p = null;
        for (Entry<Integer, Picture> e : picturesStored.entrySet())
        {
            p = e.getValue();
            if ((p.getMood() != null) && p.getMood().getRating() == rating)
            {
                return p;
            }
        }
        return null;
    }

    /**
     * Adds a picture. The picture might be new or already existent. If the
     * picture is new, it will be stored to the database, the moment this person
     * is updated / created.
     * 
     * @param picture the picture to be added.
     */
    public void addPicture(Picture picture)
    {
        if (picture == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        if (!picture.pictureDataSet())
        {
            throw new IllegalArgumentException("Picture data must be set.");
        }
        // Check the id.
        Integer id = picture.getPictureId();
        if (id == null)
        {
            // New picture!
            if (!picturesNew.contains(picture))
            {
                picturesNew.add(picture);
            }
        }
        else
        {
            // Remove pictures from removed pictures ;) Just in case it has been
            // removed recently.
            // Add stored picture!
            picturesStored.put(id, picture);
        }
    }

    /**
     * Removes the picture with the given id from the stored pictures. The
     * picture will eventually be deleted from the database the moment this
     * person is updated.
     * 
     * @param id the id of the picture to be removed.
     */
    public void removePicture(int id)
    {
        Picture p = picturesStored.remove(id);
        if (p != null)
        {
            picturesRemoved.add(p);
        }
    }

    /**
     * Removes all stored pictures. All pictures will eventually be deleted from
     * the database the moment this person is updated.
     */
    public void removeAllPictures()
    {
        Picture p = null;
        Set<Integer> keys = picturesStored.keySet();
        for (Integer key : keys)
        {
            p = picturesStored.get(key);
            if (p != null)
            {
                picturesRemoved.add(p);
            }
        }
    }

    /**
     * Clears all new pictures. Do not clear the new pictures if the person has
     * not been updated / created yet. Otherwise the new pictures are lost.
     */
    public void clearNewPictures()
    {
        this.picturesNew.clear();
    }

    /**
     * Clears all removed pictures. Do not clear the removed pictures if the
     * person has not been updated yet. Otherwise the removed pictures are lost.
     */
    public void clearRemovedPictures()
    {
        this.picturesRemoved.clear();
    }

    /**
     * Returns whether this person has new pictures to be stored.
     * 
     * @return true, if this person has new pictures to be stored, false
     *         otherwise.
     */
    public boolean hasNewPictures()
    {
        return !picturesNew.isEmpty();
    }

    /**
     * Returns whether this person has stored pictures associated
     * 
     * @return true, if this person has stored pictures associated, false
     *         otherwise.
     */
    public boolean hasStoredPictures()
    {
        return !picturesStored.isEmpty();
    }

    /**
     * Returns whether this person has removed pictures to be deleted.
     * 
     * @return true, if this person has removed pictures to be deleted, false
     *         otherwise.
     */
    public boolean hasRemovedPictures()
    {
        return !picturesRemoved.isEmpty();
    }

    public TagSet getTags()
    {
        return tags;
    }

    public void setTags(TagSet tags)
    {
        
        this.tags = tags;
    }

    public TagSet getRemovedTags()
    {
        return tagsRemoved;
    }

    public TagSet getTagsByCategory(Category category)
    {
        TagSet catTags = new TagSet();
        for (Tag item : tags)
        {
            if (item.getCategory() == category)
            {
                catTags.add(item);
            }
        }
        return catTags;
    }

    public void addTags(TagSet tags)
    {
        this.tagsRemoved.removeAll(tags);
        this.tags.addAll(tags);
    }

    public void removeTags(TagSet removeTags)
    {
        this.tags.removeAll(removeTags);
        this.tagsRemoved.addAll(removeTags);
    }
    
    public void clearTags()
    {
        this.tags.clear();
    }

    public void clearRemovedTags()
    {
        this.tagsRemoved.clear();
    }

    public boolean hasTags()
    {
        return !tags.isEmpty();
    }

    public boolean hasRemovedTags()
    {
        return !tagsRemoved.isEmpty();
    }

    public TagSet getSymptoms()
    {
        return getTagsByCategory(Category.Symptom);
    }

    public void setSymptoms(TagSet symptomTags)
    {
        for (Tag tag : symptomTags)
        {
            tag.setCategory(Category.Symptom);
            this.tags.add(tag);
        }
    }

    public TagSet getMedications()
    {
        return getTagsByCategory(Category.Medication);
    }

    public void setMedications(TagSet medicationTags)
    {
        for (Tag tag : medicationTags)
        {
            tag.setCategory(Category.Medication);
            this.tags.add(tag);
        }
    }
    
    public void deletePictures(){
    	picturesNew = null;
    	picturesRemoved = null;
    	picturesStored = null;
    }

    public TagSet getProfileTags()
    {
        return getTagsByCategory(Category.ProfileTag);
    }

    public void setProfileTags(TagSet profileTags)
    {
        for (Tag tag : profileTags)
        {
            tag.setCategory(Category.ProfileTag);
            this.tags.add(tag);
        }
    }

}
