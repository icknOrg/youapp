package youapp.converters;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;

public class TimestampStringConverter implements Converter<Timestamp, String>
{
    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(TimestampStringConverter.class);
    
    @Override
    public String convert(Timestamp timestamp)
    {
        if (log.isDebugEnabled()) {
            log.debug("Converting timestamp to string: " + timestamp);
        }
        if(timestamp == null)
               return null;
        
        return timestamp.toString();
    }

}
