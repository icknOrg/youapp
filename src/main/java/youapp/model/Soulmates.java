package youapp.model;

import java.util.Date;

public class Soulmates {
	private Long requesterId;
	private Long requestedId;
	private Boolean requestPending;
	private Date requestSince;
	private Date soulmatesSince;

	/**
	 * @return the requester
	 */
	public Long getRequesterId() {
		return requesterId;
	}

	/**
	 * @param requester
	 *            the requester to set
	 */
	public void setRequesterId(Long requester) {
		this.requesterId = requester;
	}

	/**
	 * @return the requested
	 */
	public Long getRequestedId() {
		return requestedId;
	}

	/**
	 * @param requested
	 *            the requested to set
	 */
	public void setRequestedId(Long requested) {
		this.requestedId = requested;
	}

	/**
	 * @return the requestPending
	 */
	public Boolean getRequestPending() {
		return requestPending;
	}

	/**
	 * @param requestPending
	 *            the requestPending to set
	 */
	public void setRequestPending(Boolean requestPending) {
		this.requestPending = requestPending;
	}

	/**
	 * @return the requestSince
	 */
	public Date getRequestSince() {
		return requestSince;
	}

	/**
	 * @param requestSince
	 *            the requestSince to set
	 */
	public void setRequestSince(Date requestSince) {
		this.requestSince = requestSince;
	}

	/**
	 * @return the soulmatesSince
	 */
	public Date getSoulmatesSince() {
		return soulmatesSince;
	}

	/**
	 * @param soulmatesSince
	 *            the soulmatesSince to set
	 */
	public void setSoulmatesSince(Date soulmatesSince) {
		this.soulmatesSince = soulmatesSince;
	}

	@Override
	public String toString() {
		return requesterId + " to " + requestedId + " request pending: " + requestPending + " since: " + requestSince
				+ " soulmates since: " + soulmatesSince;
	}
}
