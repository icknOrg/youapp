package youapp.model;

import java.util.HashSet;

public class TagSet
    extends HashSet<Tag>
{
    public TagSet(TagSet tags)
    {
        super(tags);
    }
    
    public TagSet() {
        super();
    }

    private static final long serialVersionUID = 5934250801223417831L;

}
