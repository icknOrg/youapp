package youapp.dataaccess.dao.jdbc.filter;

import org.springframework.stereotype.Component;

import youapp.dataaccess.dao.filter.IFilterTranslator;
import youapp.model.filter.AgeFilter;
import youapp.model.filter.FirstNameFilter;
import youapp.model.filter.GenderFilter;
import youapp.model.filter.IFilter;
import youapp.model.filter.LastNameFilter;
import youapp.model.filter.NameFilter;
import youapp.model.filter.PersonBlockedFilter;
import youapp.model.filter.SoulmatesFilter;

@Component
public class JdbcFilterTranslator
    implements IFilterTranslator
{

    /*
     * (non-Javadoc)
     * 
     * @see
     * youapp.dataaccess.dao.jdbc.filter.IFilterTranslator#getFilterString(youapp
     * .model.filter.AgeFilter)
     */
    @Override
    public String getFilterString(AgeFilter filter)
    {
        if (filter == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        if (filter.getMinAge() <= 0 && filter.getMaxAge() == Integer.MAX_VALUE)
        {
            return "";
        }

        if (filter.getMinAge() <= 0)
        {
            return "birthdate >= DATE(NOW()) - INTERVAL " + filter.getMaxAge() + " YEAR";
        }

        if (filter.getMaxAge() == Integer.MAX_VALUE)
        {
            return "birthdate <= DATE(NOW()) - INTERVAL " + filter.getMinAge() + " YEAR";
        }

        return "birthdate between DATE(NOW()) - INTERVAL " + filter.getMaxAge() + " YEAR"
            + " and DATE(NOW()) - INTERVAL " + filter.getMinAge() + " YEAR";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * youapp.dataaccess.dao.jdbc.filter.IFilterTranslator#getFilterString(youapp
     * .model.filter.FirstNameFilter)
     */
    @Override
    public String getFilterString(FirstNameFilter filter)
    {
        if (filter == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        switch (filter.getSearchType())
        {
        case EXACT:
            return "first_name = '" + filter.getPattern() + "'";
        case CONTAINS:
            return "first_name LIKE '%" + filter.getPattern() + "%'";
        default:
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * youapp.dataaccess.dao.jdbc.filter.IFilterTranslator#getFilterString(youapp
     * .model.filter.LastNameFilter)
     */
    @Override
    public String getFilterString(LastNameFilter filter)
    {
        if (filter == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        switch (filter.getSearchType())
        {
        case EXACT:
            return "last_name = '" + filter.getPattern() + "'";
        case CONTAINS:
            return "last_name LIKE '%" + filter.getPattern() + "%'";
        default:
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * youapp.dataaccess.dao.filter.IFilterTranslator#getFilterString(youapp
     * .model.filter.NameFilter)
     */
    @Override
    public String getFilterString(NameFilter filter)
    {
        if (filter == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        final String trimmedPattern = filter.getPattern().trim();

        if (trimmedPattern.contains(" "))
        {
            String[] splitted = trimmedPattern.split(" ", 2);
            switch (filter.getSearchType())
            {
            case EXACT:
                return "first_name = '" + splitted[0] + "' AND last_name = '" + splitted[1] + "'";
            case CONTAINS:
                return "first_name LIKE '%" + splitted[0] + "%' AND last_name LIKE '%" + splitted[1] + "%'";
            default:
                return "";
            }
        }
        else
        {
            switch (filter.getSearchType())
            {
            case EXACT:
                return "first_name = '" + trimmedPattern + "' OR last_name = '" + trimmedPattern + "'";
            case CONTAINS:
                return "first_name LIKE '%" + trimmedPattern + "%' OR last_name LIKE '%" + trimmedPattern + "%'";
            default:
                return "";
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * youapp.dataaccess.dao.filter.IFilterTranslator#getFilterString(youapp
     * .model.filter.PersonBlockedFilter)
     */
    @Override
    public String getFilterString(PersonBlockedFilter filter)
    {
        if (filter == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        String inverter = filter.isInverted() ? "NOT " : "";

        switch (filter.getBlockDirection())
        {
        case BLOCKED:
            return "(" + inverter + "id_person IN (SELECT id_blocker FROM personblocked WHERE id_blocked = "
                + filter.getPersonId() + "))";
        case BLOCKER:
            return "(" + inverter + "id_person IN (SELECT id_blocked FROM personblocked WHERE id_blocker = "
                + filter.getPersonId() + "))";
        case BOTHDIRECTIONS:
            return "(" + inverter + "id_person IN (SELECT id_blocker FROM personblocked WHERE id_blocked = "
                + filter.getPersonId() + " UNION SELECT id_blocked FROM personblocked WHERE id_blocker = "
                + filter.getPersonId() + "))";
        default:
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * youapp.dataaccess.dao.filter.IFilterTranslator#getFilterString(youapp
     * .model.filter.GenderFilter)
     */
    @Override
    public String getFilterString(GenderFilter filter)
    {
        if (filter == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        switch (filter.getGender())
        {
        case MALE:
            return "gender = 'M'";
        case FEMALE:
            return "gender = 'F'";
        default:
            return "";
        }
    }

    @Override
    public String getFilterString(SoulmatesFilter filter)
    {
        if (filter == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        switch (filter.getRequestDirection())
        {
        case REQUESTER:
            if (filter.isRequestPending() != null)
            {
                return "id_person in (select id_requested from soulmates where id_requester = " + filter.getPersonId()
                    + " and request_pending = " + filter.isRequestPending() + ")";
            }
            else
            {
                return "id_person in (select id_requested from soulmates where id_requester = " + filter.getPersonId()
                    + ")";
            }
        case REQUESTED:
            if (filter.isRequestPending() != null)
            {
                return "id_person in (select id_requester from soulmates where id_requested = " + filter.getPersonId()
                    + " and request_pending = " + filter.isRequestPending() + ")";
            }
            else
            {
                return "id_person in (select id_requester from soulmates where id_requested = " + filter.getPersonId()
                    + ")";
            }
        case BOTHDIRECTIONS:
            if (filter.isRequestPending() != null)
            {
                return "id_person in (select id_requested from soulmates where id_requester = " + filter.getPersonId()
                    + " and request_pending = " + filter.isRequestPending()
                    + " union select id_requester from soulmates where id_requested = " + filter.getPersonId()
                    + " and request_pending = " + filter.isRequestPending() + ")";
            }
            else
            {
                return "id_person in (select id_requested from soulmates where id_requester = " + filter.getPersonId()
                    + " union select id_requester from soulmates where id_requested = " + filter.getPersonId() + ")";
            }
        default:
            return "";
        }

    }

    @Override
    public String getComposedFilterStrings(IFilter... filters)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filters.length; i++)
        {
            sb.append("(");
            sb.append(filters[i].visitFilter(this));
            sb.append(")");
            if (i + 1 < filters.length)
            {
                sb.append(" AND ");
            }
        }
        return sb.toString();
    }
}
