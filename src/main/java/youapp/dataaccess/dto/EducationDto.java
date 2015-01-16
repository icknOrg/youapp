package youapp.dataaccess.dto;

/**
 * Data transfer object for table Education.
 * @author neme
 *
 */
public class EducationDto {

	private Integer id;
	
	private String name;

	/**
	 * Returns this education's id.
	 * @return this education's id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets this education's id.
	 * @param id the id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Returns this education's name.
	 * @return this education's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets this education's name.
	 * @param name the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
