package youapp.services;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.model.AccessLevel;

public interface AccessLevelService {

	public AccessLevel getDefault() throws DataAccessLayerException;
	
}
