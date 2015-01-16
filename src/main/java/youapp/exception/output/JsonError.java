package youapp.exception.output;


public class JsonError
{

    private final String message;

    private String title;

    public String getMessage()
    {
        return message;
    }

    public String getTitle()
    {
        return title;
    }

    public JsonError(String message, String title)
    {
        this.message = message;
        this.title = title;
    }
}
