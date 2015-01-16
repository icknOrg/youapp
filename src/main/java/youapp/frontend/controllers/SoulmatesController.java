package youapp.frontend.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import youapp.MatchMaking.ComputeMatching;
import youapp.MatchMaking.ComputeMatchingJob;
import youapp.exception.GenericException;
import youapp.exception.NotFoundException;
import youapp.frontend.interceptors.InterceptorUtils;
import youapp.model.MatchingScore;
import youapp.model.Person;
import youapp.model.Soulmates;
import youapp.model.StatusUpdate;
import youapp.model.filter.PersonBlockedFilter;
import youapp.model.filter.PersonBlockedFilter.BlockDirection;
import youapp.model.filter.SoulmatesFilter;
import youapp.model.filter.SoulmatesFilter.RequestDirection;
import youapp.services.MatchingService;
import youapp.services.PersonBlockerService;
import youapp.services.PersonService;
import youapp.services.SoulmatesService;
import youapp.services.StatusUpdateService;

@Controller
@RequestMapping(value = "/*")
public class SoulmatesController
    extends ExceptionHandlingController
{

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(SoulmatesController.class);

    private SoulmatesService soulmatesService;

    @Autowired
    public void setSoulmatesService(SoulmatesService soulmatesService)
    {
        this.soulmatesService = soulmatesService;
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

    /**
     * Provides access to the database.
     */
    private MatchingService matchingService;

    @Autowired
    public void setMatchingService(MatchingService matchingService)
    {
        this.matchingService = matchingService;
    }

    private StatusUpdateService statusUpdateService;

    @Autowired
    public void setStatusUpdateService(StatusUpdateService statusUpdateService)
    {
        this.statusUpdateService = statusUpdateService;
    }
    
    private AsyncTaskExecutor taskExecutor;
	
    @Autowired
	public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
    
    private ComputeMatching computeMatching;
   
    @Autowired
	public void setComputeMatching(ComputeMatching computeMatching) {
		this.computeMatching = computeMatching;
	}

    @RequestMapping(value = "soulmates/showMySoulmates", method = RequestMethod.GET)
    public ModelAndView showMySoulmates(HttpSession session) throws Exception
    {
        // Get person id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        ModelAndView mav = new ModelAndView("soulmates/showMySoulmates");
        List<Person> soulmates = null;
        Map<Long, MatchingScore> matches = new HashMap<Long, MatchingScore>();
        Map<Long, StatusUpdate> statusUpdates = new HashMap<Long, StatusUpdate>();
        
         //compute matching
        if(session.getAttribute("matchesTask") != null){
        	Future<?> future = (Future<?>)session.getAttribute("matchesTask");
       		future.get();
        }else{
        	Future<?> future = taskExecutor.submit(new ComputeMatchingJob(personId,computeMatching));
			session.setAttribute("matchesTask", future);	
			future.get();
        }

        soulmates = personService.getByFilters(new SoulmatesFilter(personId, RequestDirection.BOTHDIRECTIONS, false));
        for (Person person : soulmates)
        {
            if (matchingService.existsMatchingScore(personId, person.getId(), false))
            {
                matches.put(person.getId(), matchingService.getMatchingScoreById(personId, person.getId(), false));
            }
            if (statusUpdateService.exists(person.getId()))
            {
                statusUpdates.put(person.getId(), statusUpdateService.getLastByPerson(person.getId()));
            }
        }

        mav.addObject("soulmatesList", soulmates);
        mav.addObject("matchesMap", matches);
        mav.addObject("statusUpdatesMap", statusUpdates);

        List<Person> personBlocked =
            personService.getByFilters(new PersonBlockedFilter(personId, BlockDirection.BLOCKER, false));
        mav.addObject("personBlockedList", personBlocked);

        List<Person> requestedPeople =
            personService.getByFilters(new SoulmatesFilter(personId, RequestDirection.REQUESTED, true));
        mav.addObject("requestedPeopleList", requestedPeople);

        return mav;
    }

    @RequestMapping(value = "soulmates/createSoulmatesRequest", method = RequestMethod.GET)
    public ModelAndView createSoulmatesRequest(@RequestParam(value = "requestedId", required = true)
    Long requestedId, HttpSession session) throws Exception
    {
        // Get person id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        if (requestedId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Requested id is null.");
            }
            throw new GenericException("Requested id is null.");
        }
        Long requesterId = (Long) session.getAttribute("personId");
        if (requesterId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Requester id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Requester id: " + requesterId);
            log.debug("Requested id: " + requestedId);
        }

        if (personBlockerService.blockedAnyDirection(requesterId, requestedId))
        {
            throw new NotFoundException();
        }

        Soulmates soulmates = new Soulmates();
        soulmates.setRequesterId(requesterId);
        soulmates.setRequestedId(requestedId);
        soulmates.setRequestPending(true);
        soulmates.setRequestSince(new Date(System.currentTimeMillis()));
        soulmates.setSoulmatesSince(null);

        // Save or update soulmates in database.
        try
        {
            if (soulmatesService.exists(requesterId, requestedId))
            {
                // Soulmates is already existent --> update! if
                if (log.isDebugEnabled())
                {
                    log.debug("Updating soulmates for requester with id " + requesterId + " and requested with id "
                        + requestedId);
                }
                soulmatesService.update(soulmates);
            }
            else if (soulmatesService.exists(requestedId, requesterId))
            {
                // Soulmates is already existent in other direction --> confirm
                // request
                if (log.isDebugEnabled())
                {
                    log.debug("Getting existing soulmates for requester with id " + requestedId
                        + " and requested with id " + requesterId);
                }
                Soulmates existingSoulmates = soulmatesService.getById(requestedId, requesterId);
                if ((existingSoulmates != null) && existingSoulmates.getRequestPending())
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("Confirm existing soulmates request for requester with id " + requestedId
                            + " and requested with id " + requesterId);
                    }
                    existingSoulmates.setRequestPending(false);
                    existingSoulmates.setSoulmatesSince(new Date(System.currentTimeMillis()));
                    soulmatesService.update(existingSoulmates);
                }

            }
            else
            {
                // Soulmates is new --> create!
                if (log.isDebugEnabled())
                {
                    log.debug("Create soulmates for requester with id " + requesterId + " and requested with id "
                        + requestedId);
                }
                soulmatesService.create(soulmates);
            }

        }
        catch (Exception e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException(
                "Creating soulmates failed because of an error during database access. Please try to create again later.");
        }

        return new ModelAndView("empty");
    }

    @RequestMapping(value = "/confirmSoulmatesRequest", method = RequestMethod.GET)
    public ModelAndView confirmSoulmatesRequest(@RequestParam(value = "requesterId", required = true)
    Long requesterId, HttpSession session) throws Exception
    {
        // Get person id and question index.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        if (requesterId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Requester id is null.");
            }
            throw new GenericException("Requester id is null.");
        }
        Long requestedId = (Long) session.getAttribute("personId");
        if (requestedId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Requester id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Requester id: " + requesterId);
            log.debug("Requested id: " + requestedId);
        }

        if (personBlockerService.blockedAnyDirection(requesterId, requestedId))
        {
            throw new NotFoundException();
        }

        Soulmates soulmates = null;

        if (soulmatesService.exists(requesterId, requestedId))
        {
            soulmates = soulmatesService.getById(requesterId, requestedId);
        }

        if (soulmates == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("No soulmates existing in database with requesterId " + requesterId + " requestedId "
                    + requestedId);
            }
            throw new GenericException("No soulmates request existing.");
        }

        if (soulmates.getRequestPending())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Confirm soulmates request for requester with id " + requesterId + " and requested with id "
                    + requestedId);
            }
            soulmates.setRequestPending(false);
            soulmates.setSoulmatesSince(new Date(System.currentTimeMillis()));

            // Update soulmates in database.
            try
            {
                if (soulmatesService.exists(requesterId, requestedId))
                {
                    // Soulmates is already existent --> update!
                    if (log.isDebugEnabled())
                    {
                        log.debug("Updating soulmates for requester with id " + requesterId + " and requested with id "
                            + requestedId);
                    }
                    soulmatesService.update(soulmates);
                }

            }
            catch (Exception e)
            {
                if (log.isErrorEnabled())
                {
                    log.error("Error during database access: " + e.getMessage());
                }
                throw new GenericException(
                    "Creating soulmates failed because of an error during database access. Please try to reply again later.");
            }
        }

        return new ModelAndView("empty");
    }

    @RequestMapping(value = "soulmates/deleteSoulmates", method = RequestMethod.GET)
    public ModelAndView deleteSoulmates(@RequestParam(value = "personBId", required = true)
    Long personBId, HttpSession session) throws Exception
    {
        // Get person id and question index.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        if (personBId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Requester id is null.");
            }
            throw new GenericException("Person B id is null.");
        }
        Long personAId = (Long) session.getAttribute("personId");
        if (personAId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Requester id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person A id: " + personAId);
            log.debug("Person B id: " + personBId);
        }

        if (soulmatesService.existsAnyDirection(personAId, personBId))
        {
            soulmatesService.deleteAnyDirection(personAId, personBId);
        }

        return new ModelAndView("empty");
    }
}
