package youapp.services.standard;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.ISoulmatesDao;
import youapp.dataaccess.dto.SoulmatesDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.Soulmates;
import youapp.services.PersonBlockerService;
import youapp.services.PersonService;
import youapp.services.SoulmatesService;

public class StandardSoulmatesService
    implements SoulmatesService
{

    /**
     * Logger.
     */
    static final Log log = LogFactory.getLog(StandardSoulmatesService.class);

    private ISoulmatesDao soulmatesDao;

    @Autowired
    public void setSoulmatesDao(ISoulmatesDao soulmatesDao)
    {
        this.soulmatesDao = soulmatesDao;
    }

    private PersonBlockerService personBlockerService;

    @Autowired
    public void setPersonBlockerService(PersonBlockerService personBlockerService)
    {
        this.personBlockerService = personBlockerService;
    }

    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }

    @Override
    public Soulmates getById(Long requesterId, Long requestedId) throws DataAccessLayerException, ModelException
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester person id must not be null.");
        }
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving soulmates of requester person with id " + requesterId
                + " and requested person with id " + requestedId + ".");
        }
        SoulmatesDto soulmates = null;

        // Retrieve soulmates and the persons for it
        soulmates = soulmatesDao.getById(requesterId, requestedId);

        return reassembleSoulmates(soulmates);
    }

    @Override
    public Soulmates getByIdAnyDirection(Long personAId, Long personBId)
        throws DataAccessLayerException,
        ModelException
    {
        if (personAId == null)
        {
            throw new IllegalArgumentException("Person A id must not be null.");
        }
        if (personBId == null)
        {
            throw new IllegalArgumentException("Person B id must not be null.");
        }
        {
            log.debug("Retrieving soulmates of person A with id " + personAId + " and person B with id " + personBId
                + ".");
        }
        SoulmatesDto soulmates = null;

        // Retrieve soulmates and the persons for it
        soulmates = soulmatesDao.getByIdAnyDirection(personAId, personBId);

        return reassembleSoulmates(soulmates);
    }

    @Override
    public List<Soulmates> getByRequester(Long requesterId) throws DataAccessLayerException, ModelException
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving soulmates of requester person with id " + requesterId);
        }

        // Retrieve soulmates and the persons for it
        List<SoulmatesDto> soulmatesDtos = soulmatesDao.getByRequester(requesterId);
        List<Soulmates> soulmates = new LinkedList<Soulmates>();
        for (SoulmatesDto soulmatesDto : soulmatesDtos)
        {
            soulmates.add(reassembleSoulmates(soulmatesDto));
        }

        return soulmates;
    }

    @Override
    public List<Soulmates> getByRequester(Long requesterId, Boolean requestPending)
        throws DataAccessLayerException,
        ModelException
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester person id must not be null.");
        }
        if (requestPending == null)
        {
            throw new IllegalArgumentException("Request pending must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving soulmates of requester person with id " + requesterId);
        }

        // Retrieve soulmates and the persons for it
        List<SoulmatesDto> soulmatesDtos = soulmatesDao.getByRequester(requesterId, requestPending);
        List<Soulmates> soulmates = new LinkedList<Soulmates>();
        for (SoulmatesDto soulmatesDto : soulmatesDtos)
        {
            soulmates.add(reassembleSoulmates(soulmatesDto));
        }

        return soulmates;
    }

    @Override
    public List<Soulmates> getByRequested(Long requestedId) throws DataAccessLayerException, ModelException
    {
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving soulmates of requested person with id " + requestedId);
        }

        // Retrieve soulmates and the persons for it
        List<SoulmatesDto> soulmatesDtos = soulmatesDao.getByRequested(requestedId);
        List<Soulmates> soulmates = new LinkedList<Soulmates>();
        for (SoulmatesDto soulmatesDto : soulmatesDtos)
        {
            soulmates.add(reassembleSoulmates(soulmatesDto));
        }

        return soulmates;
    }

    @Override
    public List<Soulmates> getByRequested(Long requestedId, Boolean requestPending)
        throws DataAccessLayerException,
        ModelException
    {
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested person id must not be null.");
        }
        if (requestPending == null)
        {
            throw new IllegalArgumentException("Request pending must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving soulmates of requested person with id " + requestedId);
        }

        // Retrieve soulmates and the persons for it
        List<SoulmatesDto> soulmatesDtos = soulmatesDao.getByRequested(requestedId, requestPending);
        List<Soulmates> soulmates = new LinkedList<Soulmates>();
        for (SoulmatesDto soulmatesDto : soulmatesDtos)
        {
            soulmates.add(reassembleSoulmates(soulmatesDto));
        }

        return soulmates;
    }

    @Override
    public List<Soulmates> getAll(Long personId) throws DataAccessLayerException, ModelException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving soulmates of person with id " + personId);
        }

        // Retrieve soulmates and the persons for it
        List<SoulmatesDto> soulmatesDtos = soulmatesDao.getAll(personId);
        List<Soulmates> soulmates = new LinkedList<Soulmates>();
        for (SoulmatesDto soulmatesDto : soulmatesDtos)
        {
            soulmates.add(reassembleSoulmates(soulmatesDto));
        }

        return soulmates;
    }

    @Override
    public List<Soulmates> getAll(Long personId, Boolean requestPending)
        throws DataAccessLayerException,
        ModelException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (requestPending == null)
        {
            throw new IllegalArgumentException("Request pending must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving soulmates of person with id " + personId);
        }

        // Retrieve soulmates and the persons for it
        List<SoulmatesDto> soulmatesDtos = soulmatesDao.getAll(personId, requestPending);
        List<Soulmates> soulmates = new LinkedList<Soulmates>();
        for (SoulmatesDto soulmatesDto : soulmatesDtos)
        {
            soulmates.add(reassembleSoulmates(soulmatesDto));
        }

        return soulmates;
    }

    @Override
    public int getNumberByRequester(Long requesterId)
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving number of soulmates of requester person with id " + requesterId);
        }
        return soulmatesDao.getNumberByRequester(requesterId);
    }

    @Override
    public int getNumberByRequester(Long requesterId, Boolean requestPending)
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester person id must not be null.");
        }
        if (requestPending == null)
        {
            throw new IllegalArgumentException("Request pending must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving number of soulmates of requester person with id " + requesterId);
        }
        return soulmatesDao.getNumberByRequester(requesterId, requestPending);
    }

    @Override
    public int getNumberByRequested(Long requestedId)
    {
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving number of soulmates of requested person with id " + requestedId);
        }
        return soulmatesDao.getNumberByRequested(requestedId);
    }

    @Override
    public int getNumberByRequested(Long requestedId, Boolean requestPending)
    {
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested person id must not be null.");
        }
        if (requestPending == null)
        {
            throw new IllegalArgumentException("Request pending must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving number of soulmates of requested person with id " + requestedId);
        }
        return soulmatesDao.getNumberByRequested(requestedId, requestPending);
    }

    @Override
    public int getNumber(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving number of soulmates of person with id " + personId);
        }
        return soulmatesDao.getNumber(personId);
    }

    @Override
    public int getNumber(Long personId, Boolean requestPending)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (requestPending == null)
        {
            throw new IllegalArgumentException("Request pending must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving number of soulmates of person with id " + personId);
        }
        return soulmatesDao.getNumber(personId, requestPending);
    }

    @Override
    public boolean exists(Long requesterId, Long requestedId)
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester person id must not be null.");
        }
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested person id must not be null.");
        }
        return soulmatesDao.exists(requesterId, requestedId);
    }

    @Override
    public boolean existsAnyDirection(Long personAId, Long personBId)
    {
        if (personAId == null)
        {
            throw new IllegalArgumentException("Person A id must not be null.");
        }
        if (personBId == null)
        {
            throw new IllegalArgumentException("Person B id must not be null.");
        }
        return soulmatesDao.existsAnyDirection(personAId, personBId);
    }

    @Override
    public boolean areSoulmates(Long personAId, Long personBId)
    {
        if (personAId == null)
        {
            throw new IllegalArgumentException("Person A id must not be null.");
        }
        if (personBId == null)
        {
            throw new IllegalArgumentException("Person B id must not be null.");
        }
        return soulmatesDao.areSoulmates(personAId, personBId);
    }

    @Override
    public void create(Soulmates soulmates) throws DataAccessLayerException, ModelException
    {
        if (soulmates == null)
        {
            throw new IllegalArgumentException("Soulmates must not be null.");
        }
        // Perform validation checks.
        validateSoulmates(soulmates);
        if (!personService.exists(soulmates.getRequestedId(), false)
            || !personService.isActive(soulmates.getRequestedId(), false))
        {
            throw new IllegalArgumentException("Soulmates requested with id " + soulmates.getRequestedId()
                + " does not exist or is not active.");
        }
        if (!personService.exists(soulmates.getRequesterId(), false)
            || !personService.isActive(soulmates.getRequesterId(), false))
        {
            throw new IllegalArgumentException("Soulmates requester with id " + soulmates.getRequesterId()
                + " does not exist or is not active.");
        }

        if (personBlockerService.blockedAnyDirection(soulmates.getRequesterId(), soulmates.getRequestedId()))
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Requester and Requested are blocked. Stopped soulmates creation.");
            }
            return;
        }

        if (soulmatesDao.exists(soulmates.getRequesterId(), soulmates.getRequestedId()))
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Soulmates in same direction already exists.");
            }
            return;
        }
        else if (soulmatesDao.exists(soulmates.getRequestedId(), soulmates.getRequesterId()))
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Soulmates in opposite direction already exists. Set request pending on false and update soulmates.");
            }
            Soulmates existingSoulmates = getById(soulmates.getRequestedId(), soulmates.getRequesterId());
            if (existingSoulmates.getRequestPending())
            {
                existingSoulmates.setRequestPending(false);
                existingSoulmates.setSoulmatesSince(new Date(System.currentTimeMillis()));
                update(existingSoulmates);
            }
            return;
        }

        SoulmatesDto soulmatesDto = disassembleSoulmates(soulmates);
        soulmatesDao.create(soulmatesDto);
    }

    @Override
    public void update(Soulmates soulmates) throws InconsistentModelException
    {
        if (soulmates == null)
        {
            throw new IllegalArgumentException("Soulmates must not be null.");
        }

        // Perform validation checks.
        validateSoulmates(soulmates);

        SoulmatesDto soulmatesDto = disassembleSoulmates(soulmates);
        soulmatesDao.update(soulmatesDto);
    }

    @Override
    public void delete(Long requesterId, Long requestedId)
    {
        if (requesterId == null)
        {
            throw new IllegalArgumentException("Requester id must not be null.");
        }
        if (requestedId == null)
        {
            throw new IllegalArgumentException("Requested id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug(">>> Delete soulmates between requester with id " + requesterId + " and requested with id "
                + requestedId);
        }
        soulmatesDao.delete(requesterId, requestedId);
    }

    @Override
    public void deleteAnyDirection(Long personAId, Long personBId)
    {
        if (personAId == null)
        {
            throw new IllegalArgumentException("Person A id must not be null.");
        }
        if (personBId == null)
        {
            throw new IllegalArgumentException("Person B id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug(">>> Delete soulmates between person A with id " + personAId + " and person B with id "
                + personBId);
        }
        soulmatesDao.deleteAnyDirection(personAId, personBId);
    }

    private static Soulmates reassembleSoulmates(SoulmatesDto soulmatesDto)
        throws InconsistentStateException,
        InconsistentModelException
    {
        // Check parameter null pointers.
        if (soulmatesDto == null)
        {
            throw new IllegalArgumentException("Soulmates Dto must not be null.");
        }

        // Validate.
        validateSoulmates(soulmatesDto);

        // Reassemble.
        if (log.isDebugEnabled())
        {
            log.debug("Reassembling soulmates: " + soulmatesDto.toString());
        }

        // Create soulmates object.
        Soulmates soulmates = new Soulmates();
        soulmates.setRequesterId(soulmatesDto.getRequesterId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Requester person: " + soulmates.getRequesterId());
        }
        soulmates.setRequestedId(soulmatesDto.getRequestedId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Requested person: " + soulmates.getRequestedId());
        }
        soulmates.setRequestPending(soulmatesDto.getRequestPending());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Request pending: " + soulmates.getRequestPending());
        }
        soulmates.setRequestSince(soulmatesDto.getRequestSince());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Request since: " + soulmates.getRequestSince());
        }
        soulmates.setSoulmatesSince(soulmatesDto.getSoulmatesSince());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Soulmates since: " + soulmates.getSoulmatesSince());
        }

        return soulmates;
    }

    private SoulmatesDto disassembleSoulmates(Soulmates soulmates)
    {
        if (soulmates == null)
        {
            throw new IllegalArgumentException("Soulmates must not be null.");
        }
        SoulmatesDto soulmatesDto = new SoulmatesDto();
        soulmatesDto.setRequesterId(soulmates.getRequesterId());
        soulmatesDto.setRequestedId(soulmates.getRequestedId());
        soulmatesDto.setRequestPending(soulmates.getRequestPending());
        soulmatesDto.setRequestSince(soulmates.getRequestSince());
        soulmatesDto.setSoulmatesSince(soulmates.getSoulmatesSince());
        return soulmatesDto;
    }

    private static void validateSoulmates(SoulmatesDto soulmatesDto) throws InconsistentStateException
    {
        if (soulmatesDto == null)
        {
            throw new IllegalArgumentException("Soulmates DTO must not be null.");
        }

        // Check mandatory friendship data
        if (soulmatesDto.getRequesterId() == null)
        {
            throw new InconsistentStateException("Requester id must not be null.");
        }
        if (soulmatesDto.getRequestedId() == null)
        {
            throw new InconsistentStateException("Requested id must not be null.");
        }
        if ((soulmatesDto.getRequestedId() == soulmatesDto.getRequesterId()))
        {
            throw new InconsistentStateException("Requester and requested id's must not be equal.");
        }
        if (soulmatesDto.getRequestPending() == null)
        {
            throw new InconsistentStateException("Request pending must not be null.");
        }
        if (soulmatesDto.getRequestPending() && soulmatesDto.getSoulmatesSince() != null)
        {
            throw new InconsistentStateException("Soulmates since must be null, if request is pending.");
        }
        if (!soulmatesDto.getRequestPending() && soulmatesDto.getSoulmatesSince() == null)
        {
            throw new InconsistentStateException("Soulmates since must not be null, if request is not pending.");
        }
        if (soulmatesDto.getRequestPending() && soulmatesDto.getRequestSince() == null)
        {
            throw new InconsistentStateException("Request since must not be null, if request is pending.");
        }
    }

    private void validateSoulmates(Soulmates soulmates) throws InconsistentModelException
    {
        if (soulmates == null)
        {
            throw new IllegalArgumentException("Soulmates DTO must not be null.");
        }

        // Check mandatory personBlocked data
        if (soulmates.getRequesterId() == null)
        {
            throw new InconsistentModelException("Requester must not be null.");
        }
        if (soulmates.getRequesterId() == null)
        {
            throw new InconsistentModelException("Requester id must not be null.");
        }
        if (soulmates.getRequestedId() == null)
        {
            throw new InconsistentModelException("Requested must not be null.");
        }
        if (soulmates.getRequestedId() == null)
        {
            throw new InconsistentModelException("Requested id must not be null.");
        }
        if ((soulmates.getRequestedId() == soulmates.getRequesterId()))
        {
            throw new InconsistentModelException("Requester and requested id's must not be equal.");
        }
        if (soulmates.getRequestPending() == null)
        {
            throw new InconsistentModelException("Request pending must not be null.");
        }
        if (soulmates.getRequestPending() && soulmates.getSoulmatesSince() != null)
        {
            throw new InconsistentModelException("Soulmates since must be null, if request is pending.");
        }
        if (!soulmates.getRequestPending() && soulmates.getSoulmatesSince() == null)
        {
            throw new InconsistentModelException("Soulmates since must not be null, if request is not pending.");
        }
        if (soulmates.getRequestPending() && soulmates.getRequestSince() == null)
        {
            throw new InconsistentModelException("Request since must not be null, if request is pending.");
        }
    }
}
