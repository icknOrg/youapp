package youapp.frontend.interceptors;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import youapp.jobs.FacebookSyncJob;
import youapp.model.AccessToken;
import youapp.services.FacebookFriendshipService;
import youapp.services.FacebookGroupService;
import youapp.services.FacebookPageService;
import youapp.services.PersonService;

/**
 * Checks whether a user authenticated via facebook.
 * @author neme
 *
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
	
	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(AuthenticationInterceptor.class);
	
	@Value("${fb.appId}")
	private String appId;
	
	@Value("${fb.apiKey}")
	private String apiKey;
	
	@Value("${fb.appSecret}")
	private String appSecret;
	
	@Value("${fb.scope}")
	private String scope;
	
	/**
	 * Executes the Facebook synchronization jobs.
	 */
	private AsyncTaskExecutor taskExecutor;
	
	/**
	 * Provides access to the database.
	 */
	private PersonService personService;
	
	/**
	 * Provides access to the database.
	 */
	private FacebookFriendshipService facebookFriendshipService;
	
	/**
	 * Provides access to the databaes.
	 */
	private FacebookGroupService facebookGroupService;
	
	/**
	 * Provides access to the databaes.
	 */
	private FacebookPageService facebookPageService;
	
 
	
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
	
	@Autowired
	public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	@Autowired
	public void setFacebookFriendshipService(FacebookFriendshipService facebookFriendshipService) {
		this.facebookFriendshipService = facebookFriendshipService;
	}
	
	@Autowired
	public void setFacebookGroupService(FacebookGroupService facebookGroupService) {
		this.facebookGroupService = facebookGroupService;
	}
	
	@Autowired
	public void setFacebookPageService(FacebookPageService facebookPageService) {
		this.facebookPageService = facebookPageService;
	}
 
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
	
		
      
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// Log time.
		Long currentTime = System.currentTimeMillis();
		
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
			if (log.isDebugEnabled()) {
				log.error("Session is null.");
			}
			ModelAndView mav = new ModelAndView("error/error");
			mav.addObject("message", "Authentication failed due to unknown reasons. Please try it again later.");
			throw new ModelAndViewDefiningException(mav);
		}
		
		// Get Facebook authentication information from session.
		String accessToken = (String)session.getAttribute("accessToken");
		Long expires = (Long)session.getAttribute("expires");
		Long tokenCreationTime = (Long)session.getAttribute("tokenCreationTime");
		String code = request.getParameter("code");
		if (log.isDebugEnabled()) {
			log.debug("Session data: ");
			log.debug(">>> Access token: " + accessToken);
			log.debug(">>> Expires: " + expires);
			log.debug(">>> Token creation time: " + tokenCreationTime);
			log.debug("Request parameters: ");
			log.debug(">>> Code: " + code);
		}
		
		if ((accessToken == null || expires == null) && (code == null)) {
			// Code has not been requested yet --> send redirect.
			if (log.isDebugEnabled()) {
                log.debug("No code from facebook requested yet.");
                log.debug("Redirecting to " + redirectUri + " afterwards.");
			}
			URI uri = new URI("https://www.facebook.com/dialog/oauth?client_id=" + appId +  "&redirect_uri=" + redirectUri + "&scope=" + scope);
			response.sendRedirect(uri.toString());
			return false;
		}
		else if ((accessToken != null) && (expires != null) && (code == null)) {
			// Access token has already been requested --> check validity.
			switch (InterceptorUtils.validateAccessToken(accessToken)) {
				case VALID: {
					// Everything's fine --> continue!
					if (log.isDebugEnabled()) {
						log.debug("User is authenticated --> passing through.");
						log.debug("Registration still has to be checked.");
					}
					return true;
				}
				case EXPIRED: {
					// Access token is not valid anymore. Request new!
					if (log.isDebugEnabled()) {
						log.debug("Access token expired. Requesting new!");
						log.debug("Invalidating access token and redircting to: " + redirectUri);
					}
					InterceptorUtils.clearSessionAccessToken(session);
					response.sendRedirect(redirectUri.toString());
					return false;
				}
				case CHANGED_PASSWORD: {
					// Access token is not valid anymore. Request new!
					if (log.isDebugEnabled()) {
						log.debug("User changed password. Requesting new access token!");
						log.debug("Invalidating access token and redircting to: " + redirectUri);
					}
					InterceptorUtils.clearSessionAccessToken(session);
					response.sendRedirect(redirectUri.toString());
					return false;
				}
				case DE_AUTHORIZED: {
					if (log.isDebugEnabled()) {
						log.debug("User de-authorized the app!");
						log.debug("Invalidating session and redircting to: /home/show.html");
					}
					session.invalidate();
					response.sendRedirect(response.encodeRedirectURL(request.getContextPath()) + "/");
					return false;
				}
				case LOGGED_OUT: {
					// Access token is not valid anymore. Request new!
					if (log.isDebugEnabled()) {
						log.debug("The user logged out from facebook!");
						log.debug("Invalidating access token and redircting to: " + redirectUri);
					}
					InterceptorUtils.clearSessionAccessToken(session);
					response.sendRedirect(redirectUri.toString());
					return false;
				}
				case OTHER: {
					if (log.isDebugEnabled()) {
						log.debug("Some other reason led to an invalid access token.!");
						log.debug("Invalidating session and redircting to: /home/show.html");
					}
					session.invalidate();
					response.sendRedirect(response.encodeRedirectURL(request.getContextPath()) + "/");
					return false;
				}
				default: {
					if (log.isDebugEnabled()) {
						log.debug("Some other reason led to an invalid access token.!");
						log.debug("Invalidating session and redircting to: /home/show.html");
					}
					session.invalidate();
					response.sendRedirect(response.encodeRedirectURL(request.getContextPath()) + "/");
					return false;
				}
			}
		}
		else if ((accessToken == null || expires == null) && (code != null)) {
			// Request access token!
			if (log.isDebugEnabled()) {
				log.debug("Code available, but not access token from facebook requested yet.");
			}
			AccessToken requestedToken = InterceptorUtils.requestAccessToken(appId, redirectUri.toString(), appSecret, code, currentTime);
			if (requestedToken == null) {
				if (log.isDebugEnabled()) {
					log.debug("Access token is null.");
				}
				session.invalidate();
				ModelAndView mav = new ModelAndView("error/error");
				mav.addObject("message", "Authentication failed due to unknown reasons. Please try it again later.");
				throw new ModelAndViewDefiningException(mav);
			}
	        // Store access token and continue.
	        session.setAttribute("accessToken", requestedToken.getAccessToken());
	        session.setAttribute("expires", requestedToken.getExpires());
	        session.setAttribute("tokenCreationTime", requestedToken.getTokenCreationTime());
	        session.setAttribute("isAuthenticated", true);
	        session.setAttribute("justLoggedIn", true);
	        
	        // Synchronize with Facebook.
	        Runnable job = new FacebookSyncJob(requestedToken.getAccessToken(), personService, facebookFriendshipService, facebookGroupService, facebookPageService);
	        taskExecutor.submit(job);
 
	        return true;
		}
		else {
			// Something went wrong.
			if (log.isErrorEnabled()) {
				log.error("Something went awfully wrong during authentication.");
			}
			session.invalidate();
			ModelAndView mav = new ModelAndView("error/error");
			mav.addObject("message", "Authentication failed due to unknown reasons. Please try it again later.");
			throw new ModelAndViewDefiningException(mav);
		}
	}
	
}
