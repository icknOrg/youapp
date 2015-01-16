package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.FacebookGroupDto;

public interface IFacebookGroupDao {

	public FacebookGroupDto getById(Long groupId, Boolean fbId);
	
	public List<FacebookGroupDto> getByPerson(Long personId);
	
	public List<FacebookGroupDto> getByName(String name);
	
	public List<FacebookGroupDto> getByNetworkId(Long networkId);
	
	public List<FacebookGroupDto> getByCreator(Long creatorId);
	
	public List<FacebookGroupDto> getByType(String type);
	
	public List<FacebookGroupDto> getAll();
	
	public List<FacebookGroupDto> getCommonGroups(Long personIdA, Long personIdB);
	
	public Integer getNumberOfGroups(Long personId);
	
	public Integer getNumberOfCommonGroups(Long personIdA, Long personIdB);
	
	/**
	 * Returns the number of people that are member of this group.
	 * @param groupId the id of the group its frequency has to be returned.
	 * @return the number of people that are member of this group.
	 */
	public Integer getGroupFrequency(Long groupId);
	
	public Boolean exists(Long groupId, Boolean fbId);
	
	public List<FacebookGroupDto> getGroupRecommendation(Long personId);
	
	public void addAssociation(Long personId, Long groupId, Boolean visible);
	
	public boolean existsAssociation(Long personId, Long groupId);
	
	public void removeAssociation(Long personId, Long groupId);
	
	public void removeAllAssociations(Long personId);
	
	public Long create(FacebookGroupDto group);
	
	public void delete(Long groupId);
	
}
