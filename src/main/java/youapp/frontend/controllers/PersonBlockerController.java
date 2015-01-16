package youapp.frontend.controllers;

import java.util.Date;

import javax.servlet.http.HttpSession;

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
import youapp.model.PersonBlocked;
import youapp.services.PersonBlockerService;

@Controller
@RequestMapping("/*")
public class PersonBlockerController
    extends ExceptionHandlingController
{
    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(SoulmatesController.class);

    private PersonBlockerService personBlockerService;

    @Autowired
    public void setPersonBlockerService(PersonBlockerService personBlockerService)
    {
        this.personBlockerService = personBlockerService;
    }

    @RequestMapping(value = "blocker/blockPerson", method = RequestMethod.GET)
    public ModelAndView blockPerson(@RequestParam(value = "blockedId", required = true)
    Long blockedId, HttpSession session) throws Exception
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
        if (blockedId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Blocked id is null.");
            }
            throw new GenericException("Blocked id is null.");
        }
        Long blockerId = (Long) session.getAttribute("personId");
        if (blockerId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Blocker id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Blocker id: " + blockerId);
            log.debug("Blocked id: " + blockedId);
        }

        PersonBlocked personBlocked = new PersonBlocked();
        personBlocked.setBlockerId(blockerId);
        personBlocked.setBlockedId(blockedId);
        personBlocked.setSince(new Date(System.currentTimeMillis()));

        // Save or update soulmates in database.
        try
        {
            if (!personBlockerService.exists(blockerId, blockedId))
            {
                // Soulmates is new --> create!
                if (log.isDebugEnabled())
                {
                    log.debug("Create personBlocked for blocker with id " + blockerId + " and blocked with id "
                        + blockedId);
                }
                personBlockerService.create(personBlocked);
            }

        }
        catch (Exception e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException(
                "Creating personBlocked failed because of an error during database access. Please try to block again later.");
        }

        return new ModelAndView("empty");
    }

    @RequestMapping(value = "blocker/unblockPerson", method = RequestMethod.GET)
    public ModelAndView unblockPerson(@RequestParam(value = "blockedId", required = true)
    Long blockedId, HttpSession session) throws Exception
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
        if (blockedId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Blocked id is null.");
            }
            throw new GenericException("Blocked id is null.");
        }
        Long blockerId = (Long) session.getAttribute("personId");
        if (blockerId == null)
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
            log.debug("Blocker id: " + blockerId);
            log.debug("Blocked id: " + blockedId);
        }

        if (personBlockerService.exists(blockerId, blockedId))
        {
            personBlockerService.delete(blockerId, blockedId);
        }

        return new ModelAndView("empty");
    }
}
