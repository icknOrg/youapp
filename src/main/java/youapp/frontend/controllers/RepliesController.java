package youapp.frontend.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import youapp.exception.GenericException;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.frontend.interceptors.InterceptorUtils;
import youapp.frontend.validators.ReplyValidator;
import youapp.model.Importance;
import youapp.model.Question;
import youapp.model.Reply;
import youapp.services.QuestionService;

@Controller
@RequestMapping(value = "/*")
public class RepliesController extends ExceptionHandlingController
{

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(RepliesController.class);

    /**
     * Provides access to the database.
     */
    private QuestionService questionService;

    @Autowired
    public void setQuestionService(QuestionService questionService)
    {
        this.questionService = questionService;
    }
//    private AsyncTaskExecutor taskExecutor;
//	
//    @Autowired
//	public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
//		this.taskExecutor = taskExecutor;
//	}

    @RequestMapping(value = "replies/create", method = RequestMethod.GET)
    public ModelAndView create(HttpSession session) throws GenericException
    {
    	// Get person id and question index.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        Long questionId = null;
        try
        {
            List<Question> question = questionService.fetchNextQuestions(personId, 1);

            if (question.isEmpty())
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No unanswered questions available.");
                }
                ModelAndView mav = new ModelAndView("replies/create");
    			mav.addObject("hasQuestions", false);
    			return mav;
            }

            questionId = question.get(0).getId();
        }
        catch (DataAccessLayerException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException("Error during database access. Please try again later.");
        }
        catch (ModelException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException("Error during database access. Please try again later.");
        }

		ModelAndView mav = getEditView(personId, questionId, new ModelAndView("replies/create"));
		mav.addObject("hasQuestions", true);
		return mav;
    }

    @RequestMapping(value = "replies/reply", method = RequestMethod.GET)
    public ModelAndView reply(HttpSession session) throws GenericException
    {
        // Get person id and question index.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        Long questionId = null;
        try
        {
            List<Question> question = questionService.fetchNextQuestions(personId, 1);

            if (question.isEmpty())
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No unanswered questions available.");
                }
                return new ModelAndView("empty");
            }

            questionId = question.get(0).getId();
        }
        catch (DataAccessLayerException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException("Error during database access. Please try again later.");
        }
        catch (ModelException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException("Error during database access. Please try again later.");
        }

        return getEditView(personId, questionId, new ModelAndView("replies/edit"));
    }

    @RequestMapping(value = "replies/reply", method = RequestMethod.POST)
    public ModelAndView reply(@ModelAttribute("reply")
    Reply reply, BindingResult result, HttpSession session) throws GenericException
    {
        // save the reply
        ModelAndView mav = saveReply(reply, result, session);

        // if the return is null, then it's everything fine, otherwise it
        // returns the error-mav
        if (mav == null)
        {
            // Show next question!
            return new ModelAndView("redirect:/replies/reply.html");
        }
        else
        {
            return mav;
        }
    }

    @RequestMapping(value = "replies/skip", method = RequestMethod.GET)
    public ModelAndView skip(@RequestParam(value = "selectedquestion")
    Long questionId, HttpSession session) throws GenericException
    {
        // save the reply
        ModelAndView mav = skipQuestion(questionId, session);

        // if the return is null, then it's everything fine, otherwise it
        // returns the error-ModelAndView
        if (mav == null)
        {
            // Show next question!
            return new ModelAndView("redirect:/replies/reply.html");
        }
        else
        {
            return mav;
        }
    }

    @RequestMapping(value = "replies/unskip", method = RequestMethod.POST)
    public ModelAndView unskip(@ModelAttribute("reply")
    Reply reply, BindingResult result, HttpSession session) throws GenericException
    {
        // save the reply
        ModelAndView mav = saveReply(reply, result, session);

        // if the return is null, then it's everything fine, otherwise it
        // returns the error-ModelAndView
        if (mav == null)
        {
            // show the updated answers of the question
            return new ModelAndView("empty");
        }
        else
        {
            return mav;
        }
    }

    @RequestMapping(value = "replies/showAnswer", method = RequestMethod.GET)
    public ModelAndView showAnswer(@RequestParam(value = "selectedquestion")
    Long questionId, HttpSession session) throws GenericException
    {
        // Get person id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        Question question = null;
        Reply reply = null;
        try
        {
            // Get question and reply from database.
            question = questionService.getQuestionById(questionId);
            reply = questionService.getReplyById(personId, questionId);

            if (question == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No question with the given id in the database found: " + questionId);
                }
                throw new GenericException(
                    "Invalid skipped question selected. Please try to answer the skipped question again.");
            }

            if (reply == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No reply with the given id in the database found.");
                }

                throw new GenericException("Invalid question selected. Please try again later.");
            }

            // Build model.
            ModelAndView mav = new ModelAndView("replies/showAnswer");
            mav.addObject("question", question);
            mav.addObject("reply", reply);

            return mav;
        }
        catch (DataAccessLayerException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException("Error during database access. Please try again later.");
        }
        catch (ModelException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException("Error during database access. Please try again later.");
        }
    }

    @RequestMapping(value = "replies/showAll", method = RequestMethod.GET)
    public ModelAndView showAll(HttpSession session) throws GenericException
    {
        // Get person id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        List<Reply> replies = null;
        Map<Long, Question> questions = new HashMap<Long, Question>();
        try
        {
            if (log.isDebugEnabled())
            {
                log.debug("Retrieving replies by person with id: " + personId);
            }
            replies = questionService.getRepliesByPerson(personId, false, false, null, false);
            for (Reply r : replies)
            {
                if (log.isDebugEnabled())
                {
                    log.debug(">>> Retrieving question with id: " + r.getQuestionId());
                }
                questions.put(r.getQuestionId(), questionService.getQuestionById(r.getQuestionId()));
            }

            ModelAndView mav = new ModelAndView("replies/showAll");
            mav.addObject("repliesList", replies);
            mav.addObject("questionMap", questions);
            return mav;
        }
        catch (Exception e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException("Error during database accesss. Please try again later.");
        }
    }

    @RequestMapping(value = "replies/showSkipped", method = RequestMethod.GET)
    public ModelAndView showSkipped(HttpSession session) throws GenericException
    {
        // Get person id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        List<Reply> replies = null;
        Map<Long, Question> questions = new HashMap<Long, Question>();
        try
        {
            if (log.isDebugEnabled())
            {
                log.debug("Retrieving replies by person with id: " + personId);
            }
            replies = questionService.getRepliesByPerson(personId, false, true, false, false);
            for (Reply r : replies)
            {
                if (log.isDebugEnabled())
                {
                    log.debug(">>> Retrieving question with id: " + r.getQuestionId());
                }
                questions.put(r.getQuestionId(), questionService.getQuestionById(r.getQuestionId()));
            }

            ModelAndView mav = new ModelAndView("replies/showSkipped");
            mav.addObject("repliesList", replies);
            mav.addObject("questionMap", questions);
            return mav;
        }
        catch (Exception e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException("Error during database accesss. Please try again later.");
        }
    }

    @RequestMapping(value = "replies/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam(value = "selectedquestion")
    Long questionId, HttpSession session) throws GenericException
    {
        // Get person id and question index.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        return getEditView(personId, questionId, new ModelAndView("replies/edit"));
    }

    @RequestMapping(value = "replies/edit", method = RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute("reply")
    Reply reply, BindingResult result, HttpSession session) throws GenericException
    {
        // save the reply
        ModelAndView mav = saveReply(reply, result, session);

        // if the return is null, then it's everything fine, otherwise it
        // returns the error-ModelAndView
        if (mav == null)
        {
            // show the updated answers of the question
            return showAnswer(reply.getQuestionId(), session);
        }
        else
        {
            return mav;
        }
    }

    @RequestMapping(value = "replies/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam(value = "selectedquestion")
    Long questionId, HttpSession session) throws GenericException
    {
        // save the reply
        ModelAndView mav = skipQuestion(questionId, session);

        // if the return is null, then it's everything fine, otherwise it
        // returns the error-ModelAndView
        if (mav == null)
        {
            // Return empty page
            return new ModelAndView("empty");
        }
        else
        {
            return mav;
        }
    }

    /**
     * Validate and save the reply from the in the database
     * 
     * @param Reply object from the form
     * @param BindingResult
     * @param session
     * @return return null if everything is fine, otherwise return the
     *         Error-ModelAndView
     * @throws GenericException 
     */
    public ModelAndView saveReply(Reply reply, BindingResult result, HttpSession session) throws GenericException
    {
        // Get person and question id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }

        // Check reply.
        if (reply == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Form backing is null.");
            }
            throw new GenericException(
                "Error while retrieving form data: No form data available.");
        }

        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }

        Long questionId = reply.getQuestionId();
        if (questionId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Question id is null.");
            }
            throw new GenericException("Question id is null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
            log.debug("Question id: " + questionId);
        }

        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + reply.getPersonId());
            log.debug("Question id: " + reply.getQuestionId());
            log.debug("Critical: " + reply.getCritical());
            log.debug("Private: " + reply.getInPrivate());
            log.debug("Skipped: " + reply.getSkipped());
            log.debug("Last update: " + reply.getLastUpdate());
            log.debug("Importance: " + reply.getImportance());
            log.debug("Explanation: " + reply.getExplanation());
            log.debug("Answer A: " + reply.getAnswerA());
            log.debug("Answer B: " + reply.getAnswerB());
            log.debug("Answer C: " + reply.getAnswerC());
            log.debug("Answer D: " + reply.getAnswerD());
            log.debug("Answer E: " + reply.getAnswerE());
        }

        // set reply not as skipped
        reply.setSkipped(false);

        // Validate user input.
        ReplyValidator validator = new ReplyValidator();
        validator.setQuestionService(questionService);
        validator.validate(reply, result);
        if (result.hasErrors())
        {
            if (log.isDebugEnabled())
            {
                log.debug("Found errors in form data.");
            }

            // Create model and view.
            ModelAndView mav = new ModelAndView("replies/edit");

            // Get the question and importance levels from database.
            Question question = null;
            List<Importance> weights = null;
            try
            {
                weights = questionService.getAllImportanceLevels();
                question = questionService.getQuestionById(questionId);
                if ((question == null) || weights.isEmpty())
                {
                    if (log.isErrorEnabled())
                    {
                        log.debug("Question or list of importance levels null.");
                    }
                    throw new GenericException(
                        "Error during database access. Please try again later.");
                }

                // Build model.
                mav.addObject("replyForm", reply);
                mav.addObject("weights", weights);
                mav.addObject("question", question);

                return mav;
            }
            catch (DataAccessLayerException e)
            {
                if (log.isErrorEnabled())
                {
                    log.error("Error during database access: " + e.getMessage());
                }
                throw new GenericException(
                    "Error during database access. Please try again later.");
            }
            catch (ModelException e)
            {
                if (log.isErrorEnabled())
                {
                    log.error("Error during database access: " + e.getMessage());
                }
                throw new GenericException(
                    "Error during database access. Please try again later.");
            }
        }

        // Save or update reply in database.
        try
        {
            if (questionService.existsReply(reply.getPersonId(), reply.getQuestionId()))
            {
                // Reply is already existent --> update!
                if (log.isDebugEnabled())
                {
                    log.debug("Updating reply by person with id " + reply.getPersonId() + " for question with id "
                        + reply.getQuestionId());
                }
                questionService.updateReply(reply);
//                //compute matching
//                taskExecutor.submit(new ComputeMatchingJob(reply.getPersonId()));
                
            }
            else
            {
                // Reply is new --> create!
                if (log.isDebugEnabled())
                {
                    log.debug("Creating reply by person with id " + reply.getPersonId() + " for question with id "
                        + reply.getQuestionId());
                }
                questionService.createReply(reply);
//                //compute matching
//                taskExecutor.submit(new ComputeMatchingJob(reply.getPersonId()));
            }

        }
        catch (Exception e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException(
                "Creating reply failed because of an error during database access. Please try to reply again later.");
        }
        return null;
    }

    /**
     * Get the edit form of a reply. If a reply doesn't exist yet, create a new
     * reply form
     * 
     * @param The Id of the Person
     * @param The Id of the question
     * @param The ModelAndView for returning
     * @return Return the form. At a error, return the Error-ModelAndView
     * @throws GenericException 
     */
    public ModelAndView getEditView(Long personId, Long questionId, ModelAndView mav) throws GenericException
    {
        Question question = null;
        List<Importance> weights = null;
        Reply reply = null;
        try
        {
            // Get question and importance levels from database.
            weights = questionService.getAllImportanceLevels();
            question = questionService.getQuestionById(questionId);
            reply = questionService.getReplyById(personId, questionId);

            if (question == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No question with the given id in the database found: " + questionId);
                }
                throw new GenericException(
                    "Invalid question selected. Please try to answer the question again.");
            }

            if (reply == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No reply with the given id in the database found --> created a new reply");
                }

                // create a new reply if no reply already exists.
                reply = new Reply();
                reply.setQuestionId(questionId);
                reply.setQuestion(question);
                reply.setPersonId(personId);
                reply.setInPrivate(true);
                reply.setCritical(false);
                reply.setSkipped(false);
            }
            
            // Set Importance to null, so no importance level is pre-choosen in a skipped question
            if(reply.getSkipped()) {
                reply.setImportance(null);
            }

            // set the LastUpdate to the actual date, because we'll edit it.
            reply.setLastUpdate(new Date(System.currentTimeMillis()));

            // Build model.
            mav.addObject("weights", weights);
            mav.addObject("question", question);
            mav.addObject("reply", reply);

            return mav;
        }
        catch (DataAccessLayerException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException("Error during database access. Please try again later.");
        }
        catch (ModelException e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException("Error during database access. Please try again later.");
        }
    }

    /**
     * Skip a question.
     * 
     * @param questionId
     * @param session
     * @return return null if everything is fine, otherwise return the
     *         Error-ModelAndView
     * @throws GenericException 
     */
    public ModelAndView skipQuestion(Long questionId, HttpSession session) throws GenericException
    {
        // Get person id and question index.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if(questionId == null){
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Question is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        // Create skipped reply and save it to the database.
        Reply reply = new Reply();
        reply.setQuestionId(questionId);
        reply.setPersonId(personId);
        reply.setCritical(false);
        reply.setInPrivate(false);
        reply.setSkipped(true);
        reply.setLastUpdate(new Date(System.currentTimeMillis()));
        reply.setImportance(0);
        reply.setExplanation(null);
        reply.setAnswerA(false);
        reply.setAnswerB(false);
        reply.setAnswerC(false);
        reply.setAnswerD(false);
        reply.setAnswerE(false);

        // Save or update reply in database.
        try
        {
            if (questionService.existsReply(reply.getPersonId(), reply.getQuestionId()))
            {
                // Reply is already existent --> update! if
                if (log.isDebugEnabled())
                {
                    log.debug("Updating reply by person with id " + reply.getPersonId() + " for question with id "
                        + reply.getQuestionId());
                }
                questionService.updateReply(reply);
            }
            else
            {
                // Reply is new --> create!
                if (log.isDebugEnabled())
                {
                    log.debug("Creating reply by person with id " + reply.getPersonId() + " for question with id "
                        + reply.getQuestionId());
                }
                questionService.createReply(reply);
            }

        }
        catch (Exception e)
        {
            if (log.isErrorEnabled())
            {
                log.error("Error during database access: " + e.getMessage());
            }
            throw new GenericException(
                "Creating reply failed because of an error during database access. Please try to reply again later.");
        }

        return null;
    }
}
