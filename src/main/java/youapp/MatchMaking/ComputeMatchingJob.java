package youapp.MatchMaking;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ComputeMatchingJob implements Runnable{

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(ComputeMatchingJob.class);
	
	private Long personId;	
	private ComputeMatching computeMatching;

	
  public ComputeMatchingJob(Long personId, ComputeMatching computeMatching) {
	  
	  if (personId == null) {
		throw new IllegalArgumentException("Access personId must not be null.");
	  }
	  if (computeMatching == null) {
			throw new IllegalArgumentException("Compute matching service must not be null.");
	  }	  
		
		this.personId = personId;
		this.computeMatching = computeMatching;
	}
	
	
	@Override
	public void run() {
		
		if (log.isDebugEnabled()) {
			log.debug("Computing matching data for the loged user.");
		}
		
		computeMatching.computeMatchingForPerson(personId);
		
		log.debug("Computing matching data complete.");
	}
	

}
