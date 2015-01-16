package youapp.model;

public class AccessToken {
	
	private String accessToken;
	
	private Long expires;
	
	private Long tokenCreationTime;
	
	public AccessToken(String accessToken, Long expires, Long tokenCreationTime) {
		if (accessToken == null) {
			throw new IllegalArgumentException("Access token must not be null.");
		}
		if (expires == null) {
			throw new IllegalArgumentException("Expires must not be null.");
		}
		if (tokenCreationTime == null) {
			throw new IllegalArgumentException("Token creation time must not be null.");
		}
		this.accessToken = accessToken;
		this.expires = expires;
		this.tokenCreationTime = tokenCreationTime;
	}
	
	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}
	
	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * @return the expires
	 */
	public Long getExpires() {
		return expires;
	}
	
	/**
	 * @param expires the expires to set
	 */
	public void setExpires(Long expires) {
		this.expires = expires;
	}
	
	/**
	 * @return the tokenCreationTime
	 */
	public Long getTokenCreationTime() {
		return tokenCreationTime;
	}
	
	/**
	 * @param tokenCreationTime the tokenCreationTime to set
	 */
	public void setTokenCreationTime(Long tokenCreationTime) {
		this.tokenCreationTime = tokenCreationTime;
	}
	
}
