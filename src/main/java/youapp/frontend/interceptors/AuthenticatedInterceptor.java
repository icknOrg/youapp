package youapp.frontend.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import youapp.model.AccessToken;

public class AuthenticatedInterceptor implements HandlerInterceptor {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(AuthenticatedInterceptor.class);
	
	@Value("${fb.appId}")
	private String appId;
	
	@Value("${fb.apiKey}")
	private String apiKey;
	
	@Value("${fb.appSecret}")
	private String appSecret;
	
	@Value("${fb.scope}")
	private String scope;
	
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
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception arg3) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav) throws Exception {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
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
		
		Boolean isRegistered = (Boolean)session.getAttribute("isRegistered");
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
			// User has not been authenticated yet and code has not been requested --> send redirect.
			if (log.isDebugEnabled()) {
				log.debug("User has not been authenticated and code has not been requested yet.");
				log.debug("Redirecting to login welcome page.");
			}
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath()) + "/home/showAll.html");
			return false;
		}
		else if (isRegistered == null) {
			// Something wrong --> redirect to general home screen.
			if (log.isDebugEnabled()) {
				log.debug("Registered value is not set.");
				log.debug("Redirecting to login welcome page.");
			}
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath()) + "/home/showAll.html");
			return false;
		}
		else if (isRegistered) {
			// User is already authenticated and registered --> send redirect.
			if (log.isDebugEnabled()) {
				log.debug("User is already authenticated and registered.");
				log.debug("Redirecting to registered welcome page.");
			}
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath()));
			return false;
		}
		else if ((accessToken != null || expires != null) && (code == null)) {
			// Access token has already been requested --> check validity.
//			if (!InterceptorUtils.validateAccessToken(accessToken)) {
//				// Access token is not valid anymore. Request new!
//				if (log.isDebugEnabled()) {
//					log.debug("Access token is not valid anymore. Requesting new!");
//					log.debug("Invalidating access token and redircting to: " + redirectUri);
//				}
//				session.removeAttribute("accessToken");
//				session.removeAttribute("expires");
//				session.removeAttribute("tokenCreationTime");
//				response.sendRedirect(redirectUri.toString());
//				return false;
//			} else {
//				// Everything's fine --> continue!
//				if (log.isDebugEnabled()) {
//					log.debug("User still has to register.");
//					log.debug("Redirecting to register page.");
//				}
//				return true;
//			}
			return false;
		}
		else if ((accessToken == null || expires == null) && (code != null)) {
			// Request access token!
			if (log.isDebugEnabled()) {
				log.debug("Code available, but not access token from facebook requested yet.");
			}
			AccessToken requestedToken = InterceptorUtils.requestAccessToken(appId, redirectUri.toString(), appSecret, code, currentTime);
	        // Store access token and continue.
	        session.setAttribute("accessToken", requestedToken.getAccessToken());
	        session.setAttribute("expires", requestedToken.getExpires());
	        session.setAttribute("tokenCreationTime", requestedToken.getTokenCreationTime());
	        return true;
		}
		else {
			// Something wrong --> redirect to general home screen.
			if (log.isDebugEnabled()) {
				log.debug("Registered value is not set.");
				log.debug("Redirecting to login welcome page.");
			}
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath()) + "/home/showAll.html");
			return false;
		}
	}

}
