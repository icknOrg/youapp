package youapp.dataaccess.dao;

import java.util.List;

public interface IFacebookFriendshipDao {
	
	/**
	 * Returns the Facebook IDs of all friends of the person with the given Facebook id from the database.
	 * @param sourceId the id of the person whose friend's IDs should be returned.
	 * @return the Facebook IDs of all friends of the person with the given id from the database.
	 */
	public List<Long> getAllFriendsIds(Long sourceId);
	
	/**
	 * Returns all friends that the person with Facebook id A and the person with Facebook id B have in common.
	 * @param sourceIdA the Facebook id of the first person.
	 * @param sourceIdB the Facebook id of the second person.
	 * @return all common friends of person A and B.
	 */
	public List<Long> getCommonFriends(Long sourceIdA, Long sourceIdB);
	
	/**
	 * Returns the number of friends of the person with the given id.
	 * @param personId the id of the person whose number of friends should be returned.
	 * @return the number of friends of the person with the given id.
	 */
	public Integer getNumberOfFriends(Long personId);
	
	/**
	 * Returns the number of common friends of person with id A and person with id B.
	 * @param idPersonA the id of person A.
	 * @param idPersonB the id of person B.
	 * @return the number of common friends of person with id A and person with id B.
	 */
	public Integer getNumberOfCommonFriends(Long idPersonA, Long idPersonB);
	
	/**
	 * Checks whether there exists a Facebook friendship between the two given persons with the given Facebook ids in the database.
	 * @param sourceId the Facebook id of the person at the source of the friendship link.
	 * @param targetId the Facebook id of the person at the target of the friendship link.
	 * @return true, if there exists a Facebook friendship link, false otherwise.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public Boolean areFriends(Long sourceId, Long targetId);
	
	/**
	 * Creates a Facebook friendship in the database. The friendship must not be present.
	 * @param sourceId the Facebook id of the source person.
	 * @param targetId the Facebook id of the target person.
	 */
	public void create(Long sourceId, Long targetId);
	
	/**
	 * Deletes the Facebook friendship between the two given persons from the database.
	 * @param sourceId the Facebook id of the person at the source of the friendship link.
	 * @param targetId the Facebook id of the person at the target of the friendship link.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void delete(Long sourceId, Long targetId);

}
