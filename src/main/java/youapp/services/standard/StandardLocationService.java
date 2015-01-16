package youapp.services.standard;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.ILocationDao;
import youapp.dataaccess.dto.LocationDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.model.Location;
import youapp.services.LocationService;

public class StandardLocationService implements LocationService {

	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(StandardLocationService.class);
	
	private ILocationDao locationDao;
	
	@Autowired
	public void setLocationDao(ILocationDao locationDao) {
		this.locationDao = locationDao;
	}
	
	@Override
	public Location createLocation(Location location) throws DataAccessLayerException, InconsistentModelException {
		if (location == null) {
			throw new IllegalArgumentException("Location must not be null.");
		}
		
		// Perform validation checks.
		validateLocation(location);
		
		// Create new location in the database.
		LocationDto locationDto = disassembleLocation(location);
		if (log.isDebugEnabled()) {
			log.debug("Creating location: " + location);
		}
		if (existsLocation(location)) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Location already exists! Stopped creation.");
			}
			
			return location;
		}
		
		locationDao.create(locationDto);
		return location;
	}
	
	@Override
	public boolean existsLocation(Location location) throws DataAccessLayerException {
		if (location.getName() == null) {
			throw new IllegalArgumentException("Name must not be null.");
		}
		if (location.getLongitude() == null) {
			throw new IllegalArgumentException("Longitude must not be null.");
		}
		if (location.getLatitude() == null) {
			throw new IllegalArgumentException("Latitude must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Check whether locations in the given name and coordinates exists: " + location.getName());
		}
		
		return locationDao.exists(location);
	}
	
	public static void validateLocation(LocationDto location, boolean isNew) throws InconsistentStateException {
		if (location == null) {
			throw new IllegalArgumentException("Location must not be null.");
		}
		if (isNew) {
			if (location.getId() != null) {
				throw new InconsistentStateException("Location id must not be set.");
			}
		} else {
			if (location.getId() == null) {
				throw new InconsistentStateException("Location id must not be null.");
			}
		}
		if (location.getName() == null) {
			throw new InconsistentStateException("Name must not be null.");
		}
		if (location.getLongitude() == null) {
			throw new InconsistentStateException("Longitude must not be null.");
		}
		if (location.getLatitude() == null) {
			throw new InconsistentStateException("Latitude must not be null.");
		}
	}

	public static void validateLocation(Location location) throws InconsistentModelException {
		if (location == null) {
			throw new IllegalArgumentException("Location must not be null.");
		}
		if (location.getId() == null) {
			throw new InconsistentModelException("Location id must not be null.");
		}		
		if (location.getName() == null) {
			throw new InconsistentModelException("Name must not be null.");
		}
		if (location.getLongitude() == null) {
			throw new InconsistentModelException("Longitude must not be null.");
		}
		if (location.getLatitude() == null) {
			throw new InconsistentModelException("Latitude must not be null.");
		}
	}

	public static LocationDto disassembleLocation(Location location) {
		if (location == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		LocationDto locationDto = new LocationDto();
		locationDto.setId(location.getId());
		locationDto.setName(location.getName());
		locationDto.setLatitude(location.getLatitude());
		locationDto.setLongitude(location.getLongitude());
		return locationDto;
	}

	public static Location reassembleLocation(LocationDto locationDto) throws InconsistentStateException {
		// Check parameter null pointers.
		if (locationDto == null) {
			throw new IllegalArgumentException("Location must not be null.");
		}
		
		// Validate.
		StandardLocationService.validateLocation(locationDto, false);
		
		// Reassemble.
		if (StandardPersonService.log.isDebugEnabled()) {
			StandardPersonService.log.debug("Reassembling location: " + locationDto.toString());
		}
		Integer locationId = locationDto.getId();
		Location location = new Location();
		location.setId(locationId);
		if (StandardPersonService.log.isDebugEnabled()) {
			StandardPersonService.log.debug(">>> Id: " + locationId);
		}
		location.setName(locationDto.getName());
		if (StandardPersonService.log.isDebugEnabled()) {
			StandardPersonService.log.debug(">>> Name: " + locationDto.getName());
		}
		location.setLongitude(locationDto.getLongitude());
		if (StandardPersonService.log.isDebugEnabled()) {
			StandardPersonService.log.debug(">>> Longitude: " + locationDto.getLongitude());
		}
		location.setLatitude(locationDto.getLatitude());
		if (StandardPersonService.log.isDebugEnabled()) {
			StandardPersonService.log.debug(">>> Latitude: " + locationDto.getLatitude());
		}
		return location;
	}

	@Override
	public Location getLocationByIdAndName(Integer locationId, String locationName)
			throws DataAccessLayerException {

		if (locationId == null) {
			throw new IllegalArgumentException("Location id must not be null.");
		}
		
		if (locationName == null) {
			throw new IllegalArgumentException("Location name must not be null.");
		}

		if (log.isDebugEnabled()) {
			log.debug("Retrieving location with id: " + locationId);

		}
		LocationDto locationDto = locationDao.getByIdAndName(locationId,locationName);
		return reassembleLocation(locationDto);

	}

	@Override
	public List<Location> getLocationsByName(String name)
			throws DataAccessLayerException {
		if (name == null) {
			throw new IllegalArgumentException("Location name must not be null.");
		}

		if (log.isDebugEnabled()) {
			log.debug("Retrieving all locations with name: " + name);
		}

		List<LocationDto> locationsDtos = locationDao.getByName(name);

		List<Location> locations = new LinkedList<Location>();

		for (LocationDto c : locationsDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found location: " + c.toString());
			}
			locations.add(reassembleLocation(c));
		}

		return locations;
	}
	

}
