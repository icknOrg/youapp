package youapp.MatchMaking;

import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.model.Location;
import youapp.model.MatchingScore;
import youapp.model.Person;
import youapp.model.ReplyPair;
import youapp.model.Tag.Category;
import youapp.services.DistanceCalculationService;
import youapp.services.FacebookFriendshipService;
import youapp.services.FacebookGroupService;
import youapp.services.FacebookPageService;
import youapp.services.MatchingService;
import youapp.services.PersonService;
import youapp.services.TagService;
import youapp.services.standard.StandardAccessLevelService;

public class ComputeMatching  {

	private PersonService personService;
	private MatchingService matchingService;
	private FacebookPageService facebookPageService;
	private FacebookGroupService facebookGroupService;
	private FacebookFriendshipService facebookFriendshipService;
	private DistanceCalculationService distanceCalculationService;
	private TagService tagService;

	private CriteriaWeights defaultCriteriaWeights;
	
	/**
	 * Number of necessary question matches for a 100% match.
	 */
	private double nMin;

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory
			.getLog(StandardAccessLevelService.class);

	
		
	public void computeMatchingForAll() {
		
		try {
			
			if (log.isDebugEnabled()) {
				log.debug("Start matching process...");
			}

			// Get all persons.
				List<Person> persons = personService.getAll();
				Person[] pArray = persons.toArray(new Person[persons.size()]);				

				if (log.isDebugEnabled()) {
					log.debug("Computing matching scores for " + pArray.length
							+ " persons.");
				}

				// Build all possible friend pairs.
				for (int i = 0; i < pArray.length; i++) {
					for (int j = i + 1; j < pArray.length; j++) {
						Person personA = pArray[i];
						Person personB = pArray[j];						

						if (log.isTraceEnabled()) {
							log.trace("Next pair");
							log.trace("------------------------");
							log.trace(">>> Person A: " + personA.getId()
									+ " - " + personA.getFirstName() + " "
									+ personA.getLastName());
							log.trace(">>> Person B: " + personB.getId()
									+ " - " + personB.getFirstName() + " "
									+ personB.getLastName());			
							
						}	
						
						CriteriaWeights personACriteriaWeights = defaultCriteriaWeights.reCalculateWeightsForPerson(personA);
						CriteriaWeights personBCriteriaWeights = defaultCriteriaWeights.reCalculateWeightsForPerson(personB);
						
						computeMatchingScore(personA,personB,personACriteriaWeights,personBCriteriaWeights);
						
					}
				}
			} catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error("An exception occurred while accessing or computing the matichng data.");
					log.error("Reason: " + e);
					e.printStackTrace();
				}
			}		 
	}
	
	
	public void computeMatchingForPerson(Long personID)
	{	
		log.debug("Calculating maching scores for person with ID " + personID);
		
		Person personA;
		try {
			
			try {
				personA = personService.getById(personID);		
			}
			catch(Exception e )			
			{
				log.error("Error retrieving data for personID=" + personID);
				log.error("Reason: " + e.getMessage());
				throw new Exception("Error retrieving data for personID=" + personID + e.getMessage());
			}
			
			
			CriteriaWeights personACriteriaWeights = defaultCriteriaWeights.reCalculateWeightsForPerson(personA);			
			
			//the list of persons to be matched to	
			List<Person> persons = personService.getAll();
			
			for(Person personB : persons)
			{
				if( personB.getId() != personID )
				{
					CriteriaWeights personBCriteriaWeights = defaultCriteriaWeights.reCalculateWeightsForPerson(personB);
					computeMatchingScore(personA,personB,personACriteriaWeights,personBCriteriaWeights);
				}
			}
		
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("An exception occurred while accessing or computing the matichng data.");
				log.error("Reason: " + e);
				e.printStackTrace();
			}
		}		
	}	
	
	
	public void computeMatchingScore(Person personA, Person personB,
									CriteriaWeights personACriteriaWeights,CriteriaWeights personBCriteriaWeights ) throws Exception
	{
		MatchingScore matchingScore =  new MatchingScore();
		
		double scoreAB =  computeMatchingScoreAB(personA,personB,personACriteriaWeights);
		double scoreBA = computeMatchingScoreAB(personB,personA,personBCriteriaWeights);
		
		double finalScore= Math.sqrt(scoreAB * scoreBA );
		
		log.debug("Matching score " + personA.getFirstName() + " " + personA.getLastName() 
				+ " -  " +  personB.getFirstName() + " " + personB.getLastName()
				+ " = " + finalScore);

		matchingScore.setFinalScore(finalScore);
		
		matchingScore.setRepliesSimon(0.0);
		matchingScore.setRepliesScore(0.0);
		matchingScore.setFriendsScore(0.0);		
		matchingScore.setDistanceScore(0.0);

		// Simons scores.
		matchingScore.setFriendsSimon(0.0);
		matchingScore.setGroupsSimon(0.0);
		matchingScore.setPagesSimon(0.0);

		// Jaccards scores.
		matchingScore.setRepliesJaccard(0.0);
		matchingScore.setFriendsJaccard(0.0);
		matchingScore.setGroupsJaccard(0.0);
		matchingScore.setPagesJaccard(0.0);

		// Adamic/Adar scores.
		matchingScore.setRepliesAdamicAdar(0.0);
		matchingScore.setFriendsAdamicAdar(0.0);
		matchingScore.setGroupsAdamicAdar(0.0);
		matchingScore.setPagesAdamicAdar(0.0);

		// Cosim scores.
		matchingScore.setRepliesCosim(0.0);
		matchingScore.setFriendsCosim(0.0);
		matchingScore.setGroupsCosim(0.0);
		matchingScore.setPagesCosim(0.0);

		// Total scores.
		// matchingScore.setRepliesScore(0.0);
		// matchingScore.setFriendsScore(0.0);
		matchingScore.setGroupsScore(0.0);
		matchingScore.setPagesScore(0.0);

		// Store matching score in database.
		matchingScore.setSourceId(personA.getId());
		matchingScore.setDestinationId(personB.getId());
		matchingService.createMatchingScore(matchingScore);

		matchingScore.setSourceId(personB.getId());
		matchingScore.setDestinationId(personA.getId());
		matchingService.createMatchingScore(matchingScore);
	}
	

	public double computeMatchingScoreAB(Person personA, Person personB, CriteriaWeights personACriteriaWeights) throws Exception
	{				
		double questionsScoreAB = getQzScore(personA.getId(), personB.getId());
		double fbScoreAB = getFbScore(personA.getId(),personB.getId());
		double distanceScore = getDistanceScore(personA.getLocation(), personB.getLocation());
		double symtomsScoreAB = getSymptomsScore(personA,personB);
		double medicationScoreAB = getMedicationScore(personA,personB);		
		
		double scoreAB = personACriteriaWeights.getWeightQuiz() * questionsScoreAB +
						 personACriteriaWeights.getWeightFB() * fbScoreAB +
						 personACriteriaWeights.getWeightDistance() * distanceScore +
						 personACriteriaWeights.getWeightSymptoms() * symtomsScoreAB + 
						 personACriteriaWeights.getWeightMedication() * medicationScoreAB;		 

		return scoreAB;		
	}
	
	private double getQzScore( Long personidA, Long personidB) 
			throws DataAccessLayerException, ModelException {
		
		double scoreAB = 0.0;
		double sumQuestions = 0.0;
		double sumImportances = 0.0;
		
		List<ReplyPair> commonReplies = matchingService.getMatchingData(personidA, personidB,false).getCommonReplies();
		
		if (commonReplies.size() !=0)		
		{
			for (ReplyPair rPair : commonReplies) {
				
				if (rPair.getReplyA().getImportance() == null) {
					throw new IllegalArgumentException("Importance must not be null.");
				} 
				else {
					sumQuestions +=  rPair.getReplyA().getImportance()* sameAnswer(rPair);
					sumImportances += rPair.getReplyA().getImportance();
				}
		}
		
		if(sumImportances != 0)
		{
			scoreAB = sumQuestions/sumImportances * getCircVal(commonReplies.size() / nMin);
		}
		}
		
		return scoreAB;
	}

	private int sameAnswer(ReplyPair rPair) {

		if (rPair.getReplyA().getAnswerA() == rPair.getReplyB().getAnswerA()
				&& rPair.getReplyA().getAnswerB() == rPair.getReplyB()
						.getAnswerB()
				&& rPair.getReplyA().getAnswerC() == rPair.getReplyB()
						.getAnswerC()
				&& rPair.getReplyA().getAnswerD() == rPair.getReplyB()
						.getAnswerD()
				&& rPair.getReplyA().getAnswerE() == rPair.getReplyB()
						.getAnswerE())

			return 0;
		else
			return 1;

	}

	private double getFbScore(Long personidA, Long personidB) throws DataAccessLayerException {
		
		double scoreFBPages = 0;
		double scoreFBGroups = 0;
		double scoretFBFriends = 0;
		double facebookScoreAB = 0;

		if (facebookPageService.getNumberOfCommonPages(personidA, personidB,
				false) != 0) {
			scoreFBPages = getCircVal(facebookPageService
					.getNumberOfCommonPages(personidA, personidB, false)
					/ facebookPageService.getNumberOfPages(personidA, false));
		}

		if (facebookGroupService.getNumberOfCommonGroups(personidA, personidB,
				false) != 0) {
			scoreFBGroups = getCircVal(facebookFriendshipService
					.getNumberOfCommonFacebookFriends(personidA, personidB,
							false)
					/ facebookGroupService.getNumberOfGroups(personidA, false));
		}

		if (facebookFriendshipService.getNumberOfCommonFacebookFriends(
				personidA, personidB, false) != 0) {
			scoretFBFriends = getCircVal(facebookFriendshipService
					.getNumberOfCommonFacebookFriends(personidA, personidB,
							false)
					/ facebookFriendshipService.getNumberOfFacebookFriends(
							personidA, false));
		}

		facebookScoreAB = Math.min(1, defaultCriteriaWeights.getWeightFBPages()
				* scoreFBPages + defaultCriteriaWeights.getWeightFBGroups()
				* scoreFBGroups + defaultCriteriaWeights.getWeightFBFriends()
				* scoretFBFriends);

		return facebookScoreAB;
	}

	//TODO TEST
	private double getDistanceScore(Location personLocationA,Location personLocationB) throws Exception {
		double scoreDistance = 0;
		String xmlFilePath = "/distanceIntervalsWeights.xml";
		
		// Calculate the geo-location distance between person A
		// and B as result of their hometown.
		double distance = distanceCalculationService.calcDistanceFromInKm(
				personLocationA.getLatitude().doubleValue(), personLocationA
						.getLongitude().doubleValue(), personLocationB
						.getLatitude().doubleValue(), personLocationB
						.getLatitude().doubleValue());

		DistanceIntervalsWeights newDistIntW;

		try {
			URL fileLocation = getClass().getClassLoader().getResource(xmlFilePath);			
			newDistIntW = JAXB.unmarshal(fileLocation,DistanceIntervalsWeights.class);
			
		} catch (Exception e) {
			
			log.debug(e.getMessage());
			throw new Exception("Error parcing distances intervals from xml configuration file");
		}
		
		for(DistanceInterval di:newDistIntW.getIntervals())
		{	
			if(( distance> di.getLeft() || (di.isLeftClosed() && distance>=di.getLeft())) &&
			  (distance < di.getRight() || (di.isRightClosed() && distance <= di.getRight())))
			{
				System.out.println("DISTANCE: " + distance);
				System.out.print("INTREVAL: ");
				System.out.print( di.isLeftClosed() ? "[ ": "( " );
				System.out.print(di.getLeft() + "- " + di.getRight());
				System.out.println(di.isRightClosed() ? " ] ": " ) ");
				System.out.println("WEIGHT: " + di.getWeight());
				
				scoreDistance = di.getWeight();
			}			
		}		
		
		return scoreDistance;

	}

	//TODO TEST
	public double getSymptomsScore(Person personA,Person personB){
		
		double scoreSymtoms = 0;
		
		if (tagService.getNumberOfCommonTagsByCategory(personA.getId(), personB.getId(), Category.Symptom) != 0) {
			scoreSymtoms = getCircVal(tagService.getNumberOfCommonTagsByCategory(personA.getId(), personB.getId(), Category.Symptom)
					/ tagService.getNumberOfTagsByCategory(personA.getId(), Category.Symptom));
		}
		
		return scoreSymtoms;		
	}
	
	//TODO TEST
	public double getMedicationScore(Person personA,Person personB){
		
		double scoreMedication = 0;
		
		if (tagService.getNumberOfCommonTagsByCategory(personA.getId(), personB.getId(), Category.Medication) != 0) {
			scoreMedication = getCircVal(tagService.getNumberOfCommonTagsByCategory(personA.getId(), personB.getId(), Category.Medication)
					/ tagService.getNumberOfTagsByCategory(personA.getId(), Category.Medication));
		}
		
		return scoreMedication;		
	}
	
	private static double getCircVal(double value) {
		// Calculate value [0;1] according to the circle function
		return Math.sqrt(1 - Math.pow(1 - Math.min(1, value), 2));
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public MatchingService getMatchingService() {
		return matchingService;
	}

	public void setMatchingService(MatchingService matchingService) {
		this.matchingService = matchingService;
	}

	public FacebookPageService getFacebookPageService() {
		return facebookPageService;
	}

	public void setFacebookPageService(FacebookPageService facebookPageService) {
		this.facebookPageService = facebookPageService;
	}

	public FacebookGroupService getFacebookGroupService() {
		return facebookGroupService;
	}

	public void setFacebookGroupService(
			FacebookGroupService facebookGroupService) {
		this.facebookGroupService = facebookGroupService;
	}

	public FacebookFriendshipService getFacebookFriendshipService() {
		return facebookFriendshipService;
	}

	public void setFacebookFriendshipService(
			FacebookFriendshipService facebookFriendshipService) {
		this.facebookFriendshipService = facebookFriendshipService;
	}


	public DistanceCalculationService getDistanceCalculationService() {
		return distanceCalculationService;
	}

	public void setDistanceCalculationService(
			DistanceCalculationService distanceCalculationService) {
		this.distanceCalculationService = distanceCalculationService;
	}

	public TagService getTagService() {
		return tagService;
	}

	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	public double getnMin() {
		return nMin;
	}

	public void setnMin(double nMin) {
		this.nMin = nMin;
	}


	public CriteriaWeights getDefaultCriteriaWeights() {
		return defaultCriteriaWeights;
	}


	public void setDefaultCriteriaWeights(CriteriaWeights defaultCriteriaWeights) {
		this.defaultCriteriaWeights = defaultCriteriaWeights;
	}
}
