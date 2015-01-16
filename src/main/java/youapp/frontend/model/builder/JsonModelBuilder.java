package youapp.frontend.model.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import youapp.frontend.model.JsonPerson;
import youapp.model.Person;
import youapp.services.SoulmatesService;
import youapp.utility.ProfileUtility;

@Component
public class JsonModelBuilder
{
    private ProfileUtility profileUtility;

    @Autowired
    public void setProfileUtility(ProfileUtility profileUtility)
    {
        this.profileUtility = profileUtility;
    }

    private SoulmatesService soulmatesService;

    @Autowired
    public void setSoulmatesService(SoulmatesService soulmatesService)
    {
        this.soulmatesService = soulmatesService;
    }

    public JsonPerson getJsonPerson(Person person)
    {
        return getJsonPerson(person, null);
    }

    public JsonPerson getJsonPerson(Person person, Long ownPersonId)
    {
        JsonPerson jsonPerson = new JsonPerson();
        jsonPerson.setId(person.getId());
        jsonPerson.setFbId(person.getFbId());
        jsonPerson.setFirstName(person.getFirstName());
        jsonPerson.setLastName(person.getLastName());
        jsonPerson.setNickName(person.getNickName());
        jsonPerson.setGender(person.getGender());
        jsonPerson.setPictureUrl(profileUtility.getProfilePictureUrl(person.getId()));
        jsonPerson.setThumbnailUrl(profileUtility.getProfileThumbnailUrl(person.getId()));
        if (ownPersonId != null)
        {
            if (person.getId() != ownPersonId)
            {
                jsonPerson.setName(profileUtility.getProfileName(person,
                    !soulmatesService.areSoulmates(ownPersonId, person.getId())));
            }
            else
            {
                jsonPerson.setName(profileUtility.getProfileName(person, false));
            }
        }
        return jsonPerson;
    }
}
