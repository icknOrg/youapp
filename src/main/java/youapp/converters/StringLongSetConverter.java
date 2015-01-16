package youapp.converters;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringLongSetConverter
    implements Converter<String, Set<Long>>
{
    @Override
    public Set<Long> convert(String string)
    {
        Set<Long> longs = new HashSet<Long>();
        if (!StringUtils.isBlank(string))
        {
            String[] splitted = string.split(",");
            for (String s : splitted)
            {
                if (!StringUtils.isBlank(s))
                {
                    longs.add(Long.valueOf(s));
                }
            }
        }
        return longs;
    }

}
