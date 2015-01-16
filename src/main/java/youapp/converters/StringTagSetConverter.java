package youapp.converters;

import org.springframework.core.convert.converter.Converter;

import youapp.model.Tag;
import youapp.model.TagSet;

public class StringTagSetConverter
    implements Converter<String, TagSet>
{

    @Override
    public TagSet convert(String tagsString)
    {
        TagSet tags = new TagSet();
        if ((tagsString != null) && !tagsString.isEmpty())
        {
            String[] splitted = tagsString.split(",");

            for (String s : splitted)
            {
                Tag newTag = new Tag();
                newTag.setName(s);
                tags.add(newTag);
            }
        }
        return tags;
    }

}
