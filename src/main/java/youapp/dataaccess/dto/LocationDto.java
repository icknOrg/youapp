package youapp.dataaccess.dto;

import java.math.BigDecimal;

/**
 * Data transfer object for table Location.
 * 
 * @author jonas.lauener
 * 
 */
public class LocationDto {

	private Integer id;

	private String name;

	private BigDecimal longitude;

	private BigDecimal latitude;

	/**
	 * @return the locationId
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param locationId
	 *            the locationId to set
	 */
	public void setId(Integer locationId) {
		this.id = locationId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the longitude
	 */
	public BigDecimal getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public BigDecimal getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return id + " - " + name + " (" + longitude
				+ "," + latitude + ")";
	}

}
