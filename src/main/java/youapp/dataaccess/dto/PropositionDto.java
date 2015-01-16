package youapp.dataaccess.dto;

import java.util.Date;

/**
 * Data transfer object for table Proposition.
 * @author neme
 *
 */
public class PropositionDto {

	private Long personId;
	
	private Long propositionId;
	
	private Date created;

	/**
	 * Returns the id of the person this proposition was dedicated to.
	 * @return the id of the person this proposition was dedicated to.
	 */
	public Long getPersonId() {
		return personId;
	}

	/**
	 * Sets the id of the person this proposition is dedicated to.
	 * @param personId the person id to set.
	 */
	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	/**
	 * Returns the id of the person which represents the proposition.
	 * @return the id of the person which represents the proposition.
	 */
	public Long getPropositionId() {
		return propositionId;
	}

	/**
	 * Sets the id of the person which represents the proposition.
	 * @param propositionId the person id to set.
	 */
	public void setPropositionId(Long propositionId) {
		this.propositionId = propositionId;
	}

	/**
	 * Returns the date when this proposition was made.
	 * @return the date when this proposition was made.
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Sets the date when this proposition was made.
	 * @param when the date to set.
	 */
	public void setCreated(Date when) {
		this.created = when;
	}
	
	@Override
	public String toString() {
		return personId + " - " + propositionId + " / " + created;
	}
	
}
