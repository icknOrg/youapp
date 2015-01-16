package youapp.dataaccess.dto;

/**
 * Data transfer object for table AccessLevel.
 * @author neme
 *
 */
public class AccessLevelDto {

	private Integer id;
	
	private String description;

	/**
	 * Returns this access level's id.
	 * @return the access level id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets this access level's id.
	 * @param id the id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Returns this access level's description.
	 * @return the access level's description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets this access level's description.
	 * @param description the description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return id + " - " + description;
	}
}
