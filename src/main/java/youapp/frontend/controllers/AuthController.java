package youapp.frontend.controllers;

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
import youapp.model.Person;
import youapp.services.PersonService;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;

@Controller
@RequestMapping("/*")
public class AuthController extends ExceptionHandlingController {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(AuthController.class);

	/**
	 * Provides access to the database.
	 */
	private PersonService personService;

	@Autowired
	public void setPersonDao(PersonService personService) {
		this.personService = personService;
	}

	@RequestMapping(value = "auth/access", method = RequestMethod.GET)
	public ModelAndView access(HttpSession session) throws GenericException {
		ModelAndView mav = null;

		// Retrieve access token from session.
		String accessToken = (String) session.getAttribute("access_token");
		if (accessToken == null) {
			if (log.isErrorEnabled()) {
				log.error("No access token found! Cannot proceed!");
			}
			throw new GenericException("No access token found.");
		}
		else {
			if (log.isDebugEnabled()) {
				log.debug("Access token found: " + accessToken);
			}
		}

		// Set up facebook client and fetch basic user data.
		if (log.isDebugEnabled()) {
			log.debug("Setting up facebook client and fetching basic user facebook data.");
		}
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
		User user = facebookClient.fetchObject("me", User.class,
				Parameter.with("fields", "id, name, first_name, last_name, gender, birthday"));
		String userId = user.getId();
		Long uId = null;
		try {
			uId = Long.parseLong(userId);
		}
		catch (NumberFormatException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
			throw new GenericException(e.getMessage());
		}
		if (log.isDebugEnabled()) {
			log.debug(">>> User name: " + user.getName());
			log.debug(">>> User first name: " + user.getFirstName());
			log.debug(">>> User last name: " + user.getLastName());
			log.debug(">>> User id: " + uId);
			log.debug(">>> User gender: " + user.getGender());
			log.debug(">>> User birthday: " + user.getBirthday());
		}

		// Check whether the given person is already present in the database.
		Person person = null;
		try {
			if (personService.exists(uId, true)) {
				// Person with the given facebook id already exists actively or
				// passively.
				if (log.isDebugEnabled()) {
					log.debug("Person already exists: " + uId);
				}
				person = personService.getByFbId(uId);
				if (person.getActivated()) {
					// Person already registered --> skip registration!
					if (log.isDebugEnabled()) {
						log.debug("Person is activated, fb id: " + uId);
					}
					// TODO Check for updates on facebook and update the
					// database.
					mav = new ModelAndView("redirect:/");
					if (person.getId() == null) {
						if (log.isErrorEnabled()) {
							log.error("Person id is null.");
						}
						throw new GenericException("Person id is null.");
					}
					mav.addObject("command", person);
					session.setAttribute("command", person);
					mav.addObject("personId", person.getId());
					session.setAttribute("personId", person.getId());
					return mav;
				}
				else {
					// Person is not activated yet --> forward to registration.
					if (log.isDebugEnabled()) {
						log.debug("Person is not active yet: " + uId);
					}
					mav = new ModelAndView("redirect:/registration/form.html");
					person = ControllerUtils.fillPerson(user);
					if (person == null) {
						if (log.isErrorEnabled()) {
							log.error("Person backing is null.");
						}
						throw new GenericException("Person backing is null");
					}
					mav.addObject("command", person);
					session.setAttribute("command", person);
					return mav;
				}
			}
			else {
				// Person is new --> forward to registration.
				if (log.isDebugEnabled()) {
					log.debug("Person is completely new: " + uId);
				}
				mav = new ModelAndView("redirect:/registration/form.html");
				person = ControllerUtils.fillPerson(user);
				if (person == null) {
					if (log.isErrorEnabled()) {
						log.error("Person backing is null.");
					}
					throw new GenericException("Person backing is null");
				}
				mav.addObject("command", person);
				session.setAttribute("command", person);
				return mav;
			}
		}
		catch (DataAccessLayerException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
			throw new GenericException(e.getMessage());
		}
		catch (ModelException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
			throw new GenericException(e.getMessage());
		}
	}

}
