package youapp.services.standard;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.IPictureDao;
import youapp.dataaccess.dto.PictureDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.Mood;
import youapp.model.Picture;
import youapp.services.MoodService;
import youapp.services.PictureService;

public class StandardPictureService
    implements PictureService
{
    /**
     * Logger.
     */
    static final Log log = LogFactory.getLog(StandardQuestionService.class);

    private MoodService moodService;

    private IPictureDao pictureDao;

    @Autowired
    public void setMoodService(MoodService moodService)
    {
        this.moodService = moodService;
    }

    @Autowired
    public void setPictureDao(IPictureDao pictureDao)
    {
        this.pictureDao = pictureDao;
    }

    @Override
    public Picture getById(Long personId, Integer pictureId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (pictureId == null)
        {
            throw new IllegalArgumentException("Picture id must not be null.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Retrieving picture by id. Person id: " + personId + ", picture id: " + pictureId);
        }

        PictureDto pictureDto = pictureDao.getById(personId, pictureId);
        Mood mood = null;
        if (pictureDto.getMood() != null)
        {
            mood = moodService.getById(pictureDto.getMood());
        }
        return reassemblePicture(pictureDto, mood);
    }

    @Override
    public List<Picture> getByPerson(Long personId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving picture by id. Person id: " + personId);
        }

        List<PictureDto> pictureDtos = pictureDao.getByPerson(personId);
        List<Picture> pictures = new LinkedList<Picture>();
        for (PictureDto pictureDto : pictureDtos)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found picture: " + pictureDto.toString());
            }
            Mood mood = null;
            if (pictureDto.getMood() != null)
            {
                mood = moodService.getById(pictureDto.getMood());
            }
            pictures.add(reassemblePicture(pictureDto, mood));
        }
        return pictures;
    }
    
    @Override
    public Integer create(Picture picture) throws DataAccessLayerException, ModelException
    {
        if (picture == null)
        {
            throw new IllegalArgumentException("Picture must not be null.");
        }

        // Perform validation checks.
        validatePicture(picture, true);

        if (picture.getPictureId() != null)
        {
            if (exists(picture.getPersonId(), picture.getPictureId()))
            {
                if (log.isDebugEnabled())
                {
                    log.debug(">>> Picture already exists! Stopped creation.");
                }
                return getById(picture.getPersonId(), picture.getPictureId()).getPictureId();
            }
        }
        else
        {
            picture.setPictureId(this.getValidPictureId(picture.getPersonId()));
        }

        Integer moodId = null;
        if (picture.getMood() != null)
        {
            moodId = moodService.create(picture.getMood());
        }

        PictureDto pictureDto = disassemblePicture(picture, moodId);
        if (log.isDebugEnabled())
        {
            log.debug("Creating picture: " + pictureDto.toString());
        }

        pictureDao.create(pictureDto);
        if (log.isDebugEnabled())
        {
            log.debug(">>> Created picture with id: " + pictureDto.getPictureId());
        }

        return pictureDto.getPictureId();
    }

    @Override
    public void createAndAssociate(Long personId, Iterator<Picture> pictures)
        throws DataAccessLayerException,
        ModelException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (pictures == null)
        {
            throw new IllegalArgumentException("Pictures must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Creating pictures and corresponding associations if not present.");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Creating new pictures for person with id: " + personId);
        }

        Picture newPicture = null;
        Integer pictureId = this.getValidPictureId(personId);
        while (pictures.hasNext())
        {
            // Because this person is new (it's created right now) there
            // cannot be
            // any pictures associated with that person already. Therefore a
            // new
            // picture id is created for every picture!!
            newPicture = pictures.next();
            newPicture.setPersonId(personId);
            newPicture.setPictureId(pictureId);
            if (log.isTraceEnabled())
            {
                log.trace(">>> Next id: " + pictureId);
            }

            this.create(newPicture);
            pictureId = pictureId + 1;
        }
    }

    @Override
    public void update(Picture picture) throws DataAccessLayerException, ModelException
    {
        if (picture == null)
        {
            throw new IllegalArgumentException("Picture must not be null.");
        }
        if (picture.getPersonId() == null)
        {
            throw new IllegalArgumentException("Picture's person id must be set.");
        }
        if (picture.getPictureId() == null)
        {
            throw new IllegalArgumentException("Picture id must not be null.");
        }

        // Perform validation checks.
        validatePicture(picture, true);

        Integer moodId = null;
        if (picture.getMood() != null)
        {
            moodService.update(picture.getMood());
            moodId = picture.getMood().getId();
        }

        // Create new mood in the database
        PictureDto pictureDto = disassemblePicture(picture, moodId);
        if (log.isDebugEnabled())
        {
            log.debug("Update picture: " + picture.toString());
        }
        pictureDao.update(pictureDto);
    }

    @Override
    public void delete(Integer pictureId, Long personId) throws DataAccessLayerException
    {
        if (pictureId == null)
        {
            throw new IllegalArgumentException("Picture id must not be null.");
        }
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Deleting picture with id: " + pictureId);
        }

        // Deleting the picture from the database.
        pictureDao.delete(pictureId, personId);
        if (log.isDebugEnabled())
        {
            log.debug("Deleted picture with id: " + pictureId);
        }
    }

    @Override
    public void deleteAll(Long personId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }

        pictureDao.deleteAll(personId);
        if (log.isDebugEnabled())
        {
            log.debug("Deleted all pictures of person: " + personId);
        }
    }

    @Override
    public Integer getValidPictureId(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }

        return pictureDao.getValidPictureId(personId);
    }

    public static PictureDto disassemblePicture(Picture picture, Integer moodId)
    {
        if (picture == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        PictureDto pictureDto = new PictureDto();
        pictureDto.setPictureId(picture.getPictureId());
        pictureDto.setPersonId(picture.getPersonId());
        pictureDto.setCaption(picture.getCaption());
        pictureDto.setMood(moodId);
        pictureDto.setPictureFormatName(picture.getPictureFormatName());
        pictureDto.setThumbnailFormatName(picture.getThumbnailFormatName());
        
        Blob image = imageToBlob(picture.getPicture(), picture.getPictureFormatName());
        if (image != null)
        {
            pictureDto.setPicture(image);
        }

        if (picture.getThumbnail() != null)
        {
            Blob thumbnail = imageToBlob(picture.getThumbnail(), picture.getThumbnailFormatName());
            if (thumbnail != null)
            {
                pictureDto.setThumbnail(thumbnail);
            }
        }

        return pictureDto;
    }

    public static Picture reassemblePicture(PictureDto pictureDto, Mood mood) throws InconsistentStateException
    {
        // Check parameter null pointers.
        if (pictureDto == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        // Validate.
        validatePicture(pictureDto, false);

        // Reassemble.
        if (log.isDebugEnabled())
        {
            log.debug("Reassembling picture: " + pictureDto.toString());
        }

        Picture picture = new Picture();
        picture.setPictureId(pictureDto.getPictureId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Id: " + pictureDto.getPictureId());
        }
        picture.setPersonId(pictureDto.getPersonId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Person id: " + pictureDto.getPersonId());
        }
        if (pictureDto.getCaption() != null)
        {
            picture.setCaption(pictureDto.getCaption());
            if (log.isDebugEnabled())
            {
                log.debug(">>> Caption: " + pictureDto.getCaption());
            }
        }

        if (pictureDto.getPicture() == null)
        {
            throw new InconsistentStateException("No picture data set for the picture with id: "
                + pictureDto.getPictureId());
        }
        BufferedImage image = blobToImage(pictureDto.getPicture());
        if (image != null)
        {
            picture.setPicture(image);
        }
        
        picture.setPictureFormatName(pictureDto.getPictureFormatName());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Picture Format Name: " + pictureDto.getPictureFormatName());
        }

        if (pictureDto.getThumbnail() != null)
        {
            BufferedImage thumbnail = blobToImage(pictureDto.getThumbnail());
            if (thumbnail != null)
            {
                picture.setThumbnail(thumbnail);
            }
        }
        
        picture.setThumbnailFormatName(pictureDto.getThumbnailFormatName());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Thumbnail Format Name: " + pictureDto.getThumbnailFormatName());
        }
        
        if (pictureDto.getMood() != null)
        {
            picture.setMood(mood);
            if (log.isDebugEnabled())
            {
                log.debug(">>> Mood: " + pictureDto.getMood());
            }
        }

        return picture;
    }

    private boolean exists(Long personId, Integer pictureId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (pictureId == null)
        {
            throw new IllegalArgumentException("Picture id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Check whether picture with id " + pictureId + " already exists.");
        }
        return pictureDao.exists(personId, pictureId);
    }
    
    public static void validatePicture(PictureDto picture, boolean isNew) throws InconsistentStateException
    {
        if (picture == null)
        {
            throw new IllegalArgumentException("Picture must not be null.");
        }
        if (isNew)
        {
            if (picture.getPictureId() != null)
            {
                throw new InconsistentStateException("Picture id must not be set.");
            }
        }
        else
        {
            if (picture.getPictureId() == null)
            {
                throw new InconsistentStateException("Picture id must not be null.");
            }
        }
        if (picture.getPersonId() == null)
        {
            throw new InconsistentStateException("Person id must not be null.");
        }

        if (picture.getPicture() == null)
        {
            throw new InconsistentStateException("Picture blob must not be null.");
        }
        else
        {
            if (picture.getPictureFormatName() == null)
            {
                throw new InconsistentStateException("Picture format name must not be null.");
            }
        }

        if ((picture.getThumbnail() != null) && picture.getThumbnailFormatName() == null)
        {
            throw new InconsistentStateException("Thumbnail format name must not be null.");
        }
    }

    public static void validatePicture(Picture picture, boolean isPersonIdSet) throws InconsistentModelException
    {
        if (picture == null)
        {
            throw new IllegalArgumentException("Picture must not be null.");
        }
        
        if (isPersonIdSet)
        {
            if (picture.getPersonId() == null)
            {
                throw new InconsistentModelException("Person id must not be null.");
            }
        }
        else
        {
            if (picture.getPersonId() != null)
            {
                throw new InconsistentModelException("Person id must be null.");
            }
        }
        
        if (!picture.pictureDataSet())
        {
            throw new InconsistentModelException("Picture data must not be null.");
        }
        
        if (picture.getPicture() == null)
        {
            throw new InconsistentModelException("Picture blob must not be null.");
        }
        else
        {
            if (picture.getPictureFormatName() == null)
            {
                throw new InconsistentModelException("Picture format name must not be null.");
            }
        }

        if ((picture.getThumbnail() != null) && picture.getThumbnailFormatName() == null)
        {
            throw new InconsistentModelException("Thumbnail format name must not be null.");
        }
    }

    public static Blob imageToBlob(BufferedImage image, String imageFormatName)
    {
        if (image == null)
        {
            throw new IllegalArgumentException("Image must not be null.");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            ImageIO.write(image, imageFormatName, baos);
            baos.flush();
            return new SerialBlob(baos.toByteArray());
        }
        catch (IOException e)
        {
            if (log.isErrorEnabled())
            {
                log.error(e.getMessage());
            }
            return null;
        }
        catch (SerialException e)
        {
            if (log.isErrorEnabled())
            {
                log.error(e.getMessage());
            }
            return null;
        }
        catch (SQLException e)
        {
            if (log.isErrorEnabled())
            {
                log.error(e.getMessage());
            }
            return null;
        }
    }

    public static BufferedImage blobToImage(Blob image)
    {
        if (image == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        try
        {
            InputStream input = image.getBinaryStream();
            return ImageIO.read(input);
        }
        catch (IOException e)
        {
            if (log.isErrorEnabled())
            {
                log.error(e.getMessage());
            }
            return null;
        }
        catch (SQLException e)
        {
            if (log.isErrorEnabled())
            {
                log.error(e.getMessage());
            }
            return null;
        }
    }
}
