package youapp.exception.dataaccess;

/**
 * Thrown to indicate that the data inside the database is inconsistent.
 * @author neme
 *
 */
public class InconsistentStateException extends DataAccessLayerException {

	/**
     * 
     */
    private static final long serialVersionUID = -4575513866974453624L;

    public InconsistentStateException() {
		super();
	}
	
	public InconsistentStateException(String message) {
		super(message);
	}
}
