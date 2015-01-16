package youapp.model.filter;

import youapp.dataaccess.dao.filter.IFilterTranslator;

public class PersonBlockedFilter
    implements IFilter
{
    public enum BlockDirection
    {
        BLOCKER, BLOCKED, BOTHDIRECTIONS
    }

    private Long personId;

    private BlockDirection blockDirection;

    private boolean inverted;

    public Long getPersonId()
    {
        return personId;
    }

    public void setPersonId(Long personId)
    {
        this.personId = personId;
    }

    /**
     * @return the blockDirection
     */
    public BlockDirection getBlockDirection()
    {
        return blockDirection;
    }

    /**
     * @param blockDirection the blockDirection to set
     */
    public void setBlockDirection(BlockDirection blockDirection)
    {
        this.blockDirection = blockDirection;
    }

    public boolean isInverted()
    {
        return inverted;
    }

    public void setInverted(boolean inverted)
    {
        this.inverted = inverted;
    }

    public PersonBlockedFilter(Long personId,BlockDirection blockDirection, boolean inverted)
    {
        this.personId = personId;
        this.blockDirection = blockDirection;
        this.inverted = inverted;
    }

    @Override
    public String visitFilter(IFilterTranslator translator)
    {
        return translator.getFilterString(this);
    }

}
