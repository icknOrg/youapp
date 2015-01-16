package youapp.dataaccess.dto;

import java.util.Date;

/**
 * Data transfer object for table Person.
 * @author neme
 *
 */
public class PersonDto {

	private Long id;
	
	private Integer accessLevel;
	
	private Long fbId;
	
	private String firstName;
	
	private String lastName;
	
	private String gender;
	
	private Boolean activated;
	
	private String nickName;
	
	private String description;
	
	private Date memberSince;
	
	private Date lastOnline;
	
	private Date birthday;
	
	private Integer location;
	
	private String locationName;
	
	private Integer education;
	
	private Integer job;
	
	private Integer diet;
	
	private Boolean dietStrict;
	
	private Integer religion;
	
	private Integer religionSerious;
	
	private Byte sign;
	
	private Integer signSerious;
	
	private Boolean useFBMatching;
	
	private Boolean useQuestionMatching;
	
	private Boolean useDistanceMatching;
	
	private Boolean useSymptomsMatching;
	
	private Boolean useMedicationMatching;	
	
	/**
	 * Returns the person's id.
	 * @return the person's id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets this person's id.
	 * @param id the id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns the id of this person's access level.
	 * @return the access level id.
	 */
	public Integer getAccessLevel() {
		return accessLevel;
	}

	/**
	 * Sets this person's access level id.
	 * @param accessLevel the access level id to set.
	 */
	public void setAccessLevel(Integer accessLevel) {
		this.accessLevel = accessLevel;
	}

	/**
	 * Returns the person's facebook id.
	 * @return the facebook id.
	 */
	public Long getFbId() {
		return fbId;
	}

	/**
	 * Sets this person's facebook id.
	 * @param fbId the facebook id to set.
	 */
	public void setFbId(Long fbId) {
		this.fbId = fbId;
	}

	/**
	 * Returns the person's first name.
	 * @return the first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets this person's first name.
	 * @param firstName the first name to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the person's last name.
	 * @return the last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets this person's last name.
	 * @param lastName the last name to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the person's gender.
	 * @return "M" if this person is male and "F" if this person is female.
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets this person's gender. Use "M" for male and "F" female.
	 * @param gender the gender to set.
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Returns whether this person's account is activated.
	 * @return true if this account is activated, false otherwise.
	 */
	public Boolean getActivated() {
		return activated;
	}

	/**
	 * Sets whether this person's account is activated or not.
	 * @param activated boolean value representing an active (true)
	 * or inactive (false) account.
	 */
	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	/**
	 * Returns the person's nick name.
	 * @return the nick name.
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * Sets this person's nick name.
	 * @param nickName the nick name to set.
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
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
		this.description = description;
	}

	/**
	 * Returns the date this person became a member.
	 * @return the date this person became a member.
	 */
	public Date getMemberSince() {
		return memberSince;
	}

	/**
	 * Sets this person became a member.
	 * @param memberSince the date this person became a member.
	 */
	public void setMemberSince(Date memberSince) {
		this.memberSince = memberSince;
	}

	/**
	 * Returns the person's last login date.
	 * @return the person's last login date.
	 */
	public Date getLastOnline() {
		return lastOnline;
	}

	/**
	 * Sets this person's last login date.
	 * @param lastOnline the lastOnline to set
	 */
	public void setLastOnline(Date lastOnline) {
		this.lastOnline = lastOnline;
	}

	/**
	 * Returns the person's birthday.
	 * @return the birthday.
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * Sets this person's birthday.
	 * @param birthday the birthday to set.
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the location
	 */
	public Integer getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Integer location) {
		this.location = location;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}	
	
	/**
	 * Returns the id of this person's education type.
	 * @return the education id.
	 */
	public Integer getEducation() {
		return education;
	}

	/**
	 * Sets this person's education type.
	 * @param education the education id to set.
	 */
	public void setEducation(Integer education) {
		this.education = education;
	}

	/**
	 * Returns the id of this person's job type.
	 * @return the job id.
	 */
	public Integer getJob() {
		return job;
	}

	/**
	 * Sets this person's job type.
	 * @param job the job id to set.
	 */
	public void setJob(Integer job) {
		this.job = job;
	}

	/**
	 * Returns the id of this person's diet type.
	 * @return the diet id.
	 */
	public Integer getDiet() {
		return diet;
	}

	/**
	 * Sets this person's diet type.
	 * @param diet the diet id to set.
	 */
	public void setDiet(Integer diet) {
		this.diet = diet;
	}

	/**
	 * Returns whether this person is strictly following her diet.
	 * @return true, if the person is strictly following her diet, false if not and null if not set.
	 */
	public Boolean getDietStrict() {
		return dietStrict;
	}

	/**
	 * Sets whether this person is strictly following her diet.
	 * @param dietStrict true, if this person is strictly following her diet, false if not and null if not specified.
	 */
	public void setDietStrict(Boolean dietStrict) {
		this.dietStrict = dietStrict;
	}

	/**
	 * Returns the id of this person's religion type.
	 * @return the religion id.
	 */
	public Integer getReligion() {
		return religion;
	}

	/**
	 * Sets this person's religion type.
	 * @param religion the religion id to set.
	 */
	public void setReligion(Integer religion) {
		this.religion = religion;
	}

	/**
	 * Returns whether this person is serious about her religion.
	 * @return true, if this person is serious about her religion, false if not and null if not set.
	 */
	public Integer getReligionSerious() {
		return religionSerious;
	}

	/**
	 * Sets whether this person is serious about her religion.
	 * @param religionSerious true, if this person is serious, false if not and null if not specified.
	 */
	public void setReligionSerious(Integer religionSerious) {
		this.religionSerious = religionSerious;
	}

	/**
	 * Returns the id of this person's sign.
	 * @return the sign id.
	 */
	public Byte getSign() {
		return sign;
	}

	/**
	 * Sets this person's sign.
	 * @param sign the sign id to set.
	 */
	public void setSign(Byte sign) {
		this.sign = sign;
	}

	/**
	 * Returns whether this person is serious about her sign.
	 * @return true, if this person is serious about her sign, false if not and null if not set.
	 */
	public Integer getSignSerious() {
		return signSerious;
	}

	/**
	 * Sets whether this person is serious about her sign.
	 * @param signSerious true, if this person is serious, false if not and null if not specified.
	 */
	public void setSignSerious(Integer signSerious) {
		this.signSerious = signSerious;
	}
	
	public Boolean getUseFBMatching() {
		return useFBMatching;
	}

	public void setUseFBMatching(Boolean useFBMatching) {
		this.useFBMatching = useFBMatching;
	}

	public Boolean getUseQuestionMatching() {
		return useQuestionMatching;
	}

	public void setUseQuestionMatching(Boolean useQuestionMatching) {
		this.useQuestionMatching = useQuestionMatching;
	}

	public Boolean getUseDistanceMatching() {
		return useDistanceMatching;
	}

	public void setUseDistanceMatching(Boolean useDistanceMatching) {
		this.useDistanceMatching = useDistanceMatching;
	}

	public Boolean getUseSymptomsMatching() {
		return useSymptomsMatching;
	}

	public void setUseSymptomsMatching(Boolean useSymtomsMatching) {
		this.useSymptomsMatching = useSymtomsMatching;
	}

	public Boolean getUseMedicationMatching() {
		return useMedicationMatching;
	}

	public void setUseMedicationMatching(Boolean useMedicationMatching) {
		this.useMedicationMatching = useMedicationMatching;
	}

	
	@Override
	public String toString() {
		return id + "/" + fbId + " - " + firstName + " " + lastName + " (" + ((activated)?"active":"not active") + ")";
	}


}
