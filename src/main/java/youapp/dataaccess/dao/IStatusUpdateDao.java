package youapp.dataaccess.dao;

import java.sql.Timestamp;
import java.util.List;

import youapp.dataaccess.dto.StatusUpdateDto;

public interface IStatusUpdateDao
{
    public StatusUpdateDto getById(Long personId, Timestamp when);

    public StatusUpdateDto getLastById(Long personId);

    public List<StatusUpdateDto> getByPerson(Long personId);

    public List<StatusUpdateDto> getByDate(Timestamp when);

    public List<StatusUpdateDto> getByPersonAndSoulmates(Long personId, Integer offset, Integer resultSize);

    public List<StatusUpdateDto> getByPersonAndSoulmates(Long personId, Timestamp startDate, Integer offset,
        Integer resultSize);

    public boolean exists(Long personId, Timestamp when);

    public boolean exists(Long personId);

    public void create(StatusUpdateDto statusUpdate);

    public void update(StatusUpdateDto statusUpdate);

    public void delete(Long personId, Timestamp when);

    public void deleteAll(Long personId);

}
