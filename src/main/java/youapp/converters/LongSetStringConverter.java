package youapp.converters;

import java.util.Iterator;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;

public class LongSetStringConverter
    implements Converter<Set<Long>, String>
{

    @Override
    public String convert(Set<Long> longs)
    {
        StringBuilder sb = new StringBuilder();
        for (Iterator<Long> iterator = longs.iterator(); iterator.hasNext();)
        {
            sb.append(iterator.next());
            if (iterator.hasNext())
            {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
