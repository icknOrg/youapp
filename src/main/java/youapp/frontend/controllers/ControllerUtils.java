package youapp.frontend.controllers;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import youapp.frontend.forms.QuestionForm;
import youapp.model.Name;
import youapp.model.Person;
import youapp.model.Question;

import com.restfb.types.User;

public class ControllerUtils {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(ControllerUtils.class);
	
	public static Character parseGender(String gender) {
		if (gender == null) {
			throw new IllegalArgumentException("Gender must not be null.");
		}
		if (gender.length() == 0) {
			throw new IllegalArgumentException("Gender must not be empty.");
		}
		if ("female".equals(gender) || "weiblich".equals(gender) || "F".equals(gender) ||
				"W".equals(gender) || "f".equals(gender) || "w".equals(gender)) {
			return 'F';
		}
		if ("male".equals(gender) || "m�nnlich".equals(gender) ||
				"M".equals(gender) || "m".equals(gender)) {
			return 'M';
		}
		return null;
	}
	
	/**
	 * Converts a string containing a birthday to a date.
	 * @param birthday date expected to have the following structure: MM/DD/YYYY.
	 * @return a date with the following format: YYYY-MM-DD
	 */
	public static Date parseBirthday(String birthday) {
		Calendar c = Calendar.getInstance();
		
		// Set birthday to "1900-01-01".
		c.set(Calendar.YEAR, 1900);
		c.set(Calendar.MONTH, 0); // Month starts at 0 (whose idea was this??).
		c.set(Calendar.DATE, 1);
		
		if (birthday == null) {
			return c.getTime();
		}
		else {
			String[] parts = birthday.split("/");
			if (parts.length == 2) {
				// No year available.
				if ((parts[0].length() == 2) && (parts[1].length() == 2)) {
					// Set birthday to "1900-MM-DD".
					try {
						int month = Integer.parseInt(parts[0]);
						c.set(Calendar.MONTH, month - 1); // Month starts at 0.
					} catch (NumberFormatException e) {
						if (log.isErrorEnabled()) {
							log.error(e.getMessage());
						}
						c.set(Calendar.MONTH, 0);
					}
					try {
						int day = Integer.parseInt(parts[1]);
						c.set(Calendar.DATE, day); // Does not start at 0.
					} catch (NumberFormatException e) {
						if (log.isErrorEnabled()) {
							log.error(e.getMessage());
						}
						c.set(Calendar.DATE, 1);
					}
					return c.getTime();
				} else {
					return c.getTime();
				}
			} else if (parts.length == 3) {
				if ((parts[2].length() == 4) && (parts[0].length() == 2) && (parts[1].length() == 2)) {
					// Set birthday to "YYYY-MM-DD".
					try {
						int year = Integer.parseInt(parts[2]);
						c.set(Calendar.YEAR, year);
					} catch (NumberFormatException e) {
						if (log.isErrorEnabled()) {
							log.error(e.getMessage());
						}
						c.set(Calendar.YEAR, 1900);
					}
					try {
						int month = Integer.parseInt(parts[0]);
						c.set(Calendar.MONTH, month - 1); // Month starts at 0.
					} catch (NumberFormatException e) {
						if (log.isErrorEnabled()) {
							log.error(e.getMessage());
						}
						c.set(Calendar.MONTH, 0);
					}
					try {
						int day = Integer.parseInt(parts[1]);
						c.set(Calendar.DATE, day);
					} catch (NumberFormatException e) {
						if (log.isErrorEnabled()) {
							log.error(e.getMessage());
						}
						c.set(Calendar.DATE, 1);
					}
					return c.getTime();
				}
			}
		}
		return c.getTime();
	}
	
	public static Person fillPerson(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Filling up person backing form bean with facebook data.");
		}
		Person person = new Person();
		Long uId = null;
		try {
			uId = Long.parseLong(user.getId());
		} catch (NumberFormatException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
			return null;
		}
		person.setFbId(uId);
		person.setFirstName(user.getFirstName());
		person.setLastName(user.getLastName());
		if (user.getGender() != null) {
			person.setGender(parseGender(user.getGender()).toString());
		}
		if (user.getBirthday() != null) {
			person.setBirthday(parseBirthday(user.getBirthday()));
		}
		return person;
	}
	
	public static QuestionForm fillQuestionFormBacking(Question question) {
		if (question == null) {
			throw new IllegalArgumentException("Question must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Filling up question backing form bean with database data.");
		}
		QuestionForm questionBacking = new QuestionForm();
		questionBacking.setQuestionId(question.getId());
		questionBacking.setAuthorId(question.getAuthorId());
		Name authorName = question.getAuthorName();
		questionBacking.setAuthorName(authorName.getFirstName() + " " + authorName.getLastName());
		questionBacking.setAuthorNick(authorName.getNickName());
		Calendar c = Calendar.getInstance();
		c.setTime(question.getCreated());
		questionBacking.setCreated(question.getCreated());
		questionBacking.setQuestion(question.getQuestion());
		questionBacking.setAnswerA(question.getAnswerA());
		questionBacking.setAnswerB(question.getAnswerB());
		questionBacking.setAnswerC(question.getAnswerC());
		questionBacking.setAnswerD(question.getAnswerD());
		return questionBacking;
	}
	
}
