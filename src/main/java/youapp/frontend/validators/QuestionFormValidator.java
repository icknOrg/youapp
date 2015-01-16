package youapp.frontend.validators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import youapp.frontend.forms.QuestionForm;

public class QuestionFormValidator implements Validator {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(QuestionFormValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return QuestionForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// Perform general checks.
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "question", "error.question.empty", "Question is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "answerA", "error.answerA.empty", "First answer is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "answerB", "error.answerB.empty", "Second answer is required.");
		
		// Perform specific checks.
		QuestionForm questionForm = (QuestionForm)target;
		if (questionForm == null) {
			if (log.isDebugEnabled()) {
				log.debug("Question form is null.");
			}
			// Global error.
			if (!errors.hasGlobalErrors()) {
				errors.reject("error.global.emptyForm", "An error occurred while processing the form data. Please try again later.");
			}
		}
		else {
			// Check answer D.
			String answerD = questionForm.getAnswerD();
			if (answerD != null) {
				// Check if whitespace. If so --> reject.
				if (answerD.length() > 0) {
					if (answerD.matches("[\\p{Z}]*")) {
						// Contains nothing but whitespace!
						if (!errors.hasFieldErrors("answerD")) {
							errors.rejectValue("answerD", "error.answerD.whitespace", "The fourth answer must not contain whitespace only.");
						}
					} else {
						// Contains useful content, therefore answerC must contain useful content too!
						if (!errors.hasFieldErrors("answerC")) {
							ValidationUtils.rejectIfEmptyOrWhitespace(errors, "answerC", "error.answerC.empty", "The third answer must not be empty if the fourth answer is set.");
						}
					}
				}
			}
			
			// Check answer C.
			String answerC = questionForm.getAnswerC();
			if (answerC != null) {
				// Check if whitespace. If so --> reject.
				if (answerC.length() > 0) {
					if (answerC.matches("[\\p{Z}]*")) {
						// Contains nothing but whitespace!
						if (!errors.hasFieldErrors("answerC")) {
							errors.rejectValue("answerC", "error.answerC.whitespace", "The fourth answer must not contain whitespace only.");
						}
					}
				}
			}
		}
	}

}
