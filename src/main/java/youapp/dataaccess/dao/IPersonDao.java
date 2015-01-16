package youapp.dataaccess.dao;

import java.util.Date;
import java.util.List;

import youapp.dataaccess.dto.NameDto;
import youapp.dataaccess.dto.PersonDto;
import youapp.model.filter.IFilter;

/**
 * Represents an SQL data access object for a person.
 * @author neme
 *
 */
public interface IPersonDao {

	/**
	 * Returns the person from the database matching the given id.
	 * @param id the id of the person to be returned.
	 * @return the person matching the given id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public PersonDto getById(Long id);
	
	/**
	 * Returns the person from the database matching the given facebook id.
	 * @param fbId the facebook id of the person to be returned.
	 * @return the person matching the given facebook id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public PersonDto getByFbId(Long fbId);
	
	/**
     * Returns the person from the database matching the given filters.
     * @param filters the filters for the matching person to be returned.
     * @return the person matching the given filters.
     * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
     */
	public List<PersonDto> getByFilters(IFilter[] filters);
	
	/**
     * Returns the person from the database matching the given filters.
     * @param filters the filters for the matching person to be returned.
	 * @param offset offset for the limit
	 * @param resultSize number of results
     * @return the person matching the given filters.
     * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PersonDto> getByFilters(IFilter[] filters, Integer offset, Integer resultSize);
	
	/**
	 * Checks whether a person with the given id already exists.
	 * @param id the person's id (either the person id or facebook id).
	 * @param fbId true if the given id is a facebook id, false if the given id is a person id.
	 * @return true if a person with the given id already exists, false otherwise.
	 */
	public Boolean exists(Long id, boolean fbId);
	
	/**
	 * Checks whether a person with the given id is active.
	 * @param id the person's id (either the person id or facebook id).
	 * @param fbId true if the given id is a facebook id, false if the given id is a person id.
	 * @return true, if a person with the given id is active, false otherwise.
	 */
	public Boolean isActive(Long id, boolean fbId);
	
	/**
	 * Checks whether a person with the given nick name already exists.
	 * @param nick the nick name to be checked.
	 * @return true if the given nick name is still available, false if there exists a person
	 * with the given nick name already.
	 */
	public Boolean nickAvailable(String nick);
	
	/**
	 * Returns all persons from the database matching the given nick name, otherwise none.
	 * @param nick the nick name of all persons to be returned.
	 * @return the persons matching the given nick name.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PersonDto> getByNick(String nick);
	
	/**
	 * Returns all persons from the database matching the given first name, otherwise none.
	 * @param firstName the first name of all persons to be returned.
	 * @return the persons matching the given first name.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PersonDto> getByFirstName(String firstName);
	
	/**
	 * Returns all persons from the database matching the given last name, otherwise none.
	 * @param lastName the last name of all persons to be returned.
	 * @return the persons matching the given last name.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PersonDto> getByLastName(String lastName);
	
	/**
	 * Returns all persons from the database matching the given fist and last name, otherwise none.
	 * @param firstName the first name of all persons to be returned.
	 * @param lastName the last name of all persons to be returned.
	 * @return the persons matching the given first and last name.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PersonDto> getByName(String firstName, String lastName);
	
	/**
	 * Returns the name of the person with the given id from the database. If the person with the given id is not
	 * active anymore, only the first name and last name can be provided. The nick name in this case is empty.
	 * If the person with the given id does not exist, an exception is thrown!
	 * @param personId the id of the person whose name is retrieved.
	 * @return the name of the person with the given id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public NameDto getName(Long personId);
	
	/**
	 * Returns the nick name of the person with the given id from the database. If the person with the given id
	 * does not exist, an exception is thrown!
	 * @param personId the id of the person whose nick name is retrieved.
	 * @return the nick name of the person with the given id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public String getNickName(Long personId);
	
	/**
	 * Returns a person's Facebook id if the provided id is the YouApp id or a person's YouApp id if the provided id is the Facebook id. If the
	 * person with the given id does not exist, null is returned.
	 * @param personId the id of the person whose alternative id (either Facebook id or YouApp id) is returned.
	 * @param fbId true, if the provided person id is the Facebook id, false if the id is the YouApp id.
	 * @return a person's Facebook id if the provided id is the YouApp id or a person's YouApp id if the provided id is the Facebook id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public Long getAlternativeId(Long personId, Boolean fbId);
	
	/**
	 * Returns all friends of the given person from the database, starting at the given offset. The
	 * number of friends does not exceed the result size.
	 * @param personId the person whose friends are retrieved.
	 * @param offset the offset at which the next bunch of friends starts.
	 * The offset is zero when calling this method for the first time.
	 * @param resultSize the maximum number of friends returned.
	 * @param includeProvisional true, if provisional friends should be included, false otherwise.
	 * @return all friends.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PersonDto> getAllFriends(Long personId, Integer offset, Integer resultSize, Boolean includeProvisional);
	
	/**
	 * Returns the newest friends of the given person from the database.
	 * @param personId the person whose newest friends are retrieved.
	 * @param resultSize the maximum number of friends returned.
	 * @param includeProvisional true, if provisional friends should be included, false otherwise.
	 * @return the most recent friends.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PersonDto> getRecentFriends(Long personId, Integer resultSize, Boolean includeProvisional);
	
	/**
	 * Get all persons from the database.
	 * @return all persons from the database.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PersonDto> getAll();
	
	/**
	 * Creates the given person in the database. The person must not be present, the person id
	 * must be null and the facebook id must be unique.
	 * @param person the person to be created.
	 * @return the person's new id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public Long create(PersonDto person);
	
	/**
	 * Updates the given person in the database. The person must be present and have a valid
	 * person id and facebook id.
	 * @param person the person to be updated.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void update(PersonDto person);
	
	/**
	 * Update the last online Date of a person. The person must be present and have a valid person id.
	 * @param personId the person id to be updated
	 * @param lastOnline the date when the person was online
	 */
	public void updateLastOnline(Long personId, Date lastOnline);
	
	/**
	 * Deletes the given person from the database.
	 * @param personId the id of the person to be deleted.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void delete(Long personId);
	
	/**
	 * Deactivates the given person. The activation property is set to false and all attributes except
	 * the mandatory attributes are set to null.
	 * @param personId the id of the person to be deactivated.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void deactivate(Long personId);
}
