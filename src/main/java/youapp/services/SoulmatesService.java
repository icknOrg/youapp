package youapp.services;

import java.util.List;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.Soulmates;

public interface SoulmatesService {
	public Soulmates getById(Long requesterId, Long requestedId) throws DataAccessLayerException, ModelException;

	/**
     * Return the soulmates object, regardless of which request direction.
     * @param personAId First person's  id
     * @param personBId Second person's id
     * @return the soulmates object, regardless of which request direction.
     */
    public Soulmates getByIdAnyDirection(Long personAId, Long personBId) throws DataAccessLayerException, ModelException;;
	
	/**
	 * Get all soulmates object of person, which has started being soulmates
	 * @param requesterId id of person, which has started being solmates
	 * @return List of soulmates objects
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public List<Soulmates> getByRequester(Long requesterId) throws DataAccessLayerException, ModelException;

	/**
	 * Get all soulmates object of person, which has started being soulmates
	 * @param requesterId id of person, which has started being solmates
	 * @param requestPending only get soulmates objects, what are the request is pending or not.
	 * @return List of soulmates objects
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public List<Soulmates> getByRequester(Long requesterId, Boolean requestPending) throws DataAccessLayerException, ModelException;

	/**
	 * Get all soulmates object of person, which has started being soulmates
	 * @param requestedId id of person, which has started being solmates
	 * @return List of soulmates objects
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public List<Soulmates> getByRequested(Long requestedId) throws DataAccessLayerException, ModelException;

	/**
	 * Get all soulmates object of person, which hasn't started being soulmates
	 * @param requestedId id of person, which has started being solmates
	 * @param requestPending only get soulmates objects, what are the request is pending or not.
	 * @return List of soulmates objects
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public List<Soulmates> getByRequested(Long requestedId, Boolean requestPending) throws DataAccessLayerException, ModelException;

	/**
	 * Get all soulmates object of person
	 * @param personId id of person, which has started being solmates
	 * @return List of soulmates objects
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public List<Soulmates> getAll(Long personId) throws DataAccessLayerException, ModelException;
	
	/**
	 * Get all soulmates object of person
	 * @param personId id of person, which has started being solmates
	 * @param requestPending only get soulmates objects, what are the request is pending or not.
	 * @return List of soulmates objects
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public List<Soulmates> getAll(Long personId, Boolean requestPending) throws DataAccessLayerException, ModelException;

	/**
	 * Get number of soulmates of person, which has started being soulmates
	 * @param requesterId id of person, which has started being solmates
	 * @return Number of soulmates
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public int getNumberByRequester(Long requesterId);
	/**
	 * Get number of soulmates of person, which has started being soulmates
	 * @param requesterId id of person, which has started being solmates
	 * @param requestPending only get soulmates objects, what are the request is pending or not.
	 * @return Number of soulmates
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public int getNumberByRequester(Long requesterId, Boolean requestPending);
	
	/**
	 * Get number of soulmates of person, which has started being soulmates
	 * @param requestedId id of person, which has started being solmates
	 * @return Number of soulmates
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public int getNumberByRequested(Long requestedId);
	
	/**
	 * Get number of soulmates of person, which hasn't started being soulmates
	 * @param requestedId id of person, which has started being solmates
	 * @param requestPending only get soulmates objects, what are the request is pending or not.
	 * @return Number of soulmates
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public int getNumberByRequested(Long requestedId, Boolean requestPending);
	
	/**
	 * Get number of soulmates of person
	 * @param personId id of person, which has started being solmates
	 * @return Number of soulmates
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public int getNumber(Long personId);
	
	/**
	 * Get number of soulmates of person
	 * @param personId id of person, which has started being solmates
	 * @param requestPending only get soulmates objects, what are the request is pending or not.
	 * @return Number of soulmates
	 * @throws DataAccessLayerException
	 * @throws ModelException
	 */
	public int getNumber(Long personId, Boolean requestPending);
	
	public boolean exists(Long requesterId, Long requestedId);
	
    /**
     * Check if there is a soulmates object, regardless of which request direction.
     * @param personAId First person's  id
     * @param personBId Second person's id
     * @return Boolean if there is a soulmates object, regardless of which request direction.
     */
    public boolean existsAnyDirection(Long personAId, Long personBId);
    
    /**
     * Check if the given persons are soulmates without a request pending.
     * @param personAId First person's  id
     * @param personBId Second person's id
     * @return Boolean if the given persons are soulmates without a request pending.
     */
    public boolean areSoulmates(Long personAId, Long personBId);
	
	public void create(Soulmates soulmates) throws InconsistentModelException, DataAccessLayerException, ModelException;

	public void update(Soulmates soulmates) throws InconsistentModelException;

	public void delete(Long requesterId, Long requestedId);
	
	/**
     * Delete the soulmates object, regardless of which request direction.
     * @param personAId First person's  id
     * @param personBId Second person's id
     */
    public void deleteAnyDirection(Long personAId, Long personBId);
}
