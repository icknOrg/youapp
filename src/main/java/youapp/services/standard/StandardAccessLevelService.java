package youapp.services.standard;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.IAccessLevelDao;
import youapp.dataaccess.dto.AccessLevelDto;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.model.AccessLevel;
import youapp.services.AccessLevelService;

public class StandardAccessLevelService implements AccessLevelService {

	/**
	 * Logger.
	 */
	static final Log log = LogFactory.getLog(StandardAccessLevelService.class);
	
	private IAccessLevelDao accessLevelDao;
	
	@Autowired
	public void setAccessLevelDao(IAccessLevelDao accessLevelDao) {
		this.accessLevelDao = accessLevelDao;
	}
	
	@Override
	public AccessLevel getDefault() throws InconsistentStateException {
		return reassembleAccessLevel(accessLevelDao.getById(2));
	}

	public static void validateAccessLevel(AccessLevelDto accessLevel) throws InconsistentStateException {
		if (accessLevel == null) {
			throw new IllegalArgumentException("Access level must not be null.");
		}
		if (accessLevel.getDescription() == null) {
			throw new InconsistentStateException("Description must not be null.");
		}
	}

	public static void validateAccessLevel(AccessLevel accessLevel) throws  InconsistentModelException {
		if (accessLevel == null) {
			throw new IllegalArgumentException("Access level must not be null.");
		}
		if (accessLevel.getDescription() == null) {
			throw new InconsistentModelException("Description must not be null.");
		}
	}

	public static AccessLevelDto disassembleAccessLevel(AccessLevel accessLevel) {
		if (accessLevel == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		AccessLevelDto accessLevelDto = new AccessLevelDto();
		accessLevelDto.setId(accessLevel.getId());
		accessLevelDto.setDescription(accessLevel.getDescription());
		return accessLevelDto;
	}
	
	public static AccessLevel reassembleAccessLevel(AccessLevelDto accessLevelDto) throws InconsistentStateException {
		// Check parameter null pointers.
		if (accessLevelDto == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		
		// Validate.
		validateAccessLevel(accessLevelDto);
		
		// Reassemble.
		if (log.isDebugEnabled()) {
			log.debug("Reassembling access level: " + accessLevelDto.toString());
		}
		AccessLevel accessLevel = new AccessLevel();
		accessLevel.setId(accessLevelDto.getId());
		if (log.isDebugEnabled()) {
			log.debug(">>> Id: " + accessLevelDto.getId());
		}
		accessLevel.setDescription(accessLevelDto.getDescription());
		if (log.isDebugEnabled()) {
			log.debug(">>> Description: " + accessLevelDto.getDescription());
		}
		return accessLevel;
	}
	
}
