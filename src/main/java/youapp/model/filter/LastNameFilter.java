package youapp.model.filter;

import youapp.dataaccess.dao.filter.IFilterTranslator;

public class LastNameFilter implements IFilter
{
    public enum SearchType
    {
        EXACT, CONTAINS
    }

    private String pattern;

    private SearchType searchType;

    public LastNameFilter(String pattern, SearchType searchType)
    {
        this.pattern = pattern;
        this.searchType = searchType;
    }

    public String getPattern()
    {
        return pattern;
    }

    public void setPattern(String pattern)
    {
        this.pattern = pattern;
    }

    public SearchType getSearchType()
    {
        return searchType;
    }

    public void setSearchType(SearchType searchType)
    {
        this.searchType = searchType;
    }
    
    @Override
    public String visitFilter(IFilterTranslator translator)
    {
        return translator.getFilterString(this);
    }
}
