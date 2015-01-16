package youapp.dataaccess.dto;

import java.util.Date;

/**
 * Data transfer object for table Question.
 * @author neme
 *
 */
public class QuestionDto {

	private Long id;
	
	private Date created;
	
	private Long author;
	
	private String question;
	
	private String answerA;
	
	private String answerB;
	
	private String answerC;
	
	private String answerD;
	
	private String answerE;
	
	// XXX Think about a good solution for character weights.
	
	/**
	 * Returns this question's id.
	 * @return this question's id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets this question's id.
	 * @param id the id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns the date this question has been created.
	 * @return the date this question has been created.
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Sets the date this question has been created.
	 * @param created the date to set.
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * Returns this question's author id.
	 * @return this question's author id.
	 */
	public Long getAuthor() {
		return author;
	}

	/**
	 * Sets this question's author id.
	 * @param author the author id to set.
	 */
	public void setAuthor(Long author) {
		this.author = author;
	}

	/**
	 * Returns the actual question.
	 * @return the question.
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Sets the actual question.
	 * @param question the question to set.
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * Returns this question's first answer.
	 * @return this question's first answer.
	 */
	public String getAnswerA() {
		return answerA;
	}

	/**
	 * Sets this question's first answer.
	 * @param answerA the first answer to set.
	 */
	public void setAnswerA(String answerA) {
		this.answerA = answerA;
	}

	/**
	 * Returns this question's second answer.
	 * @return this question's second answer.
	 */
	public String getAnswerB() {
		return answerB;
	}

	/**
	 * Sets this question's second answer.
	 * @param answerB the second answer to set.
	 */
	public void setAnswerB(String answerB) {
		this.answerB = answerB;
	}

	/**
	 * Returns this question's third answer.
	 * @return this question's third answer.
	 */
	public String getAnswerC() {
		return answerC;
	}

	/**
	 * Sets this question's third answer.
	 * @param answerC the third answer to set.
	 */
	public void setAnswerC(String answerC) {
		this.answerC = answerC;
	}

	/**
	 * Returns this question's fourth answer.
	 * @return this question's fourth answer.
	 */
	public String getAnswerD() {
		return answerD;
	}

	/**
	 * Sets this question's fourth answer.
	 * @param answerD the fourth answer to set.
	 */
	public void setAnswerD(String answerD) {
		this.answerD = answerD;
	}

	/**
	 * Returns this question's fifth answer.
	 * @return this question's fifth answer.
	 */
	public String getAnswerE() {
		return answerE;
	}

	/**
	 * Sets this question's fifth answer.
	 * @param answerE the fifth answer to set.
	 */
	public void setAnswerE(String answerE) {
		this.answerE = answerE;
	}
	
	@Override
	public String toString() {
		return id + " - " + question;
	}
	
}
