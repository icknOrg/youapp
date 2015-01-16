package youapp.dataaccess.dto;

/**
 * Data transfer object for table Importance.
 * @author neme
 *
 */
public class ImportanceDto {

	private Integer weight;
	
	private String description;

	/**
	 * Returns this importance level's weight.
	 * @return this importance level's weight.
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * Sets this importance level's weight.
	 * @param weight the weight to set.
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * Returns this importance level's description.
	 * @return this importance level's description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets this importance level's description.
	 * @param description the description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return weight + " - " + description;
	}
	
}
