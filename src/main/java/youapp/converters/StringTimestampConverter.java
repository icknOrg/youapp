package youapp.converters;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;

public class StringTimestampConverter implements Converter<String, Timestamp>
{

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(StringTimestampConverter.class);
    
    @Override
    public Timestamp convert(String timestamp)
    {
        if (log.isDebugEnabled()) {
            log.debug("Converting string to timestamp: " + timestamp);
        }
        if(timestamp == null)
               return null;
        
        return Timestamp.valueOf(timestamp);
    }

}
