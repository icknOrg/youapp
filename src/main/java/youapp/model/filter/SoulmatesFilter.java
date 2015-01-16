package youapp.model.filter;

import youapp.dataaccess.dao.filter.IFilterTranslator;

public class SoulmatesFilter
    implements IFilter
{

    public enum RequestDirection {
        REQUESTER, REQUESTED, BOTHDIRECTIONS
    }
    
    private Long personId;

    private RequestDirection requestDirection;
    
    private Boolean requestPending;

    /**
     * @return the personId
     */
    public Long getPersonId()
    {
        return personId;
    }

    /**
     * @param personId the personId to set
     */
    public void setPersonId(Long personId)
    {
        this.personId = personId;
    }

    /**
     * @return the requestDirection
     */
    public RequestDirection getRequestDirection()
    {
        return requestDirection;
    }

    /**
     * @param requestDirection the requestDirection to set
     */
    public void setRequestDirection(RequestDirection requestDirection)
    {
        this.requestDirection = requestDirection;
    }

    /**
     * @return the requestPending
     */
    public Boolean getRequestPending()
    {
        return requestPending;
    }

    /**
     * @return the requestPending
     */
    public Boolean isRequestPending()
    {
        return requestPending;
    }

    /**
     * @param requestPending the requestPending to set
     */
    public void setRequestPending(Boolean requestPending)
    {
        this.requestPending = requestPending;
    }
    
    public SoulmatesFilter(Long personId, RequestDirection requestDirection)
    {
        this.personId = personId;
        this.requestDirection = requestDirection;
        this.requestPending = null;
    }
    
    public SoulmatesFilter(Long personId, RequestDirection requestDirection, boolean requestPending)
    {
        this.personId = personId;
        this.requestDirection = requestDirection;
        this.requestPending = requestPending;
    }

    @Override
    public String visitFilter(IFilterTranslator translator)
    {
        return translator.getFilterString(this);
    }

}
