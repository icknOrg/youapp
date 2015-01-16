package youapp.dataaccess.dao;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import youapp.dataaccess.dto.NameDto;
import youapp.dataaccess.dto.QuestionDto;

/**
 * Represents a data access object for a search index.
 * @author Linda
 *
 */
public interface ISearchableQuestionDao {
	
	/**
	 * 
	 * @param questionId
	 * @return
	 * @throws SolrServerException
	 */
	public QuestionDto getById(Long questionId) throws SolrServerException;
	
	/**
	 * Returns all questions stored in the search index. If there are no questions stored in the search index, an
	 * empty list is returned.
	 * @return all questions stored in the search index.
	 * @throws SolrServerException if an error occurs while accessing the search index.
	 */
	public List<QuestionDto> getAll() throws SolrServerException;
	
	/**
	 * Returns all questions from the search index which match the given search term according to the search index.
	 * If there are no questions matching the given search term stored in the search index, an empty list is returned.
	 * @param searchTerm a term consisting of a search sentence or several search words.
	 * @return all questions which match the given search term.
	 * @throws SolrServerException if an error occurs while accessing the search index.
	 */
	public List<QuestionDto> getBySearchTerm(String searchTerm) throws SolrServerException;
	
	/**
	 * Returns all questions from the search index created by the author with the given id. If there are no questions
	 * created by the given author stored in the search index, an empty list is returned.
	 * @param authorId the id of the author of all returned questions. Must not be null.
	 * @return all questions created by the given author.
	 * @throws SolrServerException if an error occurs while accessing the search index.
	 */
	public List<QuestionDto> getByAuthor(Long authorId) throws SolrServerException;
	
	/**
	 * Creates a new question in the search index. If a question with the same question id already exists in the search index,
	 * the old values are overwritten. The question's author id and the name's author id must be equal.
	 * @param question the question to be added to the search index.
	 * @param authorName the question's author's name.
	 * @throws SolrServerException if an error occurs while accessing the search index.
	 * @throws IOException if an error occurs while accessing the search index.
	 */
	public void create(QuestionDto question, NameDto authorName) throws SolrServerException, IOException;
	
	/**
	 * Updates the given question in the search index. If the question does not exist yet, it is created. The question's author
	 * id and the name's author id must be equal.
	 * @param question the question to be updated in the search index.
	 * @param authorName the question's author's name.
	 * @throws SolrServerException if an error occurs while accessing the search index.
	 * @throws IOException if an error occurs while accessing the search index.
	 */
	public void update(QuestionDto question, NameDto authorName) throws SolrServerException, IOException;
	
	/**
	 * Updates all the given questions of the given author in the search index. If a question does not already exist, it is created.
	 * The questions' author id's and the name's author id must be equal.
	 * @param questions the questions to be updated in the search index.
	 * @param authorName the questions' author's name.
	 * @throws SolrServerException if an error occurs while accessing the search index.
	 */
	public void update(List<QuestionDto> questions, NameDto authorName) throws SolrServerException, IOException;
	
	/**
	 * Deletes the question with the given id from the search index. This operation has no effect if no question with the given id
	 * exists.
	 * @param questionId the id of the question to be deleted.
	 * @throws SolrServerException if an error occurs while accessing the search index.
	 * @throws IOException if an error occurs while accessing the search index.
	 */
	public void delete(Long questionId) throws SolrServerException, IOException;
	
	/**
	 * Deletes the questions with the given id's from the search index. A question is simply skipped if it does not exist in the
	 * search index.
	 * @param questionIds
	 * @throws SolrServerException if an error occurs while accessing the search index.
	 * @throws IOException if an error occurs while accessing the search index.
	 */
	public void delete(List<Long> questionIds) throws SolrServerException, IOException;
	
}
