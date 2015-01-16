package youapp.services;

import java.sql.Timestamp;
import java.util.List;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.StatusUpdate;

public interface StatusUpdateService {
	public StatusUpdate getById(Long personId, Timestamp when) throws DataAccessLayerException, ModelException;

    public StatusUpdate getLastByPerson(Long personId) throws DataAccessLayerException, ModelException;
	
	public List<StatusUpdate> getByPerson(Long personId) throws InconsistentStateException, DataAccessLayerException, ModelException;

	public List<StatusUpdate> getByDate(Timestamp when) throws InconsistentStateException, DataAccessLayerException, ModelException;

    public List<StatusUpdate> getByPersonAndSoulmates(Long personId, Integer offset, Integer resultSize)
        throws DataAccessLayerException,
        ModelException;

    public List<StatusUpdate> getByPersonAndSoulmates(Long personId, Timestamp startDate, Integer offset, Integer resultSize)
        throws DataAccessLayerException,
        ModelException;

	public boolean exists(Long personId, Timestamp when);

	public boolean exists(Long personId);
	
	public void create(StatusUpdate statusUpdate) throws InconsistentModelException, DataAccessLayerException, ModelException;

	public void update(StatusUpdate statusUpdate) throws InconsistentModelException, DataAccessLayerException, ModelException;

	public void delete(Long personId, Timestamp when);

	public void deleteAll(Long personId);
   
}
