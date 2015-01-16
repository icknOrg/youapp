package youapp.services;

import java.util.Iterator;
import java.util.List;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.Picture;

public interface PictureService
{
    public Picture getById(Long personId, Integer pictureId) throws DataAccessLayerException;
    
    public List<Picture> getByPerson(Long personId) throws DataAccessLayerException;
       
    public Integer create(Picture picture) throws DataAccessLayerException, ModelException;
    
    public void createAndAssociate(Long personId, Iterator<Picture> pictures) throws InconsistentModelException, DataAccessLayerException, ModelException;
    
    public void update(Picture picture) throws DataAccessLayerException, ModelException;
    
    public void delete(Integer pictureId, Long personId) throws DataAccessLayerException;
    
    public void deleteAll(Long personId) throws DataAccessLayerException;
    
    public Integer getValidPictureId(Long personId);
}
