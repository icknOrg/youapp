package youapp.frontend.controllers;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import youapp.exception.GenericException;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.frontend.interceptors.InterceptorUtils;
import youapp.model.Person;
import youapp.services.PersonService;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.types.User;

@Controller
@RequestMapping("/*")
public class LoginController extends ExceptionHandlingController
{	
    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(LoginController.class);

    /**
     * Provides access to the database.
     */
    private PersonService personService;    
   

    @Autowired
    public void setPersonDao(PersonService personService)
    {
        this.personService = personService;
    }
 
    @RequestMapping(value = "login/show", method = RequestMethod.GET)
    public ModelAndView show(HttpSession session) throws GenericException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Entered show.");
        }
        return prepareView(session, false);
    }

    @RequestMapping(value = "login/register", method = RequestMethod.GET)
    public ModelAndView register(HttpSession session) throws GenericException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Entered register.");
        }
        return prepareView(session, true);
    }

    /**
     * Prepares the model and view for both /show and /register.
     * 
     * @param session the current session.
     * @param isRegister true, if this method is called from /register, false
     *            otherwise.
     * @return the model and view, ready to be returned.
     * @throws GenericException
     */
    public ModelAndView prepareView(HttpSession session, boolean isRegister) throws GenericException
    {
        if (session == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Session is null.");
            }
            throw new GenericException("Session is null.");
        }        
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        if (isAuthenticated == null)
        {
            // User did not authenticate.
            if (log.isErrorEnabled())
            {
                log.error("Authenticated flag is null.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Access denied. Please login or register.");
        }
        else if (!isAuthenticated)
        {
            // User did not authenticate.
            if (log.isErrorEnabled())
            {
                log.error("Authenticated flag is false.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Access denied. Please login or register.");
        }
        else
        {
            // User did authenticate --> get access token.
            if (log.isDebugEnabled())
            {
                log.debug("User is authenticated.");
            }
            String accessToken = (String) session.getAttribute("accessToken");
            if (accessToken == null)
            {
                if (log.isErrorEnabled())
                {
                    log.error("Access token is null.");
                }
                InterceptorUtils.clearSessionAttributesComplete(session);
                throw new GenericException("No access token found.");
            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Found access token: " + accessToken);
                }
                Boolean isRegistered = (Boolean) session.getAttribute("isRegistered");
                // TODO it was isRegistered = false, mistake or wanted?
                if ((isRegistered == null) || (isRegistered == false))
                {
                    // User is not logged in --> set up facebook client and
                    // fetch basic user data.
                    if (log.isDebugEnabled())
                    {
                        log.debug("Setting up facebook client and fetching basic user facebook data.");
                    }
                    FacebookClient facebookClient = null;
                    User user = null;
                    try
                    {
                        facebookClient = new DefaultFacebookClient(accessToken);
                        user =
                            facebookClient.fetchObject("me", User.class,
                                Parameter.with("fields", "id, name, first_name, last_name, gender, birthday"));
                    }
                    catch (FacebookException e)
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug(e.getMessage());
                        }
                        InterceptorUtils.clearSessionAttributesComplete(session);
                        throw new GenericException(
                            "There was an error while communicating with facebook. Please try again later.");
                    }
                    String userId = user.getId();
                    Long uId = null;
                    try
                    {
                        uId = Long.parseLong(userId);
                    }
                    catch (NumberFormatException e)
                    {
                        if (log.isErrorEnabled())
                        {
                            log.error(e.getMessage());
                        }
                        throw new GenericException(
                            "Authentication failed due to unknown reasons. Please try it again later.");
                    }
                    if (log.isDebugEnabled())
                    {
                        log.debug(">>> User name: " + user.getName());
                        log.debug(">>> User first name: " + user.getFirstName());
                        log.debug(">>> User last name: " + user.getLastName());
                        log.debug(">>> User id: " + uId);
                        log.debug(">>> User gender: " + user.getGender());
                        log.debug(">>> User birthday: " + user.getBirthday());
                    }

                    Person person = null;
                    try
                    {
                        if (personService.exists(uId, true))
                        {
                            // Person with the given facebook id already exists
                            // actively or passively.
                            if (log.isDebugEnabled())
                            {
                                log.debug("Person already exists, fb id: " + uId);
                            }
                            person = personService.getByFbId(uId);
                            if (person.getActivated())
                            {
                                // Person already activated and therefore
                                // registered.
                                if (log.isDebugEnabled())
                                {
                                    log.debug("Person is activated, fb id: " + uId);
                                }
                                personService.updateLastOnline(person.getId(), new Date(System.currentTimeMillis())); 
                                 
                                // Synchronize with Facebook for additional Facebook data.
//                                taskExecutor.execute(new FacebookSyncJob(accessToken, personService, facebookFriendshipService,
//                                    facebookGroupService, facebookPageService));
                                
                                return new ModelAndView("redirect:/");
                            }
                            else
                            {
                                // Person is not activated yet and still has to
                                // register.
                                if (log.isErrorEnabled())
                                {
                                    log.error("Person is not active yet, fb id: " + uId);
                                }
                                if (isRegister)
                                {
                                    if (log.isDebugEnabled())
                                    {
                                        log.debug("Redirecting to registration form.");
                                    }
                                    return new ModelAndView("redirect:/registration/register.html");
                                }
                                else
                                {
                                    if (log.isDebugEnabled())
                                    {
                                        log.debug("Redirecting to registration notification.");
                                    }
                                    return new ModelAndView("redirect:/registration/register.html");
                                }
                            }
                        }
                        else
                        {
                            // Person is new and still has to register.
                            if (log.isErrorEnabled())
                            {
                                log.error("Person is completely new, fb id: " + uId);
                            }
                            if (isRegister)
                            {
                                if (log.isDebugEnabled())
                                {
                                    log.debug("Redirecting to registration form.");
                                }
                                return new ModelAndView("redirect:/registration/register.html");
                            }
                            else
                            {
                                if (log.isDebugEnabled())
                                {
                                    log.debug("Redirecting to registration notification.");
                                }
                                return new ModelAndView("redirect:/registration/register.html");
                            }
                        }
                    }
                    catch (DataAccessLayerException e)
                    {
                        if (log.isErrorEnabled())
                        {
                            log.error(e.getMessage());
                        }
                        throw new GenericException("Error while accessing the database. Please try again later.");
                    }
                    catch (ModelException e)
                    {
                        if (log.isErrorEnabled())
                        {
                            log.error(e.getMessage());
                        }
                        throw new GenericException("Error while accessing the database. Please try again later.");
                    }
                }
                else
                {
                    // User registration has been checked already and user is
                    // logged in.
                    if (log.isDebugEnabled())
                    {
                        log.debug("User is logged in already.");
                    }
                    Long personId = (Long) session.getAttribute("personId");
                    if (personId != null)
                    {
                        try
                        {
                            personService.updateLastOnline(personId, new Date(System.currentTimeMillis()));
                        }
                        catch (Exception e)
                        {
                            if (log.isDebugEnabled())
                            {
                                log.debug("Exception while updating updating last login: " + e.getMessage());
                            }
                        }                         
                       
//                        computeMatching.computeMatchingForPerson(personId);
                    }
                    else
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("Person id is null. Can't update last login.");
                        }
                    }

                    if (log.isDebugEnabled())
                    {
                        log.debug("Redirect to root page.");
                    }
                    return new ModelAndView("redirect:/");
                }
            }
        }
    }

}
