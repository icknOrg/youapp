package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.FacebookPageDto;

public interface IFacebookPageDao {
	
	public FacebookPageDto getById(Long pageId, Boolean fbId);
	
	public List<FacebookPageDto> getByPerson(Long personId);
	
	public List<FacebookPageDto> getByName(String name);
	
	public List<FacebookPageDto> getByCategory(String category);
	
	public List<FacebookPageDto> getAll();
	
	public List<FacebookPageDto> getCommonPages(Long personIdA, Long personIdB);
	
	public Integer getNumberOfPages(Long personId);
	
	public Integer getNumberOfCommonPages(Long personIdA, Long personIdB);
	
	/**
	 * Returns the number of people that like this page.
	 * @param pageId the id of the page its frequency has to be returned.
	 * @return the number of people that like this page.
	 */
	public Integer getPageFrequency(Long pageId);
	
	/**
	 * Returns a pages's Facebook id if the provided id is the YouApp id or a page's YouApp id if the provided id is the Facebook id. If the
	 * page with the given id does not exist, null is returned.
	 * @param pageId the id of the page whose alternative id (either Facebook id or YouApp id) is returned.
	 * @param fbId true, if the provided page id is the Facebook id, false if the id is the YouApp id.
	 * @return a page's Facebook id if the provided id is the YouApp id or a page's YouApp id if the provided id is the Facebook id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public Long getAlternativeId(Long pageId, Boolean fbId);
	
	public Boolean exists(Long pageId, Boolean fbId);
	
	public void addAssociation(Long personId, Long pageId, Boolean visible, String profileSection, Long createdTime);
	
	public boolean existsAssociation(Long personId, Long pageId);
	
	public void removeAssociation(Long personId, Long pageId);
	
	public void removeAllAssociations(Long personId);
	
	public Long create(FacebookPageDto page);
	
	public void delete(Long pageId);
	
}
