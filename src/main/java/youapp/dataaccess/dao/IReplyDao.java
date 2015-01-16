package youapp.dataaccess.dao;

import java.util.Date;
import java.util.List;

import youapp.dataaccess.dto.ReplyDto;
import youapp.dataaccess.dto.ReplyPairDto;

/**
 * Represents an SQL data access object for a reply.
 * @author Linda
 *
 */
public interface IReplyDao {

	/**
	 * Returns the reply from the database matching the given id's. Both id's, the person id and the question id must be existent.
	 * @param personId the id of the person who has given the reply.
	 * @param questionId the id of the question to which the reply was given.
	 * @return the reply matching the given id's.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public ReplyDto getById(Long personId, Long questionId);
	
	/**
	 * Returns the replies of the person with the given id to all the questions with the given ids from the database. If the person with the given id does not exist,
	 * an empty list is returned. If a reply for question with id x for the given person does not exist, it is ignored.
	 * @param personId the id of the person whose replies are returned.
	 * @param questionIds the ids of the questions for which replies are returned. Must not be empty.
	 * @return all the replies of the person with the given id to the given questions.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ReplyDto> getByIds(Long personId, List<Long> questionIds);
	
	/**
	 * Returns all replies from the database matching the given person. If the person with the given id has not given any replies yet,
	 * an empty list is returned. If the person with the given id does not exist, an empty list is returned.
	 * @param personId the id of the person of all replies returned.
	 * @param skipped true, if the skipped flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @param inPrivate true, if the private flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @param critical true, if the critical falg of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @return all replies matching the given person.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ReplyDto> getByPerson(Long personId, Boolean skipped, Boolean inPrivate, Boolean critical);
	
	/**
	 * Returns all replies from the database matching the given person, starting at the given offset. The number of replies
	 * does not exceed the result size. Offset must be greater than or equal zero. Result size must be greater than zero. If a
	 * person did not reply to any questions yet or if a person with the given person id does not exist, an empty list is returned.
	 * @param personId the id of the person who has given the replies.
	 * @param offset the offset at which the next bunch of replies starts. The offset is usually zero when calling this method
	 * for the first time. Must be greater than or equal zero.
	 * @param resultSize the maximum number of replies returned. Must be greater than zero.
	 * @return all replies matching the given person.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ReplyDto> getByPerson(Long personId, int offset, int resultSize);
	
	/**
	 * Returns all replies from the database matching the given question. If there are no replies to the question with the given id or if
	 * a question with the given id does not exist, an empty list is returned.
	 * @param questionId the id of the question all replies refer to.
	 * @return all replies matching the given question.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ReplyDto> getByQuestion(Long questionId);
	
	/**
	 * Returns all replies from the database matching the given question, starting at the given offset. The number of replies does not
	 * exceed the result size. Offset must be greater than or equal zero. Result size must be greater than zero. If there is no reply to
	 * the question with the given id or if a question with the given id does not exist, an empty list is returned.
	 * @param questionId the id of the question all replies refer to.
	 * @param offset the offset at which the next bunch of replies starts. The offset is usually zero when calling this method for the first
	 * time. Must be greater than or equal zero.
	 * @param resultSize the maximum number of replies returned. Must be greater than zero.
	 * @return all replies matching the given question.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ReplyDto> getByQuestion(Long questionId, int offset, int resultSize);
	
	/**
	 * Returns all replies from the database matching the given date.
	 * @param date the date all replies have been updated last.
	 * @return all replies matching the given date.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ReplyDto> getByDate(Date date);
	
	/**
	 * Returns all replies from the database matching the given date, starting
	 * at the given offset. The number of replies does not exceed the result size.
	 * @param date the date all replies have been updated last.
	 * @param offset the offset at which the next bunch of replies starts. The offset
	 * is zero when calling this method for the first time.
	 * @param resultSize the maximum number of replies returned.
	 * @return all replies matching the given date.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ReplyDto> getByDate(Date date, int offset, int resultSize);
	
	/**
	 * Returns all replies from the database matching the given person and date.
	 * @param personId the id of the person who has given the replies.
	 * @param date the date all replies have been updated last.
	 * @return all replies matching the given person and date.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ReplyDto> getByDate(Long personId, Date date);
	
	/**
	 * Returns the number of replies to the question with the given id from the database. If the question with the given id does not exist or
	 * if no replies were given to that question so far, 0 is returned.
	 * @param questionId the id of the question its number of replies have to be returned.
	 * @param skipped true, if the skipped flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @param inPrivate true, if the private flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @param critical true, if the critical flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @return the number of replies to the given question.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public Long getNumberOfRepliesByQuestion(Long questionId, Boolean skipped, Boolean inPrivate, Boolean critical);
	
	/**
	 * Returns the number of replies by the person with the given id from the database. If the person with the given id does not exist or
	 * if the person did not answer any questions yet, 0 is returned.
	 * @param personId the id of the person whose replies should be returned.
	 * @param skipped true, if the skipped flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @param inPrivate true, if the private flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @param critical true, if the critical flag of the reply should be set, false if not and null if the value of the flag does not matter.
	 * @return number of replies by the given person.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public Long getNumberOfRepliesByPerson(Long personId, Boolean skipped, Boolean inPrivate, Boolean critical);
	
	/**
	 * Returns reply pairs to all common questions of person with id A and person with id B. Only non-skipped replies are returned.
	 * @param personIdA the id of the first person.
	 * @param personIdB the id of the second person.
	 * @return reply pairs to all common questions of person A and B.
	 */
	public List<ReplyPairDto> getCommonRepliesByPair(Long personIdA, Long personIdB);
	
	/**
	 * Returns all recently given replies from the database. Number of days must be greater or equal zero.
	 * @param days determines what "recently created" means / represents the interval of days. Must be greater or equal zero.
	 * @param publicOnly true, if only publicly answered questions should be returned, false otherwise.
	 * @return all recently given replies.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ReplyDto> getMostRecentByDays(int days, boolean publicOnly);
	
	/**
	 * Returns all recently given replies from the database. The result size must be greater than zero. If there are less replies
	 * available than specified by the result size, only the available replies are returned. If there are no replies available, an empty
	 * list is returned.
	 * @param resultSize the maximum number of replies returned. Must be greater than zero.
	 * @param publicOnly true, if only publicly answered questions should be returned, false otherwise.
	 * @return all recently given replies.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ReplyDto> getMostRecentBySize(int resultSize, boolean publicOnly);
	
	/**
	 * Returns all replies from the database, starting at the given offset. The
	 * number of replies does not exceed the result size.
	 * @param offset the offset at which the next bunch of replies starts. The offset
	 * is zero when calling this method for the first time.
	 * @param resultSize the maximum number of replies returned.
	 * @return all replies.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ReplyDto> getAll(int offset, int resultSize);
	
	/**
	 * Checks whether a reply for the given question of the given person already exists in the database.
	 * @param personId the id of the person who answered the question.
	 * @param questionId the id of the question who was answered by the given person.
	 * @return true, if the question has already been answered by the given person, false otherwise.
	 */
	public boolean exists(Long personId, Long questionId);
	
	/**
	 * Creates a new reply in the database. The reply must not be present and the
	 * person id as well as the question id must be valid.
	 * @param reply the reply to be created.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void create(ReplyDto reply);
	
	/**
	 * Updates the given reply in the database. The reply must be present and have
	 * a valid person id as well as a valid question id.
	 * @param reply the reply to be updated.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void update(ReplyDto reply);
	
	/**
	 * Deletes the given reply from the database. 
	 * @param personId the id of the person whose reply has to be deleted.
	 * @param questionId the id of the question whose reply has to be deleted.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void delete(Long personId, Long questionId);
	
	/**
	 * Deletes all replies of the given person from the database.
	 * @param personId the id of the person whose replies have to be deleted.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void deletePersonReplies(Long personId);
	
	/**
	 * Deletes all replies for the given question from the database.
	 * @param questionId the id fo the question whose replies have to be deleted.
	 */
	public void deleteQuestionReplies(Long questionId);
	
}
