package youapp.dataaccess.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import youapp.dataaccess.dao.IMatchingDao;
import youapp.dataaccess.dto.MatchingDto;

public class JdbcMatchingDao implements IMatchingDao {

	/**
	 * Used for accessing the database. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcTemplate jdbcTemplate;
	
	/**
	 * Used for simple inserts. JdbcTemplate is thread safe once configured.
	 */
	private SimpleJdbcInsert jdbcMatchingInsert;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcMatchingInsert = new SimpleJdbcInsert(dataSource)
        									.withTableName("matching");
    }
	
	@Override
	public MatchingDto getById(Long sourceId, Long destinationId) {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (destinationId == null) {
			throw new IllegalArgumentException("Destination id must not be null.");
		}
		String sql = "select * from matching where id_source = ? and id_destination = ?";
		return this.jdbcTemplate.queryForObject(sql, new MatchingMapper(), sourceId, destinationId);
	}

	@Override
	public List<MatchingDto> getBestMatches(Long sourceId, Double minimumScore, Integer offset, Integer resultSize) {
		if (sourceId == null) {
            throw new IllegalArgumentException("Source id must not be null.");
        }
        if (offset == null) {
            offset = 0;
        }
		List<MatchingDto> result = null;
		String sql = "select * from matching where id_source = ?";
		if (resultSize == null && minimumScore == null) {
			sql = sql + " order by final_score desc";
			result = this.jdbcTemplate.query(sql, new MatchingMapper(), sourceId);
		} else if (resultSize != null && minimumScore == null) {
			sql = sql + " order by final_score desc limit ?, ?";
			result = this.jdbcTemplate.query(sql, new MatchingMapper(), sourceId, offset, resultSize);
		} else if (resultSize == null && minimumScore != null) {
			sql = sql + " and final_score > ? order by final_score desc";
			result = this.jdbcTemplate.query(sql, new MatchingMapper(), sourceId, minimumScore);
		} else {
			sql = sql + " and final_score > ? order by final_score desc limit ?, ?";
			result = this.jdbcTemplate.query(sql, new MatchingMapper(), sourceId, minimumScore, offset, resultSize);
		}
		return result;
	}
	
	@Override
	public Boolean exists(Long sourceId, Long destinationId) {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (destinationId == null) {
			throw new IllegalArgumentException("Destination id must not be null.");
		}
		String sql = "select id_source from matching where id_source = ? and id_destination = ?";
		List<Long> ids = this.jdbcTemplate.query(sql, new MyLongMapper(), sourceId, destinationId);
		return (!(ids.isEmpty() || (ids.size() > 1)));
	}

	@Override
	public void create(MatchingDto matching) {
		if (matching == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (matching.getSourceId() == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (matching.getDestinationId() == null) {
			throw new IllegalArgumentException("Destination id must not be null.");
		}

		// Prepare parameters.
		Map<String, Object> parameters = new HashMap<String, Object>(23);
		parameters.put("id_source", matching.getSourceId());
		parameters.put("id_destination", matching.getDestinationId());
		
		parameters.put("replies_simon", matching.getRepliesSimon());
		parameters.put("replies_jaccard", matching.getRepliesJaccard());
		parameters.put("replies_adamic_adar", matching.getRepliesAdamicAdar());
		parameters.put("replies_cosim", matching.getRepliesCosim());
		parameters.put("replies_score", matching.getRepliesScore());
		
		parameters.put("friends_simon", matching.getFriendsSimon());
		parameters.put("friends_jaccard", matching.getFriendsJaccard());
		parameters.put("friends_adamic_adar", matching.getFriendsAdamicAdar());
		parameters.put("friends_cosim", matching.getFriendsCosim());
		parameters.put("friends_score", matching.getFriendsScore());
		
		parameters.put("groups_simon", matching.getGroupsSimon());
		parameters.put("groups_jaccard", matching.getGroupsJaccard());
		parameters.put("groups_adamic_adar", matching.getGroupsAdamicAdar());
		parameters.put("groups_cosim", matching.getGroupsCosim());
		parameters.put("groups_score", matching.getGroupsScore());
		
		parameters.put("pages_simon", matching.getPagesSimon());
		parameters.put("pages_jaccard", matching.getPagesJaccard());
		parameters.put("pages_adamic_adar", matching.getPagesAdamicAdar());
		parameters.put("pages_cosim", matching.getPagesCosim());
		parameters.put("pages_score", matching.getPagesScore());
		
		parameters.put("final_score", matching.getFinalScore());
		
//		parameters.put("distance_score", matching.getDistanceScore());

		// Execute.
		this.jdbcMatchingInsert.execute(parameters);
	}

	@Override
	public void update(MatchingDto matching) {
		if (matching == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		if (matching.getSourceId() == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (matching.getDestinationId() == null) {
			throw new IllegalArgumentException("Destination id must not be null.");
		}

/*		String sql = "update matching set " +
				"replies_simon = ?, replies_jaccard = ?, replies_adamic_adar = ?, replies_cosim = ?, replies_score = ?, " +
				"friends_simon = ?, friends_jaccard = ?, friends_adamic_adar = ?, friends_cosim = ?, friends_score = ?, " +
				"groups_simon = ?, groups_jaccard = ?, groups_adamic_adar = ?, groups_cosim = ?, groups_score = ?, " +
				"pages_simon = ?, pages_jaccard = ?, pages_adamic_adar = ?, pages_cosim = ?, pages_score = ?, " +
				"final_score = ? where id_source = ? and id_destination = ? and distance_score = ?";
*/

		String sql = "update matching set " +
				"replies_simon = ?, replies_jaccard = ?, replies_adamic_adar = ?, replies_cosim = ?, replies_score = ?, " +
				"friends_simon = ?, friends_jaccard = ?, friends_adamic_adar = ?, friends_cosim = ?, friends_score = ?, " +
				"groups_simon = ?, groups_jaccard = ?, groups_adamic_adar = ?, groups_cosim = ?, groups_score = ?, " +
				"pages_simon = ?, pages_jaccard = ?, pages_adamic_adar = ?, pages_cosim = ?, pages_score = ?, " +
				"final_score = ? where id_source = ? and id_destination = ?";

/*		this.jdbcTemplate.update(sql,
				matching.getRepliesSimon(), matching.getRepliesJaccard(), matching.getRepliesAdamicAdar(), matching.getRepliesCosim(), matching.getRepliesScore(),
				matching.getFriendsSimon(), matching.getFriendsJaccard(), matching.getFriendsAdamicAdar(), matching.getFriendsCosim(), matching.getFriendsScore(),
				matching.getGroupsSimon(), matching.getGroupsJaccard(), matching.getGroupsAdamicAdar(), matching.getGroupsCosim(), matching.getGroupsScore(),
				matching.getPagesSimon(), matching.getPagesJaccard(), matching.getPagesAdamicAdar(), matching.getPagesCosim(), matching.getPagesScore(),
				matching.getFinalScore(), matching.getSourceId(), matching.getDestinationId(), matching.getDistanceScore());
	}
*/

		this.jdbcTemplate.update(sql,
				matching.getRepliesSimon(), matching.getRepliesJaccard(), matching.getRepliesAdamicAdar(), matching.getRepliesCosim(), matching.getRepliesScore(),
				matching.getFriendsSimon(), matching.getFriendsJaccard(), matching.getFriendsAdamicAdar(), matching.getFriendsCosim(), matching.getFriendsScore(),
				matching.getGroupsSimon(), matching.getGroupsJaccard(), matching.getGroupsAdamicAdar(), matching.getGroupsCosim(), matching.getGroupsScore(),
				matching.getPagesSimon(), matching.getPagesJaccard(), matching.getPagesAdamicAdar(), matching.getPagesCosim(), matching.getPagesScore(),
				matching.getFinalScore(), matching.getSourceId(), matching.getDestinationId());
	}

	@Override
	public void delete(Long sourceId, Long destinationId) {
		if (sourceId == null) {
			throw new IllegalArgumentException("Source id must not be null.");
		}
		if (destinationId == null) {
			throw new IllegalArgumentException("Destination id must not be null.");
		}
		this.jdbcTemplate.update("delete from matching where id_source = ? and id_destination = ?", sourceId, destinationId);
	}
	
//	@Override
//	public MatchingDto getById(Long sourceId, Long destinationId) {
//		if (sourceId == null) {
//    		throw new IllegalArgumentException("Source id must not be null.");
//    	}
//		if (destinationId == null) {
//    		throw new IllegalArgumentException("Destination id must not be null.");
//    	}
//    	String sql = "select * from matching where id_source = ? and id_destination = ?";
//    	return this.jdbcTemplate.queryForObject(sql, new MatchingMapper(), sourceId, destinationId);
//	}
//
//	@Override
//	public List<MatchingDto> getBySource(Long sourceId) {
//		if (sourceId == null) {
//    		throw new IllegalArgumentException("Source id must not be null.");
//    	}
//    	String sql = "select * from matching where id_source = ?";
//    	return this.jdbcTemplate.query(sql, new MatchingMapper(), sourceId);
//	}
//
//	@Override
//	public List<MatchingDto> getBySource(Long sourceId, int offset, int resultSize) {
//		if (sourceId == null) {
//    		throw new IllegalArgumentException("Source id must not be null.");
//    	}
//		if (offset < 0) {
//			throw new IllegalArgumentException("Offset must be greater or equals zero.");
//		}
//		if (resultSize <= 0) {
//			throw new IllegalArgumentException("Result size must be greater than zero.");
//		}
//    	String sql = "select * from matching where id_source = ? limit ?,?";
//    	return this.jdbcTemplate.query(sql, new MatchingMapper(), sourceId, offset, resultSize);
//	}
//
//	@Override
//	public List<MatchingDto> getBySource(Long sourceId, int minFriend) {
//		if (sourceId == null) {
//    		throw new IllegalArgumentException("Source id must not be null.");
//    	}
//		if (minFriend < 0) {
//			throw new IllegalArgumentException("Minimum friend percentage must be greater or equals zero.");
//		}
//		String sql = "select * from matching where id_source = ? and friend >= ?";
//    	return this.jdbcTemplate.query(sql, new MatchingMapper(), sourceId, minFriend);
//	}
//
//	@Override
//	public List<MatchingDto> getBySource(Long sourceId, int minFriend, int offset,
//			int resultSize) {
//		if (sourceId == null) {
//    		throw new IllegalArgumentException("Source id must not be null.");
//    	}
//		if (minFriend < 0) {
//			throw new IllegalArgumentException("Minimum friend percentage must be greater or equals zero.");
//		}
//		if (offset < 0) {
//			throw new IllegalArgumentException("Offset must be greater or equals zero.");
//		}
//		if (resultSize <= 0) {
//			throw new IllegalArgumentException("Result size must be greater than zero.");
//		}
//		String sql = "select * from matching where id_source = ? and friend >= ? limit ?,?";
//    	return this.jdbcTemplate.query(sql, new MatchingMapper(), sourceId, minFriend, offset, resultSize);
//	}
//	
//	@Override
//	public List<MatchingDto> getByDestination(Long destinationId) {
//		if (destinationId == null) {
//    		throw new IllegalArgumentException("Destination id must not be null.");
//    	}
//    	String sql = "select * from matching where id_destination = ?";
//    	return this.jdbcTemplate.query(sql, new MatchingMapper(), destinationId);
//	}
//
//	@Override
//	public List<MatchingDto> getByDestination(Long destinationId, int offset,
//			int resultSize) {
//		if (destinationId == null) {
//    		throw new IllegalArgumentException("Destination id must not be null.");
//    	}
//    	String sql = "select * from matching where id_destination = ?";
//    	return this.jdbcTemplate.query(sql, new MatchingMapper(), destinationId);
//	}
//
//	@Override
//	public List<MatchingDto> getByDestination(Long destinationId, int minFriend) {
//		if (destinationId == null) {
//    		throw new IllegalArgumentException("Destination id must not be null.");
//    	}
//		if (minFriend < 0) {
//			throw new IllegalArgumentException("Minimum friend percentage must be greater or equals zero.");
//		}
//		String sql = "select * from matching where id_destination = ? and friend >= ?";
//    	return this.jdbcTemplate.query(sql, new MatchingMapper(), destinationId, minFriend);
//	}
//
//	@Override
//	public List<MatchingDto> getByDestination(Long destinationId, int minFriend,
//			int offset, int resultSize) {
//		if (destinationId == null) {
//    		throw new IllegalArgumentException("Destination id must not be null.");
//    	}
//		if (minFriend < 0) {
//			throw new IllegalArgumentException("Minimum friend percentage must be greater or equals zero.");
//		}
//		if (offset < 0) {
//			throw new IllegalArgumentException("Offset must be greater or equals zero.");
//		}
//		if (resultSize <= 0) {
//			throw new IllegalArgumentException("Result size must be greater than zero.");
//		}
//		String sql = "select * from matching where id_destination = ? and friend >= ? limit ?,?";
//    	return this.jdbcTemplate.query(sql, new MatchingMapper(), destinationId, minFriend, offset, resultSize);
//	}
//	
//	@Override
//	public List<MatchingDto> getBestBySource(Long sourceId, int minFriend,
//			int offset, int resultSize) {
//		if (sourceId == null) {
//    		throw new IllegalArgumentException("Source id must not be null.");
//    	}
//		if (minFriend < 0) {
//			throw new IllegalArgumentException("Minimum friend percentage must be greater or equals zero.");
//		}
//		if (offset < 0) {
//			throw new IllegalArgumentException("Offset must be greater or equals zero.");
//		}
//		if (resultSize <= 0) {
//			throw new IllegalArgumentException("Result size must be greater than zero.");
//		}
//		
//		String sql = "select * from matching where id_source = ? and id_destination not in " +
//				"(select id_proposition from proposition where id_person = ?) " +
//				"and friend >= ? limit ?,?";
//    	return this.jdbcTemplate.query(sql, new MatchingMapper(), sourceId, sourceId, minFriend, offset, resultSize);
//	}
//	
//	@Override
//	public List<MatchingDto> getAll(int offset, int resultSize) {
//		if (offset < 0) {
//			throw new IllegalArgumentException("Offset must be greater or equals zero.");
//		}
//		if (resultSize <= 0) {
//			throw new IllegalArgumentException("Result size must be greater than zero.");
//		}
//		String sql = "select * from matching limit ?,?";
//    	return this.jdbcTemplate.query(sql, new MatchingMapper(), offset, resultSize);
//	}
//	
//	@Override
//	public void create(MatchingDto matching) {
//		if (matching == null) {
//			throw new IllegalArgumentException("Parameter must not be null.");
//		}
//		if (matching.getSourceId() == null) {
//			throw new IllegalArgumentException("Source id must not be null.");
//		}
//		if (matching.getDestinationId() == null) {
//			throw new IllegalArgumentException("Destination id must not be null.");
//		}
//		
//		// Prepare parameters.
//		Map<String, Object> parameters = new HashMap<String, Object>(3);
//		parameters.put("id_source", matching.getSourceId());
//		parameters.put("id_destination", matching.getDestinationId());
//		parameters.put("friend", matching.getFriend());
//		parameters.put("enemy", matching.getEnemy());
//		
//		// Execute.
//        this.jdbcMatchingInsert.execute(parameters);
//	}
//
//	@Override
//	public void update(MatchingDto matching) {
//		if (matching == null) {
//			throw new IllegalArgumentException("Parameter must not be null.");
//		}
//		if (matching.getSourceId() == null) {
//			throw new IllegalArgumentException("Source id must not be null.");
//		}
//		if (matching.getDestinationId() == null) {
//			throw new IllegalArgumentException("Destination id must not be null.");
//		}
//		
//        String sql = "update matching set friend = ?, enemy = ? " +
//        		"where id_source = ? and id_destination = ?";
//        
//        this.jdbcTemplate.update(sql, matching.getFriend(), matching.getEnemy(),
//        		matching.getSourceId(), matching.getDestinationId());
//	}
//	
//	@Override
//	public void delete(MatchingDto matching) {
//		if (matching == null) {
//			throw new IllegalArgumentException("Parameter must not be null.");
//		}
//		if (matching.getSourceId() == null) {
//			throw new IllegalArgumentException("Source id must not be null.");
//		}
//		if (matching.getDestinationId() == null) {
//			throw new IllegalArgumentException("Destination id must not be null.");
//		}
//		this.jdbcTemplate.update("delete from matching where id_source = ? and id_destination = ?", matching.getSourceId(), matching.getDestinationId());
//	}
	
	/**
	 * Maps a database row to a Matching data transfer object.
	 * @author neme
	 *
	 */
	private static final class MatchingMapper implements RowMapper<MatchingDto> {

	    public MatchingDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	MatchingDto matching = new MatchingDto();
	    	matching.setSourceId(rs.getLong("id_source"));
	    	if (rs.wasNull()) {
	    		matching.setSourceId(null);
	    	}
	    	matching.setDestinationId(rs.getLong("id_destination"));
	    	if (rs.wasNull()) {
	    		matching.setDestinationId(null);
	    	}
	    	// Set the replies coefficients.
	    	matching.setRepliesSimon(rs.getDouble("replies_simon"));
	    	if (rs.wasNull()) {
	    		matching.setRepliesSimon(null);
	    	}
	    	matching.setRepliesJaccard(rs.getDouble("replies_jaccard"));
	    	if (rs.wasNull()) {
	    		matching.setRepliesJaccard(null);
	    	}
	    	matching.setRepliesAdamicAdar(rs.getDouble("replies_adamic_adar"));
	    	if (rs.wasNull()) {
	    		matching.setRepliesAdamicAdar(null);
	    	}
	    	matching.setRepliesCosim(rs.getDouble("replies_cosim"));
	    	if (rs.wasNull()) {
	    		matching.setRepliesCosim(null);
	    	}
	    	matching.setRepliesScore(rs.getDouble("replies_score"));
	    	if (rs.wasNull()) {
	    		matching.setRepliesScore(null);
	    	}
	    	// Set the friends coefficients.
	    	matching.setFriendsSimon(rs.getDouble("friends_simon"));
	    	if (rs.wasNull()) {
	    		matching.setFriendsSimon(null);
	    	}
	    	matching.setFriendsJaccard(rs.getDouble("friends_jaccard"));
	    	if (rs.wasNull()) {
	    		matching.setFriendsJaccard(null);
	    	}
	    	matching.setFriendsAdamicAdar(rs.getDouble("friends_adamic_adar"));
	    	if (rs.wasNull()) {
	    		matching.setFriendsAdamicAdar(null);
	    	}
	    	matching.setFriendsCosim(rs.getDouble("friends_cosim"));
	    	if (rs.wasNull()) {
	    		matching.setFriendsCosim(null);
	    	}
	    	matching.setFriendsScore(rs.getDouble("friends_score"));
	    	if (rs.wasNull()) {
	    		matching.setFriendsScore(null);
	    	}
	    	// Set the groups coefficients.
	    	matching.setGroupsSimon(rs.getDouble("groups_simon"));
	    	if (rs.wasNull()) {
	    		matching.setGroupsSimon(null);
	    	}
	    	matching.setGroupsJaccard(rs.getDouble("groups_jaccard"));
	    	if (rs.wasNull()) {
	    		matching.setGroupsJaccard(null);
	    	}
	    	matching.setGroupsAdamicAdar(rs.getDouble("groups_adamic_adar"));
	    	if (rs.wasNull()) {
	    		matching.setGroupsAdamicAdar(null);
	    	}
	    	matching.setGroupsCosim(rs.getDouble("groups_cosim"));
	    	if (rs.wasNull()) {
	    		matching.setGroupsCosim(null);
	    	}
	    	matching.setGroupsScore(rs.getDouble("groups_score"));
	    	if (rs.wasNull()) {
	    		matching.setGroupsScore(null);
	    	}
	    	// Set the pages coefficients.
	    	matching.setPagesSimon(rs.getDouble("pages_simon"));
	    	if (rs.wasNull()) {
	    		matching.setPagesSimon(null);
	    	}
	    	matching.setPagesJaccard(rs.getDouble("pages_jaccard"));
	    	if (rs.wasNull()) {
	    		matching.setPagesJaccard(null);
	    	}
	    	matching.setPagesAdamicAdar(rs.getDouble("pages_adamic_adar"));
	    	if (rs.wasNull()) {
	    		matching.setPagesAdamicAdar(null);
	    	}
	    	matching.setPagesCosim(rs.getDouble("pages_cosim"));
	    	if (rs.wasNull()) {
	    		matching.setPagesCosim(null);
	    	}
	    	matching.setPagesScore(rs.getDouble("pages_score"));
	    	if (rs.wasNull()) {
	    		matching.setPagesScore(null);
	    	}
	    	// Set the final matching score.
	    	matching.setFinalScore(rs.getDouble("final_score"));
	    	if (rs.wasNull()) {
	    		matching.setFinalScore(null);
/*	    	}
	    	matching.setDistanceScore(rs.getDouble("distance_score"));
	    	if(rs.wasNull()){
	    		matching.setDistanceScore(null);
*/
	    	}
			return matching;
	    }        
	}
	
	/**
	 * Maps a row containing an id to a Long.
	 * @author neme
	 *
	 */
	private static final class MyLongMapper implements RowMapper<Long> {

		@Override
		public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = rs.getLong(1);
			if (rs.wasNull()) {
				id = null;
			}
			return id;
		}
	}
	
}
