package youapp.frontend.interceptors;

import java.net.URI;

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

public class LoginInterceptor implements HandlerInterceptor {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(LoginInterceptor.class);
	
	public static final Long tokenRequestBuffer = 600L;
	
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
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e)
			throws Exception {
		// do nothing
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mv) throws Exception {
		// do nothing
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
			}
			URI uri = new URI("https://www.facebook.com/dialog/oauth?client_id=" + appId +  "&redirect_uri=" + redirectUri + "&scope=" + scope);
			response.sendRedirect(uri.toString());
			return false;
		}
		else if ((accessToken != null) && (expires != null) && (code == null)) {
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
//					log.debug("Access has already been granted - passing through! :)");
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
			// Something went wrong.
			if (log.isErrorEnabled()) {
				log.error("Something went awfully wrong during authentication.");
			}
			ModelAndView mav = new ModelAndView("error/error");
			mav.addObject("message", "Authentication failed due to unknown reasons. Please try it again later.");
			throw new ModelAndViewDefiningException(mav);
		}
	}
	
}
