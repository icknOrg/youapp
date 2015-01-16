package youapp.model;

public class FacebookPerson {

	private Long fbId;
	
	private Integer numberOfFriends;
	
	public FacebookPerson() {
		// Default constructor.
	}
	
	public FacebookPerson(Long fbId, Integer numberOfFriends) {
		this.fbId = fbId;
		this.numberOfFriends = numberOfFriends;
	}

	public Long getFbId() {
		return fbId;
	}

	public void setFbId(Long fbId) {
		this.fbId = fbId;
	}

	public Integer getNumberOfFriends() {
		return numberOfFriends;
	}

	public void setNumberOfFriends(Integer numberOfFriends) {
		this.numberOfFriends = numberOfFriends;
	}
	
	@Override
	public String toString() {
		return fbId + " (" + numberOfFriends + ")";
	}
	
}
