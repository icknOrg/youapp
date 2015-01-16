package youapp.utility;

import java.sql.Timestamp;
import java.util.Locale;

import org.ocpsoft.pretty.time.PrettyTime;
import org.springframework.stereotype.Component;

@Component
public class TimeUtility
{
    public String getElapsedTime(Timestamp timestamp, Locale locale)
    {
        PrettyTime p = new PrettyTime(locale);
        return p.format(timestamp);
    }
}
