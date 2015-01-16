package youapp.services.standard;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.IAccessLevelDao;
import youapp.dataaccess.dao.IMatchingDao;
import youapp.dataaccess.dao.IPersonDao;
import youapp.dataaccess.dao.IQuestionDao;
import youapp.dataaccess.dao.IReplyDao;
import youapp.dataaccess.dao.ISearchableQuestionDao;
import youapp.dataaccess.dto.AccessLevelDto;
import youapp.dataaccess.dto.MatchingDto;
import youapp.dataaccess.dto.NameDto;
import youapp.dataaccess.dto.PersonDto;
import youapp.dataaccess.dto.QuestionDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.AccessLevel;
import youapp.model.Location;
import youapp.model.Name;
import youapp.model.Person;
import youapp.model.Picture;
import youapp.model.Tag;
import youapp.model.TagSet;
import youapp.model.filter.IFilter;
import youapp.services.LocationService;
import youapp.services.PersonService;
import youapp.services.PictureService;
import youapp.services.TagService;

public class StandardPersonService
    implements PersonService
{

    /**
     * Logger.
     */
    static final Log log = LogFactory.getLog(StandardPersonService.class);

    private IPersonDao personDao;

    private IAccessLevelDao accessLevelDao;

    private IMatchingDao matchingDao;

    private TagService tagService;

    private IReplyDao replyDao;

    private IQuestionDao questionDao;

    private ISearchableQuestionDao searchableQuestionDao;

    private PictureService pictureService;

    private LocationService locationService;

    @Autowired
    public void setPersonDao(IPersonDao personDao)
    {
        this.personDao = personDao;
    }

    @Autowired
    public void setAccessLevelDao(IAccessLevelDao accessLevelDao)
    {
        this.accessLevelDao = accessLevelDao;
    }

    @Autowired
    public void setMatchingDao(IMatchingDao matchingDao)
    {
        this.matchingDao = matchingDao;
    }

    @Autowired
    public void setTagService(TagService tagService)
    {
        this.tagService = tagService;
    }

    @Autowired
    public void setReplyDao(IReplyDao replyDao)
    {
        this.replyDao = replyDao;
    }

    @Autowired
    public void setQuestionDao(IQuestionDao questionDao)
    {
        this.questionDao = questionDao;
    }

    @Autowired
    public void setSearchableQuestionDao(ISearchableQuestionDao searchableQuestion)
    {
        this.searchableQuestionDao = searchableQuestion;
    }

    @Autowired
    public void setPictureService(PictureService pictureService)
    {
        this.pictureService = pictureService;
    }

    @Autowired
    public void setLocationService(LocationService locationService)
    {
        this.locationService = locationService;
    }

    @Override
    public Long getAlternativeId(Long personId, Boolean fbId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (fbId == null)
        {
            throw new IllegalArgumentException("Facbook id indicator must not be null.");
        }
        return personDao.getAlternativeId(personId, fbId);
    }

    @Override
    public Person getById(Long id) throws DataAccessLayerException, InconsistentModelException
    {
//    	log.debug("''''''############################");
        if (id == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving person with id: " + id);
        }
        PersonDto person = null;
        AccessLevelDto accLevel = null;
        Location location = null;
        List<Picture> pictures = null;
        TagSet tags = null;
        // Retrieve person.
        person = personDao.getById(id);
        if (log.isDebugEnabled())
        {
            log.debug(">>> Found person: " + person.toString());
        }
        if (person.getAccessLevel() == null)
        {
            throw new InconsistentStateException("No access level set for the given person.");
        }
        // Retrieve access level.
        accLevel = accessLevelDao.getById(person.getAccessLevel());
        // Retrieve location.
        location = locationService.getLocationByIdAndName(person.getLocation(),person.getLocationName());
        // Retrieve pictures.
        pictures = pictureService.getByPerson(person.getId());
        // Retrieve tags.
        tags = tagService.getByPerson(person.getId());
        return reassemblePerson(person, accLevel, location, pictures, tags);
    }

    @Override
    public Person getByFbId(Long id) throws DataAccessLayerException, InconsistentModelException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving person with facebook id: " + id);
        }
        PersonDto person = null;
        AccessLevelDto accLevel = null;
        Location location = null;
        List<Picture> pictures = null;
        TagSet tags = null;
        // Retrieve person.
        person = personDao.getByFbId(id);
        if (log.isDebugEnabled())
        {
            log.debug(">>> Found person: " + person.toString());
        }
        if (person.getAccessLevel() == null)
        {
            throw new InconsistentStateException("No access level set for the given person.");
        }
        // Retrieve access level.
        accLevel = accessLevelDao.getById(person.getAccessLevel());
        // Retrieve location.
        location = locationService.getLocationByIdAndName(person.getLocation(),person.getLocationName());
        // Retrieve picture.
        pictures = pictureService.getByPerson(person.getId());
        // Retrieve tags.
        tags = tagService.getByPerson(person.getId());
        return reassemblePerson(person, accLevel, location, pictures, tags);
    }

    @Override
    public Boolean exists(Long id, boolean fbId) throws DataAccessLayerException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id must not be null.");
        }
        if (fbId)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Existence check for person with facebook id: " + id);
            }
        }
        else
        {
            if (log.isDebugEnabled())
            {
                log.debug("Existence check for person with id: " + id);
            }
        }
        return personDao.exists(id, fbId);
    }

    @Override
    public Boolean isActive(Long id, boolean fbId) throws DataAccessLayerException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id must not be null.");
        }
        if (fbId)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Activation check for person with facebook id: " + id);
            }
        }
        else
        {
            if (log.isDebugEnabled())
            {
                log.debug("Activation check for person with id: " + id);
            }
        }
        return personDao.isActive(id, fbId);
    }

    @Override
    public List<Person> getByFilters(IFilter... filters) throws DataAccessLayerException, InconsistentModelException
    {
        return getByFilters(null, null, filters);
    }

    @Override
    public List<Person> getByFilters(Integer offset, Integer limit, IFilter... filters) throws DataAccessLayerException, InconsistentModelException
    {
        if (filters == null)
        {
            throw new IllegalArgumentException("Filters must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving persons with given filters");
        }
        List<Person> result = new LinkedList<Person>();
        List<PersonDto> persons = null;
        AccessLevelDto accLevel = null;
        Location location = null;
        List<Picture> pictures = null;
        TagSet tags = null;

        // Retrieve persons. With limit, if limit is not null.
        if (limit != null && offset != null)
        {
            persons = personDao.getByFilters(filters, 0, limit);
        }
        else
        {
            persons = personDao.getByFilters(filters);
        }

        for (PersonDto person : persons)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found person: " + person.toString());
            }
            // Reset!
            accLevel = null;
            location = null;
            pictures = null;
            if (person.getAccessLevel() == null)
            {
                throw new InconsistentStateException("No access level set for the given person.");
            }
            // Retrieve access level.
            accLevel = accessLevelDao.getById(person.getAccessLevel());
            // Retrieve location.
            location = locationService.getLocationByIdAndName(person.getLocation(),person.getLocationName());
            // Retrieve picture.
            pictures = pictureService.getByPerson(person.getId());
            // Retrieve tags.
            tags = tagService.getByPerson(person.getId());

            result.add(reassemblePerson(person, accLevel, location, pictures, tags));
        }
        return result;
    }

    @Override
    public List<Person> getByName(String firstName, String lastName) throws DataAccessLayerException, InconsistentModelException
    {
        if (firstName == null)
        {
            throw new IllegalArgumentException("First name must not be null.");
        }
        if (lastName == null)
        {
            throw new IllegalArgumentException("Last name must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving persons with name: " + firstName + " " + lastName);
        }
        List<Person> result = new LinkedList<Person>();
        List<PersonDto> persons = null;
        AccessLevelDto accLevel = null;
        Location location = null;
        List<Picture> pictures = null;
        TagSet tags = null;
        // Retrieve persons.
        persons = personDao.getByName(firstName, lastName);
        for (PersonDto person : persons)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found person: " + person.toString());
            }
            // Reset!
            accLevel = null;
            location = null;
            pictures = null;
            if (person.getAccessLevel() == null)
            {
                throw new InconsistentStateException("No access level set for the given person.");
            }
            // Retrieve access level.
            accLevel = accessLevelDao.getById(person.getAccessLevel());
            // Retrieve location.
            location = locationService.getLocationByIdAndName(person.getLocation(),person.getLocationName());
            // Retrieve picture.
            pictures = pictureService.getByPerson(person.getId());
            // Retrieve tags.
            tags = tagService.getByPerson(person.getId());

            result.add(reassemblePerson(person, accLevel, location, pictures, tags));
        }
        return result;
    }

    @Override
    public Name getName(Long personId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving name of person with id: " + personId);
        }
        if (!exists(personId, false))
        {
            return null;
        }
        else
        {
            return reassembleName(personDao.getName(personId));
        }
    }

    @Override
    public String getNickName(Long personId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving nick name of person with id: " + personId);
        }
        if (!(exists(personId, false) && isActive(personId, false)))
        {
            return null;
        }
        else
        {
            return personDao.getNickName(personId);
        }
    }

    @Override
    public List<Person> getBestMatchings(Long personId, int minFriend, int offset, int resultSize) throws DataAccessLayerException, InconsistentModelException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving matchings for person: " + personId);
        }
        List<Person> result = new LinkedList<Person>();
        List<MatchingDto> matchings = null;
        PersonDto person = null;
        AccessLevelDto accLevel = null;
        Location location = null;
        List<Picture> pictures = null;
        TagSet tags = null;
        // Retrieve matchings.
        matchings = matchingDao.getBestMatches(personId, 0.0, offset, resultSize);
        for (MatchingDto matching : matchings)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found matching: " + matching.toString());
            }
            // Reset!
            person = null;
            accLevel = null;
            location = null;
            pictures = null;
            // Null pointer and id checking.
            if (matching.getSourceId() == null)
            {
                throw new InconsistentStateException("Source id is null.");
            }
            if (matching.getDestinationId() == null)
            {
                throw new InconsistentStateException("Destination id is null.");
            }
            if (!personId.equals(matching.getSourceId()))
            {
                throw new InconsistentStateException("Retrieved source id does not equal the given person person id.");
            }
            // Retrieve destination person.
            person = personDao.getById(matching.getDestinationId());
            if (person == null)
            {
                throw new InconsistentStateException("Person with the destination id does not exist / is null.");
            }
            if (!matching.getDestinationId().equals(person.getId()))
            {
                throw new InconsistentStateException("Id of the retrieved person does not equal the given destination id.");
            }
            if (person.getAccessLevel() == null)
            {
                throw new InconsistentStateException("No access level set for the given person.");
            }
            if (log.isDebugEnabled())
            {
                log.debug(">>> Matching person: " + person.toString());
            }
            // Retrieve access level.
            accLevel = accessLevelDao.getById(person.getAccessLevel());
            // Retrieve location.
            location = locationService.getLocationByIdAndName(person.getLocation(),person.getLocationName());
            // Retrieve picture.
            pictures = pictureService.getByPerson(person.getId());
            // Retrieve tags.
            tags = tagService.getByPerson(person.getId());

            result.add(reassemblePerson(person, accLevel, location, pictures, tags));
        }
        return result;
    }

    @Override
    public List<Person> getAllFriends(Long personId, int offset, int resultSize, boolean includeProvisional) throws DataAccessLayerException, InconsistentModelException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Retrieving all friends of person with id: " + personId);
        }
        List<Person> result = new LinkedList<Person>();
        List<PersonDto> friends = null;
        AccessLevelDto accLevel = null;
        Location location = null;
        List<Picture> pictures = null;
        TagSet tags = null;
        // Retrieve persons.
        friends = personDao.getAllFriends(personId, offset, resultSize, includeProvisional);
        for (PersonDto person : friends)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found person: " + person.toString());
            }
            // Reset!
            accLevel = null;
            location = null;
            pictures = null;
            if (person.getAccessLevel() == null)
            {
                throw new InconsistentStateException("No access level set for the given person.");
            }
            // Retrieve access level.
            accLevel = accessLevelDao.getById(person.getAccessLevel());
            // Retrieve location.
            location = locationService.getLocationByIdAndName(person.getLocation(),person.getLocationName());
            // Retrieve picture.
            pictures = pictureService.getByPerson(person.getId());
            // Retrieve tags.
            tags = tagService.getByPerson(person.getId());
            result.add(reassemblePerson(person, accLevel, location, pictures, tags));
        }
        return result;
    }

    @Override
    public List<Person> getAll() throws DataAccessLayerException, ModelException
    {
        List<Person> result = new LinkedList<Person>();
        List<PersonDto> persons = null;
        AccessLevelDto accLevel = null;
        Location location = null;
        List<Picture> pictures = null;
        TagSet tags = null;
        // Retrieve persons.
        persons = personDao.getAll();
        for (PersonDto person : persons)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found person: " + person.toString());
            }
            // Reset!
            accLevel = null;
            location = null;
            pictures = null;
            if (person.getAccessLevel() == null)
            {
                throw new InconsistentStateException("No access level set for the given person.");
            }
            // Retrieve access level.
            accLevel = accessLevelDao.getById(person.getAccessLevel());
            // Retrieve location.
            location = locationService.getLocationByIdAndName(person.getLocation(),person.getLocationName());
            // Retrieve picture.
            pictures = pictureService.getByPerson(person.getId());
            // Retrieve tags.
            tags = tagService.getByPerson(person.getId());
            result.add(reassemblePerson(person, accLevel, location, pictures, tags));
        }
        return result;
    }

    @Override
    public List<Person> getRecentFriends(Long personId, int resultSize, boolean includeProvisional) throws DataAccessLayerException, InconsistentModelException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Retrieving most recent friends of person with id: " + personId);
        }
        List<Person> result = new LinkedList<Person>();
        List<PersonDto> friends = null;
        AccessLevelDto accLevel = null;
        Location location = null;
        List<Picture> pictures = null;
        TagSet tags = null;
        // Retrieve persons.
        friends = personDao.getRecentFriends(personId, resultSize, includeProvisional);
        for (PersonDto person : friends)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found person: " + person.toString());
            }
            // Reset!
            accLevel = null;
            location = null;
            pictures = null;
            if (person.getAccessLevel() == null)
            {
                throw new InconsistentStateException("No access level set for the given person.");
            }
            // Retrieve access level.
            accLevel = accessLevelDao.getById(person.getAccessLevel());
            // Retrieve location.
            location = locationService.getLocationByIdAndName(person.getLocation(),person.getLocationName());
            // Retrieve picture.
            pictures = pictureService.getByPerson(person.getId());
            // Retrieve tags.
            tags = tagService.getByPerson(person.getId());

            result.add(reassemblePerson(person, accLevel, location, pictures, tags));
        }
        return result;
    }

    @Override
    public Boolean nickAvailable(String nick)
    {
        if (nick == null)
        {
            throw new IllegalArgumentException("Nick name must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Checking existency of nick name: " + nick);
        }
        return personDao.nickAvailable(nick);
    }

    @Override
    public Long create(Person person) throws DataAccessLayerException, ModelException, InconsistentModelException, InconsistentStateException
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Person must not be null.");
        }

        // Perform validation checks.
        validatePerson(person, true);

        // Create location, if not existing
        Location location = person.getLocation();
        if (location != null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Creating location for person: " + person.getLocation());
            }
            Location pLocation = locationService.createLocation(location);
            location.setId(pLocation.getId());
            location.setName(location.getName());
        }

        // Create new person in the database.
        PersonDto personDto = disassemblePerson(person);
        if (log.isDebugEnabled())
        {
            log.debug("Creating person: " + personDto.toString());
        }
        Long personId = personDao.create(personDto);
        person.setId(personId);

        // Create new pictures (if any) in the database.
        if (person.hasNewPictures())
        {
            // There are new pictures to be stored.
            pictureService.createAndAssociate(personId, person.getNewPictures());
            person.clearNewPictures();
        }

        // Make tags associations (if any) in the database.
        if (person.hasTags())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Creating new tags associations for person with id: " + personId);
            }
            tagService.createAndAssociate(personId, person.getTags());
        }

        if (log.isDebugEnabled())
        {
            log.debug("Created person: " + person.getId());
        }
        return personId;
    }

    @Override
    public void update(Person person) throws DataAccessLayerException, ModelException
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Person must not be null.");
        }

        // Perform validation checks.
        validatePerson(person, false);

        // Create location, if not existing
        Location location = person.getLocation();
        if (location != null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Creating location for person: " + person.getLocation());
            }
            Location pLocationId = locationService.createLocation(location);
            location.setId(pLocationId.getId());
            location.setName(pLocationId.getName());
        }

        // Update person in database.
        PersonDto personDto = disassemblePerson(person);
        if (log.isDebugEnabled())
        {
            log.debug("Updating person: " + personDto.toString());
        }
        personDao.update(personDto);

        // Delete removed pictures (if any) from database.
        Iterator<Picture> it = null;
        Picture picture = null;
        if (person.hasRemovedPictures())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Deleting pictures for person: " + personDto.getId());
            }
            it = person.getRemovedPictures();
            while (it.hasNext())
            {
                picture = it.next();
                if (log.isTraceEnabled())
                {
                    log.trace(">>> Deleting removed picture: " + picture.getPictureId());
                }
                pictureService.delete(picture.getPictureId(), picture.getPersonId());
            }
        }

        // Update existing pictures (if any) in the database.
        if (person.hasStoredPictures())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Updating existing pictures for person: " + personDto.getId());
            }
            it = person.getStoredPictures();
            while (it.hasNext())
            {
                picture = it.next();
                if (log.isTraceEnabled())
                {
                    log.trace(">>> Updating picture: " + picture.getPictureId());
                }
                pictureService.update(picture);
            }
        }

        // Create new pictures (if any) in the database.
        if (person.hasNewPictures())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Creating new pictures for person: " + person.getId());
            }

            // There are new pictures to be stored.
            pictureService.createAndAssociate(person.getId(), person.getNewPictures());
            person.clearNewPictures();
        }

        // Delete removed tag associations (if any).
        Iterator<Tag> tagIt = null;
        if (person.hasRemovedTags())
        {
            tagIt = person.getRemovedTags().iterator();
            Tag current = null;
            while (tagIt.hasNext())
            {
                current = tagIt.next();
                // Remove medication association if one is present.
                if ((current.getId() != null) && tagService.existsAssociation(person.getId(), current.getId()))
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("Removing existing tag association between person " + "with id " + person.getId() + " and tag with id: " + current.getId());
                    }
                    tagService.removeAssociation(person.getId(), current.getId());
                }
            }
            person.clearRemovedTags();
        }

        // Create new tag associations (if any).
        tagIt = null;
        if (person.hasTags())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Creating new tag associations for person with id: " + person.getId());
            }
            tagService.createAndAssociate(person.getId(), person.getTags());
        }

        if (log.isDebugEnabled())
        {
            log.debug("Updated person: " + personDto.toString());
        }
    }

    @Override
    public void updateLastOnline(Long personId, Date lastLogin)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (lastLogin == null)
        {
            throw new IllegalArgumentException("Last Login must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Update last login: " + lastLogin + " of person with id " + personId);
        }

        personDao.updateLastOnline(personId, lastLogin);
    }

    @Override
    public void deactivate(Long personId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (exists(personId, false) && isActive(personId, false))
        {
            if (log.isDebugEnabled())
            {
                log.debug("Deactivating person with id: " + personId);
            }
        }
        else
        {
            if (log.isDebugEnabled())
            {
                log.debug("No such person or no such activated person with id " + personId + " available.");
            }
            return;
        }
        personDao.deactivate(personId);
        if (log.isDebugEnabled())
        {
            log.debug("Deleting all pictures of person with id: " + personId);
        }
        pictureService.deleteAll(personId);
        if (log.isDebugEnabled())
        {
            log.debug("Removing all tag associations of person with id: " + personId);
        }
        tagService.removeAllAssociations(personId);
        if (log.isDebugEnabled())
        {
            log.debug("Deleting all replies of person with id: " + personId);
        }
        replyDao.deletePersonReplies(personId);
        if (log.isDebugEnabled())
        {
            log.debug("Removing nick name from all questions of person with id " + personId + " stored inside the search index.");
        }
        List<QuestionDto> questions = questionDao.getByAuthor(personId);
        NameDto name = personDao.getName(personId);
        name.setNickName(null);
        try
        {
            searchableQuestionDao.update(questions, name);
        }
        catch (SolrServerException e)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Error while updating questions by author with id " + personId + ". Reason: ", e);
            }
        }
        catch (IOException e)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Error while updating questions by author with id " + personId + ". Reason: ", e);
            }
        }
        catch (Exception e)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Error while updating questions by author with id " + personId + ". Reason: ", e);
            }
        }
        if (log.isDebugEnabled())
        {
            log.debug("Deactivated person with id: " + personId);
        }
    }

    public static Person reassemblePerson(PersonDto personDto, AccessLevelDto accLevelDto, Location location, List<Picture> pictures, TagSet tags) throws InconsistentStateException, InconsistentModelException
    {
        // Check parameter null pointers.
        if (personDto == null)
        {
            throw new IllegalArgumentException("Person must not be null.");
        }
        if (accLevelDto == null)
        {
            throw new IllegalArgumentException("Access level must not be null.");
        }

        // Validate.
        validatePerson(personDto, accLevelDto, location, pictures, tags, false);

        // Reassemble.
        if (log.isDebugEnabled())
        {
            log.debug("Reassembling person: " + personDto.toString());
        }
        // Create person object.
        Person person = new Person();
        Long id = personDto.getId();
        person.setId(id);
        if (log.isDebugEnabled())
        {
            log.debug(">>> Id: " + id);
        }
        // Retrieve basic person data.
        person.setFbId(personDto.getFbId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> FbId: " + personDto.getFbId());
        }
        person.setFirstName(personDto.getFirstName());
        if (log.isDebugEnabled())
        {
            log.debug(">>> First name: " + personDto.getFirstName());
        }
        person.setLastName(personDto.getLastName());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Last name: " + personDto.getLastName());
        }
        if (personDto.getGender() != null)
        {
            person.setGender(personDto.getGender());
            if (log.isDebugEnabled())
            {
                log.debug(">>> Gender: " + personDto.getGender());
            }
        }
        person.setActivated(personDto.getActivated());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Activated: " + personDto.getActivated());
        }
        person.setAccessLevel(reassembleAccessLevel(accLevelDto));
        // Retrieve additional data of an activated person.
        if (personDto.getActivated())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Reassembling further person data: " + personDto.toString());
            }
            person.setGender(personDto.getGender());
            if (log.isDebugEnabled())
            {
                log.debug(">>> Gender: " + personDto.getGender());
            }
            person.setNickName(personDto.getNickName());
            if (log.isDebugEnabled())
            {
                log.debug(">>> Nick name: " + personDto.getNickName());
            }
            person.setDescription(personDto.getDescription());
            if (log.isDebugEnabled())
            {
                log.debug(">>> Description: " + personDto.getDescription());
            }
            person.setMemberSince(personDto.getMemberSince());
            if (log.isDebugEnabled())
            {
                log.debug(">>> Member since: " + personDto.getMemberSince());
            }
            person.setLastOnline(personDto.getLastOnline());
            if (log.isDebugEnabled())
            {
                log.debug(">>> Last online: " + personDto.getLastOnline());
            }
            person.setBirthday(personDto.getBirthday());
            if (log.isDebugEnabled())
            {
                log.debug(">>> Birthday: " + personDto.getBirthday());
            }
            // Retrieve location.
            person.setLocation(location);            
            // Retrieve pictures if any.
            if ((pictures != null) && !pictures.isEmpty())
            {
                if (log.isDebugEnabled())
                {
                    log.debug(">>> Number of pictures: " + pictures.size());
                }
                for (Picture p : pictures)
                {
                    person.addPicture(p);
                }
            }
            // Retrieve medications if any.
            if ((tags != null) && !tags.isEmpty())
            {
                if (log.isDebugEnabled())
                {
                    log.debug(">>> Number of tags: " + tags.size());
                }
                person.addTags(tags);
            }
        }
        person.setUseFBMatching(personDto.getUseFBMatching());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Use FB Matching: " + personDto.getUseFBMatching());
        }
        person.setUseQuestionMatching(personDto.getUseQuestionMatching());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Use QUESTIONS Matching: " + personDto.getUseQuestionMatching());
        }
        person.setUseDistanceMatching(personDto.getUseDistanceMatching());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Use DISTANCE Matching: " + personDto.getUseDistanceMatching());
        }
        person.setUseSymptomsMatching(personDto.getUseSymptomsMatching());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Use SYMTOMS Matching: " + personDto.getUseSymptomsMatching());
        }
        person.setUseMedicationMatching(personDto.getUseMedicationMatching());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Use Questions Matching: " + personDto.getUseMedicationMatching());
        }
        
        return person;
    }

    public static Name reassembleName(NameDto nameDto) throws InconsistentStateException
    {
        if (nameDto == null)
        {
            throw new IllegalArgumentException("Name DTO must not be null.");
        }
        validateName(nameDto);
        if (log.isDebugEnabled())
        {
            log.debug("Reassembling name of person with id: " + nameDto.getPersonId());
        }
        Name name = new Name();
        name.setPersonId(nameDto.getPersonId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Person id: " + nameDto.getPersonId());
        }
        name.setFirstName(nameDto.getFirstName());
        if (log.isDebugEnabled())
        {
            log.debug(">>> First name: " + nameDto.getFirstName());
        }
        name.setLastName(nameDto.getLastName());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Last name: " + nameDto.getLastName());
        }
        name.setNickName(nameDto.getNickName());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Nick name: " + nameDto.getNickName());
        }
        return name;
    }

    public static AccessLevel reassembleAccessLevel(AccessLevelDto accLevelDto) throws InconsistentStateException
    {
        // Check parameter null pointers.
        if (accLevelDto == null)
        {
            throw new IllegalArgumentException();
        }

        // Validate.
        validateAccessLevel(accLevelDto, false);

        // Reassemble.
        if (log.isDebugEnabled())
        {
            log.debug("Reassembling access level: " + accLevelDto.toString());
        }
        AccessLevel accessLevel = new AccessLevel();
        accessLevel.setId(accLevelDto.getId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Id: " + accLevelDto.getId());
        }
        accessLevel.setDescription(accLevelDto.getDescription());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Description: " + accLevelDto.getDescription());
        }
        return accessLevel;
    }

    public static PersonDto disassemblePerson(Person person)
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Person must not be null.");
        }
        PersonDto personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setFbId(person.getFbId());
        if (person.getAccessLevel() != null)
        {
            personDto.setAccessLevel(person.getAccessLevel().getId());
        }
        personDto.setFirstName(person.getFirstName());
        personDto.setLastName(person.getLastName());
        personDto.setActivated(person.getActivated());
        personDto.setGender(person.getGender());
        personDto.setNickName(person.getNickName());
        personDto.setDescription(person.getDescription());
        personDto.setMemberSince(person.getMemberSince());
        personDto.setLastOnline(person.getLastOnline());
        personDto.setBirthday(person.getBirthday());
        if (person.getLocation() != null)
        {
            personDto.setLocation(person.getLocation().getId());
            personDto.setLocationName(person.getLocation().getName());
        }
        personDto.setUseFBMatching(person.getUseFBMatching());
        personDto.setUseQuestionMatching(person.getUseQuestionMatching());
        personDto.setUseDistanceMatching(person.getUseDistanceMatching());
        personDto.setUseSymptomsMatching(person.getUseSymptomsMatching());
        personDto.setUseMedicationMatching(person.getUseMedicationMatching());
        
        return personDto;
    }

    public static AccessLevelDto disassembleAccessLevel(AccessLevel accLevel)
    {
        if (accLevel == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        AccessLevelDto accLevelDto = new AccessLevelDto();
        accLevelDto.setId(accLevel.getId());
        accLevelDto.setDescription(accLevel.getDescription());
        return accLevelDto;
    }

    public static Blob imageToBlob(BufferedImage image)
    {
        if (image == null)
        {
            throw new IllegalArgumentException("Image must not be null.");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            // TODO: Think about image formats. Maybe store the format in the
            // database?
            ImageIO.write(image, "jpeg", baos);
            baos.flush();
            return new SerialBlob(baos.toByteArray());
        }
        catch (IOException e)
        {
            if (log.isErrorEnabled())
            {
                log.error(e.getMessage());
            }
            return null;
        }
        catch (SerialException e)
        {
            if (log.isErrorEnabled())
            {
                log.error(e.getMessage());
            }
            return null;
        }
        catch (SQLException e)
        {
            if (log.isErrorEnabled())
            {
                log.error(e.getMessage());
            }
            return null;
        }
    }

    public static BufferedImage blobToImage(Blob image)
    {
        if (image == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        try
        {
            InputStream input = image.getBinaryStream();
            return ImageIO.read(input);
        }
        catch (IOException e)
        {
            if (log.isErrorEnabled())
            {
                log.error(e.getMessage());
            }
            return null;
        }
        catch (SQLException e)
        {
            if (log.isErrorEnabled())
            {
                log.error(e.getMessage());
            }
            return null;
        }
    }

    public static void validatePerson(Person person, boolean isNew) throws InconsistentModelException
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Person must not be null.");
        }

        // Check mandatory person data.
        if (isNew)
        {
            if (person.getId() != null)
            {
                throw new InconsistentModelException("Id must not be set.");
            }
        }
        else
        {
            if (person.getId() == null)
            {
                throw new InconsistentModelException("Id must not be null.");
            }
        }
        if (person.getFbId() == null)
        {
            throw new InconsistentModelException("Facebook id must not be null.");
        }
        if (person.getAccessLevel() == null)
        {
            throw new InconsistentModelException("Access level must not be null.");
        }
        if (person.getFirstName() == null)
        {
            throw new InconsistentModelException("First name must not be null.");
        }
        if (person.getLastName() == null)
        {
            throw new InconsistentModelException("Last name must not be null.");
        }
        if (person.getActivated() == null)
        {
            throw new InconsistentModelException("Activated must not be null.");
        }

        // Check further person data.
        if (person.getActivated())
        {
            if (person.getGender() == null)
            {
                throw new InconsistentModelException("Gender must not be null.");
            }
            if (person.getNickName() == null)
            {
                throw new InconsistentModelException("Nick name must not be null.");
            }
            if (person.getMemberSince() == null)
            {
                throw new InconsistentModelException("Member since must not be null.");
            }
            if (person.getLastOnline() == null)
            {
                throw new InconsistentModelException("Last online must not be null.");
            }
            if (person.getBirthday() == null)
            {
                throw new InconsistentModelException("Birthday must not be null.");
            }
            if (person.getLocation() == null)
            {
                throw new InconsistentModelException("Location must not be null.");
            }
        }

        // Check associated objects.
        validateAccessLevel(person.getAccessLevel(), false);
        if (person.getLocation() != null)
        {
            StandardLocationService.validateLocation(person.getLocation());
        }

        // Check pictures.
        if (isNew)
        {
            if (person.hasStoredPictures() || person.hasRemovedPictures())
            {
                throw new InconsistentModelException("A new person cannot have already stored or removed pictures.");
            }
        }

        Picture p = null;
        Iterator<Picture> it = null;
        if (person.hasNewPictures())
        {
            it = person.getNewPictures();
            while (it.hasNext())
            {
                p = it.next();
                StandardPictureService.validatePicture(p, false);
            }
        }
        if (person.hasStoredPictures())
        {
            it = person.getStoredPictures();
            while (it.hasNext())
            {
                p = it.next();
                StandardPictureService.validatePicture(p, true);
                if (!person.getId().equals(p.getPersonId()))
                {
                    throw new InconsistentModelException("Wrong person id associated with picture.");
                }
            }
        }
        if (person.hasRemovedPictures())
        {
            it = person.getRemovedPictures();
            while (it.hasNext())
            {
                p = it.next();
                StandardPictureService.validatePicture(p, true);
                if (!person.getId().equals(p.getPersonId()))
                {
                    throw new InconsistentModelException("Wrong person id associated with picture.");
                }
            }
        }

        // Check tags.
        if (isNew)
        {
            if (person.hasRemovedTags())
            {
                throw new InconsistentModelException("A new person cannot have removed tags.");
            }
        }
        Iterator<Tag> tagIt = null;
        Tag t = null;
        if (person.hasTags())
        {
            tagIt = person.getTags().iterator();
            while (tagIt.hasNext())
            {
                t = tagIt.next();
                StandardTagService.validateTag(t);
            }
        }
        tagIt = null;
        t = null;
        if (person.hasRemovedTags())
        {
            tagIt = person.getRemovedTags().iterator();
            while (tagIt.hasNext())
            {
                t = tagIt.next();
                StandardTagService.validateTag(t);
            }
        }
    }

    public static void validatePerson(PersonDto person, AccessLevelDto accLevel, Location location, List<Picture> pictures, TagSet tags, boolean isNew) throws InconsistentStateException, InconsistentModelException
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Person must not be null.");
        }

        // Check mandatory person data.
        if (isNew)
        {
            if (person.getId() != null)
            {
                throw new InconsistentStateException("Id must not be set.");
            }
        }
        else
        {
            if (person.getId() == null)
            {
                throw new InconsistentStateException("Id must not be null.");
            }
        }
        if (person.getFbId() == null)
        {
            throw new InconsistentStateException("Facebook id must not be null.");
        }
        if (person.getAccessLevel() == null)
        {
            throw new InconsistentStateException("Person access level id must not be null.");
        }
        if (person.getFirstName() == null)
        {
            throw new InconsistentStateException("First name must not be null.");
        }
        if (person.getLastName() == null)
        {
            throw new InconsistentStateException("Last name must not be null.");
        }
        if (person.getActivated() == null)
        {
            throw new InconsistentStateException("Activated information must not be null.");
        }

        // Check further person data.
        if (person.getActivated())
        {
            if (person.getGender() == null)
            {
                throw new InconsistentStateException("Gender must not be null.");
            }
            if (person.getNickName() == null)
            {
                throw new InconsistentStateException("Nick name must not be null.");
            }
            if (person.getMemberSince() == null)
            {
                throw new InconsistentStateException("Member since must not be null.");
            }
            if (person.getLastOnline() == null)
            {
                throw new InconsistentStateException("Last online must not be null.");
            }
            if (person.getBirthday() == null)
            {
                throw new InconsistentStateException("Birthday must not be null.");
            }
            if (person.getLocation() == null)
            {
                throw new InconsistentStateException("Person location id must not be null.");
            }
        }

        // Check access level.
        if (accLevel == null)
        {
            throw new IllegalArgumentException("Access level must not be null.");
        }
        validateAccessLevel(accLevel, false);
        if (!person.getAccessLevel().equals(accLevel.getId()))
        {
            throw new InconsistentStateException("Person access level does not match the access level.");
        }

        if (person.getActivated())
        {
            // Check location.
            if (person.getLocation() != null)
            {
                if (location == null)
                {
                    throw new IllegalArgumentException("Location must not be null.");
                }
                StandardLocationService.validateLocation(location);
                if (!person.getLocation().equals(location.getId()))
                {
                    throw new InconsistentStateException("Person location id does not match location id.");
                }
            }

            // Check pictures.
            if (pictures != null)
            {
                for (Picture p : pictures)
                {
                    StandardPictureService.validatePicture(p, true);
                    if (!person.getId().equals(p.getPersonId()))
                    {
                        throw new InconsistentStateException("Picture person id does not match person id.");
                    }
                }
            }

            // Check tags.
            if (tags != null)
            {
                for (Tag t : tags)
                {
                    StandardTagService.validateTag(t);
                }
            }
        }
    }

    public static void validateName(Name name) throws InconsistentModelException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Name must not be null.");
        }
        if (name.getPersonId() == null)
        {
            throw new InconsistentModelException("Person id must not be null.");
        }
        if (name.getFirstName() == null)
        {
            throw new InconsistentModelException("Frst name must not be null.");
        }
        if (name.getLastName() == null)
        {
            throw new InconsistentModelException("Last name must not be null.");
        }
    }

    public static void validateName(NameDto nameDto) throws InconsistentStateException
    {
        if (nameDto == null)
        {
            throw new IllegalArgumentException("Name DTO must not be null.");
        }
        if (nameDto.getPersonId() == null)
        {
            throw new InconsistentStateException("Person id must not be null.");
        }
        if (nameDto.getFirstName() == null)
        {
            throw new InconsistentStateException("Frst name must not be null.");
        }
        if (nameDto.getLastName() == null)
        {
            throw new InconsistentStateException("Last name must not be null.");
        }
    }

    public static void validateAccessLevel(AccessLevel accLevel, boolean isNew) throws InconsistentModelException
    {
        if (accLevel == null)
        {
            throw new IllegalArgumentException("Access level must not be null.");
        }
        if (isNew)
        {
            if (accLevel.getId() != null)
            {
                throw new InconsistentModelException("Id must not be set.");
            }
        }
        else
        {
            if (accLevel.getId() == null)
            {
                throw new InconsistentModelException("Id must not be null.");
            }
        }
        if (accLevel.getDescription() == null)
        {
            throw new InconsistentModelException("Description must not be null.");
        }
    }

    public static void validateAccessLevel(AccessLevelDto accLevel, boolean isNew) throws InconsistentStateException
    {
        if (accLevel == null)
        {
            throw new IllegalArgumentException("Access level must not be null.");
        }
        if (isNew)
        {
            if (accLevel.getId() != null)
            {
                throw new InconsistentStateException("Id must not be set.");
            }
        }
        else
        {
            if (accLevel.getId() == null)
            {
                throw new InconsistentStateException("Id must not be null.");
            }
        }
        if (accLevel.getDescription() == null)
        {
            throw new InconsistentStateException("Description must not be null.");
        }
    }
}