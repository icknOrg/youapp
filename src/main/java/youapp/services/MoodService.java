package youapp.services;

import java.util.List;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.model.Mood;

public interface MoodService
{
    public Mood getById(Integer id) throws DataAccessLayerException;
    
    public List<Mood> getByRating(Integer rating) throws DataAccessLayerException;
    
    public List<Mood> getByDescription(String description) throws DataAccessLayerException;
    
    public List<Mood> getByRatingDescription(Integer rating, String description) throws DataAccessLayerException;
    
    public List<Mood> getAll() throws DataAccessLayerException;
    
    public boolean exists(Integer rating, String description) throws DataAccessLayerException;
    
    public Integer create(Mood mood) throws DataAccessLayerException, ModelException;
    
    public void update(Mood mood) throws DataAccessLayerException, ModelException;
    
    public void delete(Integer id) throws DataAccessLayerException, ModelException;    
}
