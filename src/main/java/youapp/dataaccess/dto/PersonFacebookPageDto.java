package youapp.dataaccess.dto;

public class PersonFacebookPageDto {
	
	private Long personId;
	
	private Long pageId;
	
	private Boolean visible;
	
	private String profileSection;
	
	private Long createdTime;

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getProfileSection() {
		return profileSection;
	}

	public void setProfileSection(String profileSection) {
		this.profileSection = profileSection;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}
	
}
