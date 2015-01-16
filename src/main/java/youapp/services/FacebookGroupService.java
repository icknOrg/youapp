package youapp.services;

import java.util.List;
import java.util.Map;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.InconsistentModelException;
import youapp.model.FacebookGroup;

public interface FacebookGroupService {
	
	public FacebookGroup getById(Long groupId, Boolean fbId);
	
	public List<FacebookGroup> getByPerson(Long personId, Boolean fbId) throws DataAccessLayerException;
	
	public Map<Long, FacebookGroup> getByPersonAsMap(Long personId, Boolean fbId) throws DataAccessLayerException;
	
	public List<FacebookGroup> getByName(String name);
	
	public List<FacebookGroup> getByNetworkId(Long networkId);
	
	public List<FacebookGroup> getByCreator(Long creatorId);
	
	public List<FacebookGroup> getByType(String type);
	
	public List<FacebookGroup> getAllGroups();
	
	public List<FacebookGroup> getCommonGroups(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException;
	
	public Integer getNumberOfGroups(Long personId, Boolean fbId) throws DataAccessLayerException;
	
	public Integer getNumberOfCommonGroups(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException;
	
	public Boolean exists(Long groupId, Boolean fbId);
	
	public List<FacebookGroup> getGroupRecommendation(Long personId);
	
	public boolean existsAssociation(Long personId, Long groupId);
	
	public void addAssociation(Long personId, Long groupId, Boolean visible);
	
	public void removeAssociation(Long personId, Long groupId);
	
	public void removeAssociations(Long personId, List<FacebookGroup> groups);
	
	public void removeAllAssociations(Long personId);
	
	public void createAndAssociate(Long personId, FacebookGroup group, Boolean visible) throws InconsistentModelException;
	
	public void createAndAssociate(Long personId, List<FacebookGroup> groups, Boolean visible) throws InconsistentModelException;
	
	public Long create(FacebookGroup group) throws InconsistentModelException;
	
	public void delete(Long groupId);
	
}
