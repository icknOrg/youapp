package youapp.model;

import java.util.Date;

public class Question {

	private Long id;
	
	private Date created;
	
	private Long authorId;
	
	private Name authorName;
	
//	private String authorFirstName;
//	
//	private String authorLastName;
//	
//	private String authorNickName;
	
	private String question;
	
	private String answerA;
	
	private String answerB;
	
	private String answerC;
	
	private String answerD;
	
	private String answerE;
	
	private Long numberOfReplies;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the authorId
	 */
	public Long getAuthorId() {
		return authorId;
	}

	/**
	 * @param authorId the authorId to set
	 */
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

//	/**
//	 * @return the author's first name.
//	 */
//	public String getAuthorFirstName() {
//		return authorFirstName;
//	}
//
//	/**
//	 * @param authorFirstName the author's first name.
//	 */
//	public void setAuthorFirstName(String authorFirstName) {
//		this.authorFirstName = authorFirstName;
//	}
//
//	/**
//	 * @return the author's last name.
//	 */
//	public String getAuthorLastName() {
//		return authorLastName;
//	}
//
//	/**
//	 * @param authorLastName the author's last name.
//	 */
//	public void setAuthorLastName(String authorLastName) {
//		this.authorLastName = authorLastName;
//	}
//
//	/**
//	 * @return the authorNickName
//	 */
//	public String getAuthorNickName() {
//		return authorNickName;
//	}
//
//	/**
//	 * @param authorNickName the authorNickName to set
//	 */
//	public void setAuthorNickName(String authorNickName) {
//		this.authorNickName = authorNickName;
//	}

	public Name getAuthorName() {
		return authorName;
	}

	public void setAuthorName(Name name) {
		this.authorName = name;
	}

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return the answerA
	 */
	public String getAnswerA() {
		return answerA;
	}

	/**
	 * @param answerA the answerA to set
	 */
	public void setAnswerA(String answerA) {
		this.answerA = answerA;
	}

	/**
	 * @return the answerB
	 */
	public String getAnswerB() {
		return answerB;
	}

	/**
	 * @param answerB the answerB to set
	 */
	public void setAnswerB(String answerB) {
		this.answerB = answerB;
	}

	/**
	 * @return the answerC
	 */
	public String getAnswerC() {
		return answerC;
	}

	/**
	 * @param answerC the answerC to set
	 */
	public void setAnswerC(String answerC) {
		this.answerC = answerC;
	}

	/**
	 * @return the answerD
	 */
	public String getAnswerD() {
		return answerD;
	}

	/**
	 * @param answerD the answerD to set
	 */
	public void setAnswerD(String answerD) {
		this.answerD = answerD;
	}

	/**
	 * @return the answerE
	 */
	public String getAnswerE() {
		return answerE;
	}

	/**
	 * @param answerE the answerE to set
	 */
	public void setAnswerE(String answerE) {
		this.answerE = answerE;
	}
	
	/**
	 * Returns the total number of replies to this question.
	 * @return the total number of replies to this question.
	 */
	public Long getNumberOfReplies() {
		return numberOfReplies;
	}
	
	/**
	 * Sets the total number of replies to this question.
	 * @param numberOfReplies the total number of replies to this question.
	 */
	public void setNumberOfReplies(Long numberOfReplies) {
		this.numberOfReplies = numberOfReplies;
	}

	@Override
	public String toString() {
		return id + " - " + question + " / " + authorName;// + " / " + authorNickName;
	}
	
}
