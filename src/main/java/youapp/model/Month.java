package youapp.model;

public class Month {

	private Integer id;
	
	private String name;

	public Month(Integer id, String name) {
		if (id == null) {
			throw new IllegalArgumentException("Id must not be null.");
		}
		if (name == null) {
			throw new IllegalArgumentException("Name must not be null.");
		}
		this.id = id;
		this.name = name;
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
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
