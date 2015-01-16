package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.MoodDto;

public interface IMoodDao
{
    public MoodDto getById(Integer id);

    public List<MoodDto> getByRating(Integer rating);

    public List<MoodDto> getByDescription(String description);
    
    public List<MoodDto> getByRatingDescription(Integer rating, String description);

    public List<MoodDto> getAll();
    
    public boolean exists(Integer rating, String description);

    public Integer create(MoodDto mood);

    public void update(MoodDto mood);

    public void delete(Integer id);
}
