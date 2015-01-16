package youapp.frontend.controllers;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import youapp.exception.GenericException;
import youapp.frontend.interceptors.InterceptorUtils;
import youapp.model.Mood;
import youapp.model.Person;
import youapp.model.StatusUpdate;
import youapp.services.PersonService;
import youapp.services.StatusUpdateService;

@Controller
@RequestMapping(value = "/*")
public class StatusUpdateController
    extends ExceptionHandlingController
{
    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(StatusUpdateController.class);

    private StatusUpdateService statusUpdateService;

    @Autowired
    public void setStatusUpdateService(StatusUpdateService statusUpdateService)
    {
        this.statusUpdateService = statusUpdateService;
    }

    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }

    private WidgetController widgetController;
    
    @Autowired
    public void setWidgetController(WidgetController widgetController)
    {
        this.widgetController = widgetController;
    }
    
    @RequestMapping(value = "statusupdate/showMyStream", method = RequestMethod.GET)
    public ModelAndView showMyStream(HttpSession session) throws Exception
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

        Integer offset = 0;
        Integer resultSize = 10;
        
        List<StatusUpdate> statusUpdates = statusUpdateService.getByPersonAndSoulmates(personId, offset, resultSize);

        ModelAndView mav = new ModelAndView("statusupdate/showMyStream");
        mav.addObject("statusUpdates", statusUpdates);
        mav.addObject("myProfile", personService.getById(personId));
        mav.addObject("statusupdatesOffset", offset + resultSize);
        mav.addObject("statusupdatesResultSize", resultSize);

        // Get the objects for the widget last conversations.
        mav.addAllObjects(widgetController.getLastConversationsObjects(personId));

        return mav;
    }

    @RequestMapping(value = "statusupdate/showStatusUpdatesList", method = RequestMethod.GET)
    public ModelAndView showStatusUpdatesList(@RequestParam(value = "startDate", required = true)
    Timestamp startDate, @RequestParam(value = "offset", required = true)
    Integer offset, @RequestParam(value = "resultSize", required = true)
    Integer resultSize, HttpSession session) throws Exception
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

        List<StatusUpdate> statusUpdates =
            statusUpdateService.getByPersonAndSoulmates(personId, startDate, offset, resultSize);
        ModelAndView mav;
        if ((statusUpdates != null) && statusUpdates.size() > 0)
        {
            mav = new ModelAndView("statusupdate/showStatusUpdatesList");
            mav.addObject("statusUpdates", statusUpdates);
            mav.addObject("myProfile", personService.getById(personId));
        }
        else
        {
            mav = new ModelAndView("empty");
        }
        return mav;
    }

    @RequestMapping(value = "statusupdate/createStatusUpdate", method = RequestMethod.POST)
    public ModelAndView createStatusUpdate(@RequestParam(value = "description", required = false)
    String description, @RequestParam(value = "moodRating", required = true)
    Integer moodRating, HttpSession session) throws Exception
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
        if (StringUtils.isBlank(description) && moodRating == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Description AND moodRating are null.");
            }
            throw new GenericException("Description or moodRating must be set.");
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
            log.debug("Description: " + description);
            log.debug("Mood Rating: " + moodRating);
        }

        Person person = personService.getById(personId);
        if (person == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person is null.");
            }
            throw new GenericException("Person is null.");
        }

        Mood mood = null;
        if (moodRating != null)
        {
            mood = new Mood();
            mood.setRating(moodRating);
        }

        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setPerson(person);
        statusUpdate.setWhen(new Timestamp(System.currentTimeMillis()));
        statusUpdate.setDescription(description);
        statusUpdate.setMood(mood);

        // Save or update statusupdate in database.
        try
        {
            if (statusUpdateService.exists(statusUpdate.getPerson().getId(), statusUpdate.getWhen()))
            {
                // StatusUpdate is already existent --> update!
                if (log.isDebugEnabled())
                {
                    log.debug("Updating status update for person with id " + personId + " and timestamp "
                        + statusUpdate.getWhen());
                }
                statusUpdateService.update(statusUpdate);
            }
            else
            {
                // StatusUpdate is new --> create!
                if (log.isDebugEnabled())
                {
                    log.debug("Create status update for person with id " + personId + " and timestamp "
                        + statusUpdate.getWhen());
                }
                statusUpdateService.create(statusUpdate);
            }

        }
        catch (Exception e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException(
                "Creating StatusUpdate failed because of an error during database access. Please try to create again later.");
        }

        List<StatusUpdate> statusUpdates = new LinkedList<StatusUpdate>();
        statusUpdates.add(statusUpdate);

        ModelAndView mav = new ModelAndView("statusupdate/showStatusUpdatesList");
        mav.addObject("statusUpdates", statusUpdates);
        mav.addObject("myProfile", personService.getById(personId));
        return mav;
    }

    @RequestMapping(value = "statusupdate/deleteStatusUpdate", method = RequestMethod.GET)
    public ModelAndView deleteStatusUpdate(@RequestParam(value = "when", required = true)
    Timestamp when, HttpSession session) throws Exception
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
        if (when == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("When is null.");
            }
            throw new GenericException("When is null.");
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
            log.debug("When: " + when);
        }

        if (statusUpdateService.exists(personId, when))
        {
            statusUpdateService.delete(personId, when);
        }

        return new ModelAndView("empty");
    }
}
