package youapp.dataaccess.dto;

/**
 * Data transfer object for table Match.
 * @author neme
 *
 */
public class MatchingDto {

	private Long sourceId;
	
	private Long destinationId;

	private Double repliesSimon;
	
	private Double repliesJaccard;
	
	private Double repliesAdamicAdar;
	
	private Double repliesCosim;
	
	private Double repliesScore;
	
	private Double friendsSimon;
	
	private Double friendsJaccard;
	
	private Double friendsAdamicAdar;
	
	private Double friendsCosim;
	
	private Double friendsScore;
	
	private Double groupsSimon;
	
	private Double groupsJaccard;
	
	private Double groupsAdamicAdar;
	
	private Double groupsCosim;
	
	private Double groupsScore;
	
	private Double pagesSimon;
	
	private Double pagesJaccard;
	
	private Double pagesAdamicAdar;
	
	private Double pagesCosim;
	
	private Double pagesScore;
	
	private Double finalScore;
	
//	private Double distanceScore;

	/**
	 * @return the sourceId.
	 */
	public Long getSourceId() {
		return sourceId;
	}

	/**
	 * @param sourceId the sourceId to set.
	 */
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * @return the destinationId.
	 */
	public Long getDestinationId() {
		return destinationId;
	}

	/**
	 * @param destinationId the destinationId to set.
	 */
	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}
	
	public Double getRepliesSimon() {
		return repliesSimon;
	}

	public void setRepliesSimon(Double repliesSimon) {
		this.repliesSimon = repliesSimon;
	}

	public Double getRepliesJaccard() {
		return repliesJaccard;
	}

	public void setRepliesJaccard(Double repliesJaccard) {
		this.repliesJaccard = repliesJaccard;
	}

	public Double getRepliesAdamicAdar() {
		return repliesAdamicAdar;
	}

	public void setRepliesAdamicAdar(Double repliesAdamicAdar) {
		this.repliesAdamicAdar = repliesAdamicAdar;
	}

	public Double getRepliesCosim() {
		return repliesCosim;
	}

	public void setRepliesCosim(Double repliesCosim) {
		this.repliesCosim = repliesCosim;
	}

	public Double getRepliesScore() {
		return repliesScore;
	}

	public void setRepliesScore(Double repliesScore) {
		this.repliesScore = repliesScore;
	}

	public Double getFriendsSimon() {
		return friendsSimon;
	}

	public void setFriendsSimon(Double friendsSimon) {
		this.friendsSimon = friendsSimon;
	}

	public Double getFriendsJaccard() {
		return friendsJaccard;
	}

	public void setFriendsJaccard(Double friendsJaccard) {
		this.friendsJaccard = friendsJaccard;
	}

	public Double getFriendsAdamicAdar() {
		return friendsAdamicAdar;
	}

	public void setFriendsAdamicAdar(Double friendsAdamicAdar) {
		this.friendsAdamicAdar = friendsAdamicAdar;
	}

	public Double getFriendsCosim() {
		return friendsCosim;
	}

	public void setFriendsCosim(Double friendsCosim) {
		this.friendsCosim = friendsCosim;
	}

	public Double getFriendsScore() {
		return friendsScore;
	}

	public void setFriendsScore(Double friendsScore) {
		this.friendsScore = friendsScore;
	}

	public Double getGroupsSimon() {
		return groupsSimon;
	}

	public void setGroupsSimon(Double groupsSimon) {
		this.groupsSimon = groupsSimon;
	}

	public Double getGroupsJaccard() {
		return groupsJaccard;
	}

	public void setGroupsJaccard(Double groupsJaccard) {
		this.groupsJaccard = groupsJaccard;
	}

	public Double getGroupsAdamicAdar() {
		return groupsAdamicAdar;
	}

	public void setGroupsAdamicAdar(Double groupsAdamicAdar) {
		this.groupsAdamicAdar = groupsAdamicAdar;
	}

	public Double getGroupsCosim() {
		return groupsCosim;
	}

	public void setGroupsCosim(Double groupsCosim) {
		this.groupsCosim = groupsCosim;
	}

	public Double getGroupsScore() {
		return groupsScore;
	}

	public void setGroupsScore(Double groupsScore) {
		this.groupsScore = groupsScore;
	}

	public Double getPagesSimon() {
		return pagesSimon;
	}

	public void setPagesSimon(Double pagesSimon) {
		this.pagesSimon = pagesSimon;
	}

	public Double getPagesJaccard() {
		return pagesJaccard;
	}

	public void setPagesJaccard(Double pagesJaccard) {
		this.pagesJaccard = pagesJaccard;
	}

	public Double getPagesAdamicAdar() {
		return pagesAdamicAdar;
	}

	public void setPagesAdamicAdar(Double pagesAdamicAdar) {
		this.pagesAdamicAdar = pagesAdamicAdar;
	}

	public Double getPagesCosim() {
		return pagesCosim;
	}

	public void setPagesCosim(Double pagesCosim) {
		this.pagesCosim = pagesCosim;
	}

	public Double getPagesScore() {
		return pagesScore;
	}

	public void setPagesScore(Double pagesScore) {
		this.pagesScore = pagesScore;
	}

	public Double getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(Double finalScore) {
		this.finalScore = finalScore;
	}

	@Override
	public String toString() {
		return  sourceId + " - " + destinationId + " / " + finalScore + "% friends";
/*	}

	public Double getDistanceScore() {
		return distanceScore;
	}

	public void setDistanceScore(Double distanceScore) {
		this.distanceScore = distanceScore;
*/
	}
}
