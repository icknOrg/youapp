package youapp.services.standard;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import youapp.dataaccess.dao.IImportanceDao;
import youapp.dataaccess.dao.IQuestionDao;
import youapp.dataaccess.dao.IReplyDao;
import youapp.dataaccess.dao.ISearchableQuestionDao;
import youapp.dataaccess.dto.ImportanceDto;
import youapp.dataaccess.dto.NameDto;
import youapp.dataaccess.dto.QuestionDto;
import youapp.dataaccess.dto.ReplyDto;
import youapp.dataaccess.dto.ReplyPairDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.Importance;
import youapp.model.Name;
import youapp.model.Question;
import youapp.model.Reply;
import youapp.model.ReplyPair;
import youapp.services.PersonService;
import youapp.services.QuestionService;

public class StandardQuestionService implements QuestionService {
	
	/**
	 * Logger.
	 */
	static final Log log = LogFactory.getLog(StandardQuestionService.class);
	
	IQuestionDao questionDao;
	ISearchableQuestionDao searchableQuestionDao;
	IReplyDao replyDao;
	IImportanceDao importanceDao;
	PersonService personService;
	
	@Autowired
	public void setQuestionDao(IQuestionDao questionDao) {
		this.questionDao = questionDao;
	}
	
	@Autowired
	public void setSearchableQuestionDao(ISearchableQuestionDao searchableQuestionDao) {
		this.searchableQuestionDao = searchableQuestionDao;
	}
	
	@Autowired
	public void setReplyDao(IReplyDao replyDao) {
		this.replyDao = replyDao;
	}
	
	@Autowired
	public void setImportanceDao(IImportanceDao importanceDao) {
		this.importanceDao = importanceDao;
	}
	
	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	@Override
	public Long createQuestion(Question question) throws DataAccessLayerException, InconsistentModelException {
		if (question == null) {
			throw new IllegalArgumentException("Question must not be null.");
		}
		if (question.getId() != null) {
			throw new IllegalArgumentException("Question id must not be set.");
		}
		if (question.getAuthorId() == null) {
			throw new IllegalArgumentException("Author id must not be null.");
		}
		if (!personService.exists(question.getAuthorId(), false) || !personService.isActive(question.getAuthorId(), false)) {
			throw new IllegalArgumentException("Author with id " + question.getAuthorId() + " does not exist or is not active.");
		}
		if (question.getAuthorName() == null) {
			Name authorName = personService.getName(question.getAuthorId());
			if (authorName == null) {
				throw new IllegalArgumentException("No name information for author with id " + question.getId() + " found.");
			}
			question.setAuthorName(authorName);
		}
		
		// Perform validation checks.
		validateQuestion(question, true);
		
		// Create new question in the database.
		// TODO: Find a good way how to check whether a similar question already exists!!!
		QuestionDto questionDto = disassembleQuestion(question);
		NameDto nameDto = disassembleName(question);
		if (log.isDebugEnabled()) {
			log.debug("Creating new question: " + questionDto.toString() + 
					" created by author with id: " + nameDto.getPersonId());
		}
		Long questionId = questionDao.create(questionDto);
		question.setId(questionId);
		if (log.isDebugEnabled()) {
			log.debug("Created question in database. Question id: " + question.getId());
		}
		
		// Adding question to search index.
		try {
			searchableQuestionDao.create(questionDto, nameDto);
			if (log.isDebugEnabled()) {
				log.debug("Added question to the search index. Question id: " + question.getId());
			}
		} catch (SolrServerException e) {
			if (log.isDebugEnabled()) {
				log.debug("Error while adding question with id " + questionDto.getId() + " to the search index. Reason: ", e);
			}
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("Error while adding question with id " + questionDto.getId() + " to the search index. Reason: ", e);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Error while adding question with id " + questionDto.getId() + " to the search index. Reason: ", e);
			}
		}
		
		return questionId;
	}

	@Override
	public void createReply(Reply reply) throws InconsistentModelException, DataAccessLayerException {
		if (reply == null) {
			throw new IllegalArgumentException("Reply must not be null.");
		}
		if (reply.getPersonId() == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (reply.getQuestionId() == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (existsReply(reply.getPersonId(), reply.getQuestionId())) {
			if (log.isDebugEnabled()) {
				log.debug("Reply already exists! Skipping creation.");
			}
			return;
		}
		
		// Perform validation checks.
		validateReply(reply, true);
		
		// Create new reply in the database.
		ReplyDto replyDto = disassembleReply(reply);
		if (log.isDebugEnabled()) {
			log.debug("Creating new reply: " + replyDto.toString());
		}
		replyDao.create(replyDto);
		if (log.isDebugEnabled()) {
			log.debug("Created new reply.");
		}
	}

	@Override
	public void deleteQuestion(Long questionId) {
		if (questionId == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleting question with id: " + questionId);
		}
		
		// Deleting the question from the database.
		questionDao.delete(questionId);
		if (log.isDebugEnabled()) {
			log.debug("Deleted question with id: " + questionId);
		}
		
		// Deleting the question from the search index.
		try {
			searchableQuestionDao.delete(questionId);
			if (log.isDebugEnabled()) {
				log.debug("Updated question in the search index. Question id: " + questionId);
			}
		} catch (SolrServerException e) {
			if (log.isDebugEnabled()) {
				log.debug("Error while deleting question with id " + questionId + " from the search index. Reason: ", e);
			}
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("Error while updating question with id " + questionId + " from the search index. Reason: ", e);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Error while updating question with id " + questionId + " from the search index. Reason: ", e);
			}
		}
		
	}
	
	@Override
	public void deleteQuestionWithReplies(Long questionId) throws DataAccessLayerException {
		if (questionId == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleteing question with id " + questionId + " and all its associated replies.");
		}
		
		// Deleting all associated replies from the database.
		replyDao.deleteQuestionReplies(questionId);
		if (log.isDebugEnabled()) {
			log.debug("Deleted all replies of question with id: " + questionId);
		}
		
		// Delete the question from the database.
		deleteQuestion(questionId);
	}

	@Override
	public void deleteReply(Long personId, Long questionId) throws DataAccessLayerException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (questionId == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (!existsReply(personId, questionId)) {
			if (log.isDebugEnabled()) {
				log.debug("Reply for question with id " + questionId + " of person with id " + personId + " does not exist. Returning.");
			}
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleting reply for question with id " + questionId + " of person with id " + personId + ".");
		}
		replyDao.delete(personId, questionId);
		
	}
	
	@Override
	public void deleteRepliesByPerson(Long personId) throws DataAccessLayerException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleting all replies of person with id: " + personId);
		}
		replyDao.deletePersonReplies(personId);
	}

	@Override
	public void deleteRepliesByQuestion(Long questionId) throws DataAccessLayerException {
		if (questionId == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleting all replies for question with id: " + questionId);
		}
		replyDao.deleteQuestionReplies(questionId);
	}
	
	@Override
	public boolean existsReply(Long personId, Long questionId) throws DataAccessLayerException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (questionId == null) {
			throw new IllegalArgumentException("Quetion id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Checking wheter reply for question with id " + questionId + " of person with id " + personId + " exists.");
		}
		return replyDao.exists(personId, questionId);
	}

	@Override
	public List<Question> fetchNextQuestions(Long personId, Integer numberOfQuestions) throws DataAccessLayerException, ModelException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (numberOfQuestions == null) {
			throw new IllegalArgumentException("Number of questions must not be null.");
		}
		if (numberOfQuestions <= 0) {
			throw new IllegalArgumentException("Number of questions must be greater than zero.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Fetching next questions for person with id: " + personId);
		}
		
		List<Question> result = new LinkedList<Question>();
		List<QuestionDto> questionDtos = null;
		Name name = null;
		Long numberOfReplies = null;
		
		// Return empty list if the person with the given id is not active.
		if (!personService.isActive(personId, false)) {
			return result;
		}
		
		// Retrieve questions.
		questionDtos = questionDao.fetchNext(personId, numberOfQuestions);
		for (QuestionDto q : questionDtos) {
			name = null;
			numberOfReplies = null;
			if (log.isDebugEnabled()) {
				log.debug(">>> Found question: " + q.toString());
			}
			if (q.getAuthor() != null) {
				name = personService.getName(q.getAuthor());
				if (name != null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Found author first name: " + name.getFirstName());
						log.debug(">>> Found author last name: " + name.getLastName());
						log.debug(">>> Found author nick name: " + name.getNickName());
						log.debug(">>> Found number of replies: " + numberOfReplies);
					}
					// Only add question if name could be found.
					numberOfReplies = getNumberOfRepliesByQuestion(q.getId(), false, null, null);
					result.add(reassembleQuestion(q, name, numberOfReplies));
				} else {
					if (log.isDebugEnabled()) {
						log.debug(">>> No name for author with person id " + q.getAuthor() + " found.");
						log.debug(">>> Skipping question with id " + q.getId());
					}
				}
			}
		}
		return result;
	}
	
//	@Override
//	public List<Question> getAnsweredQuestionsByPerson(Long personId, Boolean includeSkipped, Boolean includePrivate, Boolean includeCritical) throws DataAccessLayerException {
//		if (personId == null) {
//			throw new IllegalArgumentException("Person id must not be null.");
//		}
//		if (includeSkipped == null) {
//			throw new IllegalArgumentException("Include skipped must not be null.");
//		}
//		if (includePrivate == null) {
//			throw new IllegalArgumentException("Include private must not be null.");
//		}
//		if (includeCritical == null) {
//			throw new IllegalArgumentException("Include critical must not be null.");
//		}
//		
//		List<Question> result = new LinkedList<Question>();
//		List<QuestionDto> questionDtos = null;
//		Name name = null;
//		
//		// Retrieve questions.
//		questionDtos = questionDao.getAnsweredQuestionsByPerson(personId, includeSkipped, includePrivate, includeCritical);
//		for (QuestionDto q : questionDtos) {
//			name = null;
//			if (log.isDebugEnabled()) {
//				log.debug(">>> Found question: " + q.toString());
//			}
//			if (q.getAuthor() != null) {
//				name = personService.getName(q.getAuthor());
//				if (name != null) {
//					if (log.isDebugEnabled()) {
//						log.debug(">>> Found author first name: " + name.getFirstName());
//						log.debug(">>> Found author last name: " + name.getLastName());
//						log.debug(">>> Found author nick name: " + name.getNickName());
//					}
//					// Only add question if name could be found.
//					result.add(reassembleQuestion(q, name.getFirstName(), name.getLastName(), name.getNickName()));
//				} else {
//					if (log.isDebugEnabled()) {
//						log.debug(">>> No name for author with person id " + q.getAuthor() + " found.");
//						log.debug(">>> Skipping question with id " + q.getId());
//					}
//				}
//			}
//		}
//		return result;
//	}
	
	@Override
	public List<ReplyPair> getCommonRepliesByPair(Long personIdA, Long personIdB) throws DataAccessLayerException, ModelException {
		if (personIdA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (personIdB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all common replies between person with id " + personIdA + " and person with id " + personIdB);
		}
		
		List<ReplyPair> replyPairs = new LinkedList<ReplyPair>();
		ReplyPair newReplyPair = null;
		List<ReplyPairDto> replyPairDtos = null;
		Name namePersonA = null;
		Name namePersonB = null;
		Question question = null;
		
		// Retrieve names.
		namePersonA = personService.getName(personIdA);
		if (namePersonA == null) {
			if (log.isDebugEnabled()) {
				log.debug(">>> No name for person with id " + personIdA + " found.");
				log.debug(">>> Returning empty list...");
				return replyPairs;
			}
		}
		namePersonB = personService.getName(personIdB);
		if (namePersonB == null) {
			if (log.isDebugEnabled()) {
				log.debug(">>> No name for person with id " + personIdB + " found.");
				log.debug(">>> Returning empty list...");
				return replyPairs;
			}
		}
		
		// Retrieve reply pairs.
		replyPairDtos = replyDao.getCommonRepliesByPair(personIdA, personIdB);
		for (ReplyPairDto rpDto : replyPairDtos) {
			question = null;
			if ((rpDto.getReplyA() != null) && (rpDto.getReplyA().getQuestionId() != null) && (rpDto.getReplyB() != null) && (rpDto.getReplyB().getQuestionId() != null)) {
				if (rpDto.getReplyA().getQuestionId().equals(rpDto.getReplyB().getQuestionId())) {
					// Question ids of both replies are set and the are equal. Retrieve the question from the database.
					question = getQuestionById(rpDto.getReplyA().getQuestionId());
					if (question != null) {
						newReplyPair = new ReplyPair(reassembleReply(rpDto.getReplyA(), namePersonA, question), reassembleReply(rpDto.getReplyB(), namePersonB, question));
						replyPairs.add(newReplyPair);
					} else {
						if (log.isDebugEnabled()) {
							log.debug(">>> No question for question with id " + rpDto.getReplyA().getQuestionId() + " found.");
							log.debug(">>> Skipping reply pair for question with id " + rpDto.getReplyA().getQuestionId());
						}
					}
				}
			} else {
				// Question ids are either not set or not equal.
				if (log.isDebugEnabled()) {
					log.debug(">>> Question ids of question pair are either not set or not equal. Question pairs cannot be returned because of these inconsistencies.");
				}
				throw new InconsistentStateException("Question ids of question pair are either not set or not equal.");
			}
		}
		return replyPairs;
	}
	
	@Override
	public List<Question> getMatchingQuestionsByPair(Long idPersonA, Long idPersonB) throws DataAccessLayerException, ModelException {
		if (idPersonA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (idPersonB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all matching questions between person with id " + idPersonA + " and person with id " + idPersonB);
		}
		
		List<Question> result = new LinkedList<Question>();
		List<QuestionDto> questionDtos = null;
		Long numberOfReplies = null;
		Name name = null;
		
		// Retrieve questions.
		questionDtos = questionDao.getMatchingQuestionsByPair(idPersonA, idPersonB);
		for (QuestionDto q : questionDtos) {
			numberOfReplies = null;
			name = null;
			if (log.isDebugEnabled()) {
				log.debug(">>> Found question: " + q.toString());
			}
			if (q.getAuthor() != null) {
				name = personService.getName(q.getAuthor());
				if (name != null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Found author first name: " + name.getFirstName());
						log.debug(">>> Found author last name: " + name.getLastName());
						log.debug(">>> Found author nick name: " + name.getNickName());
					}
					// Only add question if name could be found.
					numberOfReplies = getNumberOfRepliesByQuestion(q.getId(), false, null, null);
					result.add(reassembleQuestion(q, name, numberOfReplies));
				} else {
					if (log.isDebugEnabled()) {
						log.debug(">>> No name for author with person id " + q.getAuthor() + " found.");
						log.debug(">>> Skipping question with id " + q.getId());
					}
				}
			}
		}
		return result;
	}
	
	@Override
	public int getNumberOfMatchingQuestionsByPair(Long idPersonA, Long idPersonB, Boolean skipped, Boolean inPrivate, Boolean critical) {
		if (idPersonA == null) {
			throw new IllegalArgumentException("Id of person A must not be null.");
		}
		if (idPersonB == null) {
			throw new IllegalArgumentException("Id of person B must not be null.");
		}
		return questionDao.getNumberOfMatchingQuestionsByPair(idPersonA, idPersonB, skipped, inPrivate, critical);
	}
	
	@Override
	public Long getNumberOfRepliesByQuestion(Long questionId, Boolean skipped, Boolean inPrivate, Boolean critical) {
		if (questionId == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		return replyDao.getNumberOfRepliesByQuestion(questionId, skipped, inPrivate, critical);
	}
	
	@Override
	public Long getNumberOfRepliesByPerson(Long personId, Boolean skipped, Boolean inPrivate, Boolean critical) {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		return replyDao.getNumberOfRepliesByPerson(personId, skipped, inPrivate, critical);
	}

	@Override
	public Question getQuestionById(Long questionId) throws DataAccessLayerException, ModelException {
		if (questionId == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving question with id: " + questionId);
		}
		
		QuestionDto questionDto = null;
		Long numberOfReplies = null;
		Name name = null;
		
		// Retrieve question.
		try {
			questionDto = questionDao.getById(questionId);
		}
		catch (EmptyResultDataAccessException e) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Question with id " + questionId + " is not existent: " + e.getMessage());
			}
			return null;
		}
		if (log.isDebugEnabled()) {
			log.debug(">>> Found question: " + questionDto.toString());
		}
		// Retrieve author information.
		if (questionDto.getAuthor() != null) {
			name = personService.getName(questionDto.getAuthor());
			if (name != null) {
				if (log.isDebugEnabled()) {
					log.debug(">>> Found author first name: " + name.getFirstName());
					log.debug(">>> Found author last name: " + name.getLastName());
					log.debug(">>> Found author nick name: " + name.getNickName());
				}
				// Only return question if author name could be found.
				numberOfReplies = getNumberOfRepliesByQuestion(questionDto.getId(), false, null, null);
				return reassembleQuestion(questionDto, name, numberOfReplies);
			} else {
				if (log.isDebugEnabled()) {
					log.debug(">>> No name for author with person id " + questionDto.getAuthor() + " found.");
					log.debug(">>> Skipping question with id " + questionDto.getId());
				}
				return null;
			}
		}
		return null;
	}

	@Override
	public List<Question> getQuestionsByAuthor(Long authorId) throws DataAccessLayerException, ModelException {
		if (authorId == null) {
			throw new IllegalArgumentException("Author id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all questions of author with id: " + authorId);
		}
		
		List<Question> result = new LinkedList<Question>();
		List<QuestionDto> questionDtos = null;
		Long numberOfReplies = null;
		Name name = null;
		
		// Retrieve author information.
		name = personService.getName(authorId);
		if (name != null) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found author first name: " + name.getFirstName());
				log.debug(">>> Found author last name: " + name.getLastName());
				log.debug(">>> Found author nick name: " + name.getNickName());
			}
			// Only retrieve questions if the author's name could be found.
			questionDtos = questionDao.getByAuthor(authorId);
			for (QuestionDto q : questionDtos) {
				numberOfReplies = getNumberOfRepliesByQuestion(q.getId(), false, null, null);
				if (log.isDebugEnabled()) {
					log.debug(">>> Found question: " + q.toString());
				}
				result.add(reassembleQuestion(q, name, numberOfReplies));
			}
		}
		return result;
	}

	@Override
	public List<Question> getQuestionsByAuthor(Long authorId, Integer offset, Integer resultSize) throws DataAccessLayerException, ModelException {
		if (authorId == null) {
			throw new IllegalArgumentException("Author id must not be null.");
		}
		if (offset == null) {
			throw new IllegalArgumentException("Offset must not be null.");
		}
		if (resultSize == null) {
			throw new IllegalArgumentException("Result size must not be null.");
		}
		if (offset < 0) {
			throw new IllegalArgumentException("Offset must be greater or equal zero.");
		}
		if (resultSize <= 0) {
			throw new IllegalArgumentException("Result size must be greater than zero.");
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all questions of author with id: " + authorId);
		}
		
		List<Question> result = new LinkedList<Question>();
		List<QuestionDto> questionDtos = null;
		Long numberOfReplies = null;
		Name name = null;
		
		// Retrieve author information.
		name = personService.getName(authorId);
		if (name != null) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found author first name: " + name.getFirstName());
				log.debug(">>> Found author last name: " + name.getLastName());
				log.debug(">>> Found author nick name: " + name.getNickName());
			}
			// Only retrieve questions if the author's name could be found.
			questionDtos = questionDao.getByAuthor(authorId, offset, resultSize);
			for (QuestionDto q : questionDtos) {
				numberOfReplies = null;
				numberOfReplies = getNumberOfRepliesByQuestion(q.getId(), false, null, null);
				if (log.isDebugEnabled()) {
					log.debug(">>> Found question: " + q.toString());
				}
				result.add(reassembleQuestion(q, name, numberOfReplies));
			}
		}
		return result;
	}

	@Override
	public List<Question> getQuestionsByDate(Date date) throws DataAccessLayerException, ModelException {
		if (date == null) {
			throw new IllegalArgumentException("Date must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving questions by date: " + date);
		}
		
		List<Question> result = new LinkedList<Question>();
		List<QuestionDto> questionDtos = null;
		Long numberOfReplies = null;
		Name name = null;
		
		// Retrieve questions.
		questionDtos = questionDao.getByDate(date);
		for (QuestionDto q : questionDtos) {
			numberOfReplies = null;
			name = null;
			if (log.isDebugEnabled()) {
				log.debug(">>> Found question: " + q.toString());
			}
			if (q.getAuthor() != null) {
				name = personService.getName(q.getAuthor());
				if (name != null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Found author first name: " + name.getFirstName());
						log.debug(">>> Found author last name: " + name.getLastName());
						log.debug(">>> Found author nick name: " + name.getNickName());
					}
					// Only add question if name could be found.
					numberOfReplies = getNumberOfRepliesByQuestion(q.getId(), false, null, null);
					result.add(reassembleQuestion(q, name, numberOfReplies));
				} else {
					if (log.isDebugEnabled()) {
						log.debug(">>> No name for author with person id " + q.getAuthor() + " found.");
						log.debug(">>> Skipping question with id " + q.getId());
					}
				}
			}
		}
		return result;
	}

	@Override
	public List<Question> getRecentQuestionsByDays(Integer days) throws DataAccessLayerException, ModelException {
		if (days == null) {
			throw new IllegalArgumentException("Days must not be null.");
		}
		if (days < 0) {
			throw new IllegalArgumentException("Days must be greater or equal zero.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving questions by days: " + days);
		}
		
		List<Question> result = new LinkedList<Question>();
		List<QuestionDto> questionDtos = null;
		Long numberOfReplies = null;
		Name name = null;
		
		// Retrieve questions.
		questionDtos = questionDao.getMostRecentByDays(days);
		for (QuestionDto q : questionDtos) {
			numberOfReplies = null;
			name = null;
			if (log.isDebugEnabled()) {
				log.debug(">>> Found question: " + q.toString());
			}
			if (q.getAuthor() != null) {
				name = personService.getName(q.getAuthor());
				if (name != null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Found author first name: " + name.getFirstName());
						log.debug(">>> Found author last name: " + name.getLastName());
						log.debug(">>> Found author nick name: " + name.getNickName());
					}
					// Only add question if name could be found.
					numberOfReplies = getNumberOfRepliesByQuestion(q.getId(), false, null, null);
					result.add(reassembleQuestion(q, name, numberOfReplies));
				} else {
					if (log.isDebugEnabled()) {
						log.debug(">>> No name for author with person id " + q.getAuthor() + " found.");
						log.debug(">>> Skipping question with id " + q.getId());
					}
				}
			}
		}
		return result;
	}

	@Override
	public List<Question> getRecentQuestionsBySize(Integer resultSize) throws DataAccessLayerException, ModelException {
		if (resultSize == null) {
			throw new IllegalArgumentException("Result size must not be null.");
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Retrieving questions by size: " + resultSize);
		}
		
		List<Question> result = new LinkedList<Question>();
		List<QuestionDto> questionDtos = null;
		Long numberOfReplies = null;
		Name name = null;
		
		// Retrieve questions.
		questionDtos = questionDao.getMostRecentBySize(resultSize);
		for (QuestionDto q : questionDtos) {
			numberOfReplies = null;
			name = null;
			if (log.isDebugEnabled()) {
				log.debug(">>> Found question: " + q.toString());
			}
			if (q.getAuthor() != null) {
				name = personService.getName(q.getAuthor());
				if (name != null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Found author first name: " + name.getFirstName());
						log.debug(">>> Found author last name: " + name.getLastName());
						log.debug(">>> Found author nick name: " + name.getNickName());
					}
					// Only add question if name could be found.
					numberOfReplies = getNumberOfRepliesByQuestion(q.getId(), false, null, null);
					result.add(reassembleQuestion(q, name, numberOfReplies));
				} else {
					if (log.isDebugEnabled()) {
						log.debug(">>> No name for author with person id " + q.getAuthor() + " found.");
						log.debug(">>> Skipping question with id " + q.getId());
					}
				}
			}
		}
		return result;
	}

	@Override
	public List<Reply> getRecentRepliesByDays(Integer days, Boolean publicOnly) throws DataAccessLayerException, ModelException {
		if (days == null) {
			throw new IllegalArgumentException("Days must not be null.");
		}
		if (days < 0) {
			throw new IllegalArgumentException("Days must be greater than or equal zero.");
		}
		if (publicOnly == null) {
			throw new IllegalArgumentException("Public only information must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving replies by days: " + days);
		}
		
		List<Reply> result = new LinkedList<Reply>();
		List<ReplyDto> replyDtos = null;
		Name name = null;
		Question question = null;
		
		// Retrieve questions.
		replyDtos = replyDao.getMostRecentByDays(days, publicOnly);
		for (ReplyDto r : replyDtos) {
			name = null;
			question = null;
			if (log.isDebugEnabled()) {
				log.debug(">>> Found question: " + r.toString());
			}
			if (r.getPersonId() == null || r.getQuestionId() == null) {
				if (log.isDebugEnabled()) {
					log.debug(">>> Person id or question id of reply to question " + r.getQuestionId() + " by person " + r.getPersonId() + " is not set. Skipping reply.");
				}
			} else {
				name = personService.getName(r.getPersonId());
				if (name != null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Found author first name: " + name.getFirstName());
						log.debug(">>> Found author last name: " + name.getLastName());
						log.debug(">>> Found author nick name: " + name.getNickName());
					}
					question = getQuestionById(r.getQuestionId());
					if (question != null) {
						if (log.isDebugEnabled()) {
							log.debug(">>> Found question: " + question);
						}
						// Only add reply if name and question could be found.
						result.add(reassembleReply(r, name, question));
					} else {
						if (log.isDebugEnabled()) {
							log.debug(">>> No question for question with id " + r.getQuestionId() + " found.");
							log.debug(">>> Skipping reply with question id " + r.getQuestionId());
						}
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug(">>> No name for author with person id " + r.getPersonId() + " found.");
						log.debug(">>> Skipping reply with question id " + r.getQuestionId());
					}
				}
			}
		}
		return result;
	}

	@Override
	public List<Reply> getRecentRepliesBySize(Integer resultSize, Boolean publicOnly) throws DataAccessLayerException, ModelException {
		if (resultSize == null) {
			throw new IllegalArgumentException("Result size must not be null.");
		}
		if (resultSize <= 0) {
			throw new IllegalArgumentException("Result size must be greater than zero.");
		}
		if (publicOnly == null) {
			throw new IllegalArgumentException("Public only information must not be null.");
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Retrieving replies by size: " + resultSize);
		}
		
		List<Reply> result = new LinkedList<Reply>();
		List<ReplyDto> replyDtos = null;
		Name name = null;
		Question question = null;
		
		// Retrieve questions.
		replyDtos = replyDao.getMostRecentBySize(resultSize, publicOnly);
		for (ReplyDto r : replyDtos) {
			name = null;
			question = null;
			if (log.isDebugEnabled()) {
				log.debug(">>> Found question: " + r.toString());
			}
			if (r.getPersonId() == null || r.getQuestionId() == null) {
				if (log.isDebugEnabled()) {
					log.debug(">>> Person id or question id of reply to question " + r.getQuestionId() + " by person " + r.getPersonId() + " is not set. Skipping reply.");
				}
			} else {
				name = personService.getName(r.getPersonId());
				if (name != null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Found author first name: " + name.getFirstName());
						log.debug(">>> Found author last name: " + name.getLastName());
						log.debug(">>> Found author nick name: " + name.getNickName());
					}
					question = getQuestionById(r.getQuestionId());
					if (question != null) {
						if (log.isDebugEnabled()) {
							log.debug(">>> Found question: " + question);
						}
						// Only add reply if name could be found.
						result.add(reassembleReply(r, name, question));
					} else {
						if (log.isDebugEnabled()) {
							log.debug(">>> No question for question with id " + r.getQuestionId() + " found.");
							log.debug(">>> Skipping reply with question id " + r.getQuestionId());
						}
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug(">>> No name for author with person id " + r.getPersonId() + " found.");
						log.debug(">>> Skipping reply with question id " + r.getQuestionId());
					}
				}
			}
		}
		return result;
	}

	@Override
	public Reply getReplyById(Long personId, Long questionId) throws DataAccessLayerException, ModelException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (questionId == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Retrieving reply by id. Person id: " + personId + ", question id: " + questionId);
		}
		ReplyDto replyDto = null;
		Name name = null;
		Question question = null;
		
		// Retrieve reply.
		try {
			replyDto = replyDao.getById(personId, questionId);
		}
		catch (EmptyResultDataAccessException e) {
			if (log.isDebugEnabled()) {
				log.debug("No reply of person with id " + personId + " for question with id " + questionId + " found: " + e.getMessage());
			}
			return null;
		}
		if (log.isDebugEnabled()) {
			log.debug(">>> Found reply: " + replyDto.toString());
		}
		// Retrieve additional person and question info.
		if (replyDto.getPersonId() == null || replyDto.getQuestionId() == null) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Person id or question id of reply to question " + replyDto.getQuestionId() + " by person " + replyDto.getPersonId() + " is not set. Skipping reply.");
			}
		} else {
			name = personService.getName(replyDto.getPersonId());
			if (name != null) {
				if (log.isDebugEnabled()) {
					log.debug(">>> Found author first name: " + name.getFirstName());
					log.debug(">>> Found author last name: " + name.getLastName());
					log.debug(">>> Found author nick name: " + name.getNickName());
				}
				question = getQuestionById(replyDto.getQuestionId());
				if (question != null) {
					// Only return reply if name and question could be found.
					return reassembleReply(replyDto, name, question);
				} else {
					if (log.isDebugEnabled()) {
						log.debug(">>> No question for question with id " + replyDto.getQuestionId() + " found.");
						log.debug(">>> Skipping reply with question id " + replyDto.getQuestionId());
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug(">>> No name for author with person id " + personId + " found.");
					log.debug(">>> Skipping reply with question id " + questionId);
				}
			}
		}
		return null;
	}
	
	@Override
	public Map<Long,Reply> getRepliesByIds(Long personId, List<Long> questionIds) throws DataAccessLayerException, ModelException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (questionIds == null) {
			throw new IllegalArgumentException("List of question ids must not be null.");
		}
		if (questionIds.isEmpty()) {
			throw new IllegalArgumentException("List of question ids must not be empty.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving a set of replies of person with id: " + personId + ". Number of replies: " + questionIds.size());
		}
		
		Map<Long,Reply> result = new HashMap<Long,Reply>();
		List<ReplyDto> replyDtos = null;
		Name name = null;
		Question question = null;
		
		// Retrieve person and question information.
		name = personService.getName(personId);
		if (name != null) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found author first name: " + name.getFirstName());
				log.debug(">>> Found author last name: " + name.getLastName());
				log.debug(">>> Found author nick name: " + name.getNickName());
			}
			// Only retrieve replies if name could be found.
			replyDtos = replyDao.getByIds(personId, questionIds);
			for (ReplyDto r : replyDtos) {
				if (log.isDebugEnabled()) {
					log.debug(">>> Found reply: " + r.toString());
				}
				question = null;
				if (r.getQuestionId() != null) {
					question = getQuestionById(r.getQuestionId());
					if (question != null) {
						result.put(r.getQuestionId(), reassembleReply(r, name, question));
					} else {
						log.debug(">>> No data for question with id " + r.getQuestionId() + " found.");
						log.debug(">>> Skipping reply with question id " + r.getQuestionId());
					}
				} else {
					log.debug(">>> No question id set. Skipping reply with question id " + r.getQuestionId());
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug(">>> No name for author with person id " + personId + " found.");
			}
		}
		return result;
	}

	@Override
	public List<Reply> getRepliesByPerson(Long personId, Boolean fbId, Boolean skipped, Boolean inPrivate, Boolean critical) throws DataAccessLayerException, ModelException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all replies by person with id: " + personId);
			log.debug("Facebook id: " + fbId);
			log.debug("Skipped: " + skipped);
			log.debug("Private: " + inPrivate);
			log.debug("Critical: " + critical);
		}
		
		// Convert id if necessary.
		if (fbId) {
			personId = personService.getAlternativeId(personId, fbId);
		}
		
		List<Reply> result = new LinkedList<Reply>();
		List<ReplyDto> replyDtos = null;
		Name name = null;
		Question question = null;
		
		// Retrieve person information.
		name = personService.getName(personId);
		if (name != null) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found author first name: " + name.getFirstName());
				log.debug(">>> Found author last name: " + name.getLastName());
				log.debug(">>> Found author nick name: " + name.getNickName());
			}
			// Only retrieve replies if name could be found.
			replyDtos = replyDao.getByPerson(personId, skipped, inPrivate, critical);
			for (ReplyDto r : replyDtos) {
				if (log.isDebugEnabled()) {
					log.debug(">>> Found reply: " + r.toString());
				}
				question = null;
				if (r.getQuestionId() != null) {
					question = getQuestionById(r.getQuestionId());
					if (question != null) {
						result.add(reassembleReply(r, name, question));
					} else {
						log.debug(">>> No data for question with id " + r.getQuestionId() + " found.");
						log.debug(">>> Skipping reply with question id " + r.getQuestionId());
					}
				} else {
					log.debug(">>> No question id set. Skipping reply with question id " + r.getQuestionId());
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug(">>> No name for author with person id " + personId + " found.");
			}
		}
		return result;
	}

	@Override
	public List<Reply> getRepliesByPerson(Long personId, Integer offset, Integer resultSize) throws DataAccessLayerException, ModelException {
		if (personId == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (offset == null) {
			throw new IllegalArgumentException("Offset must not be null.");
		}
		if (offset < 0) {
			throw new IllegalArgumentException("Offset must be greater than or equals zero.");
		}
		if (resultSize == null) {
			throw new IllegalArgumentException("Result size must not be null.");
		}
		if (resultSize <= 0) {
			throw new IllegalArgumentException("Result size must be greater than zero.");
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all replies by person with id: " + personId);
		}
		
		List<Reply> result = new LinkedList<Reply>();
		List<ReplyDto> replyDtos = null;
		Name name = null;
		Question question = null;
		
		// Retrieve person information.
		name = personService.getName(personId);
		if (name != null) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found author first name: " + name.getFirstName());
				log.debug(">>> Found author last name: " + name.getLastName());
				log.debug(">>> Found author nick name: " + name.getNickName());
			}
			// Only retrieve replies if name could be found.
			replyDtos = replyDao.getByPerson(personId, offset, resultSize);
			for (ReplyDto r : replyDtos) {
				question = null;
				if (log.isDebugEnabled()) {
					log.debug(">>> Found reply: " + r.toString());
				}
				if (r.getQuestionId() != null) {
					question = getQuestionById(r.getQuestionId());
					if (question != null) {
						// Only add reply if question could be found.
						result.add(reassembleReply(r, name, question));
					} else {
						log.debug(">>> No data for question with id " + r.getQuestionId() + " found.");
						log.debug(">>> Skipping reply with question id " + r.getQuestionId());
					}
				} else {
					log.debug(">>> No question id set. Skipping reply with question id " + r.getQuestionId());
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug(">>> No name for author with person id " + personId + " found.");
			}
		}
		return result;
	}

	@Override
	public List<Reply> getRepliesByQuestion(Long questionId) throws DataAccessLayerException, ModelException {
		if (questionId == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all replies for qeustion with id: " + questionId);
		}
		
		List<Reply> result = new LinkedList<Reply>();
		List<ReplyDto> replyDtos = null;
		Name name = null;
		Question question = null;
		
		// Retrieve replies.
		replyDtos = replyDao.getByQuestion(questionId);
		for (ReplyDto r : replyDtos) {
			name = null;
			question = null;
			if (log.isDebugEnabled()) {
				log.debug(">>> Found reply: " + r.toString());
			}
			if (r.getPersonId() == null || r.getQuestionId() == null) {
				if (log.isDebugEnabled()) {
					log.debug(">>> Person id or question id of reply to question " + r.getQuestionId() + " by person " + r.getPersonId() + " is not set. Skipping reply.");
				}
			} else {
				name = personService.getName(r.getPersonId());
				if (name != null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Found author first name: " + name.getFirstName());
						log.debug(">>> Found author last name: " + name.getLastName());
						log.debug(">>> Found author nick name: " + name.getNickName());
					}
					question = getQuestionById(r.getQuestionId());
					if (question != null) {
						// Only add reply if name and question could be found.
						if (log.isDebugEnabled()) {
							log.debug(">>> Found question: " + question);
						}
						result.add(reassembleReply(r, name, question));
					} else {
						if (log.isDebugEnabled()) {
							log.debug(">>> No data for question with id " + r.getQuestionId() + " found.");
							log.debug(">>> Skipping reply with question id " + r.getQuestionId());
						}
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug(">>> No name for author with person id " + r.getPersonId() + " found.");
						log.debug(">>> Skipping reply with question id " + r.getQuestionId());
					}
				}
			}
		}
		return result;
	}

	@Override
	public List<Reply> getRepliesByQuestion(Long questionId, Integer offset, Integer resultSize) throws DataAccessLayerException, ModelException {
		if (questionId == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (offset == null) {
			throw new IllegalArgumentException("Offset must not be null.");
		}
		if (resultSize == null) {
			throw new IllegalArgumentException("Result size must not be null.");
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all replies for qeustion with id: " + questionId);
		}
		
		List<Reply> result = new LinkedList<Reply>();
		List<ReplyDto> replyDtos = null;
		Name name = null;
		Question question = null;
		
		// Retrieve replies.
		replyDtos = replyDao.getByQuestion(questionId, offset, resultSize);
		for (ReplyDto r : replyDtos) {
			name = null;
			question = null;
			if (log.isDebugEnabled()) {
				log.debug(">>> Found reply: " + r.toString());
			}
			if (r.getPersonId() == null || r.getQuestionId() == null) {
				if (log.isDebugEnabled()) {
					log.debug(">>> Person id or question id of reply to question " + r.getQuestionId() + " by person " + r.getPersonId() + " is not set. Skipping reply.");
				}
			} else {
				name = personService.getName(r.getPersonId());
				if (name != null) {
					if (log.isDebugEnabled()) {
						log.debug(">>> Found author first name: " + name.getFirstName());
						log.debug(">>> Found author last name: " + name.getLastName());
						log.debug(">>> Found author nick name: " + name.getNickName());
					}
					question = getQuestionById(r.getQuestionId());
					if (question != null) {
						// Only add reply if name and question could be found.
						if (log.isDebugEnabled()) {
							log.debug(">>> Found question: " + question);
						}
						result.add(reassembleReply(r, name, question));
					} else {
						if (log.isDebugEnabled()) {
							log.debug(">>> No data for question with id " + r.getQuestionId() + " found.");
							log.debug(">>> Skipping reply with question id " + r.getQuestionId());
						}
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug(">>> No name for author with person id " + r.getPersonId() + " found.");
						log.debug(">>> Skipping reply with question id " + r.getQuestionId());
					}
				}
			}
		}
		return result;
	}
	
	@Override
	public List<Question> getSimilarQuestions(Question question) throws DataAccessLayerException, ModelException {
		if (question == null) {
			throw new IllegalArgumentException("Question must not be null.");
		}
		if (question.getQuestion() == null) {
			throw new IllegalArgumentException("Question string must not be null.");
		}
		if (question.getQuestion().length() == 0) {
			throw new IllegalArgumentException("Question string must not be empty.");
		}
		List<Question> result = new LinkedList<Question>();
		Long numberOfReplies = null;
		try {
			// Beware! Before submitting any String to Solr its special characters have to be escaped!!
			List<QuestionDto> questionDtos = searchableQuestionDao.getBySearchTerm(ClientUtils.escapeQueryChars(question.getQuestion()));
			Map<Long,Name> idNameMapping = new HashMap<Long,Name>();
			for (QuestionDto qDto : questionDtos) {
				numberOfReplies = null;
				validateQuestion(qDto, false);
				if (!idNameMapping.containsKey(qDto.getAuthor())) {
					try {
						Name name = personService.getName(qDto.getAuthor());
						if (name != null) {
							numberOfReplies = getNumberOfRepliesByQuestion(qDto.getId(), false, null, null);
							idNameMapping.put(qDto.getAuthor(), name);
							result.add(reassembleQuestion(qDto, name, numberOfReplies));
						} else {
							if (log.isTraceEnabled()) {
								log.trace("Skipping question with id " + qDto.getId() +
								" because author name could not be retrieved. Author id: " + qDto.getAuthor());
							}
						}
					} catch (DataAccessLayerException e) {
						if (log.isTraceEnabled()) {
							log.trace("Skipping question with id " + qDto.getId() +
							" because author name could not be retrieved. Author id: " + qDto.getAuthor());
						}
					}
				} else {
					// Name has already been retrieved.
					Name name = idNameMapping.get(qDto.getAuthor());
					numberOfReplies = getNumberOfRepliesByQuestion(qDto.getId(), false, null, null);
					result.add(reassembleQuestion(qDto, name, numberOfReplies));
				}
			}
		} catch (SolrServerException e) {
			if (log.isDebugEnabled()) {
				log.debug("Error while accessing the Solr server: " + e.getMessage());
			}
		}
		return result;
	}

	@Override
	public void updateQuestion(Question question) throws DataAccessLayerException, InconsistentModelException {
		if (question == null) {
			throw new IllegalArgumentException("Question must not be null.");
		}
		if (question.getId() == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (question.getAuthorId() == null) {
			throw new IllegalArgumentException("Author id must not be null.");
		}
		if (!personService.exists(question.getAuthorId(), false) || !personService.isActive(question.getAuthorId(), false)) {
			throw new IllegalArgumentException("Author with id " + question.getAuthorId() + " does not exist or is not active.");
		}
		if (question.getAuthorName() == null) {
			Name authorName = personService.getName(question.getAuthorId());
			if (authorName == null) {
				throw new IllegalArgumentException("No name information for author with id " + question.getId() + " found.");
			}
			question.setAuthorName(authorName);
		}
		
		// Perform validation checks.
		validateQuestion(question, false);
		
		// Update question in database.
		QuestionDto questionDto = disassembleQuestion(question);
		NameDto nameDto = disassembleName(question);
		if (log.isDebugEnabled()) {
			log.debug("Updating question: " + questionDto.toString() + 
					" created by author with id: " + nameDto.getPersonId());
		}
		questionDao.update(questionDto);
		if (log.isDebugEnabled()) {
			log.debug("Updated question in database. Question id: " + question.getId());
		}
		
		// Add question to the search index.
		try {
			searchableQuestionDao.update(questionDto, nameDto);
			if (log.isDebugEnabled()) {
				log.debug("Updated question in the search index. Question id: " + question.getId());
			}
		} catch (SolrServerException e) {
			if (log.isDebugEnabled()) {
				log.debug("Error while updating question with id " + question.getId() + " in the search index. Reason: ", e);
			}
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug("Error while updating question with id " + question.getId() + " in the search index. Reason: ", e);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Error while updating question with id " + question.getId() + " in the search index. Reason: ", e);
			}
		}
		
	}

	@Override
	public void updateReply(Reply reply) throws InconsistentModelException {
		if (reply == null) {
			throw new IllegalArgumentException("Reply must not be null.");
		}
		if (reply.getPersonId() == null) {
			throw new IllegalArgumentException("Person id must not be null.");
		}
		if (reply.getQuestionId() == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		
		// Perform validation checks.
		validateReply(reply, false);
		
		// Update reply in database.
		ReplyDto replyDto = disassembleReply(reply);
		if (log.isDebugEnabled()) {
			log.debug("Updating reply with id: " + replyDto.toString());
		}
		replyDao.update(replyDto);
	}
	
	@Override
	public Importance getImportanceByWeight(Integer weight) throws DataAccessLayerException {
		if (weight == null) {
			throw new IllegalArgumentException("Weight must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Retrieving importance by weight: " + weight);
		}
		ImportanceDto importanceDto = importanceDao.getByWeight(weight);
		return reassembleImportance(importanceDto);
	}
	
	@Override
	public List<Importance> getAllImportanceLevels() throws DataAccessLayerException {
		if (log.isDebugEnabled()) {
			log.debug("Retrieving all importance levels.");
		}
		List<Importance> result = new LinkedList<Importance>();
		List<ImportanceDto> importanceDtos = null;
		
		// Retrieve importance levels.
		importanceDtos = importanceDao.getAll();
		for (ImportanceDto i : importanceDtos) {
			if (log.isDebugEnabled()) {
				log.debug(">>> Found importance level: " + i.toString());
			}
			result.add(reassembleImportance(i));
		}
		return result;
	}
	
	public static Question reassembleQuestion(QuestionDto questionDto, Name authorName, Long numberOfReplies) throws InconsistentStateException, InconsistentModelException {
		if (questionDto == null) {
			throw new IllegalArgumentException("Question DTO must not be null.");
		}
		if (authorName == null) {
			throw new IllegalArgumentException("Author name must not be null.");
		}
		if (numberOfReplies == null) {
			throw new IllegalArgumentException("Number of replies must not be null.");
		}
		if (numberOfReplies < 0) {
			throw new IllegalArgumentException("Number of replies must be greater or equals 0.");
		}
		
		// Validate.
		validateQuestion(questionDto, false);
		StandardPersonService.validateName(authorName);
		if (questionDto.getAuthor() != authorName.getPersonId()) {
			throw new IllegalStateException("The id of the author and the person id of the author name must be equal.");
		}
		
		// Reassemble.
		if (log.isDebugEnabled()) {
			log.debug("Reassembling question: " + questionDto.toString());
		}
		Question question = new Question();
		question.setId(questionDto.getId());
		if (log.isDebugEnabled()) {
			log.debug(">>> Id: " + questionDto.getId());
		}
		question.setCreated(questionDto.getCreated());
		if (log.isDebugEnabled()) {
			log.debug(">>> Created: "+ questionDto.getCreated());
		}
		if (questionDto.getAuthor() != null) {
			question.setAuthorId(questionDto.getAuthor());
			if (log.isDebugEnabled()) {
				log.debug(">>> Author id: " + questionDto.getAuthor());
			}
		}
		if (authorName != null) {
			question.setAuthorName(authorName);
			if (log.isDebugEnabled()) {
				log.debug(">>> Author name: " + authorName.toString());
			}
		}
//		if (authorFirstName != null) {
//			question.setAuthorFirstName(authorFirstName);
//			if (log.isDebugEnabled()) {
//				log.debug(">>> Author first name: " + authorFirstName);
//			}
//		}
//		if (authorLastName != null) {
//			question.setAuthorLastName(authorLastName);
//			if (log.isDebugEnabled()) {
//				log.debug(">>> Author last name: " + authorLastName);
//			}
//		}
//		if (authorNickName != null) {
//			question.setAuthorNickName(authorNickName);
//			if (log.isDebugEnabled()) {
//				log.debug(">>> Author nick name: " + authorNickName);
//			}
//		}
		question.setQuestion(questionDto.getQuestion());
		if (log.isDebugEnabled()) {
			log.debug(">>> Question: " + questionDto.getQuestion());
		}
		question.setAnswerA(questionDto.getAnswerA());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer A: " + questionDto.getAnswerA());
		}
		question.setAnswerB(questionDto.getAnswerB());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer B: " + questionDto.getAnswerB());
		}
		question.setAnswerC(questionDto.getAnswerC());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer C: " + questionDto.getAnswerC());
		}
		question.setAnswerD(questionDto.getAnswerD());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer D: " + questionDto.getAnswerD());
		}
		question.setAnswerE(questionDto.getAnswerE());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer E: " + questionDto.getAnswerE());
		}
		question.setNumberOfReplies(numberOfReplies);
		if (log.isDebugEnabled()) {
			log.debug(">>> Number of replies: " + numberOfReplies);
		}
		return question;
	}
	
	public static Reply reassembleReply(ReplyDto replyDto, Name personName, Question question) throws InconsistentStateException, InconsistentModelException {
		if (replyDto == null) {
			throw new IllegalArgumentException("Reply DTO must not be null.");
		}
		if (personName == null) {
			throw new IllegalArgumentException("Person name must not be null.");
		}
		if (question == null) {
			throw new IllegalArgumentException("Question must not be null.");
		}
		
		// Validate.
		validateReply(replyDto, false);
		validateQuestion(question, false);
		StandardPersonService.validateName(personName);
		
		
		// Reassemble.
		if (log.isDebugEnabled()) {
			log.debug("Reassembling reply: " + replyDto.toString());
		}
		Reply reply = new Reply();
		reply.setPersonId(replyDto.getPersonId());
		if (log.isDebugEnabled()) {
			log.debug(">>> Person id: " + replyDto.getPersonId());
		}
		reply.setPersonName(personName);
		if (log.isDebugEnabled()) {
			log.debug(">>> Person name: " + personName);
		}
//		reply.setPersonFirstName(personFirstName);
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Person first name: " + personFirstName);
//		}
//		reply.setPersonLastName(personLastName);
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Person name: " + personLastName);
//		}
//		reply.setPersonNickName(personNickName);
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Person nick name: " + personNickName);
//		}
		reply.setQuestionId(replyDto.getQuestionId());
		if (log.isDebugEnabled()) {
			log.debug(">>> Question id: " + replyDto.getQuestionId());
		}
		reply.setQuestion(question);
		if (log.isDebugEnabled()) {
			log.debug(">>> Question: " + question);
		}
		reply.setSkipped(replyDto.getSkipped());
		if (log.isDebugEnabled()) {
			log.debug(">>> Skipped: " + replyDto.getSkipped());
		}
		reply.setInPrivate(replyDto.getInPrivate());
		if (log.isDebugEnabled()) {
			log.debug(">>> Private: " + replyDto.getInPrivate());
		}
		reply.setCritical(replyDto.getCritical());
		if (log.isDebugEnabled()) {
			log.debug(">>> Critical: " + replyDto.getCritical());
		}
		reply.setLastUpdate(replyDto.getLastUpdate());
		if (log.isDebugEnabled()) {
			log.debug(">>> Last update: " + replyDto.getLastUpdate());
		}
		reply.setImportance(replyDto.getImportance());
		if (log.isDebugEnabled()) {
			log.debug(">>> Importance: " + replyDto.getImportance());
		}
		reply.setExplanation(replyDto.getExplanation());
		if (log.isDebugEnabled()) {
			log.debug(">>> Explanation: " + replyDto.getExplanation());
		}
		reply.setAnswerA(replyDto.getAnswerA());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer A: " + replyDto.getAnswerA());
		}
		reply.setAnswerB(replyDto.getAnswerB());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer B: " + replyDto.getAnswerA());
		}
		reply.setAnswerC(replyDto.getAnswerC());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer C: " + replyDto.getAnswerA());
		}
		reply.setAnswerD(replyDto.getAnswerD());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer D: " + replyDto.getAnswerA());
		}
		reply.setAnswerE(replyDto.getAnswerE());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer E: " + replyDto.getAnswerA());
		}
		return reply;
	}
	
	public static Importance reassembleImportance(ImportanceDto importanceDto) throws InconsistentStateException {
		if (importanceDto == null) {
			throw new IllegalArgumentException("Importance DTO must not be null.");
		}
		
		// Validate.
		validateImportance(importanceDto);
		
		// Reassemble.
		if (log.isDebugEnabled()) {
			log.debug("Reassembling importance: " + importanceDto.toString());
		}
		Importance importance = new Importance();
		importance.setWeight(importanceDto.getWeight());
		if (log.isDebugEnabled()) {
			log.debug(">>> Weight: " + importanceDto.getWeight());
		}
		importance.setDescription(importanceDto.getDescription());
		if (log.isDebugEnabled()) {
			log.debug(">>> Description: " + importanceDto.getDescription());
		}
		return importance;
	}
	
	public static QuestionDto disassembleQuestion(Question question) throws InconsistentModelException {
		if (question == null) {
			throw new IllegalArgumentException("Question must not be null.");
		}
		
		// Validate.
		validateQuestion(question, question.getId() == null);
		
		// Disassemble.
		if (log.isDebugEnabled()) {
			log.debug("Disassembling question: " + question.toString());
		}
		QuestionDto questionDto = new QuestionDto();
		questionDto.setId(question.getId());
		if (log.isDebugEnabled()) {
			log.debug(">>> Id: " + question.getId());
		}
		questionDto.setQuestion(question.getQuestion());
		if (log.isDebugEnabled()) {
			log.debug(">>> Question: " + question.getQuestion());
		}
		questionDto.setAuthor(question.getAuthorId());
		if (log.isDebugEnabled()) {
			log.debug(">>> Author: " + question.getAuthorId());
		}
		questionDto.setCreated(question.getCreated());
		if (log.isDebugEnabled()) {
			log.debug(">>> Created: " + question.getCreated());
		}
		questionDto.setAnswerA(question.getAnswerA());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer A: " + question.getAnswerA());
		}
		questionDto.setAnswerB(question.getAnswerB());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer B: " + question.getAnswerB());
		}
		questionDto.setAnswerC(question.getAnswerC());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer C: " + question.getAnswerC());
		}
		questionDto.setAnswerD(question.getAnswerD());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer D: " + question.getAnswerD());
		}
		questionDto.setAnswerE(question.getAnswerE());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer E: " + question.getAnswerE());
		}
		return questionDto;
	}
	
//	public static QuestionDto disassembleQuestion(Question question, Name authorName) throws InconsistentModelException {
//		if (question == null) {
//			throw new IllegalArgumentException("Question must not be null.");
//		}
//		if (authorName == null) {
//			throw new IllegalArgumentException("Author name must not be null.");
//		}
//		
//		// Validate.
//		validateQuestion(question, authorName, question.getId() == null);
//		
//		// Disassemble.
//		if (log.isDebugEnabled()) {
//			log.debug("Disassembling question: " + question.toString());
//		}
//		QuestionDto questionDto = new QuestionDto();
//		questionDto.setId(question.getId());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Id: " + question.getId());
//		}
//		questionDto.setQuestion(question.getQuestion());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Question: " + question.getQuestion());
//		}
//		questionDto.setAuthor(question.getAuthorId());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Author: " + question.getAuthorId());
//		}
//		questionDto.setCreated(question.getCreated());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Created: " + question.getCreated());
//		}
//		questionDto.setAnswerA(question.getAnswerA());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Answer A: " + question.getAnswerA());
//		}
//		questionDto.setAnswerB(question.getAnswerB());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Answer B: " + question.getAnswerB());
//		}
//		questionDto.setAnswerC(question.getAnswerC());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Answer C: " + question.getAnswerC());
//		}
//		questionDto.setAnswerD(question.getAnswerD());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Answer D: " + question.getAnswerD());
//		}
//		questionDto.setAnswerE(question.getAnswerE());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Answer E: " + question.getAnswerE());
//		}
//		return questionDto;
//	}
	
	public static ReplyDto disassembleReply(Reply reply) throws InconsistentModelException {
		if (reply == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		
		// Validate.
		validateReply(reply, false);
		
		// Disassemble.
		if (log.isDebugEnabled()) {
			log.debug("Disassembling reply: " + reply.toString());
		}
		ReplyDto replyDto = new ReplyDto();
		replyDto.setPersonId(reply.getPersonId());
		if (log.isDebugEnabled()) {
			log.debug(">>> Person id: " + reply.getPersonId());
		}
		replyDto.setQuestionId(reply.getQuestionId());
		if (log.isDebugEnabled()) {
			log.debug(">>> Question id: " + reply.getQuestionId());
		}
		replyDto.setSkipped(reply.getSkipped());
		if (log.isDebugEnabled()) {
			log.debug(">>> Skipped: " + reply.getSkipped());
		}
		replyDto.setInPrivate(reply.getInPrivate());
		if (log.isDebugEnabled()) {
			log.debug(">>> Private: " + reply.getInPrivate());
		}
		replyDto.setCritical(reply.getCritical());
		if (log.isDebugEnabled()) {
			log.debug(">>> Critical: " + reply.getCritical());
		}
		replyDto.setLastUpdate(reply.getLastUpdate());
		if (log.isDebugEnabled()) {
			log.debug(">>> Last update: " + reply.getLastUpdate());
		}
		replyDto.setImportance(reply.getImportance());
		if (log.isDebugEnabled()) {
			log.debug(">>> Importance: " + reply.getImportance());
		}
		replyDto.setExplanation(reply.getExplanation());
		if (log.isDebugEnabled()) {
			log.debug(">>> Explanation: " + reply.getExplanation());
		}
		replyDto.setAnswerA(reply.getAnswerA());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer A: " + reply.getAnswerA());
		}
		replyDto.setAnswerB(reply.getAnswerB());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer B: " + reply.getAnswerB());
		}
		replyDto.setAnswerC(reply.getAnswerC());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer C: " + reply.getAnswerC());
		}
		replyDto.setAnswerD(reply.getAnswerD());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer D: " + reply.getAnswerD());
		}
		replyDto.setAnswerE(reply.getAnswerE());
		if (log.isDebugEnabled()) {
			log.debug(">>> Answer E: " + reply.getAnswerE());
		}
		return replyDto;
	}
	
	public static ImportanceDto disassembleImportance(Importance importance) throws InconsistentModelException {
		if (importance == null) {
			throw new IllegalArgumentException("Importance must not be null.");
		}
		
		// Validate.
		validateImportance(importance);
		
		// Disassemble.
		if (log.isDebugEnabled()) {
			log.debug("Disassembling importance: " + importance.toString());
		}
		ImportanceDto importanceDto = new ImportanceDto();
		importanceDto.setWeight(importance.getWeight());
		if (log.isDebugEnabled()) {
			log.debug(">>> Weight: " + importance.getWeight());
		}
		importanceDto.setDescription(importance.getDescription());
		if (log.isDebugEnabled()) {
			log.debug(">>> Description: " + importance.getDescription());
		}
		return importanceDto;
	}
	
	public static NameDto disassembleName(Question question) throws InconsistentModelException {
		if (question == null) {
			throw new IllegalArgumentException("Question must not be null.");
		}
		if (question.getAuthorName() == null) {
			throw new IllegalArgumentException("Question author name must not be null.");
		}
		
		// Validate.
		validateQuestion(question, question.getId() == null);
		
		// Disassemble.
		if (log.isDebugEnabled()) {
			log.debug("Disassembling name from question: " + question.toString());
		}
		Name name = question.getAuthorName();
		NameDto nameDto = new NameDto();
		if (log.isDebugEnabled()) {
			log.debug(">>> Author id: " + question.getAuthorId());
		}
		nameDto.setPersonId(question.getAuthorId());
		if (log.isDebugEnabled()) {
			log.debug(">>> Author first name: " + name.getFirstName());
		}
		nameDto.setFirstName(name.getFirstName());
		if (log.isDebugEnabled()) {
			log.debug(">>> Author last name: " + name.getLastName());
		}
		nameDto.setLastName(name.getLastName());
		if (log.isDebugEnabled()) {
			log.debug(">>> Author nick name: " + name.getNickName());
		}
		nameDto.setNickName(name.getNickName());
		return nameDto;
	}
	
//	public static NameDto disassembleName(Question question, Name authorName) throws InconsistentModelException {
//		if (question == null) {
//			throw new IllegalArgumentException("Question must not be null.");
//		}
//		
//		// Validate.
//		validateQuestion(question, authorName, question.getId() == null);
//		
//		// Disassemble.
//		if (log.isDebugEnabled()) {
//			log.debug("Disassembling name from question: " + question.toString());
//		}
//		NameDto nameDto = new NameDto();
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Author id: " + question.getAuthorId());
//		}
//		nameDto.setPersonId(question.getAuthorId());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Author first name: " + question.getAuthorFirstName());
//		}
//		nameDto.setFirstName(question.getAuthorFirstName());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Author last name: " + question.getAuthorLastName());
//		}
//		nameDto.setLastName(question.getAuthorLastName());
//		if (log.isDebugEnabled()) {
//			log.debug(">>> Author nick name: " + question.getAuthorNickName());
//		}
//		nameDto.setNickName(question.getAuthorNickName());
//		return nameDto;
//	}
	
	public static void validateQuestion(QuestionDto questionDto, boolean isNew) throws InconsistentStateException {
		if (questionDto == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		
		// Check mandatory question data.
		if (isNew) {
			if (questionDto.getId() != null) {
				throw new InconsistentStateException("Id must not be set.");
			}
		} else {
			if (questionDto.getId() == null) {
				throw new InconsistentStateException("Id must not be null.");
			}
		}
		if (questionDto.getAuthor() == null) {
			throw new InconsistentStateException("Author id must not be null.");
		}
		if (questionDto.getCreated() == null) {
			throw new InconsistentStateException("Creation date must not be null.");
		}
		if (questionDto.getQuestion() == null) {
			throw new InconsistentStateException("Question must not be null.");
		}
		if (questionDto.getAnswerA() == null) {
			throw new InconsistentStateException("First answer (A) must not be null.");
		}
		if (questionDto.getAnswerB() == null) {
			throw new InconsistentStateException("Second answer (B) must not be null.");
		}
//		if (questionDto.getAnswerC() == null) {
//			throw new InconsistentStateException("Third answer (C) must not be null.");
//		}
//		if (questionDto.getAnswerD() == null) {
//			throw new InconsistentStateException("Fourth answer (D) must not be null.");
//		}
//		if (questionDto.getAnswerE() == null) {
//			throw new InconsistentStateException("Sixth answer (E) must not be null.");
//		}
	}
	
	public static void validateQuestion(Question question, boolean isNew) throws InconsistentModelException {
		if (question == null) {
			throw new IllegalArgumentException("Question must not be null.");
		}
//		if (authorName == null) {
//			throw new IllegalArgumentException("Author name must not be null.");
//		}
//		if (authorName.getPersonId() == null) {
//			throw new IllegalArgumentException("Author id must not be null.");
//		}
//		if (authorName.getFirstName() == null) {
//			throw new IllegalArgumentException("Author first name must not be null.");
//		}
//		if (authorName.getLastName() == null) {
//			throw new IllegalArgumentException("Author last name must not be null.");
//		}
//		if (authorName.getNickName() == null) {
//			throw new IllegalArgumentException("Author nick name must not be null.");
//		}
		
		// Check mandatory question data.
		if (isNew) {
			if (question.getId() != null) {
				throw new InconsistentModelException("Id must not be set.");
			}
		} else {
			if (question.getId() == null) {
				throw new InconsistentModelException("Id must not be null.");
			}
		}
		if (question.getAuthorId() == null) {
			throw new InconsistentModelException("Author id must not be null.");
		}
		if (question.getAuthorName() != null) {
			if (!question.getAuthorId().equals(question.getAuthorName().getPersonId())) {
				throw new InconsistentModelException("Author id and person id of author name must be equal.");
			}
		}
//		if (question.getAuthorFirstName() == null) {
//			throw new InconsistentModelException("Author first name must not be null.");
//		}
//		if (!authorName.getFirstName().equals(question.getAuthorFirstName())) {
//			throw new InconsistentModelException("Author first name is " + question.getAuthorFirstName() + " instead of " + authorName.getFirstName());
//		}
//		if (question.getAuthorLastName() == null) {
//			throw new InconsistentModelException("Author last name must not be null.");
//		}
//		if (!authorName.getLastName().equals(question.getAuthorLastName())) {
//			throw new InconsistentModelException("Author last name is " + question.getAuthorLastName() + " instead of " + authorName.getLastName());
//		}
//		if (question.getAuthorNickName() == null) {
//			throw new InconsistentModelException("Author nick name must not be null.");
//		}
//		if (!authorName.getNickName().equals(question.getAuthorNickName())) {
//			throw new InconsistentModelException("Author nick name is " + question.getAuthorNickName() + " instead of " + authorName.getNickName());
//		}
		if (question.getCreated() == null) {
			throw new InconsistentModelException("Creation date must not be null.");
		}
		if (question.getQuestion() == null) {
			throw new InconsistentModelException("Question must not be null.");
		}
		if (question.getAnswerA() == null) {
			throw new InconsistentModelException("First answer (A) must not be null.");
		}
		if (question.getAnswerB() == null) {
			throw new InconsistentModelException("Second answer (B) must not be null.");
		}
	}
	
	public static void validateReply(ReplyDto replyDto, boolean isNew) throws InconsistentStateException {
		if (replyDto == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		
		// Check mandatory reply data.
		if (replyDto.getPersonId() == null) {
			throw new InconsistentStateException("Person id must not be null.");
		}
		if (replyDto.getQuestionId() == null) {
			throw new InconsistentStateException("Question id must not be null.");
		}
		if (replyDto.getSkipped() == null) {
			throw new InconsistentStateException("Skipped information must not be null.");
		}
		if (replyDto.getInPrivate() == null) {
			throw new InconsistentStateException("In private information must not be null.");
		}
		if (replyDto.getCritical() == null) {
			throw new InconsistentStateException("Critical information must not be null.");
		}
		if (replyDto.getLastUpdate() == null) {
			throw new InconsistentStateException("Last update date must not be null.");
		}
		if (replyDto.getImportance() == null) {
			throw new InconsistentStateException("Importance must not be null.");
		}
		if (replyDto.getAnswerA() == null) {
			throw new InconsistentStateException("Answer A must not be null.");
		}
		if (replyDto.getAnswerB() == null) {
			throw new InconsistentStateException("Answer B must not be null.");
		}
		if (replyDto.getAnswerC() == null) {
			throw new InconsistentStateException("Answer C must not be null.");
		}
		if (replyDto.getAnswerD() == null) {
			throw new InconsistentStateException("Answer D must not be null.");
		}
		if (replyDto.getAnswerE() == null) {
			throw new InconsistentStateException("Answer E must not be null.");
		}
	}
	
	public static void validateReply(Reply reply, boolean isNew) throws InconsistentModelException {
		if (reply == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		
		// Check mandatory reply data.
		if (reply.getPersonId() == null) {
			throw new InconsistentModelException("Person id must not be null.");
		}
		if (reply.getPersonName() != null) {
			if (!reply.getPersonId().equals(reply.getPersonName().getPersonId())) {
				throw new InconsistentModelException("Person id and person name person id must be equal.");
			}
		}
		if (reply.getQuestionId() == null) {
			throw new InconsistentModelException("Question id must not be null.");
		}
		if (reply.getQuestion() != null) {
			if (!reply.getQuestionId().equals(reply.getQuestion().getId())) {
				throw new InconsistentModelException("Question id and question question id must be equal.");
			}
		}
		if (reply.getSkipped() == null) {
			throw new InconsistentModelException("Skipped information must not be null.");
		}
		if (reply.getInPrivate() == null) {
			throw new InconsistentModelException("In private information must not be null.");
		}
		if (reply.getCritical() == null) {
			throw new InconsistentModelException("Critical information must not be null.");
		}
		if (reply.getLastUpdate() == null) {
			throw new InconsistentModelException("Last update date must not be null.");
		}
		if (!reply.getSkipped() && reply.getImportance() == null) {
			throw new InconsistentModelException("Importance must not be null.");
		}
		if (!reply.getSkipped() && reply.getAnswerA() == null) {
			throw new InconsistentModelException("Answer A must not be null.");
		}
		if (!reply.getSkipped() && reply.getAnswerB() == null) {
			throw new InconsistentModelException("Answer B must not be null.");
		}
		if (!reply.getSkipped() && reply.getAnswerC() == null) {
			throw new InconsistentModelException("Answer C must not be null.");
		}
		if (!reply.getSkipped() && reply.getAnswerD() == null) {
			throw new InconsistentModelException("Answer D must not be null.");
		}
		if (!reply.getSkipped() && reply.getAnswerE() == null) {
			throw new InconsistentModelException("Answer E must not be null.");
		}
	}
	
	public static void validateImportance(ImportanceDto importanceDto) throws InconsistentStateException {
		if (importanceDto == null) {
			throw new IllegalArgumentException("Importance DTO must not be null.");
		}
		
		// Check mandatory importance data.
		if (importanceDto.getWeight() == null) {
			throw new InconsistentStateException("Weight must not be null.");
		}
		if (importanceDto.getDescription() == null) {
			throw new InconsistentStateException("Description must not be null.");
		}
	}
	
	public static void validateImportance(Importance importance) throws InconsistentModelException {
		if (importance == null) {
			throw new IllegalArgumentException("Importance must not be null.");
		}
		
		// Check mandatory importance data.
		if (importance.getWeight() == null) {
			throw new InconsistentModelException("Weight must not be null.");
		}
		if (importance.getDescription() == null) {
			throw new InconsistentModelException("Description must not be null.");
		}
	}
	
}
