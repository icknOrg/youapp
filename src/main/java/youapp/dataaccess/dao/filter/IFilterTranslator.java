package youapp.dataaccess.dao.filter;

import youapp.model.filter.AgeFilter;
import youapp.model.filter.FirstNameFilter;
import youapp.model.filter.GenderFilter;
import youapp.model.filter.IFilter;
import youapp.model.filter.LastNameFilter;
import youapp.model.filter.NameFilter;
import youapp.model.filter.PersonBlockedFilter;
import youapp.model.filter.SoulmatesFilter;

public interface IFilterTranslator {

	public abstract String getFilterString(AgeFilter filter);

	public abstract String getFilterString(FirstNameFilter filter);

    public abstract String getFilterString(LastNameFilter filter);
    
    public abstract String getFilterString(NameFilter filter);

    public abstract String getFilterString(PersonBlockedFilter filter);

    public abstract String getFilterString(GenderFilter filter);

    public abstract String getComposedFilterStrings(IFilter... filters);

    public abstract String getFilterString(SoulmatesFilter soulmatesFilter);

}