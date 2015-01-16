package youapp.services.standard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.IFacebookFriendshipDao;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.model.FacebookPerson;
import youapp.services.FacebookFriendshipService;
import youapp.services.PersonService;

public class StandardFacebookFriendshipService implements FacebookFriendshipService {

	/**
	 * Logger.
	 */
	static final Log log = LogFactory.getLog(StandardFacebookFriendshipService.class);
	
	private PersonService personService;
	private IFacebookFriendshipDao facebookFriendshipDao;
	
	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	@Autowired
	public void setFacebookFriendshipDao(IFacebookFriendshipDao facebookFriendshipDao) {
		this.facebookFriendshipDao = facebookFriendshipDao;
	}
	
//	@Override
//	public Friendship getById(Long personIdA, Long personIdB) throws DataAccessLayerException, ModelException {
//		if (personIdA == null) {
//			throw new IllegalArgumentException("Person id A must not be null.");
//		}
//		if (personIdB == null) {
//			throw new IllegalArgumentException("Person id B must not be null.");
//		}
//		if (log.isDebugEnabled()) {
//			log.debug("Retrieving friendship between person A with id " + personIdA + " and person B with id " + personIdB + ".");
//		}
//		if (!friendshipDao.areFriends(personIdA, personIdB, true)) {
//			if (log.isDebugEnabled()) {
//				log.debug(">>> No friendship between person A with id " + personIdA + " and person B with id " + personIdB + " present." );
//			}
//			return null;
//		}
//		FriendshipDto friendship = null;
//		Person personA = null;
//		Person personB = null;
//		// Retrieve friendship.
//		friendship = friendshipDao.getById(personIdA, personIdB);
//		// Retrieve persons.
//		personA = personService.getById(personIdA);
//		personB = personService.getById(personIdB);
//		return reassembleFriendship(friendship, personA, personB);
//	}
//	
//	@Override
//	public List<Friendship> getAll(Long personId) throws DataAccessLayerException, ModelException {
//		if (personId == null) {
//			throw new IllegalArgumentException("Source id must not be null.");
//		}
//		if (log.isDebugEnabled()) {
//			log.debug("Retrieving friendship data of all friends of person with id: " + personId);
//		}
//		List<FriendshipDto> friendshipDtos = friendshipDao.getAll(personId);
//		List<Friendship> friendships = new LinkedList<Friendship>();
//		for (FriendshipDto friendship : friendshipDtos) {
//			Person personA = personService.getById(friendship.getSourceId());
//			Person personB = personService.getById(friendship.getTargetId());
//			friendships.add(reassembleFriendship(friendship, personA, personB));
//		}
//		return friendships;
//	}
//	
//	@Override
//	public Map<Long, Friendship> getAllAsMap(Long personId) throws DataAccessLayerException, ModelException {
//		if (personId == null) {
//			throw new IllegalArgumentException("Source id must not be null.");
//		}
//		if (log.isDebugEnabled()) {
//			log.debug("Retrieving friendship data of all friends of person with id: " + personId);
//		}
//		List<FriendshipDto> friendshipDtos = friendshipDao.getAll(personId);
//		Map<Long, Friendship> friendships = new HashMap<Long, Friendship>();
//		for (FriendshipDto friendship : friendshipDtos) {
//			Person personA = personService.getById(friendship.getSourceId());
//			Person personB = personService.getById(friendship.getTargetId());
//			friendships.put(friendship.getTargetId(), reassembleFriendship(friendship, personA, personB));
//		}
//		return friendships;
//	}
//
//	@Override
//	public List<Long> getAllFriendsIds(Long personId) throws DataAccessLayerException {
//		if (personId == null) {
//			throw new IllegalArgumentException("Source id must not be null.");
//		}
//		if (log.isDebugEnabled()) {
//			log.debug("Retrieving all ids of all friends of perosn with id: " + personId);
//		}
//		return friendshipDao.getAllFriendsIds(personId);
//	}
//	
//	@Override
//	public Person getInitiator(Long personIdA, Long personIdB) throws DataAccessLayerException, ModelException {
//		if (personIdA == null) {
//			throw new IllegalArgumentException("Person id A must not be null.");
//		}
//		if (personIdB == null) {
//			throw new IllegalArgumentException("Person id B must not be null.");
//		}
//		if (log.isDebugEnabled()) {
//			log.debug("Retrieving friendship between person A with id " + personIdA + " and person B with id " + personIdB + ".");
//		}
//		if (!friendshipDao.areFriends(personIdA, personIdB, true)) {
//			if (log.isDebugEnabled()) {
//				log.debug(">>> No friendship between person A with id " + personIdA + " and person B with id " + personIdB + " present." );
//			}
//			return null;
//		}
//		// Retrieve initiator.
//		Long initiatorId = friendshipDao.getInitiator(personIdA, personIdB);
//		// Return initiator person.
//		return personService.getById(initiatorId);
//	}
//	
//	@Override
//	public Boolean areFriends(Long personIdA, Long personIdB, Boolean includeProvisional) {
//		if (personIdA == null) {
//			throw new IllegalArgumentException("Person id A must not be null.");
//		}
//		if (personIdB == null) {
//			throw new IllegalArgumentException("Person id B must not be null.");
//		}
//		if (includeProvisional == null) {
//			throw new IllegalArgumentException("Include provisional information must not be null.");
//		}
//		return friendshipDao.areFriends(personIdA, personIdB, includeProvisional);
//	}
//	
//	@Override
//	public Boolean isProvisional(Long personIdA, Long personIdB) {
//		if (personIdA == null) {
//			throw new IllegalArgumentException("Person id A must not be null.");
//		}
//		if (personIdB == null) {
//			throw new IllegalArgumentException("Person id B must not be null.");
//		}
//		return friendshipDao.isProvisional(personIdA, personIdB);
//	}
//	
//	@Override
//	public Date friendsSince(Long personIdA, Long personIdB) {
//		if (personIdA == null) {
//			throw new IllegalArgumentException("Person id A must not be null.");
//		}
//		if (personIdB == null) {
//			throw new IllegalArgumentException("Person id B must not be null.");
//		}
//		return friendshipDao.friendsSince(personIdA, personIdB);
//	}
//	
//	@Override
//	public void create(Friendship friendship) throws ModelException {
//		if (friendship == null) {
//			throw new IllegalArgumentException("Friendship must not be null.");
//		}
//		
//		// Perform validation checks.
//		validateFriendship(friendship);
//		
//		// Check whether friendship already exists.
//		if (friendshipDao.areFriends(friendship.getPersonA().getId(), friendship.getPersonB().getId(), true)) {
//			if (log.isDebugEnabled()) {
//				log.debug(">>> Friendship already exists.");
//			}
//			return;
//		}
//		
//		// Create new bidirectional friendship in the database.
//		FriendshipDto friendshipA = disassembleFriendship(friendship);
//		FriendshipDto friendshipB = new FriendshipDto();
//		friendshipB.setSourceId(friendshipA.getTargetId());
//		friendshipB.setTargetId(friendshipA.getSourceId());
//		friendshipB.setInitiatorId(friendshipA.getInitiatorId());
//		friendshipB.setProvisional(friendshipA.getProvisional());
//		friendshipB.setSince(friendshipA.getSince());
//		if (log.isDebugEnabled()) {
//			log.debug("Creating friendship link between " + friendshipA.getSourceId() + " and " + friendshipA.getTargetId());
//			log.debug("Creating friendship linke between " + friendshipB.getSourceId() + " and " + friendshipB.getTargetId());
//		}
//		friendshipDao.create(friendshipA);
//		friendshipDao.create(friendshipB);
//	}
//	
//	@Override
//	public void create(Long personIdA, Long personIdB, Boolean provisional,
//			Long initiatorId) {
//		if (personIdA == null) {
//			throw new IllegalArgumentException("Person id A must not be null.");
//		}
//		if (personIdB == null) {
//			throw new IllegalArgumentException("Person id B must not be null.");
//		}
//		if (provisional == null) {
//			throw new IllegalArgumentException("Provisional must not be null.");
//		}
//		if (initiatorId == null) {
//			throw new IllegalArgumentException("Initiator id must not be null.");
//		}
//		
//		// Check whether friendship already exists.
//		if (friendshipDao.areFriends(personIdA, personIdB, true)) {
//			if (log.isDebugEnabled()) {
//				log.debug(">>> Friendship already exists.");
//			}
//			return;
//		}
//		
//		// Determine current date.
//		Date since = new Date(System.currentTimeMillis());
//		
//		// Create new bidirectional friendship in the database.
//		FriendshipDto friendshipA = new FriendshipDto();
//		friendshipA.setSourceId(personIdA);
//		friendshipA.setTargetId(personIdB);
//		friendshipA.setInitiatorId(initiatorId);
//		friendshipA.setProvisional(provisional);
//		friendshipA.setSince(since);
//		FriendshipDto friendshipB = new FriendshipDto();
//		friendshipB.setSourceId(friendshipA.getTargetId());
//		friendshipB.setTargetId(friendshipA.getSourceId());
//		friendshipB.setInitiatorId(friendshipA.getInitiatorId());
//		friendshipB.setProvisional(friendshipA.getProvisional());
//		friendshipB.setSince(friendshipA.getSince());
//		if (log.isDebugEnabled()) {
//			log.debug("Creating friendship link between " + friendshipA.getSourceId() + " and " + friendshipA.getTargetId());
//			log.debug("Creating friendship linke between " + friendshipB.getSourceId() + " and " + friendshipB.getTargetId());
//		}
//		friendshipDao.create(friendshipA);
//		friendshipDao.create(friendshipB);
//	}
//	
//	@Override
//	public void create(Long personIdA, Long personIdB, Boolean provisional,
//			Long initiatorId, Date since) {
//		if (personIdA == null) {
//			throw new IllegalArgumentException("Person id A must not be null.");
//		}
//		if (personIdB == null) {
//			throw new IllegalArgumentException("Person id B must not be null.");
//		}
//		if (provisional == null) {
//			throw new IllegalArgumentException("Provisional must not be null.");
//		}
//		if (initiatorId == null) {
//			throw new IllegalArgumentException("Initiator id must not be null.");
//		}
//		if (since == null) {
//			throw new IllegalArgumentException("Since date must not be null.");
//		}
//		if (friendshipDao.areFriends(personIdA, personIdB, true)) {
//			if (log.isDebugEnabled()) {
//				log.debug(">>> Friendship already exists.");
//			}
//			return;
//		}
//		
//		// Create new bidirectional friendship in the database.
//		FriendshipDto friendshipA = new FriendshipDto();
//		friendshipA.setSourceId(personIdA);
//		friendshipA.setTargetId(personIdB);
//		friendshipA.setInitiatorId(initiatorId);
//		friendshipA.setProvisional(provisional);
//		friendshipA.setSince(since);
//		FriendshipDto friendshipB = new FriendshipDto();
//		friendshipB.setSourceId(friendshipA.getTargetId());
//		friendshipB.setTargetId(friendshipA.getSourceId());
//		friendshipB.setInitiatorId(friendshipA.getInitiatorId());
//		friendshipB.setProvisional(friendshipA.getProvisional());
//		friendshipB.setSince(friendshipA.getSince());
//		if (log.isDebugEnabled()) {
//			log.debug("Creating friendship link between " + friendshipA.getSourceId() + " and " + friendshipA.getTargetId());
//			log.debug("Creating friendship linke between " + friendshipB.getSourceId() + " and " + friendshipB.getTargetId());
//		}
//		friendshipDao.create(friendshipA);
//		friendshipDao.create(friendshipB);
//	}
//	
//	@Override
//	public void update(Friendship friendship) throws InconsistentModelException {
//		if (friendship == null) {
//			throw new IllegalArgumentException("Friendship must not be null.");
//		}
//		
//		// Perform validation checks.
//		validateFriendship(friendship);
//		
//		// Update friendship in database.
//		FriendshipDto friendshipDto = disassembleFriendship(friendship);
//		if (log.isDebugEnabled()) {
//			log.debug("Updating friendship link between " + friendshipDto.getSourceId() + " and " + friendshipDto.getTargetId() + ".");
//		}
//		friendshipDao.update(friendshipDto);
//		Long tmp = friendshipDto.getSourceId();
//		friendshipDto.setSourceId(friendshipDto.getTargetId());
//		friendshipDto.setTargetId(tmp);
//		if (log.isDebugEnabled()) {
//			log.debug("Updating friendship link between " + friendshipDto.getSourceId() + " and " + friendshipDto.getTargetId() + ".");
//		}
//		friendshipDao.update(friendshipDto);
//	}
//	
//	@Override
//	public void updateProvisional(Long personIdA, Long personIdB, Boolean provisional) {
//		if (personIdA == null) {
//			throw new IllegalArgumentException("Person id A must not be null.");
//		}
//		if (personIdB == null) {
//			throw new IllegalArgumentException("Person id B must not be null.");
//		}
//		if (provisional == null) {
//			throw new IllegalArgumentException("Provisional must not be null.");
//		}
//		if (!friendshipDao.areFriends(personIdA, personIdB, true)) {
//			if (log.isDebugEnabled()) {
//				log.debug(">>> No friendship between person A with id " + personIdA + " and person B with id " + personIdB + " present." );
//			}
//			return;
//		}
//		if (log.isDebugEnabled()) {
//			log.debug("Updating friendship link between " + personIdA + " and " + personIdB + ".");
//		}
//		friendshipDao.updateProvisional(personIdA, personIdB, provisional);
//		if (log.isDebugEnabled()) {
//			log.debug("Updating friendship link between " + personIdB + " and " + personIdA + ".");
//		}
//		friendshipDao.updateProvisional(personIdB, personIdA, provisional);
//	}
//	
//	@Override
//	public void delete(Long personIdA, Long personIdB) {
//		if (personIdA == null) {
//			throw new IllegalArgumentException("Person id A must not be null.");
//		}
//		if (personIdB == null) {
//			throw new IllegalArgumentException("Person id B must not be null.");
//		}
//		if (!friendshipDao.areFriends(personIdA, personIdB, true)) {
//			if (log.isDebugEnabled()) {
//				log.debug(">>> No friendship between person A with id " + personIdA + " and person B with id " + personIdB + " present." );
//			}
//			return;
//		}
//		
//		if (log.isDebugEnabled()) {
//			log.debug("Deleting friendship link between " + personIdA + " and " + personIdB + ".");
//		}
//		friendshipDao.delete(personIdA, personIdB);
//		if (log.isDebugEnabled()) {
//			log.debug("Deleting friendship link between " + personIdB + " and " + personIdA + ".");
//		}
//		friendshipDao.delete(personIdB, personIdA);
//	}
//	
//	public static Friendship reassembleFriendship(FriendshipDto friendshipDto, Person personSource,
//			Person personTarget) throws InconsistentStateException, InconsistentModelException {
//		// Check parameter null pointers.
//		if (friendshipDto == null) {
//			throw new IllegalArgumentException("Fiendship Dto must not be null.");
//		}
//		if (personSource == null) {
//			throw new IllegalArgumentException("Person A must not be null.");
//		}
//		if (personTarget == null) {
//			throw new IllegalArgumentException("Person B must not be null.");
//		}
//		
//		// Validate.
//		validateFriendship(friendshipDto);
//		StandardPersonService.validatePerson(personSource, false);
//		StandardPersonService.validatePerson(personTarget, false);
//		
//		// Validate wiring.
//		if (friendshipDto.getSourceId() != personSource.getId()) {
//			throw new InconsistentStateException("Friendship source id and source person id must be equal.");
//		}
//		if (friendshipDto.getTargetId() != personTarget.getId()) {
//			throw new InconsistentStateException("Friendship target id and target person id must be equal.");
//		}
//		
//		// Reassemble.
//		if (log.isDebugEnabled()) {
//			log.debug("Reassembling friendship: " + friendshipDto.toString());
//		}
//		
//		// Create friendship object.
//		Friendship friendship = new Friendship();
//		if (personSource.getId() <= personTarget.getId()) {
//			friendship.setPersonA(personSource);
//			friendship.setPersonB(personTarget);
//		} else {
//			friendship.setPersonA(personTarget);
//			friendship.setPersonB(personSource);
//		}
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Person A id: " + friendship.getPersonA().getId());
//			log.debug(">>> Person B id: " + friendship.getPersonB().getId());
//		}
//		friendship.setInitiator(friendshipDto.getInitiatorId());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Initiator id: " + friendship.getInitiator());
//		}
//		friendship.setProvisional(friendshipDto.getProvisional());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Provisional: " + friendship.getProvisional());
//		}
//		friendship.setSince(friendshipDto.getSince());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Since: " + friendship.getSince());
//		}
//		return friendship;
//	}
//	
//	public static FriendshipDto disassembleFriendship(Friendship friendship) {
//		if (friendship == null) {
//			throw new IllegalArgumentException("Friendship must not be null.");
//		}
//		FriendshipDto friendshipDto = new FriendshipDto();
//		if (friendship.getPersonA() != null) {
//			friendshipDto.setSourceId(friendship.getPersonA().getId());
//		}
//		if (friendship.getPersonB() != null) {
//			friendshipDto.setTargetId(friendship.getPersonB().getId());
//		}
//		friendshipDto.setInitiatorId(friendship.getInitiator());
//		friendshipDto.setProvisional(friendship.getProvisional());
//		friendshipDto.setSince(friendship.getSince());
//		return friendshipDto;
//	}
//	
//	public static void validateFriendship(FriendshipDto friendship) throws InconsistentStateException {
//		if (friendship == null) {
//			throw new IllegalArgumentException("Fiendship DTO must not be null.");
//		}
//		
//		// Check mandatory friendship data
//		if (friendship.getSourceId() == null) {
//			throw new InconsistentStateException("Friendship source id must not be null.");
//		}
//		if (friendship.getTargetId() == null) {
//			throw new InconsistentStateException("Friendship target id must not be null.");
//		}
//		if ((friendship.getSourceId() == friendship.getTargetId())) {
//			throw new InconsistentStateException("Source and target id's must not be equal.");
//		}
//		if (friendship.getInitiatorId() == null) {
//			throw new InconsistentStateException("Initiator id must not be null.");
//		}
//		if ((friendship.getSourceId() != friendship.getInitiatorId())
//				&& (friendship.getTargetId() != friendship.getInitiatorId())) {
//			throw new InconsistentStateException("The initiator id must either be equal to the source id or the target id.");
//		}
//		if (friendship.getProvisional() == null) {
//			throw new InconsistentStateException("Provisional information must not be null.");
//		}
//		if (friendship.getSince() == null) {
//			throw new InconsistentStateException("Since date must not be null.");
//		}
//	}
//	
//	public static void validateFriendship(Friendship friendship) throws InconsistentModelException {
//		if (friendship == null) {
//			throw new IllegalArgumentException("Friendship must not be null.");
//		}
//		
//		// Check mandatory friendship data.
//		if (friendship.getPersonA() == null) {
//			throw new InconsistentModelException("Person a must not be null.");
//		}
//		if (friendship.getPersonA().getId() == null) {
//			throw new InconsistentModelException("Person a id must not be null.");
//		}
//		if (friendship.getPersonB() == null) {
//			throw new InconsistentModelException("Person b must not be null.");
//		}
//		if (friendship.getPersonB().getId() == null) {
//			throw new InconsistentModelException("Person b id must not be null.");
//		}
//		if (friendship.getPersonA().getId() == friendship.getPersonB().getId()) {
//			throw new InconsistentModelException("Friendship source person id and friendship target person id must not be equal.");
//		}
//		if (friendship.getInitiator() == null) {
//			throw new InconsistentModelException("Initiator must not be null.");
//		}
//		if (friendship.getProvisional() == null) {
//			throw new InconsistentModelException("Provisional information must not be null.");
//		}
//		if (friendship.getSince() == null) {
//			throw new InconsistentModelException("Since date must not be null.");
//		}
//		
//		// Check associated objects.
//		StandardPersonService.validatePerson(friendship.getPersonA(), false);
//		StandardPersonService.validatePerson(friendship.getPersonB(), false);
//	}

	@Override
	public List<FacebookPerson> getAllFacebookFriends(Long sourceId, Boolean fbId) throws DataAccessLayerException {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Returning all facebook friends of person with facebook id: " + sourceId);
		}
		if (!fbId) {
			sourceId = personService.getAlternativeId(sourceId, fbId);
		}
		List<Long> friendsIds = facebookFriendshipDao.getAllFriendsIds(sourceId);
		List<FacebookPerson> friends = new LinkedList<FacebookPerson>();
		Integer numberOfFriends = null;
		for (Long id : friendsIds) {
			numberOfFriends = null;
			if (id != null) {
				numberOfFriends = facebookFriendshipDao.getNumberOfFriends(id);
				if (numberOfFriends == null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Number of friends of person with facebook id " + id + " was null.");
					}
					numberOfFriends = 0;
				}
				friends.add(new FacebookPerson(id, numberOfFriends));
			} else {
				if (log.isDebugEnabled()) {
					log.debug(">>> Facebook id was null. Skipping.");
				}
			}
		}
		return friends;
	}
	
	@Override
	public Map<Long, FacebookPerson> getAllFacebookFriendsAsMap(Long sourceId, Boolean fbId) throws DataAccessLayerException {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Returning all facebook friends of person with facebook id: " + sourceId);
		}
		if (!fbId) {
			sourceId = personService.getAlternativeId(sourceId, fbId);
		}
		List<Long> friendsIds = facebookFriendshipDao.getAllFriendsIds(sourceId);
		Map<Long, FacebookPerson> friends = new HashMap<Long, FacebookPerson>();
		Integer numberOfFriends = null;
		for (Long id : friendsIds) {
			numberOfFriends = null;
			if (id != null) {
				numberOfFriends = facebookFriendshipDao.getNumberOfFriends(id);
				if (numberOfFriends == null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Number of friends of person with facebook id " + id + " was null.");
					}
					numberOfFriends = 0;
				}
				friends.put(id, new FacebookPerson(id, numberOfFriends));
			}  else {
				if (log.isDebugEnabled()) {
					log.debug(">>> Facebook id was null. Skipping.");
				}
			}
		}
		return friends;
	}
	
	@Override
	public List<FacebookPerson> getCommonFacebookFriends(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		if (fbIds == null) {
			throw new IllegalArgumentException("FAcebook id indicator must not be null.");
		}
		if (!fbIds) {
			personIdA = personService.getAlternativeId(personIdA, fbIds);
			personIdB = personService.getAlternativeId(personIdB, fbIds);
		}
		List<Long> friendsIds = facebookFriendshipDao.getCommonFriends(personIdA, personIdB);
		List<FacebookPerson> friends = new LinkedList<FacebookPerson>();
		Integer numberOfFriends = null;
		for (Long id : friendsIds) {
			numberOfFriends = null;
			if (id != null) {
				numberOfFriends = facebookFriendshipDao.getNumberOfFriends(id);
				if (numberOfFriends == null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Number of friends of person with facebook id " + id + " was null.");
					}
					numberOfFriends = 0;
				}
				friends.add(new FacebookPerson(id, numberOfFriends));
			} else {
				if (log.isDebugEnabled()) {
					log.debug(">>> Facebook id was null. Skipping.");
				}
			}
		}
		return friends;
	}
	
	@Override
	public Integer getNumberOfFacebookFriends(Long personId, Boolean fbId) throws DataAccessLayerException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (!fbId) {
			personId = personService.getAlternativeId(personId, false);
		}
		return facebookFriendshipDao.getNumberOfFriends(personId);
	}

	@Override
	public Integer getNumberOfCommonFacebookFriends(Long idPersonA, Long idPersonB, Boolean fbIds) throws DataAccessLayerException {
		if (idPersonA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (idPersonB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		if (fbIds == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (!fbIds) {
			idPersonA = personService.getAlternativeId(idPersonA, false);
			idPersonB = personService.getAlternativeId(idPersonB, false);
		}
		return facebookFriendshipDao.getNumberOfCommonFriends(idPersonA, idPersonB);
	}

	@Override
	public Boolean areFacebookFriends(Long sourceId, Long targetId) throws DataAccessLayerException {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (targetId == null) {
			throw new IllegalArgumentException("Target id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Are these people facebook friends? " + sourceId + " - " + targetId);
		}
		return facebookFriendshipDao.areFriends(sourceId, targetId);
	}

	@Override
	public void createFacebookFriendship(Long sourceId, Long targetId) throws DataAccessLayerException {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (targetId == null) {
			throw new IllegalArgumentException("Target id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Creating facebook friendship: " + sourceId + " - " + targetId);
		}
		if (facebookFriendshipDao.areFriends(sourceId, targetId)) {
			if(log.isDebugEnabled()) {
				log.debug(">>> Facebook friendship already exists.");
			}
			return;
		}
		facebookFriendshipDao.create(sourceId, targetId);
	}

	@Override
	public void deleteFacebookFriendship(Long sourceId, Long targetId) throws DataAccessLayerException {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (targetId == null) {
			throw new IllegalArgumentException("Target id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleting facebook friendship: " + sourceId + " - " + targetId);
		}
		facebookFriendshipDao.delete(sourceId, targetId);
	}
	
}
