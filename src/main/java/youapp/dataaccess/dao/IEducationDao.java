package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.EducationDto;

/**
 * Represents an SQL data access object for an education type.
 * @author neme
 *
 */
public interface IEducationDao {

	/**
	 * Returns the education from the database matching the given id.
	 * @param id the id of the education to be returned.
	 * @return the education matching the given id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public EducationDto getById(Integer id);
	
	/**
	 * Returns all education types from the database matching the given name.
	 * @param name the name of all education types to be returned.
	 * @return the education types matching the given name.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<EducationDto> getByName(String name);
	
	/**
	 * Returns all education types from the database.
	 * @return all education types.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<EducationDto> getAll();
	
	/**
	 * Creates the given education in the database. The education must not be present
	 * and the id must not be set.
	 * @param education the education to be created.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void create(EducationDto education);
	
	/**
	 * Updates the given education in the database. The education must be present and
	 * have a valid education id.
	 * @param education the education to be updated.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void update(EducationDto education);
	
	/**
	 * Deletes the given education from the database. Warning: Watch foreign key constraints!
	 * @param education the education to be deleted.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void delete(EducationDto education);
	
}
