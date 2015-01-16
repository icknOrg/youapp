package youapp.model.filter;

import youapp.dataaccess.dao.filter.IFilterTranslator;

public interface IFilter
{
    public String visitFilter(IFilterTranslator translator);
}
