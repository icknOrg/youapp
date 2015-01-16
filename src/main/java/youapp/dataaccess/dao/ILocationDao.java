package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.LocationDto;
import youapp.model.Location;

/**
 * Represents an SQL data access object for a location.
 * @author neme
 *
 */
public interface ILocationDao {

	/**
	 * Returns the location from the database matching the given ids.
	 * @param locationId the id of the location to be returned.
	 * @locationName the name of the location
	 * @return the location matching the given ids and name.	 * 
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 * 
	 * the location is identify by the pare id and name because same location could have different names 
	 * (ex in Switzerland many locations have both german and french names )	 */
	LocationDto getByIdAndName(Integer locationId, String locationName);
	
	/**
	 * Returns all locations from the database matching the given name.
	 * @param name the name of all cities to be returned.
	 * @return all locations matching the given name.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public List<LocationDto> getByName(String name);
	
	/**
	 * Checks whether a location with the given name exists in the database.
	 * @param name the location name.
	 * @param longitude
	 * @param latitude
	 * @return true, if a location with the given name exists, false otherwise.
	 */
	boolean exists(Location location);
	
	/**
	 * Creates the given location in the database. The location must not be present and
	 * the location id must not be null.
	 * @param location the location to be created.
	 * org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void create(LocationDto location);
	
	/**
	 * Updates the given location in the database. The location must be present and have
	 * a valid id.
	 * @param location the location to be updated.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void update(LocationDto location);
	
	/**
	 * Deletes the given location from the database. Warning: Watch foreign key constraints!
	 * @param location location to be deleted.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void delete(LocationDto location);

	

	
}
