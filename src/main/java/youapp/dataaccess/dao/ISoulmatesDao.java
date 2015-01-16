package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.SoulmatesDto;

public interface ISoulmatesDao
{
    public SoulmatesDto getById(Long requesterId, Long requestedId);

    /**
     * Return the soulmates object, regardless of which request direction.
     * @param personAId First person's  id
     * @param personBId Second person's id
     * @return the soulmates object, regardless of which request direction.
     */
    public SoulmatesDto getByIdAnyDirection(Long personAId, Long personBId);
    
    public List<SoulmatesDto> getByRequester(Long requesterId);

    public List<SoulmatesDto> getByRequester(Long requesterId, Boolean requestPending);

    public List<SoulmatesDto> getByRequested(Long requestedId);

    public List<SoulmatesDto> getByRequested(Long requestedId, Boolean requestPending);

    public List<SoulmatesDto> getAll(Long personId);

    public List<SoulmatesDto> getAll(Long personId, Boolean requestPending);

    public int getNumberByRequester(Long requesterId);

    public int getNumberByRequester(Long requesterId, Boolean requestPending);

    public int getNumberByRequested(Long requestedId);

    public int getNumberByRequested(Long requestedId, Boolean requestPending);

    public int getNumber(Long personId);

    public int getNumber(Long personId, Boolean requestPending);

    public boolean exists(Long requesterId, Long requestedId);

    /**
     * Check if there is a soulmates object, regardless of which request direction.
     * @param personAId First person's  id
     * @param personBId Second person's id
     * @return Boolean if there is a soulmates object, regardless of which request direction.
     */
    public boolean existsAnyDirection(Long personAId, Long personBId);
    
    /**
     * Check if the given persons are soulmates without a request pending.
     * @param personAId First person's  id
     * @param personBId Second person's id
     * @return Boolean if the given persons are soulmates without a request pending.
     */
    public boolean areSoulmates(Long personAId, Long personBId);

    public void create(SoulmatesDto soulmates);

    public void update(SoulmatesDto soulmates);

    public void delete(Long requesterId, Long requestedId);

    /**
     * Delete the soulmates object, regardless of which request direction.
     * @param personAId First person's  id
     * @param personBId Second person's id
     */
    public void deleteAnyDirection(Long personAId, Long personBId);
}
