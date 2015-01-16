package youapp.frontend.controllers;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import youapp.exception.GenericException;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.frontend.forms.QuestionForm;
import youapp.frontend.interceptors.InterceptorUtils;
import youapp.frontend.validators.QuestionFormValidator;
import youapp.model.Name;
import youapp.model.Question;
import youapp.services.PersonService;
import youapp.services.QuestionService;

@Controller
@RequestMapping("/*")
public class QuestionsController extends ExceptionHandlingController {

	public static final Long personId = 1L;
	
	public static final Integer QUESTION_RESULT_SIZE = 5;
	
	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(QuestionsController.class);
	
	/**
	 * Provides access to the database.
	 */
	private QuestionService questionService;
	
	/**
	 * Provides access to the database.
	 */
	private PersonService personService;
	
	/**
	 * Provides access to localized strings.
	 */
	private MessageSource messageSource;
	
	@Autowired
	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}
	
	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@RequestMapping(value = "questions/create", method = RequestMethod.GET)
	public ModelAndView create(HttpSession session) {
		ModelAndView mav = new ModelAndView("questions/create");
		QuestionForm formBacking = new QuestionForm();
		mav.addObject("questionForm", formBacking);
		return mav;
	}
	
	@RequestMapping(value = "questions/create",method = RequestMethod.POST)
	public ModelAndView create(@ModelAttribute("questionForm") QuestionForm formBacking, BindingResult result, HttpSession session) throws GenericException {
		// Get person id.
		if (session == null) {
			if (log.isDebugEnabled()) {
				log.debug("Session is null.");
			}
			throw new GenericException("Session is null.");
		}
		Long personId = (Long)session.getAttribute("personId");
		if (personId == null) {
			if (log.isDebugEnabled()) {
				log.debug("Person id is null. Cleaning up session data.");
			}
			InterceptorUtils.clearSessionAttributesComplete(session);
			throw new GenericException("Person id is null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Person id: " + personId);
		}
		
		// Check form backing.
		if (formBacking == null) {
			if (log.isDebugEnabled()) {
				log.debug("Form backing is null.");
			}
			throw new GenericException("Error while retrieving form data: No form data available.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Author id: " + formBacking.getAuthorId());
			log.debug("Author name: " + formBacking.getAuthorName());
			log.debug("Author nick name: " + formBacking.getAuthorNick());
			log.debug("Created: " + formBacking.getCreated());
			log.debug("Question: " + formBacking.getQuestion());
			log.debug("Answer A: " + formBacking.getAnswerA());
			log.debug("Answer B: " + formBacking.getAnswerB());
			log.debug("Answer C: " + formBacking.getAnswerC());
			log.debug("Answer D: " + formBacking.getAnswerD());
		}
		
		// Validate user input.
		QuestionFormValidator validator = new QuestionFormValidator();
		validator.validate(formBacking, result);
		if (result.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("Found errors in form data.");
			}
			// Create model and view.
			ModelAndView mav = new ModelAndView("questions/create");			
			mav.addObject("questionForm", formBacking);
			return mav;
		}
		
		try {
			// Fill form backing.
			formBacking.setAuthorId(personId);
			Name authorName = personService.getName(personId);
			if (authorName != null) {
				formBacking.setAuthorNick(authorName.getNickName());
				formBacking.setAuthorName(authorName.getFirstName() + " " + authorName.getLastName());
			}
			formBacking.setCreated(new Date(System.currentTimeMillis()));
			
			// Create question.
			Question question = new Question();
			question.setAuthorId(personId);
			question.setAuthorName(authorName);
//			question.setAuthorFirstName(authorName.getFirstName());
//			question.setAuthorLastName(authorName.getLastName());
//			question.setAuthorNickName(authorName.getNickName());
			question.setCreated(formBacking.getCreated());
			question.setQuestion(formBacking.getQuestion());
			question.setAnswerA(formBacking.getAnswerA());
			question.setAnswerB(formBacking.getAnswerB());
			question.setAnswerC(formBacking.getAnswerC());
			question.setAnswerD(formBacking.getAnswerD());
			
			// Check whether similar questions already exist.
			List<Question> similar = questionService.getSimilarQuestions(question);
			if (similar != null && similar.size() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("Similar questions found!");
				}
				// Prepare similar questions for displaying.
				List<QuestionForm> formBackings = new LinkedList<QuestionForm>();
				for (Question q : similar) {
					formBackings.add(ControllerUtils.fillQuestionFormBacking(q));
				}
				ModelAndView mav = new ModelAndView("questions/confirm");
				mav.addObject("questionForm", formBacking);
				mav.addObject("questionToCreate", ControllerUtils.fillQuestionFormBacking(question));
				mav.addObject("questionsList", formBackings);
				return mav;
			}
			
			// If no similar question was found, create the question.
			Long questionId = questionService.createQuestion(question);
			formBacking.setQuestionId(questionId);
			
			// Return model.
			ModelAndView mav = new ModelAndView("questions/show");
			mav.addObject("questionForm", formBacking);
			return mav;
			
		} catch (DataAccessLayerException e) {
			if (log.isErrorEnabled()) {
				log.error("Error during database access: " + e.getMessage());
			}
	        throw new GenericException("Creation of question failed because of an error during database access. Please try to create the question again later.");
		} catch (ModelException e) {
			if (log.isErrorEnabled()) {
				log.error("Error during database access: " + e.getMessage());
			}
	        throw new GenericException("Creation of question failed because of an error during database access. Please try to create the question again later.");
		}
	}
	
	@RequestMapping(value = "questions/confirm", params = "create", method = RequestMethod.POST)
	public ModelAndView confirm(@ModelAttribute("questionForm") QuestionForm formBacking, BindingResult result, HttpSession session, HttpServletRequest request) throws GenericException {
		if (log.isDebugEnabled()) {
			log.debug("User decided to keep the duplicate question.");
		}
		// Get person id.
		if (session == null) {
			if (log.isDebugEnabled()) {
				log.debug("Session is null.");
			}
			throw new GenericException("Session is null.");
		}
		Long personId = (Long)session.getAttribute("personId");
		if (personId == null) {
			if (log.isDebugEnabled()) {
				log.debug("Person id is null. Cleaning up session data.");
			}
			InterceptorUtils.clearSessionAttributesComplete(session);
			throw new GenericException("Person id is null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Person id: " + personId);
		}
		
		// Get locale.
		Locale preferredLocale = null;
		if (request != null) {
			preferredLocale = request.getLocale();
		}
		
		// Check form backing.
		if (formBacking == null) {
			if (log.isDebugEnabled()) {
				log.debug("Form backing is null.");
			}
			throw new GenericException("Error while retrieving form data: No form data available.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Author id: " + formBacking.getAuthorId());
			log.debug("Author name: " + formBacking.getAuthorName());
			log.debug("Author nick name: " + formBacking.getAuthorNick());
			log.debug("Created: " + formBacking.getCreated());
			log.debug("Question: " + formBacking.getQuestion());
			log.debug("Answer A: " + formBacking.getAnswerA());
			log.debug("Answer B: " + formBacking.getAnswerB());
			log.debug("Answer C: " + formBacking.getAnswerC());
			log.debug("Answer D: " + formBacking.getAnswerD());
		}
		
		// Validate user input.
		QuestionFormValidator validator = new QuestionFormValidator();
		validator.validate(formBacking, result);
		if (result.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("Found errors in form data.");
			}
			// Create model and view.
			ModelAndView mav = new ModelAndView("questions/show");
			mav.addObject("discardedMessage", messageSource.getMessage("error.question.discarded.invalid", null, "Your question has been discarded due to errors in the question data.", preferredLocale));
			return mav;
		}
		
		try {
			// Fill form backing.
			formBacking.setAuthorId(personId);
			Name authorName = personService.getName(personId);
			if (authorName != null) {
				formBacking.setAuthorNick(authorName.getNickName());
				formBacking.setAuthorName(authorName.getFirstName() + " " + authorName.getLastName());
			}
			formBacking.setCreated(new Date(System.currentTimeMillis()));
			
			// Create question.
			Question question = new Question();
			question.setAuthorId(personId);
			question.setAuthorName(authorName);
//			question.setAuthorFirstName(authorName.getFirstName());
//			question.setAuthorLastName(authorName.getLastName());
//			question.setAuthorNickName(authorName.getNickName());
			question.setCreated(formBacking.getCreated());
			question.setQuestion(formBacking.getQuestion());
			question.setAnswerA(formBacking.getAnswerA());
			question.setAnswerB(formBacking.getAnswerB());
			question.setAnswerC(formBacking.getAnswerC());
			question.setAnswerD(formBacking.getAnswerD());
			
			// Create question.
			Long questionId = questionService.createQuestion(question);
			formBacking.setQuestionId(questionId);
			
			// Return model.
			ModelAndView mav = new ModelAndView("questions/show");
			mav.addObject("questionForm", formBacking);
			return mav;
			
		} catch (DataAccessLayerException e) {
			if (log.isErrorEnabled()) {
				log.error("Error during database access: " + e.getMessage());
			}
	        throw new GenericException("Creation of question failed because of an error during database access. Please try to create the question again later.");
		} catch (ModelException e) {
			if (log.isErrorEnabled()) {
				log.error("Error during database access: " + e.getMessage());
			}
	        throw new GenericException("Creation of question failed because of an error during database access. Please try to create the question again later.");
		}
	}
	
	@RequestMapping(value = "questions/confirm", params = "discard", method = RequestMethod.POST)
	public ModelAndView confirm(HttpSession session, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("User decided to discard the duplicate question.");
		}
		
		// Get locale.
		Locale preferredLocale = null;
		if (request != null) {
			preferredLocale = request.getLocale();
		}
		
		// Return model.
		ModelAndView mav = new ModelAndView("questions/show");
		mav.addObject("discardedMessage", messageSource.getMessage("error.question.discarded.exists", null, "Your question has been discarded because a smiliar question already exists.", preferredLocale));
		return mav;
	}
	
	@RequestMapping(value = "questions/showAll", method = RequestMethod.GET)
	public ModelAndView showAll(HttpSession session) throws GenericException {
		// Get person id.
		if (session == null) {
			if (log.isDebugEnabled()) {
				log.debug("Session is null.");
			}
			throw new GenericException("Session is null.");
		}
		Long personId = (Long)session.getAttribute("personId");
		if (personId == null) {
			if (log.isDebugEnabled()) {
				log.debug("Person id is null. Cleaning up session data.");
			}
			InterceptorUtils.clearSessionAttributesComplete(session);
			throw new GenericException("Person id is null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Person id: " + personId);
		}
		
		List<Question> createdQuestions = null;
		List<QuestionForm> formBackings = new LinkedList<QuestionForm>();
		try {
			createdQuestions = questionService.getQuestionsByAuthor(personId);
			for (Question q : createdQuestions) {
				formBackings.add(ControllerUtils.fillQuestionFormBacking(q));
			}
			ModelAndView mav = new ModelAndView("questions/showAll");
			mav.addObject("questionsList", formBackings);
			return mav;
			
		} catch (DataAccessLayerException e) {
			if (log.isErrorEnabled()) {
				log.error("Error during database access: " + e.getMessage());
			}
	        throw new GenericException("Error during database access. Please try again later.");
		} catch (ModelException e) {
			if (log.isErrorEnabled()) {
				log.error("Error during database access: " + e.getMessage());
			}
	        throw new GenericException("Error during database access. Please try again later.");
		}
	}
	
}
