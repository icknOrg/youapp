package youapp.frontend.interceptors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import youapp.model.AccessToken;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;

public class InterceptorUtils {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(InterceptorUtils.class);
	
	/**
	 * Checks whether an access token is still valid.
	 * @param accessToken the access token to be checked.
	 * @return a reason for invalidty or else the VALID code.
	 */
	protected static AccessTokenInvalidityReason validateAccessToken(String accessToken) {
		if (accessToken == null) {
			if (log.isDebugEnabled()) {
				log.debug("Access token parameter is null.");
			}
			throw new IllegalArgumentException("Access token must not be null.");
		}
		try {
			FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
			facebookClient.fetchObject("me", User.class, Parameter.with("fields", "id"));
		} catch (FacebookOAuthException e) {
			if (log.isDebugEnabled()) {
				log.debug("Access token is invalid: " + accessToken);
				log.debug("Reason: " + e.getMessage());
			}
			String reason = e.getMessage();
			if (reason.toLowerCase().contains("session has expired")) {
				return AccessTokenInvalidityReason.EXPIRED;
			} else if (reason.toLowerCase().contains("the session is invalid because the user logged out")) {
				return AccessTokenInvalidityReason.LOGGED_OUT;
			} else if (reason.toLowerCase().contains("the user has changed the password")) {
				return AccessTokenInvalidityReason.CHANGED_PASSWORD;
			} else if (reason.toLowerCase().contains("has not authorized application")) {
				return AccessTokenInvalidityReason.DE_AUTHORIZED;
			} else {
				return AccessTokenInvalidityReason.OTHER;
			}
		} catch (FacebookException e) {
			if (log.isDebugEnabled()) {
				log.debug("Other facebook exception: " + e.getMessage());
			}
			return AccessTokenInvalidityReason.OTHER;
		}
		return AccessTokenInvalidityReason.VALID;
	}
	
	/**
	 * Requests the access token from facebook.
	 * @param appId the application id.
	 * @param redirectUri the redirect uri.
	 * @param appSecret the application secret.
	 * @param code the code.
	 * @param tokenCreationTime the token creation time.
	 * @return the access token.
	 */
	protected static AccessToken requestAccessToken(String appId, String redirectUri, String appSecret, String code, Long tokenCreationTime) {
		if (appId == null) {
			throw new IllegalArgumentException("Application id must not be null.");
		}
		if (redirectUri == null) {
			throw new IllegalArgumentException("Redirect uri must not be null.");
		}
		if (appSecret == null) {
			throw new IllegalArgumentException("Application secret must not be null.");
		}
		if (code == null) {
			throw new IllegalArgumentException("Code must not be null.");
		}
		if (tokenCreationTime == null) {
			throw new IllegalArgumentException("Token creation time must not be null.");
		}
		
		String result = null;
		try {
			URL url = new URL("https://graph.facebook.com/oauth/access_token?client_id=" + appId + "&redirect_uri=" + redirectUri + "&client_secret=" + appSecret + "&code=" + code);
			result = InterceptorUtils.readURL(url);
		} catch (IOException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
			return null;
		}
		if (log.isDebugEnabled()) {
			log.debug("Facebook access token response: " + result);
		}
		if (result == null) {
			if (log.isDebugEnabled()) {
				log.error("Access token response from facebook is empty.");
			}
			return null;
		}
		
		// Parse access token.
		if (log.isDebugEnabled()) {
			log.debug("Parsing access token.");
		}
        String accessToken = null;
        Long expires = null;
		String[] pairs = result.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length != 2) {
            	if (log.isErrorEnabled()) {
            		log.error("Unexpected auth response from facebook. Invalid format of access token: " + accessToken);
            	}
            	return null;
            } else {
                if (kv[0].equals("access_token")) {
                    accessToken = kv[1];
                }
                if (kv[0].equals("expires")) {
                    expires = Long.valueOf(kv[1]);
                }
            }
        }
        if (log.isDebugEnabled()) {
        	log.debug("Parsed access token: " + accessToken);
	        log.debug("Parsed expires value: " + expires);
        }
        if ((accessToken == null) || (expires == null)) {
        	if (log.isErrorEnabled()) {
        		log.error("Either the access token or the expires value is null. Access token: " + accessToken + ", expires: " + expires);
        	}
        	return null;
        }
		return new AccessToken(accessToken, expires, tokenCreationTime);
	}
	
	/**
	 * Clears all information from the session.
	 * @param session the session from which application specific information has to be deleted.
	 */
	public static void clearSessionAttributesComplete(HttpSession session) {
		if (session == null) {
			throw new IllegalArgumentException("Session must not be null.");
		}
		session.removeAttribute("accessToken");
		session.removeAttribute("expires");
		session.removeAttribute("tokenCreationTime");
		session.removeAttribute("isAuthenticated");
		session.removeAttribute("isRegistered");
		session.removeAttribute("personId");
	}
	
	/**
	 * Clears the access token and its dependent information from the session.
	 * @param session the session from which the access token has to be deleted.
	 */
	public static void clearSessionAccessToken(HttpSession session) {
		if (session == null) {
			throw new IllegalArgumentException("Session must not be null.");
		}
		session.removeAttribute("accessToken");
		session.removeAttribute("expires");
		session.removeAttribute("tokenCreationTime");
		session.removeAttribute("isAuthenticated");
	}
	
	/**
	 * Clears the registered flag and the person id from the session.
	 * @param session the session from which the registered flag and the person id must be deleted.
	 */
	public static void clearSessionRegistered(HttpSession session) {
		if (session == null) {
			throw new IllegalArgumentException("Session must not be null.");
		}
		session.removeAttribute("isRegistered");
		session.removeAttribute("personId");
	}
	
	/**
	 * Reads the resource identified by the given url.
	 * @param url resource identifier.
	 * @return the resource's content.
	 * @throws IOException.
	 */
	protected static String readURL(URL url) throws IOException {
		if (url == null) {
			throw new IllegalArgumentException("Url must not be null.");
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			is = url.openStream();
			int r;
			while ((r = is.read()) != -1) {
				baos.write(r);
			}
		} catch (IOException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
			throw e;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					if (log.isErrorEnabled()) {
						log.error(e.getMessage());
					}
					throw e;
				}
			}
		}
		return new String(baos.toByteArray());
    }
	
	/**
	 * Converts seconds to milliseconds.
	 * @param seconds value to be converted.
	 * @return milliseconds.
	 */
	protected static Long secondsToMilliseconds(Long seconds) {
		return seconds*1000;
	}
	
	/**
	 * Reasons for access token invalidty. VALID means, that the access token is perfectly valid.
	 * @author neme
	 *
	 */
	public enum AccessTokenInvalidityReason {
		VALID,
		EXPIRED,
		CHANGED_PASSWORD,
		DE_AUTHORIZED,
		LOGGED_OUT,
		OTHER
	}
	
}
