package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.PersonBlockedDto;

public interface IPersonBlockedDao {
	public PersonBlockedDto getById(Long blockerId, Long blockedId);

	public List<PersonBlockedDto> getByBlocker(Long blockerId);

	public List<PersonBlockedDto> getByBlocked(Long blockedId);

	public boolean exists(Long blockerId, Long blockedId);

    public boolean blockedAnyDirection(Long personAId, Long personBId);
	
	public void create(PersonBlockedDto personBlocked);

	public void delete(Long blockerId, Long blockedId);

}
