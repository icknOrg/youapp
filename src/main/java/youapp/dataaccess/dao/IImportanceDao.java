package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.ImportanceDto;

/**
 * Represents an SQL data access object for an importance type.
 * @author neme
 *
 */
public interface IImportanceDao {

	/**
	 * Returns the importance level from the database matching the given weight.
	 * @param weight the weight of the importance level to be returned.
	 * @return the importance level matching the given weight.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public ImportanceDto getByWeight(Integer weight);
	
	/**
	 * Returns all importance levels from the database matching the given description.
	 * @param description the description of all importance levels to be returned.
	 * @return the importance levels matching the given description.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ImportanceDto> getByDescription(String description);
	
	/**
	 * Returns all importance levels from the database.
	 * @return all importance levels.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<ImportanceDto> getAll();
	
	/**
	 * Creates the given importance level in the database. The importance level must not
	 * be present and the weight must be valid.
	 * @param importance the importance level to be created.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void create(ImportanceDto importance);
	
	/**
	 * Updates the given importance level in the database. The importance level must
	 * be present and have a valid weight.
	 * @param importance the importance level to be updated.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void update(ImportanceDto importance);
	
	/**
	 * Deletes the given importance level from the database. Warning: Watch foreign key constraints!
	 * @param importance the importance level to be deleted.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void delete(ImportanceDto importance);
	
}
