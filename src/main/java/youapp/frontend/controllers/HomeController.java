package youapp.frontend.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import youapp.exception.GenericException;
import youapp.frontend.interceptors.InterceptorUtils;

@Controller
@RequestMapping("/*")
public class HomeController
    extends ExceptionHandlingController
{

    public static final Long personId = 1L;

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(HomeController.class);

    private StatusUpdateController statusUpdateController;

    @Autowired
    public void setStatusUpdateController(StatusUpdateController statusUpdateController)
    {
        this.statusUpdateController = statusUpdateController;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView show(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        if (log.isDebugEnabled())
        {
            log.debug("Entered show.");
        }
        if (session == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        // Check for authentication.
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        if ((isAuthenticated == null) || !isAuthenticated)
        {
            // User is not authenticated --> redirect to general welcome site.
            if (log.isDebugEnabled())
            {
                log.debug("User is not authenticated --> cleaning up session and redirecting to general welcome site.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            return showAll();
        }
        else
        {
            // User is authenticated.
            if (log.isDebugEnabled())
            {
                log.debug("User is authenticated.");
            }
            // Check for registration.
            Boolean isRegistered = (Boolean) session.getAttribute("isRegistered");
            if ((isRegistered == null) || (!isRegistered))
            {
                // User is not registered --> redirect to general welcome site.
                if (log.isDebugEnabled())
                {
                    log.debug("User is not registered --> redirecting to general welcome site.");
                }
                InterceptorUtils.clearSessionRegistered(session);
                return showAll();
            }
            else
            {
                // User is registered.
                if (log.isDebugEnabled())
                {
                    log.debug("User is registered.");
                }
                return statusUpdateController.showMyStream(session);
            }
        }
    }

    @RequestMapping(value = "home/showAll", method = RequestMethod.GET)
    public ModelAndView showAll()
    {
        if (log.isDebugEnabled())
        {
            log.debug("Entered showAll.");
        }
        return new ModelAndView("home/showAll");
    }

    @RequestMapping(value = "home/about", method = RequestMethod.GET)
    public ModelAndView about()
    {
        if (log.isDebugEnabled())
        {
            log.debug("Entered about.");
        }
        return new ModelAndView("home/about");
    }
    
    @RequestMapping(value = "home/termsofuse", method = RequestMethod.GET)
    public ModelAndView termsofuse()
    {
        if (log.isDebugEnabled())
        {
            log.debug("Entered termsofuse.");
        }
        return new ModelAndView("home/termsofuse");
    }
}