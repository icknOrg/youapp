package youapp.dataaccess.dao.solr;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.ISearchableQuestionDao;
import youapp.dataaccess.dto.NameDto;
import youapp.dataaccess.dto.QuestionDto;

public class SolrSearchableQuestionDao implements ISearchableQuestionDao {

	/**
	 * Logger.
	 */
	static final Log log = LogFactory.getLog(SolrSearchableQuestionDao.class);
	
	/**
	 * This string is prepended to every question id in order to form the Solr id.
	 */
	public static final String SOLR_ID_PREFIX = "question";
	
	/**
	 * Provides access to the search index.
	 */
	private CommonsHttpSolrServer solrServer;
	
	@Autowired
	public void setSolrServer(CommonsHttpSolrServer solrServer) {
		this.solrServer = solrServer;
	}
	
	@Override
	public QuestionDto getById(Long questionId) throws SolrServerException {
		if (questionId == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		
		// Prepare query.
		SolrQuery query = new SolrQuery();
		query.setQuery("idQuestion:"+questionId);
		
		// Retrieve matching documents.
		List<QuestionDto> questions = new LinkedList<QuestionDto>();
		QueryResponse rsp = solrServer.query(query);
		SolrDocumentList documents = rsp.getResults();
		if (log.isDebugEnabled()) {
			log.debug("Number of search results: " + documents.size());
		}
		Iterator<SolrDocument> it = documents.iterator();
		while (it.hasNext()) {
			SolrDocument document = it.next();
			if (log.isTraceEnabled()) {
				log.trace(">>> Found document: " + document.getFieldValue("id"));
			}
			QuestionDto question = mapDocument(document);
			if (question != null) {
				questions.add(question);
			} else {
				if (log.isTraceEnabled()) {
					log.trace(">>> Mapping failed. Question not added. Document id: " + document.getFieldValue("id"));
				}
			}
		}
		
		// Return result.
		if (questions.size() <= 0) {
			if (log.isDebugEnabled()) {
				log.debug("No matching document for question with id " + questionId + " found.");
			}
			return null;
		} else if (questions.size() > 1) {
			if (log.isDebugEnabled()) {
				log.debug("Found several matching documents for question with id " + questionId + ". Returning the first.");
			}
			return questions.get(0);
		}
		
		return questions.get(0);
	}

	@Override
	public List<QuestionDto> getAll() throws SolrServerException {
		// Prepare query.
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		query.addSortField("idQuestion", SolrQuery.ORDER.asc);
		
		// Retrieve matching documents.
		List<QuestionDto> questions = new LinkedList<QuestionDto>();
		QueryResponse rsp = solrServer.query(query);
		SolrDocumentList documents = rsp.getResults();
		if (log.isDebugEnabled()) {
			log.debug("Number of search results: " + documents.size());
		}
		Iterator<SolrDocument> it = documents.iterator();
		while (it.hasNext()) {
			SolrDocument document = it.next();
			if (log.isTraceEnabled()) {
				log.trace(">>> Found document: " + document.getFieldValue("id"));
			}
			QuestionDto question = mapDocument(document);
			if (question != null) {
				questions.add(question);
			} else {
				if (log.isTraceEnabled()) {
					log.trace(">>> Mapping failed. Question not added. Document id: " + document.getFieldValue("id"));
				}
			}
		}
		return questions;
	}
	
	@Override
	public List<QuestionDto> getBySearchTerm(String searchTerm) throws SolrServerException {
		if (searchTerm == null) {
			throw new IllegalArgumentException("Search term must not be null.");
		}
		if (searchTerm.length() == 0) {
			throw new IllegalArgumentException("Search term must not be empty.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Searching for questions by search term: " + searchTerm);
		}
		// Prepare Solr dismax query.
		SolrQuery query = new SolrQuery();
		query.setQuery(searchTerm);
		query.set("defType", "dismax");
		query.set("fq", "-deleted:true");
		query.set("qf", "question^2 answerA^1.5 answerB^1.5 answerC^1.5 answerD^1.5");
		query.set("mm", "1<50% 2<75%");
		
		// Retrieve matching documents.
		List<QuestionDto> questions = new LinkedList<QuestionDto>();
		QueryResponse rsp = solrServer.query(query);
		SolrDocumentList documents = rsp.getResults();
		if (log.isDebugEnabled()) {
			log.debug("Number of search results: " + documents.size());
		}
		Iterator<SolrDocument> it = documents.iterator();
		while (it.hasNext()) {
			SolrDocument document = it.next();
			if (log.isTraceEnabled()) {
				log.trace(">>> Found document: " + document.getFieldValue("id"));
			}
			QuestionDto question = mapDocument(document);
			if (question != null) {
				questions.add(question);
			} else {
				if (log.isTraceEnabled()) {
					log.trace(">>> Mapping failed. Question not added. Document id: " + document.getFieldValue("id"));
				}
			}
		}
		return questions;
	}
	
	@Override
	public List<QuestionDto> getByAuthor(Long authorId) throws SolrServerException {
		if (authorId == null) {
			throw new IllegalArgumentException("Author id must not be null.");
		}
		// Prepare query.
		SolrQuery query = new SolrQuery();
		query.setQuery("authorId:"+authorId);
		query.addSortField("idQuestion", SolrQuery.ORDER.asc);
		
		// Retrieve matching documents.
		List<QuestionDto> questions = new LinkedList<QuestionDto>();
		QueryResponse rsp = solrServer.query(query);
		SolrDocumentList documents = rsp.getResults();
		if (log.isDebugEnabled()) {
			log.debug("Number of search results: " + documents.size());
		}
		Iterator<SolrDocument> it = documents.iterator();
		while (it.hasNext()) {
			SolrDocument document = it.next();
			if (log.isTraceEnabled()) {
				log.trace(">>> Found document: " + document.getFieldValue("id"));
			}
			QuestionDto question = mapDocument(document);
			if (question != null) {
				questions.add(question);
			} else {
				if (log.isTraceEnabled()) {
					log.trace(">>> Mapping failed. Question not added. Document id: " + document.getFieldValue("id"));
				}
			}
		}
		return questions;
	}

	@Override
	public void create(QuestionDto question, NameDto authorName) throws SolrServerException, IOException {
		if (question == null) {
			throw new IllegalArgumentException("Question must not be null.");
		}
		if (question.getId() == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (authorName == null) {
			throw new IllegalArgumentException("Author name must not be null.");
		}
		if (question.getAuthor() != authorName.getPersonId()) {
			throw new IllegalArgumentException("Question author id and author name person id must be equal.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Creating new question in search index. Question id: " + question.getId() + " Author id: " + question.getAuthor());
		}
		
		// Add document to Solr search index.
		SolrInputDocument document = mapQuestion(question, authorName);
		if (document != null) {
			solrServer.add(mapQuestion(question, authorName));
			solrServer.commit();
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Mapping failed. Document not added. Question id: " + question.getId());
			}
		}
	}

	@Override
	public void update(QuestionDto question, NameDto authorName) throws SolrServerException, IOException {
		if (question == null) {
			throw new IllegalArgumentException("Question must not be null.");
		}
		if (question.getId() == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (authorName == null) {
			throw new IllegalArgumentException("Author name must not be null.");
		}
		if (question.getAuthor() != authorName.getPersonId()) {
			throw new IllegalArgumentException("Question author id and author name person id must be equal.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Updating question in search index. Question id: " + question.getId() + " Author id: " + question.getAuthor());
		}
		
		// Update document in Solr search index.
		SolrInputDocument document = mapQuestion(question, authorName);
		if (document != null) {
			solrServer.add(mapQuestion(question, authorName));
			solrServer.commit();
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Mapping failed. Document not added. Question id: " + question.getId());
			}
		}
	}

	@Override
	public void update(List<QuestionDto> questions, NameDto authorName) throws SolrServerException, IOException {
		if (questions == null) {
			throw new IllegalArgumentException("Questions must not be null.");
		}
		if (authorName == null) {
			throw new IllegalArgumentException("Author name must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Updating questions in search index. Author id: " + authorName.getPersonId());
		}
		
		// Updating documents in Solr search index.
		List<SolrInputDocument> documents = new LinkedList<SolrInputDocument>();
		for (QuestionDto question : questions) {
			if (question.getId() == null) {
				throw new IllegalArgumentException("Question id must not be null.");
			}
			if (question.getAuthor() != authorName.getPersonId()) {
				throw new IllegalArgumentException("Question author id and name person id must be equal.");
			}
			if (log.isTraceEnabled()) {
				log.trace(">>> Updating question with id: " + question.getId());
			}
			SolrInputDocument document = mapQuestion(question, authorName);
			if (document != null) {
				documents.add(document);
			} else {
				if (log.isTraceEnabled()) {
					log.trace(">>> Mapping failed. Document not added. Question id: " + question.getId());
				}
			}
		}
		solrServer.add(documents);
		solrServer.commit();
	}

	@Override
	public void delete(Long questionId) throws SolrServerException, IOException {
		if (questionId == null) {
			throw new IllegalArgumentException("Question id must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleting question from search index with id: " + questionId);
		}
		solrServer.deleteById(SOLR_ID_PREFIX+questionId);
		solrServer.commit();
	}

	@Override
	public void delete(List<Long> questionIds) throws SolrServerException, IOException {
		if (questionIds == null) {
			throw new IllegalArgumentException("Question ids must not be null.");
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleting questions from search index.");
		}
		List<String> documentIds = new LinkedList<String>();
		for (Long questionId: questionIds) {
			if (questionId == null) {
				throw new IllegalArgumentException("Question id must not be null.");
			}
			if (log.isTraceEnabled()) {
				log.trace(">>> Deleting question with id: " + questionId);
			}
			documentIds.add(SOLR_ID_PREFIX+questionId);
		}
		solrServer.deleteById(documentIds);
		solrServer.commit();
	}

	/**
	 * Maps a Solr document to a question data transfer object.
	 * @param document the Solr document.
	 * @return a question representation of the Solr document.
	 */
	private QuestionDto mapDocument(SolrDocument document) {
		if (document == null) {
			throw new IllegalArgumentException("Document must not be null.");
		}
		QuestionDto question = new QuestionDto();
		question.setId((Long)document.getFieldValue("idQuestion"));
		question.setCreated((Date)document.getFieldValue("createdOn"));
		question.setAuthor((Long)document.getFieldValue("authorId"));
		question.setQuestion((String)document.getFieldValue("question"));
		question.setAnswerA((String)document.getFieldValue("answerA"));
		question.setAnswerB((String)document.getFieldValue("answerB"));
		question.setAnswerC((String)document.getFieldValue("answerC"));
		question.setAnswerD((String)document.getFieldValue("answerD"));
		question.setAnswerE((String)document.getFieldValue("answerE"));
		return question;
	}
	
	private SolrInputDocument mapQuestion(QuestionDto question, NameDto authorName) {
		if (question == null) {
			throw new IllegalArgumentException("Question must not be null.");
		}
		if (authorName == null) {
			throw new IllegalArgumentException("Author name must not be null.");
		}
		if (question.getAuthor() != authorName.getPersonId()) {
			throw new IllegalArgumentException("Question author id and author name person id must be equal.");
		}
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", SOLR_ID_PREFIX+question.getId());
		document.addField("idQuestion", question.getId());
		document.addField("createdOn", question.getCreated());
		document.addField("authorId", authorName.getPersonId());
		document.addField("authorName", authorName.getLastName() + " " + authorName.getFirstName());
		document.addField("authorFirstName", authorName.getFirstName());
		document.addField("authorLastName", authorName.getLastName());
		document.addField("authorNickName", authorName.getNickName());
		document.addField("question", question.getQuestion());
		document.addField("answerA", question.getAnswerA());
		document.addField("answerB", question.getAnswerB());
		if (question.getAnswerC() != null) {
			document.addField("answerC", question.getAnswerC());
		}
		if (question.getAnswerD() != null) {
			document.addField("answerD", question.getAnswerD());
		}
		if (question.getAnswerE() != null) {
			document.addField("answerE", question.getAnswerE());
		}
		document.addField("deleted", false);
		return document;
	}
	
}
