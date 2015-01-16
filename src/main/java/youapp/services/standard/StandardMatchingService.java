package youapp.services.standard;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.IMatchingDao;
import youapp.dataaccess.dto.MatchingDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.MatchingData;
import youapp.model.MatchingScore;
import youapp.model.Person;
import youapp.services.FacebookGroupService;
import youapp.services.FacebookPageService;
import youapp.services.FacebookFriendshipService;
import youapp.services.MatchingService;
import youapp.services.PersonService;
import youapp.services.QuestionService;

public class StandardMatchingService implements MatchingService {

	/**
	 * Logger.
	 */
	static final Log log = LogFactory.getLog(StandardMatchingService.class);
	
	private PersonService personService;
	
	private QuestionService questionService;
	
	private FacebookFriendshipService facebookFriendshipService;
	
	private FacebookGroupService facebookGroupService;
	
	private FacebookPageService facebookPageService;
	
	public IMatchingDao matchingDao;
	
	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	@Autowired
	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}
	
	@Autowired
	public void setFacebookFriendshipService(FacebookFriendshipService facebookFriendshipService) {
		this.facebookFriendshipService = facebookFriendshipService;
	}
	
	@Autowired
	public void setFacebookGroupService(FacebookGroupService facebookGroupService) {
		this.facebookGroupService = facebookGroupService;
	}
	
	@Autowired
	public void setFacebookPageService(FacebookPageService facebookPageService) {
		this.facebookPageService = facebookPageService;
	}
	
	@Autowired
	public void setMatchingDao(IMatchingDao matchingDao) {
		this.matchingDao = matchingDao;
	}
	
	@Override
	public MatchingData getMatchingData(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException, ModelException {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		if (fbIds == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		
		// Convert Facebook ids if necessary.
		if (fbIds) {
			personIdA = personService.getAlternativeId(personIdA, fbIds);
			personIdB = personService.getAlternativeId(personIdB, fbIds);
		}
		
		// Get replies data.
		MatchingData matchingData = new MatchingData();
		matchingData.setCommonReplies(questionService.getCommonRepliesByPair(personIdA, personIdB));
		matchingData.setRepliesA(questionService.getRepliesByPerson(personIdA, false, false, null, null));
		matchingData.setRepliesB(questionService.getRepliesByPerson(personIdB, false, false, null, null));
		
		// Get friendship data.
		matchingData.setCommonFriends(facebookFriendshipService.getCommonFacebookFriends(personIdA, personIdB, false));
		matchingData.setFriendsA(facebookFriendshipService.getAllFacebookFriends(personIdA, false));
		matchingData.setFriendsB(facebookFriendshipService.getAllFacebookFriends(personIdB, false));
		
		// Get group data.
		matchingData.setCommonGroups(facebookGroupService.getCommonGroups(personIdA, personIdB, false));
		matchingData.setGroupsA(facebookGroupService.getByPerson(personIdA, false));
		matchingData.setGroupsB(facebookGroupService.getByPerson(personIdB, false));
		
		// Get page data.
		matchingData.setCommonPages(facebookPageService.getCommonPages(personIdA, personIdB, false));
		matchingData.setPagesA(facebookPageService.getByPerson(personIdA, false));
		matchingData.setPagesB(facebookPageService.getByPerson(personIdB, false));
		
		return matchingData;
	}

	@Override
	public MatchingScore getMatchingScoreById(Long sourceId, Long destinationId, Boolean fbIds) throws DataAccessLayerException, ModelException {
/*		
		log.info("*****************StandardMAtchingService sourceID: " + sourceId + " _" + destinationId) ;
*/
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (destinationId == null) {
			throw new IllegalArgumentException("Destination id must not be null.");
		}
		if (fbIds == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving matching score for person with source id " + sourceId + " and person with destination id " + destinationId + ".");
		}
		
		// Convert Facebook ids if necessary.
		if (fbIds) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Converting facebook ids.");
			}
			sourceId = personService.getAlternativeId(sourceId, fbIds);
			destinationId = personService.getAlternativeId(destinationId, fbIds);
		}		
//		log.debug("*****************StandardMAtchingService sourceID: " + sourceId + " _" + destinationId) ;

		// Get matching scores.
		MatchingDto matchingDto = matchingDao.getById(sourceId, destinationId);
		if (matchingDto == null) {
			if (log.isDebugEnabled()) {
				log.debug(">>> No matching scores found for person with source id " + sourceId + " and person with destination id " + destinationId + ". Returning null.");
			}
			return null;
		}
//		log.debug("*****************StandardMAtchingService sourceID: " + sourceId + " _" + destinationId) ;
		// Get source and destination person.
		Person sourcePerson = personService.getById(sourceId);
		Person destinationPerson = personService.getById(destinationId);
		if (sourcePerson == null || destinationPerson == null) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Either source or destination person is null. Returning null.");
			}
			return null;
		}
		return reassembleMatchingScore(matchingDto, sourcePerson, destinationPerson);
	}

	@Override
	public List<MatchingScore> getBestMatches(Long sourceId, Double minimumScore, Integer offset, Integer resultSize, Boolean fbId) throws DataAccessLayerException, ModelException {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving " + ((resultSize == null)?"all":"best " + resultSize) + " matches for person with id " + sourceId + ".");
		}
		
		// Convert Facebook id if necessary.
		if (fbId) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Converting facebook id.");
			}
			sourceId = personService.getAlternativeId(sourceId, fbId);
		}
		List<MatchingScore> matchingScores = new LinkedList<MatchingScore>();
		Person sourcePerson = personService.getById(sourceId);
		Person destinationPerson = null;
		// Get source person.
		if (sourcePerson == null) {
			if (log.isDebugEnabled()) {
				log.debug("No source person with id " + sourceId + " found. Returning empty list.");
			}
			return matchingScores;
		}
		// Get best matches.
        List<MatchingDto> matchingDtos = matchingDao.getBestMatches(sourceId, minimumScore, offset, resultSize);
        if (matchingDtos == null) {
			if (log.isDebugEnabled()) {
				log.debug("No matching scores for person with id " + sourceId + " found. Returning emtpy list.");
			}
			return matchingScores;
		}
		for (MatchingDto mDto : matchingDtos) {
			destinationPerson = null;
			if (mDto == null || mDto.getDestinationId() == null) {
				if (log.isDebugEnabled()) {
					log.debug(">>> Either matching DTO or the matching DTO's destination id is null. Skipping.");
				}
			} else {
				destinationPerson = personService.getById(mDto.getDestinationId());
				if (destinationPerson == null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> No destination person with id " + mDto.getDestinationId() + " found. Skipping.");
					}
				} else {
					// Only add matching score if source person and destination person could be retrieved.
					matchingScores.add(reassembleMatchingScore(mDto, sourcePerson, destinationPerson));
				}
			}
		}
		return matchingScores;
	}
	
	@Override
	public Boolean existsMatchingScore(Long sourceId, Long destinationId, Boolean fbId) throws DataAccessLayerException {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (destinationId == null) {
			throw new IllegalArgumentException("Destination id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		
		// Convert Facebook id if necessary.
		if (fbId) {
			sourceId = personService.getAlternativeId(sourceId, fbId);
			destinationId = personService.getAlternativeId(destinationId, fbId);
		}
		return matchingDao.exists(sourceId, destinationId);
	}

	@Override
	public void createMatchingScore(MatchingScore matchingScore) throws ModelException, DataAccessLayerException {
		if (matchingScore == null) {
			throw new IllegalArgumentException("Matching score must not be null.");
		}
		if (matchingScore.getSourceId() == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (matchingScore.getDestinationId() == null) {
			throw new IllegalArgumentException("Destination id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Creating matching score for source person with id " + matchingScore.getSourceId() + " and destination person with id " + matchingScore.getDestinationId() + ".");
		}
		
		// Check whether the matching score already exists.
		if (existsMatchingScore(matchingScore.getSourceId(), matchingScore.getDestinationId(), false)) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Matching score already exists. Updating.");
			}
			updateMatchingScore(matchingScore);
		} else {
			// Perform validation checks.
			validateMatchingScore(matchingScore);
			
			// Create new matching score in database.
			MatchingDto matchingDto = disassembleMatchingScore(matchingScore);
			if (log.isDebugEnabled()) {
				log.debug(">>> Creating matching score.");
			}
			matchingDao.create(matchingDto);
		}
	}

	@Override
	public void updateMatchingScore(MatchingScore matchingScore) throws DataAccessLayerException, ModelException {
		if (matchingScore == null) {
			throw new IllegalArgumentException("Matching score must not be null.");
		}
		if (matchingScore.getSourceId() == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (matchingScore.getDestinationId() == null) {
			throw new IllegalArgumentException("Destination id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Updating matching score for source person with id " + matchingScore.getSourceId() + " and destination person with id " + matchingScore.getDestinationId() + ".");
		}
		
		// Check whether the matching score already exists.
		if (!existsMatchingScore(matchingScore.getSourceId(), matchingScore.getDestinationId(), false)) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Matching score is new. Creating.");
			}
			createMatchingScore(matchingScore);
		} else {
			// Perform validation checks.
			validateMatchingScore(matchingScore);
			
			// Create new matching score in database.
			MatchingDto matchingDto = disassembleMatchingScore(matchingScore);
			if (log.isDebugEnabled()) {
				log.debug(">>> Updating matching score.");
			}
			matchingDao.update(matchingDto);
		}
	}

	@Override
	public void deleteMatchingScore(Long sourceId, Long destinationId, Boolean fbIds) throws DataAccessLayerException {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (destinationId == null) {
			throw new IllegalArgumentException("Destination id must not be null.");
		}
		if (fbIds == null) {
			throw new IllegalArgumentException("Facebook id indicators must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleting matching score between source person with id " + sourceId + " and destination person with id " + destinationId);
		}
		
		// Converting Facebook ids if necessary.
		if (fbIds) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Converting facebook ids.");
			}
			sourceId = personService.getAlternativeId(sourceId, fbIds);
			destinationId = personService.getAlternativeId(destinationId, fbIds);
		}
		matchingDao.delete(sourceId, destinationId);
	}
	
	public static void validateMatchingScore(MatchingScore matchingScore) throws InconsistentModelException {
		if (matchingScore == null) {
			throw new IllegalArgumentException("Matching score must not be null.");
		}
		if (matchingScore.getSourceId() == null) {
			throw new InconsistentModelException("Source id must not be null.");
		}
		if (matchingScore.getSourcePerson() != null) {
			if (!matchingScore.getSourceId().equals(matchingScore.getSourcePerson().getId())) {
				throw new InconsistentModelException("Source id and source person id must be equal.");
			}
		}
		if (matchingScore.getDestinationId() == null) {
			throw new InconsistentModelException("Destination id must not be null.");
		}
		if (matchingScore.getDestinationPerson() != null) {
			if (!matchingScore.getDestinationId().equals(matchingScore.getDestinationPerson().getId())) {
				throw new InconsistentModelException("Destination id and destination person id must be equal.");
			}
		}
		// Check replies coefficients.
		if (matchingScore.getRepliesSimon() == null) {
			throw new InconsistentModelException("Replies simon must not be null.");
		}
		if (matchingScore.getRepliesJaccard() == null) {
			throw new InconsistentModelException("Replies jaccard must not be null.");
		}
		if (matchingScore.getRepliesAdamicAdar() == null) {
			throw new InconsistentModelException("Replies adamic adar must not be null.");
		}
		if (matchingScore.getRepliesCosim() == null) {
			throw new InconsistentModelException("Replies cosim must not be null.");
		}
		if (matchingScore.getRepliesScore() == null) {
			throw new InconsistentModelException("Replies score must not be null.");
		}
		// Check friends coefficients.
		if (matchingScore.getFriendsSimon() == null) {
			throw new InconsistentModelException("Friends simon must not be null.");
		}
		if (matchingScore.getFriendsJaccard() == null) {
			throw new InconsistentModelException("Friends jaccard must not be null.");
		}
		if (matchingScore.getFriendsAdamicAdar() == null) {
			throw new InconsistentModelException("Friends adamic adar must not be null.");
		}
		if (matchingScore.getFriendsCosim() == null) {
			throw new InconsistentModelException("Friends cosim must not be null.");
		}
		if (matchingScore.getFriendsScore() == null) {
			throw new InconsistentModelException("Friends score must not be null.");
		}
		// Check groups coefficients.
		if (matchingScore.getGroupsSimon() == null) {
			throw new InconsistentModelException("Groups simon must not be null.");
		}
		if (matchingScore.getGroupsJaccard() == null) {
			throw new InconsistentModelException("Groups jaccard must not be null.");
		}
		if (matchingScore.getGroupsAdamicAdar() == null) {
			throw new InconsistentModelException("Groups adamic adar must not be null.");
		}
		if (matchingScore.getGroupsCosim() == null) {
			throw new InconsistentModelException("Groups cosim must not be null.");
		}
		if (matchingScore.getGroupsScore() == null) {
			throw new InconsistentModelException("Groups score must not be null.");
		}
		// Check pages coefficients.
		if (matchingScore.getPagesSimon() == null) {
			throw new InconsistentModelException("Pages simon must not be null.");
		}
		if (matchingScore.getPagesJaccard() == null) {
			throw new InconsistentModelException("Pages jaccard must not be null.");
		}
		if (matchingScore.getPagesAdamicAdar() == null) {
			throw new InconsistentModelException("Pages adamic adar must not be null.");
		}
		if (matchingScore.getPagesCosim() == null) {
			throw new InconsistentModelException("Pages cosim must not be null.");
		}
		if (matchingScore.getPagesScore() == null) {
			throw new InconsistentModelException("Pages score must not be null.");
		}
		// Check final score.
		if (matchingScore.getFinalScore() == null) {
			throw new InconsistentModelException("Final score must not be null.");
		}
	}
	
	public static void validateMatchingScore(MatchingDto matchingDto) throws InconsistentStateException {
		if (matchingDto == null) {
			throw new IllegalArgumentException("Matching DTO must not be null.");
		}
		// Check replies coefficients.
		if (matchingDto.getRepliesSimon() == null) {
			throw new InconsistentStateException("Replies simon must not be null.");
		}
		if (matchingDto.getRepliesJaccard() == null) {
			throw new InconsistentStateException("Replies jaccard must not be null.");
		}
		if (matchingDto.getRepliesAdamicAdar() == null) {
			throw new InconsistentStateException("Replies adamic adar must not be null.");
		}
		if (matchingDto.getRepliesCosim() == null) {
			throw new InconsistentStateException("Replies cosim must not be null.");
		}
		if (matchingDto.getRepliesScore() == null) {
			throw new InconsistentStateException("Replies score must not be null.");
		}
		// Check friends coefficients.
		if (matchingDto.getFriendsSimon() == null) {
			throw new InconsistentStateException("Friends simon must not be null.");
		}
		if (matchingDto.getFriendsJaccard() == null) {
			throw new InconsistentStateException("Friends jaccard must not be null.");
		}
		if (matchingDto.getFriendsAdamicAdar() == null) {
			throw new InconsistentStateException("Friends adamic adar must not be null.");
		}
		if (matchingDto.getFriendsCosim() == null) {
			throw new InconsistentStateException("Friends cosim must not be null.");
		}
		if (matchingDto.getFriendsScore() == null) {
			throw new InconsistentStateException("Friends score must not be null.");
		}
		// Check groups coefficients.
		if (matchingDto.getGroupsSimon() == null) {
			throw new InconsistentStateException("Groups simon must not be null.");
		}
		if (matchingDto.getGroupsJaccard() == null) {
			throw new InconsistentStateException("Groups jaccard must not be null.");
		}
		if (matchingDto.getGroupsAdamicAdar() == null) {
			throw new InconsistentStateException("Groups adamic adar must not be null.");
		}
		if (matchingDto.getGroupsCosim() == null) {
			throw new InconsistentStateException("Groups cosim must not be null.");
		}
		if (matchingDto.getGroupsScore() == null) {
			throw new InconsistentStateException("Groups score must not be null.");
		}
		// Check pages coefficients.
		if (matchingDto.getPagesSimon() == null) {
			throw new InconsistentStateException("Pages simon must not be null.");
		}
		if (matchingDto.getPagesJaccard() == null) {
			throw new InconsistentStateException("Pages jaccard must not be null.");
		}
		if (matchingDto.getPagesAdamicAdar() == null) {
			throw new InconsistentStateException("Pages adamic adar must not be null.");
		}
		if (matchingDto.getPagesCosim() == null) {
			throw new InconsistentStateException("Pages cosim must not be null.");
		}
		if (matchingDto.getPagesScore() == null) {
			throw new InconsistentStateException("Pages score must not be null.");
		}
		// Check final score.
		if (matchingDto.getFinalScore() == null) {
			throw new InconsistentStateException("Final score must not be null.");
		}
	}
	
	public static MatchingScore reassembleMatchingScore(MatchingDto matchingDto, Person sourcePerson, Person destinationPerson) throws InconsistentModelException {
		if (matchingDto == null) {
			throw new IllegalArgumentException("Matching DTO must not be null.");
		}
		if (sourcePerson == null) {
			throw new IllegalArgumentException("Source person must not be null.");
		}
		if (destinationPerson == null) {
			throw new IllegalArgumentException("Destination person must not be null.");
		}
		MatchingScore matchingScore = new MatchingScore();
		matchingScore.setSourceId(matchingDto.getSourceId());
		matchingScore.setSourcePerson(sourcePerson);
		matchingScore.setDestinationId(matchingDto.getDestinationId());
		matchingScore.setDestinationPerson(destinationPerson);
		// Set replies coefficients.
		matchingScore.setRepliesSimon(matchingDto.getRepliesSimon());
		matchingScore.setRepliesJaccard(matchingDto.getRepliesJaccard());
		matchingScore.setRepliesAdamicAdar(matchingDto.getRepliesAdamicAdar());
		matchingScore.setRepliesCosim(matchingDto.getRepliesCosim());
		matchingScore.setRepliesScore(matchingDto.getRepliesScore());
		// Set friends coefficients.
		matchingScore.setFriendsSimon(matchingDto.getFriendsSimon());
		matchingScore.setFriendsJaccard(matchingDto.getFriendsJaccard());
		matchingScore.setFriendsAdamicAdar(matchingDto.getFriendsAdamicAdar());
		matchingScore.setFriendsCosim(matchingDto.getFriendsCosim());
		matchingScore.setFriendsScore(matchingDto.getFriendsScore());
		// Set groups coefficients.
		matchingScore.setGroupsSimon(matchingDto.getGroupsSimon());
		matchingScore.setGroupsJaccard(matchingDto.getGroupsJaccard());
		matchingScore.setGroupsAdamicAdar(matchingDto.getGroupsAdamicAdar());
		matchingScore.setGroupsCosim(matchingDto.getGroupsCosim());
		matchingScore.setGroupsScore(matchingDto.getGroupsScore());
		// Set pages coefficients.
		matchingScore.setPagesSimon(matchingDto.getPagesSimon());
		matchingScore.setPagesJaccard(matchingDto.getPagesJaccard());
		matchingScore.setPagesAdamicAdar(matchingDto.getPagesAdamicAdar());
		matchingScore.setPagesCosim(matchingDto.getPagesCosim());
		matchingScore.setPagesScore(matchingDto.getPagesScore());
		// Set final score.
		matchingScore.setFinalScore(matchingDto.getFinalScore());
		
		 // Set Geo-Distance
//		 *matchingScore.setDistanceScore(matchingDto.getDistanceScore());
		
		// Check whether the constructed matching score is valid.
		validateMatchingScore(matchingScore);
		return matchingScore;
	}
	
	public static MatchingDto disassembleMatchingScore(MatchingScore matchingScore) throws InconsistentStateException {
		if (matchingScore == null) {
			throw new IllegalArgumentException("Matching score must not be null.");
		}
		// Source and destination.
		MatchingDto matchingDto = new MatchingDto();
		matchingDto.setSourceId(matchingScore.getSourceId());
		matchingDto.setDestinationId(matchingScore.getDestinationId());
		// Set replies coefficients.
		matchingDto.setRepliesSimon(matchingScore.getRepliesSimon());
		matchingDto.setRepliesJaccard(matchingScore.getRepliesJaccard());
		matchingDto.setRepliesAdamicAdar(matchingScore.getRepliesAdamicAdar());
		matchingDto.setRepliesCosim(matchingScore.getRepliesCosim());
		matchingDto.setRepliesScore(matchingScore.getRepliesScore());
		// Set friends coefficients.
		matchingDto.setFriendsSimon(matchingScore.getFriendsSimon());
		matchingDto.setFriendsJaccard(matchingScore.getFriendsJaccard());
		matchingDto.setFriendsAdamicAdar(matchingScore.getFriendsAdamicAdar());
		matchingDto.setFriendsCosim(matchingScore.getFriendsCosim());
		matchingDto.setFriendsScore(matchingScore.getFriendsScore());
		// Set groups coefficients.
		matchingDto.setGroupsSimon(matchingScore.getGroupsSimon());
		matchingDto.setGroupsJaccard(matchingScore.getGroupsJaccard());
		matchingDto.setGroupsAdamicAdar(matchingScore.getGroupsAdamicAdar());
		matchingDto.setGroupsCosim(matchingScore.getGroupsCosim());
		matchingDto.setGroupsScore(matchingScore.getGroupsScore());
		// Set pages coefficients.
		matchingDto.setPagesSimon(matchingScore.getPagesSimon());
		matchingDto.setPagesJaccard(matchingScore.getPagesJaccard());
		matchingDto.setPagesAdamicAdar(matchingScore.getPagesAdamicAdar());
		matchingDto.setPagesCosim(matchingScore.getPagesCosim());
		matchingDto.setPagesScore(matchingScore.getPagesScore());
		// Set final score.
		matchingDto.setFinalScore(matchingScore.getFinalScore());
		
		// Check whether the constructed matching DTO is valid.
		validateMatchingScore(matchingDto);
		return matchingDto;
	}
	
}
