package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.AccessLevelDto;

/**
 * Represents an SQL data access object for an access level.
 * @author neme
 *
 */
public interface IAccessLevelDao {

	/**
	 * Returns the access level from the database matching the given id.
	 * @param id the id of the access level to be returned.
	 * @return the access level matching the given id.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public AccessLevelDto getById(Integer id);
	
	/**
	 * Returns the access level from the database matching the given description.
	 * @param description the description of the access level to be returned.
	 * @return the access level matching the given description.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public AccessLevelDto getByDescription(String description);
	
	/**
	 * Returns all access levels from the database.
	 * @return all access levels.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<AccessLevelDto> getAll();
	
	/**
	 * Creates the given access level in the database. The access level must not be present, the
	 * access level id must be null and the description must be unique.
	 * @param accessLevel the access level to be created.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void create(AccessLevelDto accessLevel);
	
	/**
	 * Updates the given access level in the database. The access level must be present and have
	 * a valid access level id.
	 * @param accessLevel the access level to be updated.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void update(AccessLevelDto accessLevel);
	
	/**
	 * Deletes the given access level from the database. Warning: Watch foreign key constraints!
	 * @param accessLevel the access level to be deleted.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void delete(AccessLevelDto accessLevel);
	
}
