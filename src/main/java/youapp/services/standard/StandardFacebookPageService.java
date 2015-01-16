package youapp.services.standard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.IFacebookPageDao;
import youapp.dataaccess.dto.FacebookPageDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.model.FacebookPage;
import youapp.services.FacebookPageService;
import youapp.services.PersonService;

public class StandardFacebookPageService implements FacebookPageService {

	/**
	 * Logger.
	 */
	static final Log log = LogFactory.getLog(StandardFacebookPageService.class);
	
	private IFacebookPageDao facebookPageDao;
	
	private PersonService personService;
	
	@Autowired
	public void setFacebookPageDao(IFacebookPageDao facebookPageDao) {
		this.facebookPageDao = facebookPageDao;
	}
	
	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	@Override
	public FacebookPage getById(Long pageId, Boolean fbId) {
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving page by id: " + pageId);
		}
		FacebookPageDto pageDto = facebookPageDao.getById(pageId, fbId);
		Integer frequency = facebookPageDao.getPageFrequency(pageId);
		return reassemblePage(pageDto, frequency);
	}

	@Override
	public List<FacebookPage> getByPerson(Long personId, Boolean fbId) throws DataAccessLayerException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all facebook pages of person with id: " + personId);
		}
		if (fbId) {
			personId = personService.getAlternativeId(personId, fbId);
		}
		List<FacebookPageDto> pageDtos = facebookPageDao.getByPerson(personId);
		List<FacebookPage> pages = new LinkedList<FacebookPage>();
		for (FacebookPageDto p : pageDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found page: " + p.toString());
			}
			Integer frequency = facebookPageDao.getPageFrequency(p.getId());
			pages.add(reassemblePage(p, frequency));
		}
		return pages;
	}

	@Override
	public Map<Long, FacebookPage> getByPersonAsMap(Long personId, Boolean fbId) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all facebook pages as map of person with id: " + personId);
		}
		List<FacebookPageDto> pageDtos = facebookPageDao.getByPerson(personId);
		Map<Long, FacebookPage> pages = new HashMap<Long, FacebookPage>();
		Long pId = null;
		for (FacebookPageDto p : pageDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found page: " + p.toString());
			}
			if (fbId) {
				pId = p.getpId();
			} else {
				pId = p.getId();
			}
			Integer frequency = facebookPageDao.getPageFrequency(p.getId());
			pages.put(pId, reassemblePage(p, frequency));
		}
		return pages;
	}

	@Override
	public List<FacebookPage> getByName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving pages by name: " + name);
		}
		List<FacebookPageDto> pageDtos = facebookPageDao.getByName(name);
		List<FacebookPage> pages = new LinkedList<FacebookPage>();
		for (FacebookPageDto p : pageDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found page: " + p.toString());
			}
			Integer frequency = facebookPageDao.getPageFrequency(p.getId());
			pages.add(reassemblePage(p, frequency));
		}
		return pages;
	}

	@Override
	public List<FacebookPage> getByType(String category) {
		if (category == null) {
			throw new IllegalArgumentException("Category must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving pages by category: " + category);
		}
		List<FacebookPageDto> pageDtos = facebookPageDao.getByCategory(category);
		List<FacebookPage> pages = new LinkedList<FacebookPage>();
		for (FacebookPageDto p : pageDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found page: " + p.toString());
			}
			Integer frequency = facebookPageDao.getPageFrequency(p.getId());
			pages.add(reassemblePage(p, frequency));
		}
		return pages;
	}

	@Override
	public List<FacebookPage> getAll() {
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all pages.");
		}
		List<FacebookPageDto> pageDtos = facebookPageDao.getAll();
		List<FacebookPage> pages = new LinkedList<FacebookPage>();
		for (FacebookPageDto p : pageDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found page: " + p.toString());
			}
			Integer frequency = facebookPageDao.getPageFrequency(p.getId());
			pages.add(reassemblePage(p, frequency));
		}
		return pages;
	}
	
	@Override
	public List<FacebookPage> getCommonPages(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		if (fbIds == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		// Convert ids if necessary.
		if (fbIds) {
			personIdA = personService.getAlternativeId(personIdA, fbIds);
			personIdB = personService.getAlternativeId(personIdB, fbIds);
		}
		// Get common pages.
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all pages.");
		}
		List<FacebookPageDto> pageDtos = facebookPageDao.getCommonPages(personIdA, personIdB);
		List<FacebookPage> pages = new LinkedList<FacebookPage>();
		for (FacebookPageDto p : pageDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found page: " + p.toString());
			}
			Integer frequency = facebookPageDao.getPageFrequency(p.getId());
			pages.add(reassemblePage(p, frequency));
		}
		return pages;
	}
	
	@Override
	public Integer getNumberOfPages(Long personId, Boolean fbId) throws DataAccessLayerException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (fbId) {
			personId = personService.getAlternativeId(personId, fbId);
		}
		return facebookPageDao.getNumberOfPages(personId);
	}

	@Override
	public Integer getNumberOfCommonPages(Long personIdA, Long personIdB, Boolean fbIds) throws DataAccessLayerException {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		if (fbIds == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (fbIds) {
			personIdA = personService.getAlternativeId(personIdA, fbIds);
		}
		return facebookPageDao.getNumberOfCommonPages(personIdA, personIdB);
	}
	
	public Long getAlternativeId(Long pageId, Boolean fbId) {
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		return facebookPageDao.getAlternativeId(pageId, fbId);
	}

	@Override
	public Boolean exists(Long pageId, Boolean fbId) {
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Check whether page with id " + pageId + " already exists.");
		}
		return facebookPageDao.exists(pageId, fbId);
	}

	@Override
	public boolean existsAssociation(Long personId, Long pageId, Boolean fbId) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Check whether an association between a person with id " + personId + " and a page with id " + pageId + " exists.");
		}
		if (fbId) {
			pageId = getAlternativeId(pageId, fbId);
		}
		return facebookPageDao.existsAssociation(personId, pageId);
	}

	@Override
	public void addAssociation(Long personId, Long pageId, Boolean fbId, Boolean visible, String profileSection, Long createdTime) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		if (visible == null) {
			throw new IllegalArgumentException("Visible must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Adding association between person with id " + personId + " and page with id " + pageId + ".");
		}
		if (!existsAssociation(personId, pageId, fbId)) {
			if (fbId) {
				pageId = getAlternativeId(pageId, fbId);
			}
			facebookPageDao.addAssociation(personId, pageId, visible, profileSection, createdTime);
		}
	}

	@Override
	public void removeAssociation(Long personId, Long pageId, Boolean fbId) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Removing association between person with id " + personId + " and page with id " + pageId + ".");
		}
		if (existsAssociation(personId, pageId, fbId)) {
			if (fbId) {
				pageId = getAlternativeId(pageId, fbId);
			}
			facebookPageDao.removeAssociation(personId, pageId);
		}
	}

	@Override
	public void removeAssociations(Long personId, List<FacebookPage> pages) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (pages == null) {
			throw new IllegalArgumentException("Pages must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Removing association between person with id " + personId + " and some pages.");
		}
		for (FacebookPage p : pages) {
			if (p.getId() == null) {
				if (log.isTraceEnabled()) {
					log.trace(">>> Skipped page because of missing page id.");
				}
			} else {
				if (log.isTraceEnabled()) {
					log.trace(">>> Removing association with page with id " + p.getId() + ".");
				}
				removeAssociation(personId, p.getId(), false);
			}
		}
	}

	@Override
	public void removeAllAssociations(Long personId) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Removing all page associations of person with id: " + personId);
		}
		facebookPageDao.removeAllAssociations(personId);
	}

	@Override
	public void createAndAssociate(Long personId, FacebookPage page, Boolean visible, String profileSection, Long createdTime) throws InconsistentModelException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (page == null) {
			throw new IllegalArgumentException("Page must not be null.");
		}
		if (visible == null) {
			throw new IllegalArgumentException("Visible must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Creating page and corresponding association if not present.");
		}
		// Validate.
		validatePage(page);
		// Create page if not yet present.
		Long pageId = create(page);
		page.setId(pageId);
		// Create association between person and page if not yet present.
		addAssociation(personId, pageId, false, visible, profileSection, createdTime);
	}

	@Override
	public void createAndAssociate(Long personId, List<FacebookPage> pages, Boolean visible) throws InconsistentModelException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (pages == null) {
			throw new IllegalArgumentException("Pages must not be null.");
		}
		if (visible == null) {
			throw new IllegalArgumentException("Visible must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Creating pages and corresponding associations if not present.");
		}
		Long id = null;
		for (FacebookPage p : pages) {
			if (log.isDebugEnabled()) {
				log.debug("Handling page: " + p.toString());
			}
			// Validate.
			validatePage(p);
			// Create page if not yet present.
			if (p.getId() == null) {
				if (log.isTraceEnabled()) {
					log.trace(">>> Found new page. Creating: " + p.toString());
				}
				id = create(p);
				p.setId(id);
			}
			// Make association if not yet present.
			if (log.isTraceEnabled()) {
				log.trace(">>> Adding association if not yet present.");
			}
			addAssociation(personId, p.getId(), false, visible, null, null);
		}
	}

	@Override
	public Long create(FacebookPage page) throws InconsistentModelException {
		if (page == null) {
			throw new IllegalArgumentException("Page must not be null.");
		}
		
		// Perform validation checks.
		validatePage(page);
		
		// Create new page in the database.
		FacebookPageDto pageDto = disassemblePage(page);
		if (log.isDebugEnabled()) {
			log.debug("Creating page: " + pageDto.toString());
		}
		if (exists(page.getpId(), true)) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Page already exists! Stopped creation.");
			}
			return getById(page.getpId(), true).getId();
		}
		Long generatedId = facebookPageDao.create(pageDto);
		if (log.isDebugEnabled()) {
			log.debug(">>> Created page with id: " + generatedId);
		}
		page.setId(generatedId);
		return generatedId;
	}

	@Override
	public void delete(Long pageId, Boolean fbId) {
		if (pageId == null) {
			throw new IllegalArgumentException("Page id must not be null.");
		}
		if (fbId == null) {
			throw new IllegalArgumentException("Facebook id indicator must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleting facebook page: " + pageId);
		}
		if (fbId) {
			pageId = getAlternativeId(pageId, fbId);
		}
		facebookPageDao.delete(pageId);
	}
	
	public static void validatePage(FacebookPageDto pageDto) throws InconsistentStateException {
		if (pageDto == null) {
			throw new IllegalArgumentException("Page DTO must not be null.");
		}
		if (pageDto.getId() == null) {
			throw new InconsistentStateException("Id must not be null.");
		}
		if (pageDto.getpId() == null) {
			throw new InconsistentStateException("Facebook page id must not be null.");
		}
		if (pageDto.getName() == null) {
			throw new InconsistentStateException("Name must not be null.");
		}
	}
	
	public static void validatePage(FacebookPage page) throws InconsistentModelException {
		if (page == null) {
			throw new IllegalArgumentException("Page must not be null.");
		}
		if (page.getpId() == null) {
			throw new InconsistentModelException("Facebook page id must not be null.");
		}
		if (page.getName() == null) {
			throw new InconsistentModelException("Name must not be null.");
		}
	}
	
	public static FacebookPageDto disassemblePage(FacebookPage page) {
		if (page == null) {
			throw new IllegalArgumentException("Page must not be null.");
		}
		FacebookPageDto pageDto = new FacebookPageDto();
		pageDto.setId(page.getId());
		pageDto.setpId(page.getpId());
		pageDto.setName(page.getName());
		pageDto.setCategory(page.getCategory());
		pageDto.setType(page.getType());
		pageDto.setLikes(page.getLikes());
		pageDto.setWebsite(page.getWebsite());
		pageDto.setFounded(page.getFounded());
		return pageDto;
	}
	
	public static FacebookPage reassemblePage(FacebookPageDto pageDto, Integer pageFrequency) {
		if (pageDto == null) {
			throw new IllegalArgumentException("Page DTO must not be null.");
		}
		FacebookPage page = new FacebookPage();
		page.setId(pageDto.getId());
		page.setpId(pageDto.getpId());
		page.setName(pageDto.getName());
		page.setCategory(pageDto.getCategory());
		page.setType(pageDto.getType());
		page.setLikes(pageDto.getLikes());
		page.setWebsite(pageDto.getWebsite());
		page.setFounded(pageDto.getFounded());
		if (pageFrequency == null) {
			page.setFrequency(0);
		} else {
			page.setFrequency(pageFrequency);
		}
		return page;
	}
	
}
