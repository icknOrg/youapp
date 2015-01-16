package youapp.frontend.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import youapp.exception.GenericException;
import youapp.exception.NotFoundException;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.frontend.interceptors.InterceptorUtils;
import youapp.frontend.validators.PersonValidator;
import youapp.jobs.FacebookSyncJob;
import youapp.model.AccessLevel;
import youapp.model.Person;
import youapp.model.Picture;
import youapp.model.Soulmates;
import youapp.model.Tag;
import youapp.model.Tag.Category;
import youapp.model.TagSet;
import youapp.services.AccessLevelService;
import youapp.services.FacebookFriendshipService;
import youapp.services.FacebookGroupService;
import youapp.services.FacebookPageService;
import youapp.services.PersonBlockerService;
import youapp.services.PersonService;
import youapp.services.PictureService;
import youapp.services.SoulmatesService;
import youapp.services.TagService;
import youapp.utility.ProfileUtility;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.types.User;
// import youapp.model.MatchingScore;
// import youapp.services.MatchingService;

@Controller
@RequestMapping("/*")
public class ProfileController
    extends ExceptionHandlingController
{

    @Autowired
    private ProfileUtility profileUtility;

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(ProfileController.class);

    /**
     * Provides access to the database.
     */
    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }

    /**
     * Provides access to the database.
     */
    private TagService tagService;

    @Autowired
    public void setTagService(TagService tagService)
    {
        this.tagService = tagService;
    }

    /**
     * Provides access to the database.
     */
    private PictureService pictureService;

    @Autowired
    public void setPictureService(PictureService pictureService)
    {
        this.pictureService = pictureService;
    }

    /**
     * Provides access to the database.
     */
    private AccessLevelService accessLevelService;

    @Autowired
    public void setAccessLevelService(AccessLevelService accessLevelService)
    {
        this.accessLevelService = accessLevelService;
    }

    /**
     * Provides access to the database.
     */
    private FacebookFriendshipService facebookFriendshipService;

    @Autowired
    public void setFacebookFriendshipService(FacebookFriendshipService facebookFriendshipService)
    {
        this.facebookFriendshipService = facebookFriendshipService;
    }

    /**
     * Provides access to the database.
     */
    private FacebookGroupService facebookGroupService;

    @Autowired
    public void setFacebookGroupService(FacebookGroupService facebookGroupService)
    {
        this.facebookGroupService = facebookGroupService;
    }

    /**
     * Provides access to the database.
     */
    private FacebookPageService facebookPageService;

    @Autowired
    public void setFacebookPageService(FacebookPageService facebookPageService)
    {
        this.facebookPageService = facebookPageService;
    }

    /**
     * Provides access to the database.
     */
    private SoulmatesService soulmatesService;

    @Autowired
    public void setSoulmatesService(SoulmatesService soulmatesService)
    {
        this.soulmatesService = soulmatesService;
    }

    /**
     * Provides access to the database.
     */
    private PersonBlockerService personBlockerService;
 
	@Autowired
    public void setPersonBlockerService(PersonBlockerService personBlockerService)
    {
        this.personBlockerService = personBlockerService;
    }

    /**
     * Executes the Facebook synchronization jobs.
     */
    private AsyncTaskExecutor taskExecutor;

    @Autowired
    public void setTaskExecutor(AsyncTaskExecutor taskExecutor)
    {
        this.taskExecutor = taskExecutor;
    }    
    
//    private MatchingService matchingService;
//    @Autowired
//    public void setMatchingService(MatchingService matchingService)
//    {
//        this.matchingService = matchingService;
//    }
    
    

    @RequestMapping(value = "profile/show", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam(value = "personId", required = false)
    Long personId, HttpSession session) throws Exception
    {
        // Get person id from session.
        if (session == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long ownPersonId = (Long) session.getAttribute("personId");
        if (ownPersonId == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Own person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
            log.debug("Own person id: " + ownPersonId);
        }

        ModelAndView mav = null;

        if ((personId == null) || personId == ownPersonId)
        {
            personId = ownPersonId;
            mav = new ModelAndView("profile/showOwn");
        }
        else
        {
            if (personBlockerService.blockedAnyDirection(ownPersonId, personId))
            {
                throw new NotFoundException();
            }

            Soulmates soulmates = null;
            if (soulmatesService.existsAnyDirection(personId, ownPersonId))
            {
                soulmates = soulmatesService.getByIdAnyDirection(personId, ownPersonId);
            }

            if ((soulmates != null) && !soulmates.getRequestPending())
            {
                mav = new ModelAndView("profile/showSoulmate");
            }
            else
            {
                mav = new ModelAndView("profile/showPublic");
                boolean confirmSoulmatesRequest = false;
                boolean isSoulmatesRequestSent = false;
                if (soulmates != null)
                {
                    if (soulmates.getRequesterId() == personId && soulmates.getRequestedId() == ownPersonId)
                    {
                        confirmSoulmatesRequest = true;
                    }
                    else
                    {
                        isSoulmatesRequestSent = true;
                    }
                }
                
               /*MatchingScore distanceScore = matchingService.getMatchingScoreById(personId, ownPersonId, false);
                double distance = distanceScore.getDistanceScore().doubleValue();
                mav.addObject("distance", distance);
                */
                mav.addObject("confirmSoulmatesRequest", confirmSoulmatesRequest);
                mav.addObject("isSoulmatesRequestSent", isSoulmatesRequestSent);
            }
        }

        // Get person data and add to ModelAndView
        Person person;
        try
        {
            person = personService.getById(personId);
        }
        catch (DataAccessLayerException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
                log.error("Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Error while accessing the database. Please try again later.");
        }
        catch (ModelException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
                log.error("Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Error while accessing the database. Please try again later.");
        }
        mav.addObject("person", person);

         
        long diffInDays = (System.currentTimeMillis() - person.getMemberSince().getTime()) / 86400000;
        mav.addObject("sinceRegistered", diffInDays);

//        log.debug("********************Bevor numberOfSoulmates zu mav hinzufuegen");
        mav.addObject("numberOfSoulmates", soulmatesService.getNumber(personId, false));
/*        
        log.debug("********************MatchingService personID:" + personId + " ownpersonId: " +ownPersonId);
        double ownPersonId_d = ownPersonId, personId_d = personId;
        MatchingScore distanceScore = matchingService.getMatchingScoreById(ownPersonId, personId, false);
        double distance = distanceScore.getDistanceScore().doubleValue();
        mav.addObject("distance", distanceScore);
        log.debug("********************MatchingService personID:" + personId + " ownpersonId: " +ownPersonId);
*/
        return mav;
    }

    @RequestMapping(value = "profile/showProfilePart", method = RequestMethod.GET)
    public ModelAndView showProfilePart(HttpSession session) throws GenericException
    {
        // Get person id from session.
        if (session == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Session is null.");
            }
            throw new GenericException("Session is null.");
        }

        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        // Get person data and add to ModelAndView
        ModelAndView mav = new ModelAndView("profile/showProfilePart");
        Person person;
        try
        {
            person = personService.getById(personId);
        }
        catch (DataAccessLayerException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
                log.error("Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Error while accessing the database. Please try again later.");
        }
        catch (ModelException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
                log.error("Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Error while accessing the database. Please try again later.");
        }
        mav.addObject("person", person);

        return mav;
    }

    @RequestMapping(value = "profile/edit", method = RequestMethod.GET)
    public ModelAndView edit(HttpSession session) throws Exception
    {
        // Get person id from session.
        if (session == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Session is null.");
            }
            throw new GenericException("Session is null.");
        }

        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        Person person = null;

        ModelAndView mav = new ModelAndView("profile/edit");

        try
        {
            person = personService.getById(personId);

            if (person == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No person with the given id in the database found: " + personId);
                }
                throw new GenericException("Invalid person selected. Please try to edit the person again.");
            }

            mav.addObject("person", person);
            mav.addObject("allMedications", getAllTagsByCategoryForJsArray(Category.Medication));
            mav.addObject("allSymptoms", getAllTagsByCategoryForJsArray(Category.Symptom));
            mav.addObject("allProfileTags", getAllTagsByCategoryForJsArray(Category.ProfileTag));
        }
        catch (DataAccessLayerException e)
        {
            throw new GenericException("Error while accessing the database. Please try again later.");
        }
        catch (ModelException e)
        {
            throw new GenericException("Error while accessing the database. Please try again later.");
        }
        return mav;
    }

    @RequestMapping(value = "profile/edit", method = RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute("person")
    Person person, BindingResult result, HttpSession session) throws GenericException
    {
        // Get person id
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }

        // Check person.
        if (person == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person from form is null.");
            }
            throw new GenericException("Error while retrieving form data: No form data available.");
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

        try
        {
            // Get the original Person object from database and put the missing
            // data in the new Person object.
            Person oldPerson = personService.getById(personId);
            person.setId(personId);
            person.setFbId(oldPerson.getFbId());
            person.setActivated(oldPerson.getActivated());
            person.setAccessLevel(oldPerson.getAccessLevel());
            person.setMemberSince(oldPerson.getMemberSince());
            person.setLastOnline(oldPerson.getLastOnline());
            person.setNickName(oldPerson.getNickName());

            for (Iterator<Picture> iterator = oldPerson.getStoredPictures(); iterator.hasNext();)
            {
                person.addPicture(iterator.next());
            }

            // Compare the tags from the original Person object with the new one
            // and add the missing tags in the new List
            TagSet oldTags = oldPerson.getTags();
            TagSet newTags = person.getTags();

            oldTags.removeAll(newTags);

            person.removeTags(oldTags);
        }
        catch (Exception e)
        {
            throw new GenericException("Error while accessing the database. Please try again later.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Person id of session: " + personId);
            log.debug("Person id of Person object: " + person.getId());
            log.debug("Facebook id of Person object: " + person.getFbId());
            log.debug("AccessLevel of Person object: " + person.getAccessLevel());
            log.debug("MemberSince of Person object: " + person.getMemberSince());
            log.debug("Activated of Person object: " + person.getActivated());
            log.debug("LastOnline of Person object: " + person.getLastOnline());
            log.debug("NickName of Person object: " + person.getNickName());
            log.debug("Location of Person object: " + person.getLocation());
            log.debug("FirstName of Person object: " + person.getFirstName());
            log.debug("LastName of Person object: " + person.getLastName());
            log.debug("Gender of Person object: " + person.getGender());
            log.debug("Birthday of Person object: " + person.getBirthday());
            log.debug("Location of Person object: " + person.getLocation());
        }

        // Validate user input.
        PersonValidator validator = new PersonValidator();
        validator.validate(person, result);
        if (result.hasErrors())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Found errors in form data.");
            }

            // Create model and view
            ModelAndView mav = new ModelAndView("profile/edit");

            try
            {
                // Build model.
                mav.addObject("person", person);
                mav.addObject("allMedications", getAllTagsByCategoryForJsArray(Category.Medication));
                mav.addObject("allSymptoms", getAllTagsByCategoryForJsArray(Category.Symptom));
                mav.addObject("allProfileTags", getAllTagsByCategoryForJsArray(Category.ProfileTag));

                return mav;
            }
            catch (DataAccessLayerException e)
            {
                throw new GenericException("Error while accessing the database. Please try again later.");
            }
        }

        // Save or update person in database.
        try
        {
            if (personService.exists(person.getId(), false))
            {
                // Person is already existent --> update!
                if (log.isDebugEnabled())
                {
                    log.debug("Updating Person with id " + person.getId());
                }
                personService.update(person);
//                //compute matching
//                taskExecutor.submit(new ComputeMatchingJob(person.getId()));
                
            }
            else
            {
                // Person is new --> create!
                if (log.isDebugEnabled())
                {
                    log.debug("Create person with id " + person.getId());
                }
                personService.create(person);
//              //compute matching
//                taskExecutor.submit(new ComputeMatchingJob(person.getId()));
            }
        }
        catch (Exception e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException(
                "Edit person failed because of an error during database access. Please try to edit again later.");
        }

        return showProfilePart(session);
    }

    @RequestMapping(value = "registration/register", method = RequestMethod.GET)
    public ModelAndView register(HttpSession session) throws GenericException
    {
        // Get access token from session.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Access token is null --> cleaning up session and redirecting to general welcome page.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            return new ModelAndView("redirect:/home/showAll.html");
        }

        // Get basic user data from facebook.
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
            throw new GenericException("There was an error while communicating with facebook. Please try again later.");
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
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Error while parsing a facebook id. Please try again later.");
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

        // Build person form backing command object.
        Person person = ControllerUtils.fillPerson(user);
        if (person == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Person is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Error while assembling the registration form data. Please try again later.");
        }

        // Create model and view.
        ModelAndView mav = new ModelAndView("profile/register");

        try
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person form backing bean data:");
                log.debug(">>> Facebook id: " + person.getFbId());
                log.debug(">>> First name: " + person.getFirstName());
                log.debug(">>> Last name: " + person.getLastName());
                log.debug(">>> Gender: " + person.getGender());
                log.debug(">>> Birthday: " + person.getBirthday());
            }

            mav.addObject("person", person);
            mav.addObject("allMedications", getAllTagsByCategoryForJsArray(Category.Medication));
            mav.addObject("allSymptoms", getAllTagsByCategoryForJsArray(Category.Symptom));
            mav.addObject("allProfileTags", getAllTagsByCategoryForJsArray(Category.ProfileTag));
            mav.addObject("profilePictureUrl", "https://graph.facebook.com/" + person.getFbId()
                + "/picture?return_ssl_resources=1&type=large");

            session.setAttribute("personFbId", person.getFbId());
        }
        catch (DataAccessLayerException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException("Error during database access. Please try again later.");
        }
        return mav;
    }

    @RequestMapping(value = "registration/register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("person")
    Person person, BindingResult result, HttpSession session) throws GenericException, InterruptedException, ExecutionException
    {
        // Get session.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }

        // Check person.
        if (person == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person from form is null.");
            }
            throw new GenericException("Error while retrieving form data: No form data available.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Person id of Person object: " + person.getId());
            log.debug("Facebook id of Person object: " + person.getFbId());
            log.debug("AccessLevel of Person object: " + person.getAccessLevel());
            log.debug("MemberSince of Person object: " + person.getMemberSince());
            log.debug("Activated of Person object: " + person.getActivated());
            log.debug("LastOnline of Person object: " + person.getLastOnline());
            log.debug("NickName of Person object: " + person.getNickName());
            log.debug("Location of Person object: " + person.getLocation());
            log.debug("FirstName of Person object: " + person.getFirstName());
            log.debug("LastName of Person object: " + person.getLastName());
            log.debug("Gender of Person object: " + person.getGender());
            log.debug("Birthday of Person object: " + person.getBirthday());
            log.debug("Location of Person object: " + person.getLocation());
        }

        // Validate user input.
        PersonValidator validator = new PersonValidator();
        validator.validate(person, result);
        if (result.hasErrors())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Found errors in form data.");
            }

            // Create model and view
            ModelAndView mav = new ModelAndView("profile/register");

            try
            {
                // Build model.
                mav.addObject("person", person);
                mav.addObject("allMedications", getAllTagsByCategoryForJsArray(Category.Medication));
                mav.addObject("allSymptoms", getAllTagsByCategoryForJsArray(Category.Symptom));
                mav.addObject("allProfileTags", getAllTagsByCategoryForJsArray(Category.ProfileTag));

                return mav;
            }
            catch (DataAccessLayerException e)
            {
                throw new GenericException("Error while accessing the database. Please try again later.");
            }
        }

        try
        {
            // Get default access level.
            AccessLevel accLevel = accessLevelService.getDefault();

            Long personFbId = (Long) session.getAttribute("personFbId");
            session.removeAttribute("personFbId");
            if (personFbId == null)
            {
                if (log.isErrorEnabled())
                {
                    log.error("Person's facebook id is null. Cleaning up session data.");
                }
                InterceptorUtils.clearSessionAttributesComplete(session);
                throw new GenericException("Person's facebook id is null.");
            }
            if (log.isDebugEnabled())
            {
                log.debug("Person's facebook id: " + personFbId);
            }

            person.setFbId(personFbId);
            person.setAccessLevel(accLevel);
            person.setActivated(true);
            person.setMemberSince(new Date(System.currentTimeMillis()));
            person.setLastOnline(new Date(System.currentTimeMillis()));
            person.addPicture(getFacebookProfilePicture(personFbId));

            Long personId = null;
            // Create person if not existent yet, else update.
            if (personService.exists(personFbId, true))
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Person is already existent.");
                }

                if (personService.isActive(personFbId, true))
                {
                    if (log.isErrorEnabled())
                    {
                        log.error("Registration form called although person has already been registered.");
                    }
                    throw new GenericException("Registration form called although person has already been registered.");
                }
                else
                {
                    // Person is not yet activated. Person will be updated in
                    // this case.
                    if (log.isDebugEnabled())
                    {
                        log.debug("Person is not yet activated. Update!");
                    }
                    Person personDb = personService.getByFbId(personFbId);
                    personId = personDb.getId();
                    person.setId(personId);
                    personService.update(person);
//                    //compute matching
//                    taskExecutor.submit(new ComputeMatchingJob(person.getId()));
                }
            }
            else
            {
                // Person is completely new --> create!
                if (log.isDebugEnabled())
                {
                    log.debug("Person is completely new. Create!");
                }
                personId = personService.create(person);
                person.setId(personId);
            }

            if (log.isDebugEnabled())
            {
                log.debug("Person id: " + personId);
            }

            // Retrieve access token for Facebook synchronization.
            String accessToken = (String) session.getAttribute("accessToken");
            if (accessToken == null)
            {
                if (log.isErrorEnabled())
                {
                    log.error("Access token is null.");
                }
                throw new GenericException("Authentication failed due to unknown reasons. Please try it again later.");
            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Found access token: " + accessToken);
                    log.debug("Synchronizing with Facebook user data.");
                }
                // Synchronize with Facebook for additional Facebook data.
//                taskExecutor.execute(new FacebookSyncJob(accessToken, personService, facebookFriendshipService,
//                    facebookGroupService, facebookPageService));
                Runnable job = new FacebookSyncJob(accessToken, personService, facebookFriendshipService,
                        facebookGroupService, facebookPageService);
    	        taskExecutor.submit(job);
//    	        future.get();
//    	        
//                //compute matching
//                taskExecutor.submit(new ComputeMatchingJob(person.getId()));
            }

            // Set "isRegistered" session attribute to indicate that the person
            // is completely registered.
            session.setAttribute("isRegistered", true);
            session.setAttribute("personId", personId);

        }
        catch (DataAccessLayerException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException(
                "Registration failed because of an error during database access. Please try to register again later.");
        }
        catch (ModelException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException(
                "Registration failed because of an error during database access. Please try to register again later.");
        }
        catch (MalformedURLException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during Facebook profile image fetching: " + e.getMessage());
            }
            throw new GenericException(
                "Registration failed because of an error during facebook profile image fetching. Please try to register again later.");
        }
        catch (IOException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during Facebook profile image fetching: " + e.getMessage());
            }
            throw new GenericException(
                "Registration failed because of an error during facebook profile image fetching. Please try to register again later.");
        }

        // Return view.
        return new ModelAndView("redirect:/profile/show.html");
    }

    @RequestMapping(value = "profile/updateProfilePicture", method = RequestMethod.GET)
    @ResponseBody
    public String updateFacebookProfilePicture(HttpSession session) throws GenericException
    {
        // Get person id from session.
        if (session == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Session is null.");
            }
            throw new GenericException("Session is null.");
        }

        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        try
        {
            Long personFbId = personService.getAlternativeId(personId, false);

            if (personFbId == null)
            {
                if (log.isErrorEnabled())
                {
                    log.error("Person facebook id is null.");
                }
                throw new GenericException("Person facebook id is null.");
            }

            List<Picture> pictures = pictureService.getByPerson(personId);
            if ((pictures == null) || pictures.size() == 0)
            {
                throw new GenericException("No pictures found of this user.");
            }

            Picture profilePicture = pictures.get(0);

            Picture fbPicture = getFacebookProfilePicture(personFbId);

            profilePicture.setCaption(fbPicture.getCaption());
            profilePicture.setPicture(fbPicture.getPicture());
            profilePicture.setThumbnail(fbPicture.getThumbnail());
            profilePicture.setPictureFormatName(fbPicture.getPictureFormatName());
            profilePicture.setThumbnailFormatName(fbPicture.getThumbnailFormatName());
            pictureService.update(profilePicture);
        }
        catch (Exception e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during Facebook profile image fetching: " + e.getMessage());
            }
            throw new GenericException("Error in database access. Couldn't update profile picture.");
        }

        return profileUtility.getProfilePictureUrl(personId);
    }

    @RequestMapping(value = "profile/picture", method = RequestMethod.GET)
    public HttpEntity<byte[]> showPicture(@RequestParam(value = "personId", required = true)
    Long personId, @RequestParam(value = "type", required = false)
    String type, HttpSession session) throws Exception
    {
        List<Picture> pictures = pictureService.getByPerson(personId);
        if ((pictures == null) || pictures.size() == 0)
        {
            return null;
        }

        BufferedImage imgBuffered;

        Picture picture = pictures.get(0);
        if (type.equalsIgnoreCase("normal") || picture.getThumbnail() == null)
        {
            imgBuffered = picture.getPicture();
        }
        else
        {
            imgBuffered = picture.getThumbnail();
        }

        byte[] imgByteArray = getDataFromBufferedImage(imgBuffered);

        if (imgByteArray == null)
        {
            // TODO Logging!!!
            return null;
        }

        // Prepare HTTP headers.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imgByteArray.length);

        // Render image.
        return new HttpEntity<byte[]>(imgByteArray, headers);
    }

    private byte[] getDataFromBufferedImage(BufferedImage imgBuffered) throws IOException
    {
        if (imgBuffered == null)
        {

            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            ImageIO.write(imgBuffered, "jpg", baos);
            baos.flush();
            return baos.toByteArray();
        }
        finally
        {
            baos.close();
        }
    }

    /**
     * Get tags for specified category prepared for Javascript-Array
     * 
     * @param category
     * @return content for javascript array - "[content]"
     * @throws DataAccessLayerException
     */
    public String getAllTagsByCategoryForJsArray(Tag.Category category) throws DataAccessLayerException
    {
        StringBuilder sb;
        // Get all tag of specified category
        TagSet categoryTagsList = tagService.getByCategory(category);
        if (categoryTagsList != null)
        {
            sb = new StringBuilder();
            for (Iterator<Tag> iterator = categoryTagsList.iterator(); iterator.hasNext();)
            {
                sb.append("\'");
                sb.append(iterator.next().getName().replace("\'", "\\\'"));
                sb.append("\'");
                if (iterator.hasNext())
                {
                    sb.append(',');
                }
            }
            return sb.toString();
        }
        else
        {
            if (log.isDebugEnabled())
            {
                log.error("Didn't get all tags of category" + category + ".");
            }
            return "";
        }
    }

    public Picture getFacebookProfilePicture(Long personFbId) throws MalformedURLException, IOException
    {
        Picture profilePicture = new Picture();
        profilePicture.setCaption("Profile Picture");

        // fetch the large profile image from facebook and set as picture
        BufferedImage fbImage =
            ImageIO.read(new URL("https://graph.facebook.com/" + personFbId
                + "/picture?return_ssl_resources=1&type=large"));
        profilePicture.setPicture(fbImage);
        profilePicture.setPictureFormatName("jpg");

        // fetch the thumbnail profile image from facebook and set as thumbnail
        BufferedImage fbThumbnail =
            ImageIO.read(new URL("https://graph.facebook.com/" + personFbId
                + "/picture?return_ssl_resourcesces=1&type=square"));
        profilePicture.setThumbnail(fbThumbnail);
        profilePicture.setThumbnailFormatName("jpg");

        return profilePicture;
    }
}
