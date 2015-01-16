package youapp.utility;

import org.springframework.stereotype.Component;

import youapp.model.Person;

@Component
public class ProfileUtility
{
    private String baseUrl;;

    /**
     * @return the baseUrl
     */
    public String getBaseUrl()
    {
        return baseUrl;
    }

    /**
     * @param baseUrl the baseUrl to set
     */
    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public String getProfilePictureUrl(Long personId)
    {
        return baseUrl + "profile/picture.html?type=normal&personId=" + personId;
    }

    public String getProfileThumbnailUrl(Long personId)
    {
        return baseUrl + "profile/picture.html?type=thumbnail&personId=" + personId;
    }

    public String getProfileUrl(Long personId)
    {
        return baseUrl + "profile/show.html?personId=" + personId;
    }

    public String getProfileName(Person person, boolean isPublic)
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Person must not be null.");
        }
        if (person.getFirstName() == null)
        {
            throw new IllegalArgumentException("Person's first name must not be null.");
        }
        if (person.getLastName() == null)
        {
            throw new IllegalArgumentException("Person's last name must not be null.");
        }
        if (person.getNickName() == null)
        {
            throw new IllegalArgumentException("Person's nick name must not be null.");
        }

        if (isPublic)
        {
            return person.getFirstName() + " (@" + person.getNickName() + ")";
        }
        else
        {
            return person.getFirstName() + " " + person.getLastName() + " (@" + person.getNickName() + ")";
        }
    }
}
