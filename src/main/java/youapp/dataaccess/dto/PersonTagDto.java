package youapp.dataaccess.dto;

public class PersonTagDto {

	private Long personId;
	
	private Long tagId;

	/**
	 * @return the personId
	 */
	public Long getPersonId() {
		return personId;
	}

	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	/**
	 * @return the medicationId
	 */
	public Long getTagId() {
		return tagId;
	}

	/**
	 * @param tagId the medicationId to set
	 */
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	
}
