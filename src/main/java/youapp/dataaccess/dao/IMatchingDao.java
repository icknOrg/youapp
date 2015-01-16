package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.MatchingDto;

/**
 * Represents an SQL data access object for a match.
 * @author neme
 *
 */
public interface IMatchingDao {
	
	/**
	 * TODO
	 * @param sourceId
	 * @param destinationId
	 * @return
	 */
	public MatchingDto getById(Long sourceId, Long destinationId);

	/**
	 * TODO
	 * @param sourceId
	 * @param resultSize
	 * @param minimumScore
	 * @return
	 */
	public List<MatchingDto> getBestMatches(Long sourceId, Double minimumScore, Integer offset, Integer resultSize);
	
	/**
	 * 
	 * @param sourceId
	 * @param destinationId
	 * @return
	 */
	public Boolean exists(Long sourceId, Long destinationId);
	
	/**
	 * TODO
	 * @param match
	 */
	public void create(MatchingDto matching);
	
	/**
	 * TODO
	 * @param match
	 */
	public void update(MatchingDto matching);
	
	/**
	 * 
	 * @param sourceId
	 * @param destinationId
	 */
	public void delete(Long sourceId, Long destinationId);
	
//	/**
//	 * TODO
//	 * @param sourceId
//	 * @param destinationId
//	 * @return
//	 */
//	public MatchingDto getById(Long sourceId, Long destinationId);
//
//	/**
//	 * TODO
//	 * @param sourceId
//	 * @return
//	 */
//	public List<MatchingDto> getBySource(Long sourceId);
//	
//	/**
//	 * TODO
//	 * @param sourceId
//	 * @param offset
//	 * @param resultSize
//	 * @return
//	 */
//	public List<MatchingDto> getBySource(Long sourceId, int offset, int resultSize);
//	
//	/**
//	 * TODO
//	 * @param sourceId
//	 * @param minFriend
//	 * @return
//	 */
//	public List<MatchingDto> getBySource(Long sourceId, int minFriend);
//	
//	/**
//	 * TODO
//	 * @param sourceId
//	 * @param minFriend
//	 * @param offset
//	 * @param resultSize
//	 * @return
//	 */
//	public List<MatchingDto> getBySource(Long sourceId, int minFriend, int offset, int resultSize);
//	
//	/**
//	 * TODO
//	 * @param destinationId
//	 * @return
//	 */
//	public List<MatchingDto> getByDestination(Long destinationId);
//	
//	/**
//	 * TODO
//	 * @param destinationId
//	 * @param offset
//	 * @param resultSize
//	 * @return
//	 */
//	public List<MatchingDto> getByDestination(Long destinationId, int offset, int resultSize);
//	
//	/**
//	 * TODO
//	 * @param desetinationId
//	 * @param minFriend
//	 * @return
//	 */
//	public List<MatchingDto> getByDestination(Long destinationId, int minFriend);
//	
//	/**
//	 * TODO
//	 * @param desetinationId
//	 * @param minFriend
//	 * @param offset
//	 * @param resultSize
//	 * @return
//	 */
//	public List<MatchingDto> getByDestination(Long destinationId, int minFriend, int offset, int resultSize);
//	
//	/**
//	 * 
//	 * @param sourceId
//	 * @param minFriend
//	 * @param offset
//	 * @param resultSize
//	 * @return
//	 */
//	public List<MatchingDto> getBestBySource(Long sourceId, int minFriend, int offset, int resultSize);
//	
//	/**
//	 * TODO
//	 * @param offset
//	 * @param resultSize
//	 * @return
//	 */
//	public List<MatchingDto> getAll(int offset, int resultSize);
//	
//	/**
//	 * TODO
//	 * @param match
//	 */
//	public void create(MatchingDto matching);
//	
//	/**
//	 * TODO
//	 * @param match
//	 */
//	public void update(MatchingDto matching);
//	
//	/**
//	 * TODO
//	 * @param match
//	 */
//	public void delete(MatchingDto matching);
	
}
