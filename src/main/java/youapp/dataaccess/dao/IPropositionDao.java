package youapp.dataaccess.dao;

import java.util.Date;
import java.util.List;

import youapp.dataaccess.dto.PropositionDto;

/**
 * Represents an SQL data access object for a proposition.
 * @author neme
 *
 */
public interface IPropositionDao {

	/**
	 * Returns the proposition with the given person ids from the database.
	 * @param personId the id of the person who receives the proposition.
	 * @param propositionId the id of the person who is proposed.
	 * @return the proposition with the given person ids.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public PropositionDto getById(Long personId, Long propositionId);
	
	/**
	 * Returns all propositions made for the person with the given id from
	 * the database.
	 * @param personId the id of the person whose propositions are returned.
	 * @return all propositions made for the person with the given id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PropositionDto> getByPerson(Long personId);
	
	/**
	 * Returns all propositions from the database which are recommending /
	 * proposing the person with the given id.
	 * @param propositionId the id of the person recommended / proposed.
	 * @return all propositions which are recommmending / proposing the person
	 * with the given id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PropositionDto> getByProposition(Long propositionId);
	
	/**
	 * Returns all propositions from the database that were made at the given date.
	 * @param date the date when the propositions returned were made.
	 * @return all propositions that were made at the given date.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PropositionDto> getByDate(Date date);
	
	/**
	 * Returns all propositions from the database that were made at the given date for
	 * the given person.
	 * @param personId the id of the person the propositions were made for.
	 * @param date the date when the propositions returned were made.
	 * @return all replies matching the given date and person id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PropositionDto> getByDate(Long personId, Date date);
	
	/**
	 * Returns all propositions from the database matching the given date, starting
	 * at the given offset. The number of propositions does not exceed the result size.
	 * @param date the date when the propositions returned were made.
	 * @param offset the offset at which the next bunch of propositions starts. The offset
	 * is zero when calling this method for the first time.
	 * @param resultSize the maximum number of propositions returned.
	 * @return all propositions from the database matching the given date.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PropositionDto> getByDate(Date date, int offset, int resultSize);
	
	/**
	 * Returns all propositions from the database matching the given date and person id, starting
	 * at the given offset. The number of propositions does not exceed the result size.
	 * @param personId the id of the person the propositions were made for.
	 * @param date the date when the propositions returned were made.
	 * @param offset the offset at which the next bunch of propositions starts. The offset
	 * is zero when calling this method for the first time.
	 * @param resultSize the maximum number of propositions returned.
	 * @return all propositions from the database matching the given date and person id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PropositionDto> getByDate(Long personId, Date date, int offset, int resultSize);
	
	/**
	 * Returns all recent propositions from the database.
	 * @param days determines what "recent" means / represents the interval of days.
	 * @return all recent propositions.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PropositionDto> getMostRecent(int days);
	
	/**
	 * Returns all recent propositions of the person with the given id from the database.
	 * @param personId the id of the person whose recent propositions are returned.
	 * @param days determines what "recent" means / represents the interval of days.
	 * @return all recent propositions of the given person.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PropositionDto> getMostRecent(Long personId, int days);
	
	/**
	 * Returns all propositions from the database, starting at the given offset. The
	 * number of propositions does not exceed the result size.
	 * @param offset the offset at which the next bunch of propositions starts.
	 * The offset is zero when calling this method for the first time.
	 * @param resultSize the maximum number of propositions returned.
	 * @return all propositions.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<PropositionDto> getAll(int offset, int resultSize);
	
	/**
	 * Creates a new proposition in the database. The proposition must not be present
	 * and the person ids must be valid. A proposition can only be made once.
	 * @param proposition the proposition to be created.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void create(PropositionDto proposition);
	
	/**
	 * Updates the given proposition in the database. The proposition must be present
	 * and have valid person ids.
	 * @param proposition the proposition to be updated.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void update(PropositionDto proposition);
	
	/**
	 * Deletes the given proposition from the database. 
	 * @param proposition the proposition to be deleted.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void delete(PropositionDto proposition);
	
}
