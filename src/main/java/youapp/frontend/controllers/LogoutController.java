package youapp.frontend.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/*")
public class LogoutController extends ExceptionHandlingController
{

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(LogoutController.class);

    @RequestMapping(value = "logout/bye", method = RequestMethod.GET)
    public ModelAndView bye(HttpSession session)
    {
        if (session != null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Invalidating session.");
            }
            
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            
            //get url of this request and replace this path with the redirected controller
            String redirectUrl = request.getRequestURL().toString().replace("bye", "loggedout");
            //get accessToken for FB-logout
            String accessToken = (String) session.getAttribute("accessToken");
            
            //redirect to logout FB
            return new ModelAndView("redirect:https://www.facebook.com/logout.php?next=" + redirectUrl + "&access_token=" + accessToken);   
        }
        else
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            
            ModelAndView mav = new ModelAndView("logout/show");
            return mav;
        }       
    }

    @RequestMapping(value = "logout/loggedout", method = RequestMethod.GET)
    public ModelAndView loggedOut(HttpSession session)
    {
        if (session != null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Invalidating session.");
            }
            
        session.invalidate();
        }
        else
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
        }   
        
        ModelAndView mav = new ModelAndView("logout/show");
        return mav;
    }
}
