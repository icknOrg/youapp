package youapp.services;

import java.util.List;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.PersonBlocked;

public interface PersonBlockerService
{
    public PersonBlocked getById(Long blockerId, Long blockedId) throws DataAccessLayerException, ModelException;

    /**
     * Get all PersonBlocked objects of the person, which blocks other persons
     * 
     * @param blockerId Id of the person
     * @return List of PersonBlocked objects
     * @throws InconsistentStateException
     * @throws InconsistentModelException
     * @throws ModelException
     * @throws DataAccessLayerException
     */
    public List<PersonBlocked> getByBlocker(Long blockerId)
        throws InconsistentModelException,
        InconsistentStateException,
        DataAccessLayerException,
        ModelException;

    /**
     * Get all PersonBlocked objects of the person, which is blocked by other
     * persons
     * 
     * @param blockerId Id of the person
     * @return List of PersonBlocked objects
     * @throws InconsistentStateException
     * @throws InconsistentModelException
     * @throws ModelException
     * @throws DataAccessLayerException
     */
    public List<PersonBlocked> getByBlocked(Long blockedId)
        throws InconsistentModelException,
        InconsistentStateException,
        DataAccessLayerException,
        ModelException;

    public boolean blockedAnyDirection(Long personAId, Long personBId);

    public boolean exists(Long blockerId, Long blockedId);

    public void create(PersonBlocked personBlocked) throws InconsistentModelException, DataAccessLayerException;

    public void delete(Long blockerId, Long blockedId);

}
