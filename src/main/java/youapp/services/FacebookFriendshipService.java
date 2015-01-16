package youapp.services;

import java.util.List;
import java.util.Map;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.model.FacebookPerson;

public interface FacebookFriendshipService {
	
	/**
	 * Returns all Facebook friends of the person with the given Facebook id.
	 * @param sourceId the ID of the person whose friends should be returned.
	 * @param fbId true, if the source id is a Facebook id, false if it is a YouApp id.
	 * @return all Facebook friends of the person with the given Facebook id.
	 * @throws DataAccessLayerException
	 */
	public List<FacebookPerson> getAllFacebookFriends(Long sourceId, Boolean fbId) throws DataAccessLayerException;
	
	/**
	 * Returns all Facebook friends of the person with the given Facebook id. The key of the map is the Facebook id of the
	 * corresponding Facebook friend.
	 * @param sourceId the ID of the person whose friends should be returned.
	 * @param fbId true, if the source id is a Facebook id, false if it is a YouApp id.
	 * @return all Facebook friends of the person with the given Facebook id.
	 * @throws DataAccessLayerException
	 */
	public Map<Long, FacebookPerson> getAllFacebookFriendsAsMap(Long sourceId, Boolean fbId) throws DataAccessLayerException;
	
	/**
	 * Returns all friends that the person with Facebook id A and the person with Facebook id B have in common.
	 * @param personIdA the Facebook id of the first person.
	 * @param personIdB the Facebook id of the second person.
	 * @param fbIds true, if the given ids are Facebook ids, false if they are YouApp ids.
	 * @return all common friends of person A and B.
	 * @throws DataAccessLayerException
	 */
	public List<FacebookPerson> getCommonFacebookFriends(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException;
	
	/**
	 * Returns the number of friends of the person with the given id.
	 * @param personId the id of the person whose number of friends should be returned.
	 * @param true, if the given id is a facebook id, false if it is a YouApp id.
	 * @return the number of friends of the person with the given id.
	 * @throws DataAccessLayerException
	 */
	public Integer getNumberOfFacebookFriends(Long personId, Boolean fbId) throws DataAccessLayerException;
	
	/**
	 * Returns the number of common friends of person with id A and person with id B.
	 * @param idPersonA the id of person A.
	 * @param idPersonB the id of person B.
	 * @param fbIds true, if the given ids are facebook ids, false if they are YouApp ids.
	 * @return the number of common friends of person with id A and person with id B.
	 * @throws DataAccessLayerException
	 */
	public Integer getNumberOfCommonFacebookFriends(Long idPersonA, Long idPersonB, Boolean fbIds) throws DataAccessLayerException;
	
	/**
	 * Checks whether there exists a Facebook friendship between the two given persons with the given Facebook ids in the database.
	 * @param sourceId the Facebook id of the source person.
	 * @param targetId the Facebook id of the target person.
	 * @return whether there exists a Facebook friendship between the two given persons with the given Facebook ids in the database.
	 * @throws DataAccessLayerException
	 */
	public Boolean areFacebookFriends(Long sourceId, Long targetId) throws DataAccessLayerException;
	
	/**
	 * Creates a Facebook friendship in the database. The friendship must not be present.
	 * @param sourceId the Facebook id of the source person.
	 * @param targetId the Facebook id of the target person.
	 * @throws DataAccessLayerException
	 */
	public void createFacebookFriendship(Long sourceId, Long targetId) throws DataAccessLayerException;
	
	/**
	 * Deletes the Facebook friendship between the two given persons from the database.
	 * @param sourceId the Facebook id of the person at the source of the friendship link.
	 * @param targetId the Facebook id of the person at the target of the friendship link.
	 * @throws DataAccessLayerException
	 */
	public void deleteFacebookFriendship(Long sourceId, Long targetId) throws DataAccessLayerException;
	
}
