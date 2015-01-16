package youapp.model;

import java.util.Date;

public class PersonBlocked {
	private Long blockerId;
	private Long blockedId;
	private Date since;

	/**
	 * @return the blocker
	 */
	public Long getBlockerId() {
		return blockerId;
	}

	/**
	 * @param blocker
	 *            the blocker to set
	 */
	public void setBlockerId(Long blocker) {
		this.blockerId = blocker;
	}

	/**
	 * @return the blocked
	 */
	public Long getBlockedId() {
		return blockedId;
	}

	/**
	 * @param blocked
	 *            the blocked to set
	 */
	public void setBlockedId(Long blocked) {
		this.blockedId = blocked;
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
		return blockerId + " has blocked " + blockedId + " since " + since;
	}
}
