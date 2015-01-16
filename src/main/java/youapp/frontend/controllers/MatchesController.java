package youapp.frontend.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import youapp.MatchMaking.ComputeMatching;
import youapp.MatchMaking.ComputeMatchingJob;
import youapp.exception.GenericException;
import youapp.frontend.interceptors.InterceptorUtils;
import youapp.model.MatchingScore;
import youapp.services.MatchingService;
import youapp.services.PersonBlockerService;
import youapp.services.SoulmatesService;

@Controller
@RequestMapping("/*")
public class MatchesController
    extends ExceptionHandlingController
{

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(MatchesController.class);

    /**
     * Provides access to the database.
     */
    private MatchingService matchingService;

    @Autowired
    public void setMatchingService(MatchingService matchingService)
    {
        this.matchingService = matchingService;
    }

    private SoulmatesService soulmatesService;

    @Autowired
    public void setSoulmatesService(SoulmatesService soulmatesService)
    {
        this.soulmatesService = soulmatesService;
    }

    private PersonBlockerService personBlockerService;

    @Autowired
    public void setPersonBlockerService(PersonBlockerService personBlockerService)
    {
        this.personBlockerService = personBlockerService;    }
    
    
    private AsyncTaskExecutor taskExecutor;
	
    @Autowired
	public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
    
    private ComputeMatching computeMatching;
    
    @Autowired
	public void setComputeMatching(ComputeMatching computeMatching) {
		this.computeMatching = computeMatching;
	}

    @RequestMapping(value = "matches/showFindSoulmates", method = RequestMethod.GET)
    public ModelAndView showFindSoulmates(HttpSession session) throws Exception
    {
        // Get person id from session.
        if (session == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }
        
        //compute matching
        if(session.getAttribute("matchesTask") != null){
        	Future<?> future = (Future<?>)session.getAttribute("matchesTask");
       		future.get();
        }else{
        	Future<?> future = taskExecutor.submit(new ComputeMatchingJob(personId,computeMatching));
			session.setAttribute("matchesTask", future);	
			future.get();
        }
        
        Integer offset = 0;
        Integer resultSize = 8;

        List<MatchingScore> allMatches =
            filterMatches(matchingService.getBestMatches(personId, null, offset, resultSize, false));
        ModelAndView mav = new ModelAndView("matches/showFindSoulmates");
        mav.addObject("matchesList", allMatches);
        mav.addObject("matchesOffset", offset + resultSize);
        mav.addObject("matchesResultSize", resultSize);
        return mav;
    }

    @RequestMapping(value = "matches/showMatchesList", method = RequestMethod.GET)
    public ModelAndView showAll(@RequestParam(value = "offset", required = true)
    Integer offset, @RequestParam(value = "resultSize", required = true)
    Integer resultSize, HttpSession session) throws Exception
    {
        // Get person id from session.
        if (session == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isErrorEnabled())
            {
                log.error("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        List<MatchingScore> allMatches =
            filterMatches(matchingService.getBestMatches(personId, null, offset, resultSize, false));
        ModelAndView mav;
        if ((allMatches != null) && allMatches.size() > 0)
        {
            mav = new ModelAndView("matches/showMatchesList");
            mav.addObject("matchesList", allMatches);
        }
        else
        {
            mav = new ModelAndView("empty");
        }
        return mav;
    }

    private List<MatchingScore> filterMatches(List<MatchingScore> unfilteredMachtes)
    {
        List<MatchingScore> filteredMatches = new LinkedList<MatchingScore>();

        for (MatchingScore matchingScore : unfilteredMachtes)
        {
            if (!soulmatesService.existsAnyDirection(matchingScore.getSourceId(), matchingScore.getDestinationId())
                && !personBlockerService.blockedAnyDirection(matchingScore.getSourceId(),
                    matchingScore.getDestinationId()))
            {
                filteredMatches.add(matchingScore);
            }
        }

        return filteredMatches;
    }

}
