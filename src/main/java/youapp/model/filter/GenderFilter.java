package youapp.model.filter;

import youapp.dataaccess.dao.filter.IFilterTranslator;

public class GenderFilter implements IFilter
{
    public enum Gender
    {
        MALE, FEMALE
    }

    private Gender gender;

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public GenderFilter(Gender gender)
    {
        this.gender = gender;
    }
    
    @Override
    public String visitFilter(IFilterTranslator translator)
    {
        return translator.getFilterString(this);
    }
}
