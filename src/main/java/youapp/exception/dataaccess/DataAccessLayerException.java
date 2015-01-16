package youapp.exception.dataaccess;

/**
 * Thrown to indicate that an error occurred while accessing data from the databse.
 * @author neme
 *
 */
public class DataAccessLayerException extends Exception {
	
	/**
     * 
     */
    private static final long serialVersionUID = -4830477010344042370L;

    public DataAccessLayerException() {
		super();
	}
	
	public DataAccessLayerException(String message) {
		super(message);
	}
	
}
