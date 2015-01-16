package youapp.frontend.validators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.model.Question;
import youapp.model.Reply;
import youapp.services.QuestionService;

public class ReplyValidator implements Validator {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(ReplyValidator.class);
	
	/**
	 * Provides access to the database.
	 */
	private QuestionService questionService;
	
	public void setQuestionService(QuestionService questionService) {
		if (questionService == null) {
			throw new IllegalArgumentException("Question service must not be null.");
		}
		this.questionService = questionService;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Reply.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (questionService == null) {
			if (log.isDebugEnabled()) {
				log.debug("Question service is null.");
			}
			throw new IllegalStateException("Question service is null.");
		}
		
		// Perform general checks.
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "answerIndex", "error.answerIndex.empty", "Please choose an answer.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "importance", "error.importance.empty", "Please choose an importance weight.");
		
		// Perform specific checks.
		Reply reply = (Reply)target;
		if (reply == null) {
			if (log.isDebugEnabled()) {
				log.debug("Reply form is null.");
			}
			// Global error.
			if (!errors.hasGlobalErrors()) {
				errors.reject("error.global.emptyForm", "An error occurred while processing the form data. Please try again later.");
			}
		} else {
			// Check question id.
			Long questionId = reply.getQuestionId();
			Question question = null;
			int nofAnswers = 0;
			if (questionId != null) {
				try {
					question = questionService.getQuestionById(questionId);
					if (question == null) {
						if (log.isDebugEnabled()) {
							log.debug("Question is null.");
						}
						// Global error.
						if (!errors.hasGlobalErrors()) {
							errors.reject("error.global.dataAccess", "An error occurred while accessing the database. Please try again later.");
						}
					} else {
						String whitespace = "[\\p{Z}]*";
						if ((question.getAnswerA() != null) && !(question.getAnswerA().matches(whitespace))) {
							nofAnswers = nofAnswers + 1;
						}
						if ((question.getAnswerB() != null) && !(question.getAnswerB().matches(whitespace))) {
							nofAnswers = nofAnswers + 1;
						}
						if ((question.getAnswerC() != null) && !(question.getAnswerC().matches(whitespace))) {
							nofAnswers = nofAnswers + 1;
						}
						if ((question.getAnswerD() != null) && !(question.getAnswerD().matches(whitespace))) {
							nofAnswers = nofAnswers + 1;
						}
						if ((question.getAnswerE() != null) && !(question.getAnswerE().matches(whitespace))) {
							nofAnswers = nofAnswers + 1;
						}
					}
				} catch (DataAccessLayerException e) {
					if (log.isErrorEnabled()) {
						log.error("Error during database access: " + e.getMessage());
					}
					// Global error.
					if (!errors.hasGlobalErrors()) {
						errors.reject("error.global.dataAccess", "An error occurred while accessing the database. Please try again later.");
					}
				} catch (ModelException e) {
					if (log.isErrorEnabled()) {
						log.error("Error during database access: " + e.getMessage());
					}
					// Global error.
					if (!errors.hasGlobalErrors()) {
						errors.reject("error.global.dataAccess", "An error occurred while accessing the database. Please try again later.");
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Question id is null.");
				}
				if (!errors.hasFieldErrors("questionId")) {
					errors.rejectValue("questionId", "error.questionId.null", "Question could not be determined. Please try again later.");
				}
			}
			// Check chosen answer.
			Integer answerIndex = reply.getAnswerIndex();
			if (answerIndex != null) {
				if (!errors.hasFieldErrors("answerIndex") && !((answerIndex >= 0) && (answerIndex < 5))) {
					errors.rejectValue("answerIndex", "error.answerIndex.range", "Answer index not in range.");
				} else {
					if ((question != null) && (nofAnswers > 1)) {
						if (!errors.hasFieldErrors("answerIndex") && !((answerIndex >= 0) && (answerIndex < nofAnswers))) {
							errors.rejectValue("answerIndex", "error.answerIndex.range", "Answer index not in range.");
						}
					} else {
						if (log.isErrorEnabled()) {
							log.error("Question is null.");
						}
						// Global error.
						if (!errors.hasGlobalErrors()) {
							errors.reject("error.global.dataAccess", "An error occurred while accessing the database. Please try again later.");
						}
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Answer index is null.");
				}
				if (!errors.hasFieldErrors("answerIndex")) {
					errors.rejectValue("answerIndex", "error.answerIndex.empty", "Please choose an answer.");
				}
			}
			
			// Check importance level.
			Integer importance = reply.getImportance();
			if (importance == null) /*{
				// No answer expected / all answers expected --> must be irrelevant.
				if (((nofExpected == 0) || (nofExpected == nofAnswers)) && (importance != 0)) {
					if (!errors.hasFieldErrors("importance")) {
						errors.rejectValue("importance", "error.importance.irrelevantRequired", "Importance weight must be set to irrelevant.");
					}
				}
				// Some answers expected --> must not be irrelevant.
				if (((nofExpected > 0) && (nofExpected < nofAnswers)) && (importance == 0)) {
					if (!errors.hasFieldErrors("importance")) {
						errors.rejectValue("importance", "error.importance.irrelevantForbidden", "Importance weight must not be set to irrelevant.");
					}
				}
			} else */{
				if (log.isDebugEnabled()) {
					log.debug("Importance is null.");
				}
				if (!errors.hasFieldErrors("importance")) {
					errors.rejectValue("importance", "error.importance.empty", "Please choose an importance weight.");
				}
			}
		}
	}

}
