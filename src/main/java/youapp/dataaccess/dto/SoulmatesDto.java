package youapp.dataaccess.dto;

import java.util.Date;

public class SoulmatesDto {
	private Long requesterId;
	private Long requestedId;
	private Boolean requestPending;
	private Date requestSince;
	private Date soulmatesSince;

	/**
	 * @return the requesterId
	 */
	public Long getRequesterId() {
		return requesterId;
	}

	/**
	 * @param requesterId
	 *            the requesterId to set
	 */
	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}

	/**
	 * @return the requestedId
	 */
	public Long getRequestedId() {
		return requestedId;
	}

	/**
	 * @param requestedId
	 *            the requestedId to set
	 */
	public void setRequestedId(Long requestedId) {
		this.requestedId = requestedId;
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
