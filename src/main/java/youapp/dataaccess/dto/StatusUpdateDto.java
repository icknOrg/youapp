package youapp.dataaccess.dto;

import java.sql.Timestamp;

public class StatusUpdateDto {
	private Long personId;
	private Timestamp when;
	private Integer moodId;
	private String description;


	/**
	 * @return the personId
	 */
	public Long getPersonId() {
		return personId;
	}

	/**
	 * @param personId
	 *            the personId to set
	 */
	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	/**
	 * @return the when
	 */
	public Timestamp getWhen() {
		return when;
	}

	/**
	 * @param when
	 *            the when to set
	 */
	public void setWhen(Timestamp when) {
		this.when = when;
	}

	/**
	 * @return the mood
	 */
	public Integer getMoodId() {
		return moodId;
	}

	/**
	 * @param mood
	 *            the mood to set
	 */
	public void setMoodId(Integer mood) {
		this.moodId = mood;
	}

	/**
	 * @return the text
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setDescription(String text) {
		this.description = text;
	}


	@Override
	public String toString() {
		return "Status update with description: " + description + " which has the mood id: " + moodId
				+ " is written of person with id: " + personId + " on " + when;
	}
}
