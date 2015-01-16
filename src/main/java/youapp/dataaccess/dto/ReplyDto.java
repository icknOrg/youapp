package youapp.dataaccess.dto;

import java.util.Date;

public class ReplyDto {

	private Long personId;
	
	private Long questionId;
	
	private Boolean skipped;
	
	private Boolean inPrivate;
	
	private Boolean critical;
	
	private Date lastUpdate;
	
	private Integer importance;
	
	private String explanation;
	
	private Boolean answerA;
	
	private Boolean answerB;
	
	private Boolean answerC;
	
	private Boolean answerD;
	
	private Boolean answerE;

	/**
	 * Returns the id of the person who's given this reply.
	 * @return the id of the person who's given this reply.
	 */
	public Long getPersonId() {
		return personId;
	}

	/**
	 * Sets the id of the person who's given this reply.
	 * @param personId the person id to set.
	 */
	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	/**
	 * Returns the id of the question to which this reply's been given.
	 * @return the id of the question to which this reply's been given.
	 */
	public Long getQuestionId() {
		return questionId;
	}

	/**
	 * Sets the id of the question to which this reply's been given.
	 * @param questionId the question id to set.
	 */
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	
	/**
	 * Returns whether this question has been skipped.
	 * @return true, if the answer has been skipped, false otherwise.
	 */
	public Boolean getSkipped() {
		return skipped;
	}

	/**
	 * Sets whether this question has been skipped.
	 * @param skipped true, if the answer has been skipped, false otherwise.
	 */
	public void setSkipped(Boolean skipped) {
		this.skipped = skipped;
	}

	/**
	 * Returns whether this question has been answered privately.
	 * @return true, if the answer has been answered privately, false otherwise.
	 */
	public Boolean getInPrivate() {
		return inPrivate;
	}

	/**
	 * Sets whether this question has been answered privately.
	 * @param inPrivate true, if the answer has been answered privately, false otherwise.
	 */
	public void setInPrivate(Boolean inPrivate) {
		this.inPrivate = inPrivate;
	}

	/**
	 * Returns whether this question has been marked critical.
	 * @return true, if the question has been marked critical, false otherwise.
	 */
	public Boolean getCritical() {
		return critical;
	}

	/**
	 * Sets whether this question has been marked critical.
	 * @param critical true, if the question has been marked critical, false otherwise.
	 */
	public void setCritical(Boolean critical) {
		this.critical = critical;
	}

	/**
	 * Returns this given reply's last update.
	 * @return the last update.
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * Sets this given reply's last update.
	 * @param lastUpdate the last update to set.
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * Returns this given reply's importance weight. 
	 * @return this given reply's importance weight.
	 */
	public Integer getImportance() {
		return importance;
	}

	/**
	 * Sets this given reply's importance weight.
	 * @param importance the importance weight to set.
	 */
	public void setImportance(Integer importance) {
		this.importance = importance;
	}

	/**
	 * Returns an explanation to this given reply.
	 * @return the explanation.
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * Sets the explanation to this given reply.
	 * @param explanation the explanation to set.
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/**
	 * Returns true, if the reply to answerA was positive, false otherwise.
	 * @return true, if the reply to answerA was positive, false otherwise.
	 */
	public Boolean getAnswerA() {
		return answerA;
	}

	/**
	 * @param answerA the answerA to set
	 */
	public void setAnswerA(Boolean answerA) {
		this.answerA = answerA;
	}

	/**
	 * Returns true, if the reply to answerB was positive, false otherwise.
	 * @return true, if the reply to answerB was positive, false otherwise.
	 */
	public Boolean getAnswerB() {
		return answerB;
	}

	/**
	 * Sets whether answerB is answered positively or not.
	 * @param answerB true, if the reply to answerB is positive, false otherwise.
	 */
	public void setAnswerB(Boolean answerB) {
		this.answerB = answerB;
	}

	/**
	 * Returns true, if the reply to answerC was positive, false otherwise.
	 * @return true, if the reply to answerC was positive, false otherwise.
	 */
	public Boolean getAnswerC() {
		return answerC;
	}

	/**
	 * Sets whether answerC is answered positively or not.
	 * @param answerC true, if the reply to answerC is positive, false otherwise.
	 */
	public void setAnswerC(Boolean answerC) {
		this.answerC = answerC;
	}

	/**
	 * Returns true, if the reply to answerD was positive, false otherwise.
	 * @return true, if the reply to answerD was positive, false otherwise.
	 */
	public Boolean getAnswerD() {
		return answerD;
	}

	/**
	 * Sets whether answerD is answered positively or not.
	 * @param answerD true, if the reply to answerD is positive, false otherwise.
	 */
	public void setAnswerD(Boolean answerD) {
		this.answerD = answerD;
	}

	/**
	 * Returns true, if the reply to answerE was positive, false otherwise.
	 * @return true, if the reply to answerE was positive, false otherwise.
	 */
	public Boolean getAnswerE() {
		return answerE;
	}

	/**
	 * Sets whether answerE is answered positively or not.
	 * @param answerE true, if the reply to answerE is positive, false otherwise.
	 */
	public void setAnswerE(Boolean answerE) {
		this.answerE = answerE;
	}
	
	@Override
	public String toString() {
		return personId + " - " + questionId + " [" + answerA + "," + answerB + "," + answerC + "," + answerD + "," + answerE + "]";
	}
}
