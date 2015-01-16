package youapp.exception;

public class NotFoundException
    extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 8868233660961405009L;

    public NotFoundException()
    {
        super();
    }

    public NotFoundException(String message)
    {
        super(message);
    }
}
