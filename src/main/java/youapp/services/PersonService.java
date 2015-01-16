package youapp.services;

import java.util.Date;
import java.util.List;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.Name;
import youapp.model.Person;
import youapp.model.filter.IFilter;

public interface PersonService {
	
	public Person getById(Long id) throws DataAccessLayerException, ModelException;
	
	public Person getByFbId(Long id) throws DataAccessLayerException, ModelException;
	
	public Boolean exists(Long id, boolean fbId) throws DataAccessLayerException;
	
	public Boolean isActive(Long id, boolean fbId) throws DataAccessLayerException;

	public List<Person> getByFilters(IFilter...filters) throws DataAccessLayerException, InconsistentModelException;
	
    public List<Person> getByFilters(Integer offset, Integer resultSize, IFilter... filters) throws DataAccessLayerException, InconsistentModelException;
	
	public List<Person> getByName(String firstName, String lastName) throws DataAccessLayerException, ModelException;
	
	/**
	 * Returns the name of the person with the given id. If the person is not existent, null is returned. If the
	 * person is not active, only the first name and the last name can be provided. The nick name in this case is null.
	 * @param personId the id of the person whose name has to be returned. This is not the facebook id but the YouApp id!
	 * @return the name of the person with the given id.
	 * @throws DataAccessLayerException if there was an error while retrieving the name.
	 */
	public Name getName(Long personId) throws DataAccessLayerException;
	
	/**
	 * Returns the nick name of the person with the given id. If the person is not existent or not activated, null is returned.
	 * @param personId the id of the person whose nick name has to be returned. This is not the facebook id but the YouApp id!
	 * @return the nick name of the person with the given id.
	 * @throws DataAccessLayerException if there was an error while retrieving the nick name.
	 */
	public String getNickName(Long personId) throws DataAccessLayerException;
	
	/**
	 * Returns a person's Facebook id if the provided id is the YouApp id or a person's YouApp id if the provided id is the Facebook id. If the
	 * person with the given id does not exist, null is returned.
	 * @param personId the id of the person whose alternative id (either Facebook id or YouApp id) is returned.
	 * @param fbId true, if the provided person id is the Facebook id, false if the id is the YouApp id.
	 * @return a person's Facebook id if the provided id is the YouApp id or a person's YouApp id if the provided id is the Facebook id.
	 * @throws DataAccessLayerException if there was an error while retrieving the alternative id.
	 */
	public Long getAlternativeId(Long personId, Boolean fbId) throws DataAccessLayerException;
	
	public List<Person> getBestMatchings(Long personId, int minFriend, int offset, int resultSize) throws DataAccessLayerException, ModelException;
	
	public List<Person> getAllFriends(Long personId, int offset, int resultSize, boolean includeProvisional) throws DataAccessLayerException, ModelException;
	
	public List<Person> getRecentFriends(Long personId, int resultSize, boolean includeProvisional) throws DataAccessLayerException, ModelException;
	
	/**
	 * Get all persons.
	 * @return all persons.
	 * DataAccessLayerException if there was an error while retrieving all persons.
	 * @throws ModelException 
	 */
    public List<Person> getAll() throws DataAccessLayerException, ModelException;
	
	public Boolean nickAvailable(String nick) throws DataAccessLayerException;
	
	public Long create(Person person) throws DataAccessLayerException, ModelException;
	
	public void update(Person person) throws DataAccessLayerException, ModelException;
	
	public void updateLastOnline(Long personId, Date lastOnline);
	
	/**
	 * Deactivates a person. Only the person id, facebook id, first name, last name, possibly gender and the person's access level will remain.
	 * Symptoms, medications, pictures or replies associated with the given person are deleted. The nick name of the given person is removed too. 
	 * @param id the id of the person to deactivate. Must not be null.
	 * @throws DataAccessLayerException if there was an error while deactivating the given person.
	 * @throws ModelException if there was an error while deactivating the given person.
	 */
	public void deactivate(Long personId) throws DataAccessLayerException, ModelException;

}
