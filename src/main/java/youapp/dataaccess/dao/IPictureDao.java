package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.PictureDto;

/**
 * Represents an SQL data access object for a picture.
 * @author neme
 *
 */
public interface IPictureDao {

	/**
	 * 
	 * @param personId
	 * @param pictureId
	 * @return
	 */
	public PictureDto getById(Long personId, Integer pictureId);
	
	/**
	 * 
	 * @param personId
	 * @return
	 */
	public List<PictureDto> getByPerson(Long personId);
	
	/**
	 * Creates the given picture in the database. The picture must not be present and the
	 * person id as well as the picture id must not be null.
	 * @param picture the picture to be created.
	 * org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 * @return 
	 */
	public void create(PictureDto picture);
	
	/**
	 * Updates the given picture in the database. The picture must be present and have
	 * a valid id.
	 * @param picture the picture to be updated.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void update(PictureDto picture);
	
	/**
	 * Deletes the given picture from the database. Warning: Watch foreign key constraints!
	 * @param pictureId the id of the picture to be deleted.
	 * @param personId the owner's id of the picture to be deleted.
	 * @throws org.springframework.dao.DataAccessException if an error occurs while accessing the database.
	 */
	public void delete(Integer pictureId, Long personId);
	
	/**
	 * Deletes all pictures of the person with the given id.
	 * @param personId the id of the owner whose pictures are deleted.
	 */
	public void deleteAll(Long personId);
	
	/**
	 * Returns the next valid picture id which can be used for creating a new picture for the given person.
	 * @return the next valid picture id for a person with the given id.
	 */
	public Integer getValidPictureId(Long personId);

    public boolean exists(Long personId, Integer pictureId);
}
