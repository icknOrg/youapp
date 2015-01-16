package youapp.jobs;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import youapp.MatchMaking.ComputeMatching;

import youapp.services.standard.StandardAccessLevelService;

public class MatchingJob extends QuartzJobBean {
	private ComputeMatching computeMatching;
	
	/**
	 * Logger.
	 */
	private static final Log log = LogFactory
			.getLog(StandardAccessLevelService.class);

	@Override
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
			
//			if (log.isDebugEnabled()) {
//				log.debug("Start matching process...");
//			}

//			computeMatching.computeMatchingForAll();
	}		

	public ComputeMatching getComputeMatching() {
		return computeMatching;
	}

	public void setComputeMatching(ComputeMatching computeMatching) {
		this.computeMatching = computeMatching;
	}
}
