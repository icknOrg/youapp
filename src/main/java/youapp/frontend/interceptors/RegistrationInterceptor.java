package youapp.frontend.interceptors;

import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import youapp.MatchMaking.ComputeMatching;
import youapp.MatchMaking.ComputeMatchingJob;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.model.Person;
import youapp.services.PersonService;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;

public class RegistrationInterceptor implements HandlerInterceptor {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(RegistrationInterceptor.class);
	/**
	 * Executes the matches algorithm.
	 */
	private AsyncTaskExecutor taskExecutor;
	
	private ComputeMatching computeMatching;
	/**
	 * Provides access to the database.
	 */
	private PersonService personService;
	
	@Value("${fb.appId}")
	private String appId;
	
	@Value("${fb.apiKey}")
	private String apiKey;
	
	@Value("${fb.appSecret}")
	private String appSecret;
	
	@Value("${fb.scope}")
	private String scope;
	
	@Value("${youapp.baseUrl}")
	private String baseUrl;
		
	public void setComputeMatching(ComputeMatching computeMatching){
		this.computeMatching = computeMatching;
	}
	
	public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView exception) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// Build redirect URI
		StringBuffer redirectUri = request.getRequestURL();
		
		if (log.isDebugEnabled()) {
			log.debug("Facebook properties: ");
			log.debug(">>> App ID: " + appId);
			log.debug(">>> Api Key: " + apiKey);
			log.debug(">>> App Secret: " + appSecret);
			log.debug(">>> Redirect URL: " + redirectUri);
			log.debug(">>> Scope: " + scope);
		}
		
		HttpSession session = request.getSession();
		if (session == null) {
			if (log.isErrorEnabled()) {
				log.error("Session is null.");
			}
			ModelAndView mav = new ModelAndView("error/error");
			mav.addObject("message", "Authentication failed due to unknown reasons. Please try it again later.");
			throw new ModelAndViewDefiningException(mav);
		}
		
		Boolean isAuthenticated = (Boolean)session.getAttribute("isAuthenticated");
		if (isAuthenticated == null) {
		    // if user is not authenticated, show root page - do not check if he is registered
		    response.setHeader("Cache-Control", "no-cache");
		    return true;
		} else {
			// User did authenticate --> get access token.
			if (log.isDebugEnabled()) {
				log.debug("User is authenticated.");
			}
			String accessToken = (String)session.getAttribute("accessToken");
			if (accessToken == null) {
				if (log.isErrorEnabled()) {
					log.error("Access token is null.");
				}
				ModelAndView mav = new ModelAndView("error/error");
				mav.addObject("message", "Authentication failed due to unknown reasons. Please try it again later.");
				throw new ModelAndViewDefiningException(mav);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Found access token: " + accessToken);
				}
				// Set up facebook client and fetch basic user data.
				if (log.isDebugEnabled()) {
					log.debug("Setting up facebook client and fetching basic user facebook data.");
				}
				FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
				User user = facebookClient.fetchObject("me", User.class, Parameter.with("fields", "id, name, first_name, last_name, gender, birthday"));
				String userId = user.getId();
				Long uId = null;
				try {
					uId = Long.parseLong(userId);
				} catch (NumberFormatException e) {
					if (log.isErrorEnabled()) {
						log.error(e.getMessage());
					}
					ModelAndView mav = new ModelAndView("error/error");
					mav.addObject("message", "Authentication failed due to unknown reasons. Please try it again later.");
					throw new ModelAndViewDefiningException(mav);
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
				// TODO
				// TODO: Avoid database access if "isRegistered" flag is already set.
				// TODO
				Person person = null;
				try {
					if (personService.exists(uId, true)) {
						//Person with the given facebook id already exists actively or passively.
						if (log.isDebugEnabled()) {
							log.debug("Person already exists, fb id: " + uId);
						}
						person = personService.getByFbId(uId);
						if (person.getActivated()) {
							// Person already activated and therefore registered.
							if (log.isDebugEnabled()) {
								log.debug("Person is activated, fb id: " + uId);
							}
							if (person.getId() == null) {
								if (log.isErrorEnabled()) {
									log.error("Person id is null.");
								}
								ModelAndView mav = new ModelAndView("error/error");
								mav.addObject("message", "Authentication failed due to unknown reasons. Please try it again later.");
								throw new ModelAndViewDefiningException(mav);
							} else {
								// Success! Continue! :)
								session.setAttribute("isRegistered", true);
								session.setAttribute("personId", person.getId());
								
								if(session.getAttribute("matchesTask")==null){
									Future<?> future = taskExecutor.submit(new ComputeMatchingJob(person.getId(),computeMatching));
									session.setAttribute("matchesTask", future);	
								}
								
								return true;
							}
						} else {
							// Person is not activated yet and still has to register.
							if (log.isDebugEnabled()) {
								log.debug("Person is not active yet, fb id: " + uId + " Redirect to registration form");
							}

		                    response.sendRedirect(baseUrl + "registration/register.html");
		                    return false;
						}
					} else {
						// Person is new and still has to register.
						if (log.isDebugEnabled()) {
							log.debug("Person is completely new, fb id: " + uId + " Redirect to registration form");
						}
						
						response.sendRedirect(baseUrl + "registration/register.html");
                        return false;
					}
				} catch (DataAccessLayerException e) {
					if (log.isErrorEnabled()) {
						log.error(e.getMessage());
					}
					ModelAndView mav = new ModelAndView("error/error");
					mav.addObject("message", "Authentication failed due to unknown reasons. Please try it again later.");
					throw new ModelAndViewDefiningException(mav);
				}
			}
		}
	}
	
}
