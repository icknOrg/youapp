package youapp.services.standard;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.IPersonBlockedDao;
import youapp.dataaccess.dto.PersonBlockedDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.PersonBlocked;
import youapp.services.PersonBlockerService;
import youapp.services.PersonService;
import youapp.services.SoulmatesService;

public class StandardPersonBlockerService
    implements PersonBlockerService
{

    /**
     * Logger.
     */
    static final Log log = LogFactory.getLog(StandardPersonBlockerService.class);

    private IPersonBlockedDao personBlockedDao;

    @Autowired
    public void setPersonBlockedDao(IPersonBlockedDao personBlockedDao)
    {
        this.personBlockedDao = personBlockedDao;
    }

    private SoulmatesService soulmatesService;

    @Autowired
    public void setSoulmatesService(SoulmatesService soulmatesService)
    {
        this.soulmatesService = soulmatesService;
    }

    private PersonService personService;
    
    @Autowired
    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }
    
    @Override
    public PersonBlocked getById(Long blockerId, Long blockedId) throws DataAccessLayerException, ModelException
    {
        if (blockerId == null)
        {
            throw new IllegalArgumentException("Blocker person id must not be null.");
        }
        if (blockedId == null)
        {
            throw new IllegalArgumentException("Blocked person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving personblocked of blocker person with id " + blockerId
                + " and blocked person with id " + blockedId + ".");
        }
        PersonBlockedDto personBlocked = null;

        // Retrieve PersonBlockedDto
        personBlocked = personBlockedDao.getById(blockerId, blockedId);

        return reassemblePersonBlocked(personBlocked);
    }

    @Override
    public List<PersonBlocked> getByBlocker(Long blockerId) throws DataAccessLayerException, ModelException
    {
        if (blockerId == null)
        {
            throw new IllegalArgumentException("Blocker person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving personBlockeds of blocker person with id " + blockerId);
        }

        // Retrieve personBlockedDtos and the persons for it
        List<PersonBlockedDto> personBlockedDtos = personBlockedDao.getByBlocker(blockerId);
        List<PersonBlocked> personBlockeds = new LinkedList<PersonBlocked>();
        for (PersonBlockedDto personBlockedDto : personBlockedDtos)
        {
            personBlockeds.add(reassemblePersonBlocked(personBlockedDto));
        }

        return personBlockeds;
    }

    @Override
    public List<PersonBlocked> getByBlocked(Long blockedId) throws DataAccessLayerException, ModelException
    {
        if (blockedId == null)
        {
            throw new IllegalArgumentException("Blocked person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving personBlockeds of blocked person with id " + blockedId);
        }

        // Retrieve personBlockedDtos and the persons for it
        List<PersonBlockedDto> personBlockedDtos = personBlockedDao.getByBlocked(blockedId);
        List<PersonBlocked> personBlockeds = new LinkedList<PersonBlocked>();
        for (PersonBlockedDto personBlockedDto : personBlockedDtos)
        {
            personBlockeds.add(reassemblePersonBlocked(personBlockedDto));
        }

        return personBlockeds;
    }

    @Override
    public boolean exists(Long blockerId, Long blockedId)
    {
        if (blockerId == null)
        {
            throw new IllegalArgumentException("Blocker person id must not be null.");
        }
        if (blockedId == null)
        {
            throw new IllegalArgumentException("Blocked person id must not be null.");
        }
        return personBlockedDao.exists(blockerId, blockedId);
    }

    @Override
    public boolean blockedAnyDirection(Long personAId, Long personBId)
    {
        if (personAId == null)
        {
            throw new IllegalArgumentException("Person A id must not be null.");
        }
        if (personBId == null)
        {
            throw new IllegalArgumentException("Person B id must not be null.");
        }
        return personBlockedDao.blockedAnyDirection(personAId, personBId);
    }

    @Override
    public void create(PersonBlocked personBlocked) throws InconsistentModelException, DataAccessLayerException
    {
        if (personBlocked == null)
        {
            throw new IllegalArgumentException("PersonBlocked must not be null.");
        }
        validatePersonBlocked(personBlocked);
        if (!personService.exists(personBlocked.getBlockedId(), false)
            || !personService.isActive(personBlocked.getBlockedId(), false))
        {
            throw new IllegalArgumentException("Blocked with id " + personBlocked.getBlockedId()
                + " does not exist or is not active.");
        }
        if (!personService.exists(personBlocked.getBlockerId(), false)
            || !personService.isActive(personBlocked.getBlockerId(), false))
        {
            throw new IllegalArgumentException("Blocker with id " + personBlocked.getBlockerId()
                + " does not exist or is not active.");
        }

        // if the blocker and the blocked are soulmates, delete them in the soulmates db
        if (soulmatesService.existsAnyDirection(personBlocked.getBlockerId(), personBlocked.getBlockedId()))
        {
            soulmatesService.deleteAnyDirection(personBlocked.getBlockerId(), personBlocked.getBlockedId());
        }

        if (exists(personBlocked.getBlockedId(), personBlocked.getBlockedId()))
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> PersonBlocked for this persons already exists.");
            }
            return;
        }

        PersonBlockedDto personBlockedDto = disassemblePersonBlocked(personBlocked);
        personBlockedDao.create(personBlockedDto);
    }

    @Override
    public void delete(Long blockerId, Long blockedId)
    {
        if (blockerId == null)
        {
            throw new IllegalArgumentException("Blocker person id must not be null.");
        }
        if (blockedId == null)
        {
            throw new IllegalArgumentException("Blocked person id must not be null.");
        }
        if (!exists(blockerId, blockedId))
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> No personBlocked with blocker person with id " + blockerId
                    + " and with blocked person with id " + blockedId + " present.");
            }
            return;
        }

        if (log.isDebugEnabled())
        {
            log.debug(">>> Delete personBlocked with blocker person with id " + blockerId
                + " and with blocked person with id " + blockedId + " present.");
        }
        personBlockedDao.delete(blockerId, blockedId);
    }

    private PersonBlocked reassemblePersonBlocked(PersonBlockedDto personBlockedDto) throws InconsistentStateException
    {
        // Check parameter null pointers.
        if (personBlockedDto == null)
        {
            throw new IllegalArgumentException("PersonBlocked Dto must not be null.");
        }

        // Validate.
        validatePersonBlocked(personBlockedDto);

        // Reassemble.
        if (log.isDebugEnabled())
        {
            log.debug("Reassembling personBlocked: " + personBlockedDto.toString());
        }

        // Create personblocked object.
        PersonBlocked personBlocked = new PersonBlocked();
        personBlocked.setBlockerId(personBlockedDto.getBlockerId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Blocker person: " + personBlocked.getBlockerId());
        }
        personBlocked.setBlockedId(personBlockedDto.getBlockedId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Blocked person: " + personBlocked.getBlockedId());
        }
        personBlocked.setSince(personBlocked.getSince());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Since: " + personBlocked.getSince());
        }

        return personBlocked;
    }

    private PersonBlockedDto disassemblePersonBlocked(PersonBlocked personBlocked)
    {
        if (personBlocked == null)
        {
            throw new IllegalArgumentException("PersonBlocked must not be null.");
        }
        PersonBlockedDto personBlockedDto = new PersonBlockedDto();
        personBlockedDto.setBlockerId(personBlocked.getBlockerId());
        personBlockedDto.setBlockedId(personBlocked.getBlockedId());
        personBlockedDto.setSince(personBlocked.getSince());
        return personBlockedDto;
    }

    private void validatePersonBlocked(PersonBlockedDto personBlockedDto) throws InconsistentStateException
    {
        if (personBlockedDto == null)
        {
            throw new IllegalArgumentException("PersonBlocked DTO must not be null.");
        }

        // Check mandatory friendship data
        if (personBlockedDto.getBlockerId() == null)
        {
            throw new InconsistentStateException("Blocker id must not be null.");
        }
        if (personBlockedDto.getBlockedId() == null)
        {
            throw new InconsistentStateException("Blocked id must not be null.");
        }
        if (personBlockedDto.getSince() == null)
        {
            throw new InconsistentStateException("Since must not be null.");
        }
    }

    private void validatePersonBlocked(PersonBlocked personBlocked) throws InconsistentModelException
    {
        if (personBlocked == null)
        {
            throw new IllegalArgumentException("PersonBlocked DTO must not be null.");
        }

        // Check mandatory personBlocked data
        if (personBlocked.getBlockerId() == null)
        {
            throw new InconsistentModelException("Blocker id must not be null.");
        }
        if (personBlocked.getBlockedId() == null)
        {
            throw new InconsistentModelException("Blocked id must not be null.");
        }
        if (personBlocked.getSince() == null)
        {
            throw new InconsistentModelException("Since must not be null.");
        }
    }
}
