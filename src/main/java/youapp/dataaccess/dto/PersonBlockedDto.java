package youapp.dataaccess.dto;

import java.util.Date;

public class PersonBlockedDto {
	private Long blockerId;
	private Long blockedId;
	private Date since;

	/**
	 * @return the blockerId
	 */
	public Long getBlockerId() {
		return blockerId;
	}

	/**
	 * @param blockerId
	 *            the blockerId to set
	 */
	public void setBlockerId(Long blockerId) {
		this.blockerId = blockerId;
	}

	/**
	 * @return the blockedId
	 */
	public Long getBlockedId() {
		return blockedId;
	}

	/**
	 * @param blockedId
	 *            the blockedId to set
	 */
	public void setBlockedId(Long blockedId) {
		this.blockedId = blockedId;
	}

	/**
	 * @return the since
	 */
	public Date getSince() {
		return since;
	}

	/**
	 * @param since
	 *            the since to set
	 */
	public void setSince(Date since) {
		this.since = since;
	}

	@Override
	public String toString() {
		return "Person with id " + blockerId + " has blocked person with id " + blockedId + " since " + since;
	}
}
