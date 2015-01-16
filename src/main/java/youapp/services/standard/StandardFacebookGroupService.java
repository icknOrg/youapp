package youapp.services.standard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.IFacebookGroupDao;
import youapp.dataaccess.dto.FacebookGroupDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.model.FacebookGroup;
import youapp.services.FacebookGroupService;
import youapp.services.PersonService;

public class StandardFacebookGroupService implements FacebookGroupService {
	
	/**
	 * Logger.
	 */
	static final Log log = LogFactory.getLog(StandardFacebookGroupService.class);
	
	private IFacebookGroupDao facebookGroupDao;
	
	private PersonService personService;
	
	@Autowired
	public void setFacebookGroupDao(IFacebookGroupDao facebookGroupDao) {
		this.facebookGroupDao = facebookGroupDao;
	}
	
	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	@Override
	public FacebookGroup getById(Long groupId, Boolean fbId) {
		if (groupId == null) {
			throw new IllegalArgumentException("Group id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving group by id: " + groupId);
		}
		FacebookGroupDto groupDto = facebookGroupDao.getById(groupId, fbId);
		Integer frequency = facebookGroupDao.getGroupFrequency(groupId);
		return reassembleGroup(groupDto, frequency);
	}
	
	@Override
	public List<FacebookGroup> getByPerson(Long personId, Boolean fbId) throws DataAccessLayerException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all facebook groups of person with id: " + personId);
		}
		if (fbId) {
			personId = personService.getAlternativeId(personId, fbId);
		}
		List<FacebookGroupDto> groupDtos = facebookGroupDao.getByPerson(personId);
		List<FacebookGroup> groups = new LinkedList<FacebookGroup>();
		for (FacebookGroupDto g : groupDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found group: " + g.toString());
			}
			Integer frequency = facebookGroupDao.getGroupFrequency(g.getId());
			groups.add(reassembleGroup(g, frequency));
		}
		return groups;
	}

	@Override
	public Map<Long, FacebookGroup> getByPersonAsMap(Long personId, Boolean fbId) throws DataAccessLayerException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (fbId) {
			personId = personService.getAlternativeId(personId, fbId);
		}
		List<FacebookGroupDto> groupDtos = facebookGroupDao.getByPerson(personId);
		Map<Long, FacebookGroup> groups = new HashMap<Long, FacebookGroup>();
		for (FacebookGroupDto g : groupDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found group: " + g.toString());
			}
			Integer frequency = facebookGroupDao.getGroupFrequency(g.getId());
			groups.put(g.getgId(), reassembleGroup(g, frequency));
		}
		return groups;
	}

	@Override
	public List<FacebookGroup> getByName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving groups by name: " + name);
		}
		List<FacebookGroupDto> groupDtos = facebookGroupDao.getByName(name);
		List<FacebookGroup> groups = new LinkedList<FacebookGroup>();
		for (FacebookGroupDto g : groupDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found group: " + g.toString());
			}
			Integer frequency = facebookGroupDao.getGroupFrequency(g.getId());
			groups.add(reassembleGroup(g, frequency));
		}
		return groups;
	}

	@Override
	public List<FacebookGroup> getByNetworkId(Long networkId) {
		if (networkId == null) {
			throw new IllegalArgumentException("Network id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving groups by network id: " + networkId);
		}
		List<FacebookGroupDto> groupDtos = facebookGroupDao.getByNetworkId(networkId);
		List<FacebookGroup> groups = new LinkedList<FacebookGroup>();
		for (FacebookGroupDto g : groupDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found group: " + g.toString());
			}
			Integer frequency = facebookGroupDao.getGroupFrequency(g.getId());
			groups.add(reassembleGroup(g, frequency));
		}
		return groups;
	}

	@Override
	public List<FacebookGroup> getByCreator(Long creatorId) {
		if (creatorId == null) {
			throw new IllegalArgumentException("Creator id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving groups by creator id: " + creatorId);
		}
		List<FacebookGroupDto> groupDtos = facebookGroupDao.getByCreator(creatorId);
		List<FacebookGroup> groups = new LinkedList<FacebookGroup>();
		for (FacebookGroupDto g : groupDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found group: " + g.toString());
			}
			Integer frequency = facebookGroupDao.getGroupFrequency(g.getId());
			groups.add(reassembleGroup(g, frequency));
		}
		return groups;
	}

	@Override
	public List<FacebookGroup> getByType(String type) {
		if (type == null) {
			throw new IllegalArgumentException("Type must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving groups by type: " + type);
		}
		List<FacebookGroupDto> groupDtos = facebookGroupDao.getByType(type);
		List<FacebookGroup> groups = new LinkedList<FacebookGroup>();
		for (FacebookGroupDto g : groupDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found group: " + g.toString());
			}
			Integer frequency = facebookGroupDao.getGroupFrequency(g.getId());
			groups.add(reassembleGroup(g, frequency));
		}
		return groups;
	}

	@Override
	public List<FacebookGroup> getAllGroups() {
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all groups.");
		}
		List<FacebookGroupDto> groupDtos = facebookGroupDao.getAll();
		List<FacebookGroup> groups = new LinkedList<FacebookGroup>();
		for (FacebookGroupDto g : groupDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found group: " + g.toString());
			}
			Integer frequency = facebookGroupDao.getGroupFrequency(g.getId());
			groups.add(reassembleGroup(g, frequency));
		}
		return groups;
	}
	
	@Override
	public List<FacebookGroup> getCommonGroups(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		if (fbIds == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving common groups of person A with id " + personIdA + " and person B with id " + personIdB);
		}
		// Convert ids if necessary.
		if (fbIds) {
			personIdA = personService.getAlternativeId(personIdA, fbIds);
			personIdB = personService.getAlternativeId(personIdB, fbIds);
		}
		// Retrieve common groups.
		List<FacebookGroupDto> groupDtos = facebookGroupDao.getCommonGroups(personIdA, personIdB);
		List<FacebookGroup> groups = new LinkedList<FacebookGroup>();
		for (FacebookGroupDto g : groupDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found group: " + g.toString());
			}
			Integer frequency = facebookGroupDao.getGroupFrequency(g.getId());
			groups.add(reassembleGroup(g, frequency));
		}
		return groups;
	}
	
	@Override
	public Integer getNumberOfGroups(Long personId, Boolean fbId) throws DataAccessLayerException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (fbId) {
			personId = personService.getAlternativeId(personId, true);
		}
		return facebookGroupDao.getNumberOfGroups(personId);
	}

	@Override
	public Integer getNumberOfCommonGroups(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		if (fbIds == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (fbIds) {
			personIdA = personService.getAlternativeId(personIdA, true);
			personIdB = personService.getAlternativeId(personIdB, true);
		}
		return facebookGroupDao.getNumberOfCommonGroups(personIdA, personIdB);
	}

	@Override
	public Boolean exists(Long groupId, Boolean fbId) {
		if (groupId == null) {
			throw new IllegalArgumentException("Group id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Check whether group with id " + groupId + " already exists.");
		}
		return facebookGroupDao.exists(groupId, fbId);
	}

	@Override
	public List<FacebookGroup> getGroupRecommendation(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Get group recommendations for person with id: " + personId);
		}
		List<FacebookGroupDto> groupDtos = facebookGroupDao.getGroupRecommendation(personId);
		List<FacebookGroup> groups = new LinkedList<FacebookGroup>();
		for (FacebookGroupDto g : groupDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found group: " + g.toString());
			}
			Integer frequency = facebookGroupDao.getGroupFrequency(g.getId());
			groups.add(reassembleGroup(g, frequency));
		}
		return groups;
	}

	@Override
	public boolean existsAssociation(Long personId, Long groupId) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (groupId == null) {
			throw new IllegalArgumentException("Group id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Check whether an association between a person with id " + personId + " and a group with id " + groupId + " exists.");
		}
		return facebookGroupDao.existsAssociation(personId, groupId);
	}
	
	@Override
	public void addAssociation(Long personId, Long groupId, Boolean visible) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (groupId == null) {
			throw new IllegalArgumentException("Group id must not be null.");
		}
		if (visible == null) {
			throw new IllegalArgumentException("Visible must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Adding association between person with id " + personId + " and group with id " + groupId + ".");
		}
		if (!existsAssociation(personId, groupId)) {
			facebookGroupDao.addAssociation(personId, groupId, visible);
		}
	}

	@Override
	public void removeAssociation(Long personId, Long groupId) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (groupId == null) {
			throw new IllegalArgumentException("Group id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Removing association between person with id " + personId + " and group with id " + groupId + ".");
		}
		if (existsAssociation(personId, groupId)) {
			facebookGroupDao.removeAssociation(personId, groupId);
		}
	}
	
	@Override
	public void removeAssociations(Long personId, List<FacebookGroup> groups) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (groups == null) {
			throw new IllegalArgumentException("Groups must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Removing association between person with id " + personId + " and some groups.");
		}
		for (FacebookGroup g : groups) {
			if (g.getId() == null) {
				if (log.isTraceEnabled()) {
					log.trace(">>> Skipped group because of missing group id.");
				}
			} else {
				if (log.isTraceEnabled()) {
					log.trace(">>> Removing association with group with id " + g.getId() + ".");
				}
				removeAssociation(personId, g.getId());
			}
		}
	}

	@Override
	public void removeAllAssociations(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Removing all group associations of person with id: " + personId);
		}
		facebookGroupDao.removeAllAssociations(personId);
	}
	
	@Override
	public void createAndAssociate(Long personId, FacebookGroup group, Boolean visible) throws InconsistentModelException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (group == null) {
			throw new IllegalArgumentException("Group must not be null.");
		}
		if (visible == null) {
			throw new IllegalArgumentException("Visible must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Creating group and corresponding association if not present.");
		}
		// Validate.
		validateGroup(group);
		// Create group if not yet present.
		Long groupId = create(group);
		group.setId(groupId);
		// Create association between person and group if not yet present.
		addAssociation(personId, groupId, visible);
	}
	
	@Override
	public void createAndAssociate(Long personId, List<FacebookGroup> groups, Boolean visible) throws InconsistentModelException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (groups == null) {
			throw new IllegalArgumentException("Groups must not be null.");
		}
		if (visible == null) {
			throw new IllegalArgumentException("Visible must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Creating groups and corresponding associations if not present.");
		}
		Long id = null;
		for (FacebookGroup g : groups) {
			if (log.isDebugEnabled()) {
				log.debug("Handling group: " + g.toString());
			}
			// Validate.
			validateGroup(g);
			// Create group if not yet present.
			if (g.getId() == null) {
				if (log.isTraceEnabled()) {
					log.trace(">>> Found new group. Creating: " + g.toString());
				}
				id = create(g);
				g.setId(id);
			}
			// Make association if not yet present.
			if (log.isTraceEnabled()) {
				log.trace(">>> Adding association if not yet present.");
			}
			addAssociation(personId, g.getId(), visible);
		}
	}

	@Override
	public Long create(FacebookGroup group) throws InconsistentModelException {
		if (group == null) {
			throw new IllegalArgumentException("Group must not be null.");
		}
		if (group.getId() != null) {
			throw new IllegalArgumentException("Group id must not be set.");
		}
		
		// Perform validation checks.
		validateGroup(group);
		
		// Create new group in the database.
		FacebookGroupDto groupDto = disassembleGroup(group);
		if (log.isDebugEnabled()) {
			log.debug("Creating group: " + groupDto.toString());
		}
		if (exists(group.getgId(), true)) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Group already exists! Stopped creation.");
			}
			return getById(group.getgId(), true).getId();
		}
		Long generatedId = facebookGroupDao.create(groupDto);
		if (log.isDebugEnabled()) {
			log.debug(">>> Created group with id: " + generatedId);
		}
		group.setId(generatedId);
		return generatedId;
	}

	@Override
	public void delete(Long groupId) {
		if (groupId == null) {
			throw new IllegalArgumentException("Group id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleting facebook group: " + groupId);
		}
		facebookGroupDao.delete(groupId);
	}
	
	public static void validateGroup(FacebookGroupDto groupDto) throws InconsistentStateException {
		if (groupDto == null) {
			throw new IllegalArgumentException("Group DTO must not be null.");
		}
		if (groupDto.getId() == null) {
			throw new InconsistentStateException("Id must not be null.");
		}
		if (groupDto.getName() == null) {
			throw new InconsistentStateException("Name must not be null.");
		}
		if (groupDto.getgId() == null) {
			throw new InconsistentStateException("Facebook group id must not be null.");
		}
	}
	
	public static void validateGroup(FacebookGroup group) throws InconsistentModelException {
		if (group == null) {
			throw new IllegalArgumentException("Group must not be null.");
		}
		if (group.getName() == null) {
			throw new InconsistentModelException("Name must not be null.");
		}
		if (group.getgId() == null) {
			throw new InconsistentModelException("Facebook group id must not be null.");
		}
	}
	
	public static FacebookGroupDto disassembleGroup(FacebookGroup group) {
		if (group == null) {
			throw new IllegalArgumentException("Group must not be null.");
		}
		FacebookGroupDto groupDto = new FacebookGroupDto();
		groupDto.setId(group.getId());
		groupDto.setName(group.getName());
		groupDto.setgId(group.getgId());
		groupDto.setnId(group.getnId());
		groupDto.setCreatorId(group.getCreatorId());
		groupDto.setDescription(group.getDescription());
		groupDto.setType(group.getType());
		groupDto.setSubtype(group.getSubtype());
		groupDto.setLastUpdate(group.getLastUpdate());
		return groupDto;
	}
	
	public static FacebookGroup reassembleGroup(FacebookGroupDto groupDto, Integer groupFrequency) {
		if (groupDto == null) {
			throw new IllegalArgumentException("Group DTO must not be null.");
		}
		FacebookGroup group = new FacebookGroup();
		group.setId(groupDto.getId());
		group.setName(groupDto.getName());
		group.setgId(groupDto.getgId());
		group.setnId(groupDto.getnId());
		group.setCreatorId(groupDto.getCreatorId());
		group.setDescription(groupDto.getDescription());
		group.setType(groupDto.getType());
		group.setSubtype(groupDto.getSubtype());
		group.setLastUpdate(groupDto.getLastUpdate());
		if (groupFrequency == null) {
			group.setFrequency(0);
		} else {
			group.setFrequency(groupFrequency);
		}
		return group;
	}
	
}
