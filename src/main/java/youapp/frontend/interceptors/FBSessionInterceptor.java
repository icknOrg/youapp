package youapp.frontend.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

/**
 * Checks whether the current facebook session is still valid.
 * @author neme
 *
 */
public class FBSessionInterceptor implements HandlerInterceptor {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(FBSessionInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// Get session.
		HttpSession session = request.getSession();
		if (session == null) {
			if (log.isDebugEnabled()) {
				log.error("Session is null.");
			}
			ModelAndView mav = new ModelAndView("error/error");
			mav.addObject("message", "Authentication failed due to unknown reasons. Please try it again later.");
			throw new ModelAndViewDefiningException(mav);
		}
		
		// Check for authentication.
		Boolean isAuthenticated = (Boolean)session.getAttribute("isAuthenticated");
		if((isAuthenticated == null) || !isAuthenticated) {
			// User is not authenticated --> continue.
			if (log.isDebugEnabled()) {
				log.debug("User is not authenticated --> cleaning up session and passing through.");
			}
			InterceptorUtils.clearSessionAttributesComplete(session);
		} else {
			// User is authenticated.
			if (log.isDebugEnabled()) {
				log.debug("User is authenticated. Checking current facebook session.");
			}
			// Check whether the facebook session is still valid.
			String accessToken = (String)session.getAttribute("accessToken");
			Long expires = (Long)session.getAttribute("expires");
			if (log.isDebugEnabled()) {
				log.debug("Session data: ");
				log.debug(">>> Access token: " + accessToken);
				log.debug(">>> Expires: " + expires);
			}
			if ((accessToken != null) && (expires != null)) {
				// Access token has already been requested --> check validity.
				switch (InterceptorUtils.validateAccessToken(accessToken)) {
					case VALID: {
						// Everything's fine --> passing through!
						if (log.isDebugEnabled()) {
							log.debug("Facebook session is still valid --> passing through.");
						}
						break;
					}
					case EXPIRED: {
						// Access token is not valid anymore. Invalidate session.
						if (log.isDebugEnabled()) {
							log.debug("Access token expired --> cleaning up session and passing through.");
						}
						InterceptorUtils.clearSessionAttributesComplete(session);
						break;
					}
					case CHANGED_PASSWORD: {
						// Access token is not valid anymore. Invalidate session.
						if (log.isDebugEnabled()) {
							log.debug("User changed password --> cleaning up session and passing through.");
						}
						InterceptorUtils.clearSessionAttributesComplete(session);
						break;
					}
					case DE_AUTHORIZED: {
						if (log.isDebugEnabled()) {
							log.debug("User de-authorized the app --> cleaning up session and passing through.");
						}
						InterceptorUtils.clearSessionAttributesComplete(session);
						break;
					}
					case LOGGED_OUT: {
						// Access token is not valid anymore. Request new!
						if (log.isDebugEnabled()) {
							log.debug("The user logged out from facebook --> cleaning up session and passing through.");
						}
						InterceptorUtils.clearSessionAttributesComplete(session);
						break;
					}
					case OTHER: {
						if (log.isDebugEnabled()) {
							log.debug("Some other reason led to an invalid access token --> cleaning up session and passing through.");
						}
						InterceptorUtils.clearSessionAttributesComplete(session);
						break;
					}
					default: {
						if (log.isDebugEnabled()) {
							log.debug("Some other reason led to an invalid access token --> cleaning up session and passing through.");
						}
						InterceptorUtils.clearSessionAttributesComplete(session);
						break;
					}
				}
			}
		}
		return true;
	}
	
}
