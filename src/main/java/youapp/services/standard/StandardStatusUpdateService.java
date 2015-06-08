package youapp.services.standard;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.IStatusUpdateDao;
import youapp.dataaccess.dto.StatusUpdateDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.Mood;
import youapp.model.Person;
import youapp.model.StatusUpdate;
import youapp.services.MoodService;
import youapp.services.PersonService;
import youapp.services.StatusUpdateService;

public class StandardStatusUpdateService
    implements StatusUpdateService
{
    /**
     * Logger.
     */
    static final Log log = LogFactory.getLog(StandardStatusUpdateService.class);

    private MoodService moodService;

    private PersonService personService;

    private IStatusUpdateDao statusUpdateDao;

    @Autowired
    public void setMoodService(MoodService moodService)
    {
        this.moodService = moodService;
    }

    @Autowired
    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }

    @Autowired
    public void setStatusUpdateDao(IStatusUpdateDao statusUpdateDao)
    {
        this.statusUpdateDao = statusUpdateDao;
    }

    @Override
    public StatusUpdate getById(Long personId, Timestamp when) throws DataAccessLayerException, ModelException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (when == null)
        {
            throw new IllegalArgumentException("Date when must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Retrieving status update by id. Person id: " + personId + ", Date when: " + when);
        }

        StatusUpdateDto statusUpdateDto = statusUpdateDao.getById(personId, when);
        Person person = null;
        if (statusUpdateDto.getPersonId() != null)
        {
            person = personService.getById(statusUpdateDto.getPersonId());
        }
        Mood mood = null;
        if (statusUpdateDto.getMoodId() != null)
        {
            mood = moodService.getById(statusUpdateDto.getMoodId());
        }
        int id = 0;
        if (statusUpdateDto.getId() != 0)
        {
        	id = statusUpdateDto.getId();
        }

        return reassembleStatusUpdate(statusUpdateDto, person, mood, id);
    }

    @Override
    public List<StatusUpdate> getByPerson(Long personId) throws DataAccessLayerException, ModelException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Retrieving status update by person id: " + personId);
        }

        List<StatusUpdateDto> statusUpdateDtos = statusUpdateDao.getByPerson(personId);
        List<StatusUpdate> statusUpdates = new LinkedList<StatusUpdate>();
        Person person = personService.getById(personId);

        for (StatusUpdateDto statusUpdateDto : statusUpdateDtos)
        {
            Mood mood = null;
            if (statusUpdateDto.getMoodId() != null)
            {
                mood = moodService.getById(statusUpdateDto.getMoodId());
            }
            int id = 0;
            if (statusUpdateDto.getId() != 0)
            {
            	id = statusUpdateDto.getId();
            }

            statusUpdates.add(reassembleStatusUpdate(statusUpdateDto, person, mood, id));
        }

        return statusUpdates;
    }

    @Override
    public StatusUpdate getLastByPerson(Long personId) throws DataAccessLayerException, ModelException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Retrieving last status update of Person with id: " + personId);
        }

        StatusUpdateDto statusUpdateDto = statusUpdateDao.getLastById(personId);
        Person person = null;
        if (statusUpdateDto.getPersonId() != null)
        {
            person = personService.getById(statusUpdateDto.getPersonId());
        }
        Mood mood = null;
        if (statusUpdateDto.getMoodId() != null)
        {
            mood = moodService.getById(statusUpdateDto.getMoodId());
        }
        int id = 0;
        if (statusUpdateDto.getId() != 0)
        {
        	id = statusUpdateDto.getId();
        }

        return reassembleStatusUpdate(statusUpdateDto, person, mood, id);
    }

    @Override
    public List<StatusUpdate> getByDate(Timestamp when) throws DataAccessLayerException, ModelException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving status update by date: " + when);
        }

        List<StatusUpdateDto> statusUpdateDtos = statusUpdateDao.getByDate(when);
        List<StatusUpdate> statusUpdates = new LinkedList<StatusUpdate>();

        for (StatusUpdateDto statusUpdateDto : statusUpdateDtos)
        {
            Person person = null;
            if (statusUpdateDto.getPersonId() != null)
            {
                person = personService.getById(statusUpdateDto.getPersonId());
            }
            Mood mood = null;
            if (statusUpdateDto.getMoodId() != null)
            {
                mood = moodService.getById(statusUpdateDto.getMoodId());
            }
            int id = 0;
            if (statusUpdateDto.getId() != 0)
            {
            	id = statusUpdateDto.getId();
            }

            statusUpdates.add(reassembleStatusUpdate(statusUpdateDto, person, mood, id));
        }

        return statusUpdates;
    }

    @Override
    public List<StatusUpdate> getByPersonAndSoulmates(Long personId, Integer offset, Integer resultSize)
        throws DataAccessLayerException,
        ModelException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving status update by person id and its soulmates: " + personId);
        }

        List<StatusUpdateDto> statusUpdateDtos = statusUpdateDao.getByPersonAndSoulmates(personId, offset, resultSize);
        List<StatusUpdate> statusUpdates = new LinkedList<StatusUpdate>();

        for (StatusUpdateDto statusUpdateDto : statusUpdateDtos)
        {
            Person person = null;
            if (statusUpdateDto.getPersonId() != null)
            {
                person = personService.getById(statusUpdateDto.getPersonId());
            }
            Mood mood = null;
            if (statusUpdateDto.getMoodId() != null)
            {
                mood = moodService.getById(statusUpdateDto.getMoodId());
            }
            int id = 0;
            if (statusUpdateDto.getId() != 0)
            {
            	id = statusUpdateDto.getId();
            }

            statusUpdates.add(reassembleStatusUpdate(statusUpdateDto, person, mood, id));
        }

        return statusUpdates;
    }

    @Override
    public List<StatusUpdate> getByPersonAndSoulmates(Long personId, Timestamp startDate, Integer offset,
        Integer resultSize) throws DataAccessLayerException, ModelException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving status update by person id and its soulmates: " + personId);
        }

        List<StatusUpdateDto> statusUpdateDtos =
            statusUpdateDao.getByPersonAndSoulmates(personId, startDate, offset, resultSize);
        List<StatusUpdate> statusUpdates = new LinkedList<StatusUpdate>();

        for (StatusUpdateDto statusUpdateDto : statusUpdateDtos)
        {
            Person person = null;
            if (statusUpdateDto.getPersonId() != null)
            {
                person = personService.getById(statusUpdateDto.getPersonId());
            }
            Mood mood = null;
            if (statusUpdateDto.getMoodId() != null)
            {
                mood = moodService.getById(statusUpdateDto.getMoodId());
            }
            int id = 0;
            if (statusUpdateDto.getId() != 0)
            {
            	id = statusUpdateDto.getId();
            }

            statusUpdates.add(reassembleStatusUpdate(statusUpdateDto, person, mood, id));
        }

        return statusUpdates;
    }

    @Override
    public boolean exists(Long personId, Timestamp when)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (when == null)
        {
            throw new IllegalArgumentException("Date when must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Check existence of status update by id. Person id: " + personId + ", Date when: " + when);
        }

        return statusUpdateDao.exists(personId, when);
    }

    @Override
    public boolean exists(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Check existence of status update by id. Person id: " + personId);
        }

        return statusUpdateDao.exists(personId);
    }

    @Override
    public void create(StatusUpdate statusUpdate) throws DataAccessLayerException, ModelException
    {
        if (statusUpdate == null)
        {
            throw new IllegalArgumentException("Status update must not be null.");
        }
        // Perform validation checks.
        validateStatusUpdate(statusUpdate);

        if (!personService.exists(statusUpdate.getPerson().getId(), false)
            || !personService.isActive(statusUpdate.getPerson().getId(), false))
        {
            throw new IllegalArgumentException("Person with id " + statusUpdate.getPerson().getId()
                + " does not exist or is not active.");
        }

        if (exists(statusUpdate.getPerson().getId(), statusUpdate.getWhen()))
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> StatusUpdate already exists! Stopped creation.");
            }
            return;
        }

        if (personService.exists(statusUpdate.getPerson().getId(), false))
        {

        }

        Integer moodId = null;
        if (statusUpdate.getMood() != null)
        {
            moodId = moodService.create(statusUpdate.getMood());
        }

        StatusUpdateDto statusUpdateDto = disassembleStatusUpdate(statusUpdate, moodId);
        if (log.isDebugEnabled())
        {
            log.debug("Creating status update: " + statusUpdateDto.toString());
        }
        statusUpdateDao.create(statusUpdateDto);
    }

    @Override
    public void update(StatusUpdate statusUpdate) throws DataAccessLayerException, ModelException
    {
        if (statusUpdate == null)
        {
            throw new IllegalArgumentException("Status update must not be null.");
        }

        // Perform validation checks.
        validateStatusUpdate(statusUpdate);

        Integer moodId = null;
        if (statusUpdate.getMood() != null)
        {
            moodService.update(statusUpdate.getMood());
            moodId = statusUpdate.getMood().getId();
        }

        StatusUpdateDto statusUpdateDto = disassembleStatusUpdate(statusUpdate, moodId);
        if (log.isDebugEnabled())
        {
            log.debug("Creating status update: " + statusUpdateDto.toString());
        }
        statusUpdateDao.update(statusUpdateDto);
    }

    @Override
    public void delete(Long personId, Timestamp when)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (when == null)
        {
            throw new IllegalArgumentException("Date when must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Delete status update by id. Person id: " + personId + ", Date when: " + when);
        }
        statusUpdateDao.delete(personId, when);
    }

    @Override
    public void deleteAll(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Delete all status updates by person id: " + personId);
        }
        statusUpdateDao.deleteAll(personId);
    }

    private StatusUpdate reassembleStatusUpdate(StatusUpdateDto statusUpdateDto, Person person, Mood mood, int id)
        throws InconsistentStateException
    {
        // Check parameter null pointers.
        if (statusUpdateDto == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        if (person == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        // Validate.
        validateStatusUpdate(statusUpdateDto);

        // Reassemble.
        if (log.isDebugEnabled())
        {
            log.debug("Reassembling status update: " + statusUpdateDto.toString());
        }

        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setId(id);
        if (log.isDebugEnabled())
        {
            log.debug(">>> Id: " + id);
        }
        statusUpdate.setPerson(person);
        if (log.isDebugEnabled())
        {
            log.debug(">>> Person: " + person);
        }
        statusUpdate.setWhen(statusUpdateDto.getWhen());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Date when: " + statusUpdate.getWhen());
        }
        statusUpdate.setMood(mood);
        if (log.isDebugEnabled())
        {
            log.debug(">>> Mood: " + mood);
        }
        statusUpdate.setDescription(statusUpdateDto.getDescription());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Description: " + statusUpdate.getDescription());
        }

        return statusUpdate;
    }

    private StatusUpdateDto disassembleStatusUpdate(StatusUpdate statusUpdate, Integer moodId)
    {
        // Check parameter null pointers.
        if (statusUpdate == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        StatusUpdateDto statusUpdateDto = new StatusUpdateDto();
        statusUpdateDto.setPersonId(statusUpdate.getPerson().getId());
        statusUpdateDto.setWhen(statusUpdate.getWhen());
        statusUpdateDto.setMoodId(moodId);
        statusUpdateDto.setDescription(statusUpdate.getDescription());

        return statusUpdateDto;
    }

    private void validateStatusUpdate(StatusUpdateDto statusUpdateDto) throws InconsistentStateException
    {
        if (statusUpdateDto == null)
        {
            throw new IllegalArgumentException("StatusUpdate must not be null.");
        }
        if (statusUpdateDto.getPersonId() == null)
        {
            throw new InconsistentStateException("Person id must not be null.");
        }
        if (statusUpdateDto.getWhen() == null)
        {
            throw new InconsistentStateException("Date when must not be null.");
        }
        if (statusUpdateDto.getMoodId() == null && statusUpdateDto.getDescription() == null)
        {
            throw new InconsistentStateException("Mood or Description must not be null.");
        }
    }

    private void validateStatusUpdate(StatusUpdate statusUpdate) throws InconsistentModelException
    {
        if (statusUpdate == null)
        {
            throw new IllegalArgumentException("StatusUpdate must not be null.");
        }
        if ((statusUpdate.getPerson() == null) || statusUpdate.getPerson().getId() == null)
        {
            throw new InconsistentModelException("Person id must not be null.");
        }
        if (statusUpdate.getWhen() == null)
        {
            throw new InconsistentModelException("Date when must not be null.");
        }
        if (statusUpdate.getMood() == null && statusUpdate.getDescription() == null)
        {
            throw new InconsistentModelException("Mood or Description must not be null.");
        }
    }

}
