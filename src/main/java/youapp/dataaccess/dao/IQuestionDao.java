package youapp.dataaccess.dao;

import java.util.Date;
import java.util.List;

import youapp.dataaccess.dto.QuestionDto;

/**
 * Represents an SQL data access object for a question.
 * @author neme
 *
 */
public interface IQuestionDao {

	/**
	 * Returns the question from the database matching the given id. The question with the given id must be existent.
	 * @param id the id of the question to be returned.
	 * @return the question matching the given id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public QuestionDto getById(Long id);
	
	/**
	 * Returns all questions from the database matching the given author. If the author with the given id does not
	 * exist or if there are no questions associated with the given author, an empty list is returned.
	 * @param personId the author person id of all questions returned.
	 * @return all questions matching the given author.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<QuestionDto> getByAuthor(Long personId);
	
	/**
	 * Returns a limited set of questions starting at the given offset from the database matching the given author.
	 * If the author with the given id does not exist or if there are no questions associated with the given author,
	 * an empty list is returned.
	 * @param personId the id of the author whose questions have to be returned.
	 * @param offset the offset at which the next bunch of questions starts. The offset is usually zero when calling this
	 * method for the first time. Must be greater or equal zero.
	 * @param resultSize the maximum number of questions returned. Must be greater than zero.
	 * @return all questions created by the person with the given id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<QuestionDto> getByAuthor(Long personId, Integer offset, Integer resultSize);
	
	/**
	 * Returns all questions from the database that were created on the given date. The date must not be null. If there were no
	 * questions created on the given date, an empty list is returned.
	 * @param date the date all questions returned were created. Must not be null.
	 * @return all questions that were created on the given date.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<QuestionDto> getByDate(Date date);
	
	/**
	 * Returns all recently created questions from the database. Number of days must be greater or equal zero. If there were no
	 * questions created during the last given days, an empty list is returned.
	 * @param days determines what "recently created" means / represents the interval of days. Must be greater or equal zero.
	 * @return all recently created questions.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<QuestionDto> getMostRecentByDays(int days);
	
	/**
	 * Returns all recently created questions from the database. The result size must be greater than zero. If there are less questions
	 * available than specified by the result size, only the available questions are returned. If there are no questions stored, an
	 * empty list is returned.
	 * @param resultSize the maximum number of questions returned. Must be greater than zero.
	 * @return all recently created questions.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<QuestionDto> getMostRecentBySize(Integer resultSize);
	
	/**
	 * Returns all questions from the database which were both answered by person A with the given id and person B with the given id. If there are no matching questions
	 * or if person A, person B or both persons do not exists, an empty list is returned.
	 * @param idPersonA the first person's id.
	 * @param idPersonB the second person's id.
	 * @return all matching questions which were answered by both person A and person B.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<QuestionDto> getMatchingQuestionsByPair(Long idPersonA, Long idPersonB);
	
	/**
	 * Returns the number of questions that were both answered by person A and person B from the database. If there are no matching questions or if the id of person 
	 * a, person B or both person ids do not exist, 0 is returned.
	 * @param idPersonA the first person's id.
	 * @param idPersonB the second person's id.
	 * @param skipped true, if the skipped flag of the replies by person A respectively B should be set, false if not and null if the value of the flag does not matter.
	 * @param inPrivate true, if the private flag of the replies by person A respectively B should be set, false if not and null if the value of the flag does not matter.
	 * @param critical true, if the critical flag of the replies by person A respectively B should be set, false if not and null if the value of the flag does not matter.
	 * @return the number of questions that were both answered by person A and person B.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public int getNumberOfMatchingQuestionsByPair(Long idPersonA, Long idPersonB, Boolean skipped, Boolean inPrivate, Boolean critical);
	
//	/**
//	 * Return all questions that were answered by the person with the given id. If the person with the given id did not answer any questions
//	 * yet or if the person with the given id does not exist, an empty list is returned.
//	 * @param personId the person who answered all the questions returned.
//	 * @param includeSkipped true, if skipped questions should be included, false otherwise.
//	 * @param includePrivate true, if private questions should be included, false otherwise.
//	 * @param includeCritical true, if critical questions should be included, false otherwise.
//	 * @return all questions answered by the person with the given id.
//	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
//	 */
//	public List<QuestionDto> getAnsweredQuestionsByPerson(Long personId, Boolean includeSkipped, Boolean includePrivate, Boolean includeCritical);
	
	/**
	 * Fetches the next xyz questions to be answered by the given person. Returns questions that were not previously answered
	 * or skipped by the given person. If the person with the given id does not exist, questions are still returned. If the number
	 * of available questions is less than the given number of questions, the result contains the available questions only.
	 * @param personId the person which is going to answer the fetched questions. Must not be null.
	 * @param numberOfQuestions the number of questions to be returned. Must be greater than zero.
	 * @return next xyz questions to be answered by the given person.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<QuestionDto> fetchNext(Long personId, int numberOfQuestions);
	
	/**
	 * Creates a new question in the database. The question must not be present and the question id must be null.
	 * @param question the question to be created.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public Long create(QuestionDto question);
	
	/**
	 * Updated the given question in the database. The question must be present and have a valid question id.
	 * @param question the question to be updated.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void update(QuestionDto question);
	
	/**
	 * Updates the given questions in the database. The questions mut be present and have a valid question id.
	 * @param questions the questions to be updated.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void update(List<QuestionDto> questions);
	
	/**
	 * Deletes the given question from the database. The question id does not have to be valid but it must not
	 * be null in any case. A question must not be deleted if replies are still associated with that question.
	 * @param questionId the id of the question to be deleted. Must not be null.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void delete(Long questionId);
	
}
