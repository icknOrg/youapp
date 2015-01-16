package youapp.services;

import java.util.List;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.model.MatchingData;
import youapp.model.MatchingScore;

public interface MatchingService {
	
	public MatchingData getMatchingData(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException, ModelException;
	
	public MatchingScore getMatchingScoreById(Long sourceId, Long destinationId, Boolean fbIds) throws DataAccessLayerException, ModelException;
	
	public List<MatchingScore> getBestMatches(Long sourceId, Double minimumScore, Integer offset, Integer resultSize, Boolean fbId) throws DataAccessLayerException, ModelException;
	
	public Boolean existsMatchingScore(Long sourceId, Long destinationId, Boolean fbId) throws DataAccessLayerException;
	
	public void createMatchingScore(MatchingScore matchingScore) throws DataAccessLayerException, ModelException;
	
	public void updateMatchingScore(MatchingScore matchingScore) throws DataAccessLayerException, ModelException;
	
	public void deleteMatchingScore(Long sourceId, Long destinationId, Boolean fbIds) throws DataAccessLayerException;

}
