package youapp.model;

public class AccessLevel {

	private Integer id;
	
	private String description;
	
	/**
	 * Default constructor creates an empty access level.
	 */
	public AccessLevel() {
		this.id = null;
		this.description = null;
	}
	
	/**
	 * Constructs an access level from the given id and the given description.
	 * @param id
	 * @param description
	 */
	public AccessLevel(Integer id, String description) {
		if (id == null || description == null) {
			throw new IllegalArgumentException("Parameters must not be null");
		}
		this.id = id;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		this.description = description;
	}
	
	@Override
	public String toString() {
		return id + " - " + description;
	}
	
}
