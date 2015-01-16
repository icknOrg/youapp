package youapp.services;

import java.util.List;
import java.util.Map;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.InconsistentModelException;
import youapp.model.FacebookPage;

public interface FacebookPageService {
	
	/**
	 * Returns the Facebook page with the given id. If there is no Facebook page with the given id, null is returned.
	 * @param pageId the id of the Facebook page to be returned.
	 * @param fbId true if the given page id is the Facebook page id, false if the given page id is the YouApp page id.
	 * @return the Facebook page with the given id.
	 */
	public FacebookPage getById(Long pageId, Boolean fbId);
	
	public List<FacebookPage> getByPerson(Long personId, Boolean fbId) throws DataAccessLayerException;
	
	/**
	 * Returns a map containing all Facebook pages liked by the person with the given id. The map keys are the corresponding
	 * Facebook pages' ids. If the person with the given id has no Facebook pages associated, an empty list is returned.
	 * @param personId the id of the person whose Facebok pages have to be returned.
	 * @param fbId true if the keys should be the pages' Facebook page ids, false if the keys should be the pages' YouApp ids.
	 * @return a map containing all Facebook pages liked by the person with the given id.
	 */
	public Map<Long, FacebookPage> getByPersonAsMap(Long personId, Boolean fbId);
	
	public List<FacebookPage> getByName(String name);
	
	public List<FacebookPage> getByType(String category);
	
	public List<FacebookPage> getAll();
	
	public List<FacebookPage> getCommonPages(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException;
	
	public Integer getNumberOfPages(Long personId, Boolean fbId) throws DataAccessLayerException;
	
	public Integer getNumberOfCommonPages(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException;
	
	/**
	 * Returns a page's Facebook id if the provided id is the YouApp id or a page's YouApp id if the provided id is the Facebook id. If the
	 * page with the given id does not exist, null is returned.
	 * @param pageId the id of the page whose alternative id (either Facebook id or YouApp id) is returned.
	 * @param fbId true, if the provided page id is the Facebook id, false if the id is the YouApp id.
	 * @return a page's Facebook id if the provided id is the YouApp id or a page's YouApp id if the provided id is the Facebook id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public Long getAlternativeId(Long pageId, Boolean fbId);
	
	/**
	 * Checks whether a Facebook page with the given id exists.
	 * @param pageId the id of the Facebook page whose existence has to be checked.
	 * @param fbId true if the given page id is the Facebook page id, false if the given page id is the YouApp page id.
	 * @return true if the given page exists, false otherwise.
	 */
	public Boolean exists(Long pageId, Boolean fbId);
	
	/**
	 * Checks whether there is an association between the person with the given id and the Facebook page with the given id.
	 * @param personId the id of the person.
	 * @param pageId the id of the page.
	 * @param fbId true if the given page id is the Facebook page id, false if the given page id is the YouApp page id.
	 * @return true if there is an association between the person with the given id and the page with the given id, false otherwise.
	 */
	public boolean existsAssociation(Long personId, Long pageId, Boolean fbId);
	
	/**
	 * Adds an association between the person with the given id and the Facebook page with the given id.
	 * @param personId the id of the person.
	 * @param pageId the id of the page.
	 * @param fbId true if the given page id is the Facebook page id, false if the given page id is the YouApp page id.
	 * @param visible true if the association can be visible for others, false otherwise.
	 * @param profileSection a string identifier for the section on a user's Facebook profile, in which the Facebook page is listed. May be null.
	 * @param createdTime unix time time stamp representing the date, where the Facebook page was liked by the user. May be null.
	 */
	public void addAssociation(Long personId, Long pageId, Boolean fbId, Boolean visible, String profileSection, Long createdTime);
	
	/**
	 * Removes an association between the person with the given id and the Facebook page with the given id.
	 * @param personId the id of the person.
	 * @param pageId the id of the page.
	 * @param fbId true if the given page id is the Facebook page id, false if the given page id is the YouApp page id.
	 */
	public void removeAssociation(Long personId, Long pageId, Boolean fbId);
	
	public void removeAssociations(Long personId, List<FacebookPage> pages);
	
	public void removeAllAssociations(Long personId);
	
	public void createAndAssociate(Long personId, FacebookPage page, Boolean visible, String profileSection, Long createdTime) throws InconsistentModelException;
	
	public void createAndAssociate(Long personId, List<FacebookPage> pages, Boolean visible) throws InconsistentModelException;
	
	public Long create(FacebookPage page) throws InconsistentModelException;
	
	/**
	 * Deletes the Facebook page with the given id. If no Facebook page with the given exists, no action is taken.
	 * @param pageId the id of the page to be deleted.
	 * @param fbId true if the given page id is the Facebook page id, false if the given page id is the YouApp page id.
	 */
	public void delete(Long pageId, Boolean fbId);

}
