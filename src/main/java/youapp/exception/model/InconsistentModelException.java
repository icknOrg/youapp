package youapp.exception.model;

/**
 * Thrown to indicate that the model data is inconsistent or insufficient.
 * @author neme
 *
 */
public class InconsistentModelException extends ModelException {

	/**
     * 
     */
    private static final long serialVersionUID = 4613720697732290176L;

    public InconsistentModelException() {
		super();
	}
	
	public InconsistentModelException(String message) {
		super(message);
	}
	
}
