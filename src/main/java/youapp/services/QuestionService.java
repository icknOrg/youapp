package youapp.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.model.Importance;
import youapp.model.Question;
import youapp.model.Reply;
import youapp.model.ReplyPair;

public interface QuestionService {

	/**
	 * Returns the question matching the given id. If no questions with the given id exists, null is returned.
	 * @param questionId the id of the question to be returned.
	 * @return the question matching the given id.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public Question getQuestionById(Long questionId) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns all questions from the database matching the given author. If the author with the given id does not
	 * exist or if there are no questions associated with the given author, an empty list is returned. If no author name
	 * information for the given author can be retrieved, an empty list is returned.
	 * @param authorId the author person id of all questions returned.
	 * @return all questions matching the given author.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public List<Question> getQuestionsByAuthor(Long authorId) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns a limited set of questions starting at the given offset from the database matching the given author.
	 * If the author with the given id does not exist or if there are no questions associated with the given author,
	 * an empty list is returned. If no author name information for the given author can be retrieved, an empty list is returned.
	 * @param authorId the id of the author whose questions have to be returned. The number or returned questions might be less
	 * than the given result size if there are no more questions left.
	 * @param offset the offset at which the next bunch of questions starts. The offset is usually zero when calling this
	 * method for the first time. Must be greater or equal zero.
	 * @param resultSize the maximum number of questions returned. Must be greater than zero.
	 * @return all questions created by the person with the given id.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public List<Question> getQuestionsByAuthor(Long authorId, Integer offset, Integer resultSize) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns all questions that were created on the given date. If there were no questions created on the given date, an empty list is
	 * returned. The date must not be null. A question is excluded if no author name information for this question can be retrieved.
	 * @param date the date all questions returned were created. Must not be null.
	 * @return all questions that were created on the given date.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public List<Question> getQuestionsByDate(Date date) throws DataAccessLayerException, ModelException;
	
//	/**
//	 * Returns all questions that were answered by the person with the given person id. If the author with the given id does not
//	 * exist or if there are no questions associated with the given author, an empty list is returned. If no author name information
//	 * for the given author can be retrieved, an empty list is returned.
//	 * @param personId the person who answered all the returned questions.
//	 * @param includeSkipped true, if skipped questions should be returned too, false otherwise.
//	 * @param includePrivate true, if private questions should be returned too, false otherwise.
//	 * @param includeCritical true, if critical questions should be returned too, false otherwise.
//	 * @return all questions that were answered by the person with the given id.
//	 * @throws DataAccessLayerException if an error occurs while accessing the data.
//	 */
//	public List<Question> getAnsweredQuestionsByPerson(Long personId, Boolean includeSkipped, Boolean includePrivate, Boolean includeCritical) throws DataAccessLayerException;
	
	/**
	 * Returns all recently created questions. Number of days must be greater or equal zero. If there were no
	 * questions created during the last given days, an empty list is returned. A question is excluded if no author
	 * name information for this question can be retrieved.
	 * @param days determines what "recently created" means / represents the interval of days. Must be greater or equal zero.
	 * @return all recently created questions.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public List<Question> getRecentQuestionsByDays(Integer days) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns all recently created questions. The result size must be greater than zero. If there are less questions available
	 * than specified by the result size, only the available questions are returned. If there are no questions stored, an empty
	 * list is returned. A question is excluded if no author name information for this question can be retrieved.
	 * @param resultSize the maximum number of questions returned. Must be greater than zero.
	 * @return all recently created questions.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public List<Question> getRecentQuestionsBySize(Integer resultSize) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns questions that are similar to the given question. The given question must contain a valid question string to yield
	 * a useful result. If no similar questions can be found, an empty list is returned.
	 * @param question the question for which similar questions are returned. Must not be null.
	 * @return questions that are similar to the given question.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public List<Question> getSimilarQuestions(Question question) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns all questions from the database which were both answered by person A with the given id and person B with the given id. If there are no matching questions
	 * or if person A, person B or both persons do not exists, an empty list is returned.
	 * @param idPersonA the first person's id.
	 * @param idPersonB the second person's id.
	 * @return all matching questions which were answered by both person A and person B.
	 * @throws DataAccessLayerException if an error occurs while accessing the database.
	 */
	
	/**
	 * Returns all questions which were both answered by person A with the given id and person B with the given id. If there are no matching questions
	 * or if person A, person B or both persons do not exists, an empty list is returned.
	 * @param idPersonA the first person's id.
	 * @param idPersonB the second person's id.
	 * @return all matching questions which were answered by both person A and person B.
	 * @throws DataAccessLayerException if an error occurs while accessing the database.
	 */
	public List<Question> getMatchingQuestionsByPair(Long idPersonA, Long idPersonB) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns the number of questions that were both answered by person A and person B. If there are no matching questions or if the id of person 
	 * a, person B or both person ids do not exist, 0 is returned.
	 * @param idPersonA the first person's id.
	 * @param idPersonB the second person's id.
	 * @param skipped true, if only skipped replies should be returned, false if only not skipped replies should be returned and null
	 * if both skipped or not skipped replies are fine.
	 * @param inPrivate true, if only private replies should be returned, false if only public replies should be returned and null
	 * if both private and public replies are fine.
	 * @param critical true, if only critical replies should be returned, false if only non critical replies should be returned and null
	 * @param critical true, if the critical flag of the replies by person A respectively B should be set, false if not and null if the value of the flag does not matter.
	 * @return the number of questions that were both answered by person A and person B.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public int getNumberOfMatchingQuestionsByPair(Long idPersonA, Long idPersonB, Boolean skipped, Boolean inPrivate, Boolean critical);
	
	/**
	 * Fetches the next xyz questions to be answered by the given person. Returns questions that were not previously answered
	 * or skipped by the given person. If the person with the given id does not exist, an empty list of questions is returned.
	 * If the number of available questions is less than the given number of questions, the result contains the available questions
	 * only. A question is excluded if no author name information for this question can be retrieved.
	 * @param personId the person which is going to answer the fetched questions. Must not be null.
	 * @param numberOfQuestions the number of questions to be returned. Must be greater than zero.
	 * @return next xyz questions to be answered by the given person.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public List<Question> fetchNextQuestions(Long personId, Integer numberOfQuestions) throws DataAccessLayerException, ModelException;
	
	/**
	 * Creates a new question. The question must not be present and the question id must be null. Also, a question's
	 * complete author information (first name, last name, nick name) must be set.
	 * @param question the question to be created. Must not be null.
	 * @return The created question's id.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 * @throws ModelException if the model data is not valid.
	 */
	public Long createQuestion(Question question) throws DataAccessLayerException, ModelException;
	
	/**
	 * Updates the given question. The question must be present and have a valid question id. Also, a question's complete
	 * author information (first name, last name, nick name) must be set.
	 * @param question the question to be updated.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 * @throws ModelException if the model data is not valid.
	 */
	public void updateQuestion(Question question) throws DataAccessLayerException, ModelException;
	
	/**
	 * Deletes the question with the given id. If there is no question with the given id, nothing is deleted. A question must not
	 * be deleted if there are still replies associated with that question.
	 * @param questionId the id of the question to be deleted. Must not be null.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 * @throws ModelException if the model data is not valid.
	 */
	public void deleteQuestion(Long questionId) throws DataAccessLayerException;
	
	/**
	 * Deletes the question with the given id and all replies that are associated with that question (if any).
	 * @param questionId the id of the question to be deleted. Must not be null.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public void deleteQuestionWithReplies(Long questionId) throws DataAccessLayerException;
	
	/**
	 * Returns the reply matching the given id's. If no reply for the given question by the given person exists, null is returned.
	 * If no person name information for the reply could be found, null is returned.
	 * @param personId the id of the person who has given the reply.
	 * @param questionId the id of the question to which the reply was given.
	 * @return the reply matching the given id's.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public Reply getReplyById(Long personId, Long questionId) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns the replies of the person with the given id to all the questions with the given ids from the database. If the person with the given id does not exist,
	 * an empty list is returned. If a reply for question with id x for the given person does not exist, it is ignored.
	 * @param personId the id of the person whose replies are returned.
	 * @param questionIds the ids of the questions for which replies are returned. Must not be empty.
	 * @return all the replies of the person with the given id to the given questions.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	
	/**
	 * Returns the replies of the person with the given id to all the questions with the given ids. If the person with the given id does not exist,
	 * an empty list is returned. If a reply for question with id x for the given person does not exist, it is ignored.
	 * @param personId the id of the person whose replies are returned.
	 * @param questionIds the ids of the questions for which replies are returned. Must not be empty.
	 * @return all the replies of the person with the given id to the given questions. The key is the question id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public Map<Long, Reply> getRepliesByIds(Long personId, List<Long> questionIds) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns all replies matching the given person. If the person with the given id has not given any replies yet, an empty list is
	 * returned. If the person with the given id does not exist, an empty list is returned.
	 * @param personId the id of the person of all replies returned.
	 * @param fbId true, if the person id is a Facebook id, false if it is a YouApp id.
	 * @param skipped true, if only skipped replies should be returned, false if only not skipped replies should be returned and null
	 * if both skipped or not skipped replies are fine.
	 * @param inPrivate true, if only private replies should be returned, false if only public replies should be returned and null
	 * if both private and public replies are fine.
	 * @param critical true, if only critical replies should be returned, false if only non critical replies should be returned and null
	 * if both critical and non critical replies are fine.
	 * @return all replies matching the given person.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public List<Reply> getRepliesByPerson(Long personId, Boolean fbId, Boolean skipped, Boolean inPrivate, Boolean critical) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns all replies matching the given person, starting at the given offset. The number of replies does not exceed the result size.
	 * Offset must be greater than or equal zero. Result size must be greater than zero. If a person did not reply to any questions yet or
	 * if a person with the given person id does not exist, an empty list is returned.
	 * @param personId the id of the person who has given the replies.
	 * @param offset the offset at which the next bunch of replies starts. The offset is usually zero when calling this method for the
	 * first time. Must be greater than or equal zero.
	 * @param resultSize the maximum number of replies returned. Must be greater than zero.
	 * @return all replies matching the given person.
	 * @throws DataAccessLayerException if an error occurs while accessing the database.
	 */
	public List<Reply> getRepliesByPerson(Long personId, Integer offset, Integer resultSize) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns all replies matching the given question. If there are no replies to the question with the given id or if a question with
	 * the given id does not exist, an empty list is returned.
	 * @param questionId the id of the question all replies refer to.
	 * @return all replies matching the given question.
	 * @throws DataAccessLayerException if an error occurs while accessing the database.
	 */
	public List<Reply> getRepliesByQuestion(Long questionId) throws DataAccessLayerException, ModelException, ModelException;
	
	/**
	 * Returns all replies matching the given question, starting at the given offset. The number of replies does not exceed the result size.
	 * Offset must be greater than or equal zero. Result size must be greater than zero. If there is no reply to the question with the given
	 * id or if a question with the given id does not exist, an empty list is returned.
	 * @param questionId the id of the question all replies refer to.
	 * @param offset the offset at which the next bunch of replies starts. The offset is usually zero when calling this method for the first
	 * time. Must be greater than or equal zero.
	 * @param resultSize the maximum number of replies returned. Must be greater than zero.
	 * @return all replies matching the given question.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public List<Reply> getRepliesByQuestion(Long questionId, Integer offset, Integer resultSize) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns all recently given replies. Number of days must be greater than or equal zero. If there were no replies created during
	 * the last given days, an empty list is returned. A reply is excluded if no person name information for this reply can be retrieved.
	 * @param days determines what "recently created" means / represents the interval of days. Must be greater than or equal zero.
	 * @param publicOnly true, if only publicly answered questions should be returned, false otherwise.
	 * @return all recently given replies.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public List<Reply> getRecentRepliesByDays(Integer days, Boolean publicOnly) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns all recently given replies. The result size must be greater than zero. If there are less replies available than specified
	 * by the result size, only the available replies are returned. If there are no replies available, an empty list is returned. A reply
	 * is excluded if no person name information for this reply can be retrieved.
	 * @param resultSize the maximum number of replies returned. Must be greater than zero.
	 * @param publicOnly true, if only publicly answered questions should be returned, false otherwise.
	 * @return all recently given replies.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public List<Reply> getRecentRepliesBySize(Integer resultSize, Boolean publicOnly) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns the number of replies to the question with the given id. If the question with the given id does not exist or
	 * if no replies were given to that question so far, 0 is returned.
	 * @param questionId the id of the question its number of replies have to be returned.
	 * @param skipped true, if the skipped flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @param inPrivate true, if the private flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @param critical true, if the critical flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @return the number of replies to the given question.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public Long getNumberOfRepliesByQuestion(Long questionId, Boolean skipped, Boolean inPrivate, Boolean critical);
	
	/**
	 * Returns the number of replies by the person with the given id. If the person with the given id does not exist or
	 * if the person did not answer any questions yet, 0 is returned.
	 * @param personId the id of the person whose replies should be returned.
	 * @param skipped true, if the skipped flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @param inPrivate true, if the private flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @param critical true, if the critical falg of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @return number of replies by the given person.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public Long getNumberOfRepliesByPerson(Long personId, Boolean skipped, Boolean inPrivate, Boolean critical);
	
	/**
	 * Returns reply pairs to all common questions of person with id A and person with id B.
	 * @param personIdA the id of the first person.
	 * @param personIdB the id of the second person.
	 * @return reply pairs to all common questions of person A and B.
	 */
	public List<ReplyPair> getCommonRepliesByPair(Long personIdA, Long personIdB) throws DataAccessLayerException, ModelException;
	
	public boolean existsReply(Long personId, Long questionId) throws DataAccessLayerException;
	
	public void createReply(Reply reply) throws DataAccessLayerException, ModelException;
	
	public void updateReply(Reply reply) throws DataAccessLayerException, ModelException;
	
	public void deleteReply(Long personId, Long questionId) throws DataAccessLayerException;
	
	public void deleteRepliesByPerson(Long personId) throws DataAccessLayerException;
	
	/**
	 * Deletes all replies for the given question. If there are no replies for the given question, nothing is deleted.
	 * @param questionId the id of the question whose replies have to be deleted. Must not be null.
	 * @throws DataAccessLayerException if an error occurs while accessing the data.
	 */
	public void deleteRepliesByQuestion(Long questionId) throws DataAccessLayerException;
	
	public Importance getImportanceByWeight(Integer weight) throws DataAccessLayerException;
	
	public List<Importance> getAllImportanceLevels() throws DataAccessLayerException;
	
}
