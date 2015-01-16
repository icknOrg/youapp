package youapp.model;

public class Name {
	
	private Long personId;
	
	private String firstName;
	
	private String lastName;
	
	private String nickName;
	
	/**
	 * Returns the id of the person with this name.
	 * @return the id of the person with this name.
	 */
	public Long getPersonId() {
		return personId;
	}

	/**
	 * Sets the id of the person with this name.
	 * @param personId the id of the person with this name.
	 */
	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	/**
	 * Returns the first name of the person with this name.
	 * @return the first name of the person with this name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the person with this name.
	 * @param firstName the first name of the person with this name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the last name of the person with this name.
	 * @return the last name of the person with this name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the person with this name.
	 * @param lastName the last name of the person with this name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the nick name of the person with this name.
	 * @return the nick name of the person with this name.
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * Sets the nick name of the person with this name.
	 * @param nickName the nick name of the person with this name.
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	@Override
	public String toString() {
		return firstName + " " + lastName + " (" + nickName + ")";
	}
	
}
