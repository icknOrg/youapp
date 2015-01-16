package youapp.converters;

import java.util.Iterator;

import org.springframework.core.convert.converter.Converter;

import youapp.model.Tag;
import youapp.model.TagSet;

public class TagSetStringConverter
    implements Converter<TagSet, String>
{

    @Override
    public String convert(TagSet tags)
    {
        StringBuilder sb = new StringBuilder();
        for (Iterator<Tag> iterator = tags.iterator(); iterator.hasNext();)
        {
            Tag tag = iterator.next();
            sb.append(tag.getName());
            if(iterator.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
