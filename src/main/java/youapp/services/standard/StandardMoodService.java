package youapp.services.standard;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.IMoodDao;
import youapp.dataaccess.dto.MoodDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.model.Mood;
import youapp.services.MoodService;

public class StandardMoodService
    implements MoodService
{
    /**
     * Logger.
     */
    static final Log log = LogFactory.getLog(StandardQuestionService.class);

    private IMoodDao moodDao;

    @Autowired
    public void setMoodDao(IMoodDao moodDao)
    {
        this.moodDao = moodDao;
    }

    @Override
    public Mood getById(Integer id) throws DataAccessLayerException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Mood id must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Retrieving mood with id: " + id);
        }

        MoodDto moodDto = moodDao.getById(id);
        return reassembleMood(moodDto);
    }

    @Override
    public List<Mood> getByRating(Integer rating) throws DataAccessLayerException
    {
        if (rating == null)
        {
            throw new IllegalArgumentException("Rating must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving mood with rating: " + rating);
        }

        List<MoodDto> moodDtos = moodDao.getByRating(rating);
        List<Mood> moods = new LinkedList<Mood>();
        for (MoodDto m : moodDtos)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found mood: " + m.toString());
            }
            moods.add(reassembleMood(m));
        }
        return moods;
    }

    @Override
    public List<Mood> getByDescription(String description) throws DataAccessLayerException
    {
        if (description == null)
        {
            throw new IllegalArgumentException("Description must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving mood with description: " + description);
        }

        List<MoodDto> moodDtos = moodDao.getByDescription(description);
        List<Mood> moods = new LinkedList<Mood>();
        for (MoodDto m : moodDtos)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found mood: " + m.toString());
            }
            moods.add(reassembleMood(m));
        }
        return moods;
    }

    @Override
    public List<Mood> getByRatingDescription(Integer rating, String description) throws DataAccessLayerException
    {
        if (rating == null)
        {
            throw new IllegalArgumentException("Rating must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving mood with description: " + description + " and rating" + rating);
        }

        List<MoodDto> moodDtos = moodDao.getByRatingDescription(rating, description);
        List<Mood> moods = new LinkedList<Mood>();
        for (MoodDto m : moodDtos)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found mood: " + m.toString());
            }
            moods.add(reassembleMood(m));
        }
        return moods;
    }

    @Override
    public List<Mood> getAll() throws DataAccessLayerException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving all moods");
        }

        List<MoodDto> moodDtos = moodDao.getAll();
        List<Mood> moods = new LinkedList<Mood>();
        for (MoodDto m : moodDtos)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found mood: " + m.toString());
            }
            moods.add(reassembleMood(m));
        }
        return moods;
    }

    @Override
    public boolean exists(Integer rating, String description) throws DataAccessLayerException
    {
        if (rating == null)
        {
            throw new IllegalArgumentException("Rating must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Check whether mood with given rating and description exists: " + rating + " - " + description);
        }
        return moodDao.exists(rating, description);
    }

    @Override
    public Integer create(Mood mood) throws DataAccessLayerException, ModelException
    {
        if (mood == null)
        {
            throw new IllegalArgumentException("Mood must not be null.");
        }
        if (mood.getId() != null)
        {
            throw new IllegalArgumentException("Mood id must not be set.");
        }
        if (mood.getRating() == null)
        {
            throw new IllegalArgumentException("Mood rating must not be null.");
        }

        // Perform validation checks.
        validateMood(mood);

        // Create new mood in the database
        MoodDto moodDto = disassembleMood(mood);
        if (log.isDebugEnabled())
        {
            log.debug("Creating mood: " + mood);
        }
        if (exists(mood.getRating(), mood.getDescription()))
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Mood already exists! Stopped creation.");
            }
            List<Mood> moods = getByRatingDescription(mood.getRating(), mood.getDescription());
            if (moods.size() > 0)
            {
                mood.setId(moods.get(0).getId());
                return moods.get(0).getId();
            }
            else
            {
                return null;
            }
        }
        Integer generatedId = moodDao.create(moodDto);
        if (log.isDebugEnabled())
        {
            log.debug(">>> Created mood with id: " + generatedId);
        }
        return generatedId;
    }

    @Override
    public void update(Mood mood) throws DataAccessLayerException, ModelException
    {
        if (mood == null)
        {
            throw new IllegalArgumentException("Mood must not be null.");
        }
        if (mood.getId() == null)
        {
            throw new IllegalArgumentException("Mood id must be set.");
        }
        if (mood.getRating() == null)
        {
            throw new IllegalArgumentException("Mood rating must not be null.");
        }

        // Perform validation checks.
        validateMood(mood);

        // Create new mood in the database
        MoodDto moodDto = disassembleMood(mood);
        if (log.isDebugEnabled())
        {
            log.debug("Update mood: " + mood);
        }
        moodDao.update(moodDto);
    }

    @Override
    public void delete(Integer id) throws DataAccessLayerException, ModelException
    {
        if (id == null) {
            throw new IllegalArgumentException("Mood id must not be null.");
        }
        if (log.isDebugEnabled()) {
            log.debug("Deleting mood with id: " + id);
        }
        
        // Deleting the mood from the database.
        moodDao.delete(id);
        if (log.isDebugEnabled()) {
            log.debug("Deleted mood with id: " + id);
        }
    }

    public static MoodDto disassembleMood(Mood mood)
    {
        if (mood == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        MoodDto moodDto = new MoodDto();
        moodDto.setId(mood.getId());
        moodDto.setRating(mood.getRating());
        moodDto.setDescription(mood.getDescription());
        return moodDto;
    }

    public static Mood reassembleMood(MoodDto moodDto)
    {
        if (moodDto == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        // Validate.
        validateMood(moodDto);

        // Reassemble.
        if (log.isDebugEnabled())
        {
            log.debug("Reassembling mood: " + moodDto.toString());
        }
        Mood mood = new Mood();
        mood.setId(moodDto.getId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Id: " + moodDto.getId());
        }
        mood.setRating(mood.getRating());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Rating: " + moodDto.getRating());
        }
        mood.setDescription(mood.getDescription());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Description: " + moodDto.getDescription());
        }
        return mood;
    }

    public static void validateMood(Mood mood)
    {
        if (mood == null)
        {
            throw new IllegalArgumentException("Mood must not be null.");
        }
        if (mood.getRating() == null)
        {
            throw new IllegalArgumentException("Rating must not be null.");
        }
    }

    public static void validateMood(MoodDto moodDto)
    {
        if (moodDto == null)
        {
            throw new IllegalArgumentException("Mood must not be null.");
        }
        if (moodDto.getRating() == null)
        {
            throw new IllegalArgumentException("Rating must not be null.");
        }
    }

}
