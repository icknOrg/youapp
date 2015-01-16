package youapp.services;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.model.Tag;
import youapp.model.Tag.Category;
import youapp.model.TagSet;

public interface TagService {

	public Tag getById(Long tagId) throws DataAccessLayerException;
	
	public Tag getByNameAndCategory(String name, Tag.Category category) throws DataAccessLayerException;
	
	public TagSet getByPerson(Long personId) throws DataAccessLayerException;
	
	public TagSet getByCategory(Tag.Category category) throws DataAccessLayerException;
	
	public TagSet getAll() throws DataAccessLayerException;
	
	public boolean exists(String name, Tag.Category category) throws DataAccessLayerException;
	
	public Long create(Tag tag) throws DataAccessLayerException, ModelException;
	
	public void delete(Long tagId) throws DataAccessLayerException;
	
	public void addAssociation(Long personId, Long tagId) throws DataAccessLayerException;
	
	public void createAndAssociate(Long personId, TagSet tags) throws DataAccessLayerException, ModelException;
	
	public boolean existsAssociation(Long personId, Long tagId) throws DataAccessLayerException;
	
	public void removeAssociation(Long personId, Long tagId) throws DataAccessLayerException;
	
	public void removeAssociations(Long personId, TagSet tags) throws DataAccessLayerException;
	
	public void removeAllAssociations(Long personId) throws DataAccessLayerException;

	public Integer getNumberOfCommonTagsByCategory(Long personIdA, Long personIdB,
			Category category);

	public Integer getNumberOfTagsByCategory(Long personId, Category category);
}
