package youapp.services;

import java.util.List;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.model.Location;

public interface LocationService {
	
	public Location getLocationByIdAndName(Integer locationId, String locationName)
			throws DataAccessLayerException;
	
	public List<Location> getLocationsByName(String name) throws DataAccessLayerException;
	
	public boolean existsLocation(Location location) throws DataAccessLayerException;
	
	public Location createLocation(Location location) throws DataAccessLayerException, ModelException;


}
