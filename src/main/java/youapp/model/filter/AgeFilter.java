package youapp.model.filter;

import youapp.dataaccess.dao.filter.IFilterTranslator;

public class AgeFilter
    implements IFilter
{
    private int minAge;

    private int maxAge;

    public AgeFilter(int minAge, int maxAge)
    {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public AgeFilter(int minAge)
    {
        this.minAge = minAge;
        this.maxAge = Integer.MAX_VALUE;
    }

    public int getMinAge()
    {
        return minAge;
    }

    public void setMinAge(int minAge)
    {
        this.minAge = minAge;
    }

    public int getMaxAge()
    {
        return maxAge;
    }

    public void setMaxAge(int maxAge)
    {
        this.maxAge = maxAge;
    }

    @Override
    public String visitFilter(IFilterTranslator translator)
    {
        return translator.getFilterString(this);
    }

}
