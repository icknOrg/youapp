package youapp.dataaccess.dao.solr;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import youapp.SolrIndexInitializer;
import youapp.dataaccess.dao.jdbc.JdbcQuestionDao;
import youapp.dataaccess.dto.NameDto;
import youapp.dataaccess.dto.QuestionDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml", "classpath:dao-context.xml", "classpath:service-context.xml" })
@Ignore
public class SolrSearchableQuestionDaoTest {
	
	private ApplicationContext applicationContext;
	
	private SolrIndexInitializer solrIndexInitializer;
	
	private JdbcQuestionDao questionDao;
	
	private SolrSearchableQuestionDao searchableQuestionDao;
	
	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Autowired
	public void setSolrIndexInitializer(SolrIndexInitializer solrIndexInitializer) {
		this.solrIndexInitializer = solrIndexInitializer;
	}
	
	@Before
	public void setUp() throws Exception {
		solrIndexInitializer.resetIndex();
		questionDao = (JdbcQuestionDao)applicationContext.getBean("questionDao", JdbcQuestionDao.class);
		searchableQuestionDao = (SolrSearchableQuestionDao)applicationContext.getBean("searchableQuestionDao", SolrSearchableQuestionDao.class);
	}
	
	@After
	public void tearDown() throws Exception {
		questionDao = null;
		searchableQuestionDao = null;
	}
	
	@Test
	public void testGetById() throws Exception {
		// Get existent question from search index.
		Long questionId = 1L;
		QuestionDto question = questionDao.getById(questionId);
		QuestionDto solrQuestion = searchableQuestionDao.getById(questionId);
		
		assertNotNull(solrQuestion);
		assertEquals(question.getId(), solrQuestion.getId());
		assertEquals(question.getAuthor(), solrQuestion.getAuthor());
		Calendar cal = Calendar.getInstance();
		cal.setTime(question.getCreated());
		Calendar calSolr = Calendar.getInstance();
		calSolr.setTime(solrQuestion.getCreated());
		assertEquals(cal.get(Calendar.YEAR), calSolr.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calSolr.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calSolr.get(Calendar.DATE));
		assertEquals(question.getQuestion(), solrQuestion.getQuestion());
		assertEquals(question.getAnswerA(), solrQuestion.getAnswerA());
		assertEquals(question.getAnswerB(), solrQuestion.getAnswerB());
		assertEquals(question.getAnswerC(), solrQuestion.getAnswerC());
		assertEquals(question.getAnswerD(), solrQuestion.getAnswerD());
		assertEquals(question.getAnswerE(), solrQuestion.getAnswerE());
		
		// Get inexistent question from the search index.
		solrQuestion = null;
		solrQuestion = searchableQuestionDao.getById(9999L);
		assertNull(solrQuestion);
		
		// Exception testing.
		try {
			searchableQuestionDao.getById(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testGetAll() throws SolrServerException {
		List<QuestionDto> questions = searchableQuestionDao.getAll();
		assertNotNull(questions);
		assertEquals(6, questions.size());
		
		Map<Long, QuestionDto> idQuestionMapping = new HashMap<Long, QuestionDto>();
		for (QuestionDto question : questions) {
			idQuestionMapping.put(question.getId(), question);
		}
		assertTrue(idQuestionMapping.containsKey(1L));
		assertTrue(idQuestionMapping.containsKey(2L));
		assertTrue(idQuestionMapping.containsKey(3L));
		assertTrue(idQuestionMapping.containsKey(4L));
		assertTrue(idQuestionMapping.containsKey(5L));
		assertTrue(idQuestionMapping.containsKey(6L));
	}
	
	@Test
	public void testGetByAuthor() throws SolrServerException {
		List<QuestionDto> questions = searchableQuestionDao.getByAuthor(1L);
		assertNotNull(questions);
		assertEquals(3, questions.size());
		
		Map<Long, QuestionDto> idQuestionMapping = new HashMap<Long, QuestionDto>();
		for (QuestionDto question : questions) {
			idQuestionMapping.put(question.getId(), question);
		}
		assertTrue(idQuestionMapping.containsKey(1L));
		assertTrue(idQuestionMapping.containsKey(2L));
		assertTrue(idQuestionMapping.containsKey(6L));
		
		// Exception testing.
		try {
			searchableQuestionDao.getByAuthor(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetBySearchTerm() {
		try {
			// Check for existing questions.
			List<QuestionDto> questions = searchableQuestionDao.getBySearchTerm("cat dog");
			assertNotNull(questions);
			assertEquals(2, questions.size());
			
			// Check for inexistent questions.
			questions = null;
			questions = searchableQuestionDao.getBySearchTerm("nuclear power plant reactor catastrophe");
			assertNotNull(questions);
			assertEquals(0, questions.size());
			
			// Add new question to the search index and repeat the above check.
			questions = null;
			QuestionDto question = new QuestionDto();
			question.setId(7L);
			question.setAuthor(2L);
			question.setCreated(new Date(System.currentTimeMillis()));
			question.setQuestion("Do you support nuclear power reactors?");
			question.setAnswerA("Yes, it is a good way to produce energy.");
			question.setAnswerB("No, they should all be stopped.");
			NameDto name = new NameDto();
			name.setPersonId(2L);
			name.setFirstName("Hans-Peter");
			name.setLastName("Peter-Hans");
			name.setNickName("Hansli");
			searchableQuestionDao.create(question, name);
			
			questions = searchableQuestionDao.getBySearchTerm("nuclear power plant reactor catastrophe");
			assertNotNull(questions);
			assertEquals(1, questions.size());
			
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		// Exception testing.
		try {
			searchableQuestionDao.getBySearchTerm(null);
			fail();
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testCreate() {
		QuestionDto question = new QuestionDto();
		question.setId(7L);
		question.setAuthor(2L);
		question.setCreated(new Date(System.currentTimeMillis()));
		question.setQuestion("Do you support nuclear power reactors?");
		question.setAnswerA("Yes, it is a good way to produce energy.");
		question.setAnswerB("No, they should all be stopped.");
		
		NameDto name = new NameDto();
		name.setPersonId(2L);
		name.setFirstName("Hans-Peter");
		name.setLastName("Peter-Hans");
		name.setNickName("Hansli");
		
		try {
			searchableQuestionDao.create(question, name);
			List<QuestionDto> questions = searchableQuestionDao.getBySearchTerm("nuclear power plant reactor catastrophe");
			assertNotNull(questions);
			assertEquals(1, questions.size());
		} catch (SolrServerException e1) {
			e1.printStackTrace();
			fail();
		} catch (IOException e1) {
			e1.printStackTrace();
			fail();
		}
		
		// Exception testing.
		try {
			searchableQuestionDao.create(null, name);
			fail();
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			searchableQuestionDao.create(question, null);
			fail();
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			searchableQuestionDao.create(null, null);
			fail();
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			question.setId(null);
			searchableQuestionDao.create(question, name);
			fail();
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			question.setId(7L);
			name.setPersonId(3L);
			searchableQuestionDao.create(question, name);
			fail();
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testUpdate() {
		QuestionDto question = new QuestionDto();
		question.setId(7L);
		question.setAuthor(2L);
		question.setCreated(new Date(System.currentTimeMillis()));
		question.setQuestion("Do you support nuclear power reactors?");
		question.setAnswerA("Yes, it is a good way to produce energy.");
		question.setAnswerB("No, they should all be stopped.");
		
		NameDto name = new NameDto();
		name.setPersonId(2L);
		name.setFirstName("Hans-Peter");
		name.setLastName("Peter-Hans");
		name.setNickName("Hansli");
		
		try {
			searchableQuestionDao.create(question, name);
			List<QuestionDto> questions = searchableQuestionDao.getBySearchTerm("nuclear power plant reactor catastrophe");
			assertNotNull(questions);
			assertEquals(1, questions.size());
			QuestionDto retrievedFirst = questions.get(0);
			assertNotNull(retrievedFirst);
			retrievedFirst.setQuestion("Do you support nuclear power plants?");
			retrievedFirst.setAnswerC("I don't know. We can't live without them.");
			searchableQuestionDao.update(retrievedFirst, name);
			
			questions = null;
			questions = searchableQuestionDao.getBySearchTerm("nuclear power plant reactor catastrophe");
			assertNotNull(questions);
			assertEquals(1, questions.size());
			QuestionDto retrievedSecond = questions.get(0);
			assertNotNull(retrievedSecond);
			assertEquals(retrievedFirst.getId(), retrievedSecond.getId());
			assertEquals(retrievedFirst.getAuthor(), retrievedSecond.getAuthor());
			Calendar calFirst = Calendar.getInstance();
			calFirst.setTime(retrievedFirst.getCreated());
			Calendar calSecond = Calendar.getInstance();
			calSecond.setTime(retrievedSecond.getCreated());
			assertEquals(calFirst.get(Calendar.YEAR), calSecond.get(Calendar.YEAR));
			assertEquals(calFirst.get(Calendar.MONTH), calSecond.get(Calendar.MONTH));
			assertEquals(calFirst.get(Calendar.DATE), calSecond.get(Calendar.DATE));
			assertEquals(retrievedFirst.getQuestion(), retrievedSecond.getQuestion());
			assertEquals(retrievedFirst.getAnswerA(), retrievedSecond.getAnswerA());
			assertEquals(retrievedFirst.getAnswerB(), retrievedSecond.getAnswerB());
			assertEquals(retrievedFirst.getAnswerC(), retrievedSecond.getAnswerC());
			assertEquals(retrievedFirst.getAnswerD(), retrievedSecond.getAnswerD());
			assertEquals(retrievedFirst.getAnswerE(), retrievedSecond.getAnswerE());
		} catch (SolrServerException e1) {
			e1.printStackTrace();
			fail();
		} catch (IOException e1) {
			e1.printStackTrace();
			fail();
		}
		
		// Exception testing.
		try {
			searchableQuestionDao.update((QuestionDto)null, name);
			fail();
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			searchableQuestionDao.create(question, null);
			fail();
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			searchableQuestionDao.create(null, null);
			fail();
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			question.setId(null);
			searchableQuestionDao.create(question, name);
			fail();
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			question.setId(7L);
			name.setPersonId(3L);
			searchableQuestionDao.create(question, name);
			fail();
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testUpdateMultiple() {
		
		try {
			List<QuestionDto> questions = searchableQuestionDao.getBySearchTerm("cat dog");
			assertNotNull(questions);
			assertEquals(2, questions.size());
			QuestionDto q1 = questions.get(0);
			assertNotNull(q1);
			QuestionDto q2 = questions.get(1);
			assertNotNull(q2);
			q1.setAnswerE("Blabla");
			q1.setAuthor(2L);
			q2.setAnswerE("Blibli");
			q2.setAuthor(2L);
			List<QuestionDto> toUpdate = new LinkedList<QuestionDto>();
			toUpdate.add(q1);
			toUpdate.add(q2);
			NameDto name = new NameDto();
			name.setPersonId(q1.getAuthor());
			name.setFirstName("Hans-Peter");
			name.setLastName("Peter-Hans");
			name.setNickName("Hansli");
			searchableQuestionDao.update(toUpdate, name);
			
			questions = null;
			questions = searchableQuestionDao.getBySearchTerm("cat dog");
			assertNotNull(questions);
			assertEquals(2, questions.size());
			QuestionDto q1Updated = questions.get(0);
			QuestionDto q2Updated = questions.get(1);
			
			// Check question 1.
			assertNotNull(q1Updated);
			assertEquals(q1.getId(), q1Updated.getId());
			assertEquals(q1.getAuthor(), q1Updated.getAuthor());
			Calendar calFirst = Calendar.getInstance();
			calFirst.setTime(q1.getCreated());
			Calendar calSecond = Calendar.getInstance();
			calSecond.setTime(q1Updated.getCreated());
			assertEquals(calFirst.get(Calendar.YEAR), calSecond.get(Calendar.YEAR));
			assertEquals(calFirst.get(Calendar.MONTH), calSecond.get(Calendar.MONTH));
			assertEquals(calFirst.get(Calendar.DATE), calSecond.get(Calendar.DATE));
			assertEquals(q1.getQuestion(), q1Updated.getQuestion());
			assertEquals(q1.getAnswerA(), q1Updated.getAnswerA());
			assertEquals(q1.getAnswerB(), q1Updated.getAnswerB());
			assertEquals(q1.getAnswerC(), q1Updated.getAnswerC());
			assertEquals(q1.getAnswerD(), q1Updated.getAnswerD());
			assertEquals(q1.getAnswerE(), q1Updated.getAnswerE());
			
			// Check question 2.
			assertNotNull(q1Updated);
			assertEquals(q2.getId(), q2Updated.getId());
			assertEquals(q2.getAuthor(), q2Updated.getAuthor());
			calFirst.setTime(q2.getCreated());
			calSecond.setTime(q2Updated.getCreated());
			assertEquals(calFirst.get(Calendar.YEAR), calSecond.get(Calendar.YEAR));
			assertEquals(calFirst.get(Calendar.MONTH), calSecond.get(Calendar.MONTH));
			assertEquals(calFirst.get(Calendar.DATE), calSecond.get(Calendar.DATE));
			assertEquals(q2.getQuestion(), q2Updated.getQuestion());
			assertEquals(q2.getAnswerA(), q2Updated.getAnswerA());
			assertEquals(q2.getAnswerB(), q2Updated.getAnswerB());
			assertEquals(q2.getAnswerC(), q2Updated.getAnswerC());
			assertEquals(q2.getAnswerD(), q2Updated.getAnswerD());
			assertEquals(q2.getAnswerE(), q2Updated.getAnswerE());
			
			// Exception testing.
			try {
				searchableQuestionDao.update((List<QuestionDto>)null, name);
				fail();
			} catch (IllegalArgumentException e) {
				assertTrue(true);
			}
			try {
				searchableQuestionDao.update(questions, null);
				fail();
			} catch (IllegalArgumentException e) {
				assertTrue(true);
			}
			try {
				searchableQuestionDao.update((List<QuestionDto>)null, null);
				fail();
			} catch (IllegalArgumentException e) {
				assertTrue(true);
			}
			Long q1UpdatedId = q1Updated.getId();
			try {
				q1Updated.setId(null);
				searchableQuestionDao.update(questions, name);
				fail();
			} catch (IllegalArgumentException e) {
				assertTrue(true);
			}
			try {
				q1Updated.setId(q1UpdatedId);
				q2Updated.setAuthor(1L);
				searchableQuestionDao.update(questions, name);
				fail();
			} catch (IllegalArgumentException e) {
				assertTrue(true);
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

	}
	
	@Test
	public void testDelete() {
		
		try {
			// Delete inexistent question.
			searchableQuestionDao.delete(9999L);
			
			// Delete existent questions.
			List<QuestionDto> questions = searchableQuestionDao.getBySearchTerm("smartphone");
			assertNotNull(questions);
			assertEquals(1, questions.size());
			assertEquals(new Long(3L), questions.get(0).getId());
			
			searchableQuestionDao.delete(3L);
			questions = null;
			questions = searchableQuestionDao.getBySearchTerm("smartphone");
			assertNotNull(questions);
			assertEquals(0, questions.size());
			
			// Exception testing.
			try {
				searchableQuestionDao.delete((Long)null);
				fail();
			} catch (IllegalArgumentException e) {
				assertTrue(true);
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testDeleteMultiple() {
		
		try {
			List<QuestionDto> questions = searchableQuestionDao.getBySearchTerm("cat dog");
			assertNotNull(questions);
			assertEquals(2, questions.size());
			List<Long> ids = new LinkedList<Long>();
			assertEquals(new Long(1L), questions.get(0).getId());
			ids.add(questions.get(0).getId());
			assertEquals(new Long(6L), questions.get(1).getId());
			ids.add(questions.get(1).getId());
			
			searchableQuestionDao.delete(ids);
			questions = null;
			questions = searchableQuestionDao.getBySearchTerm("cat dog");
			assertNotNull(questions);
			assertEquals(0, questions.size());
			
			// Exception testing.
			ids.clear();
			try {
				searchableQuestionDao.delete((List<Long>)null);
				fail();
			} catch (IllegalArgumentException e) {
				assertTrue(true);
			}
			ids.add(4L);
			ids.add(5L);
			ids.add(null);
			try {
				searchableQuestionDao.delete(ids);
				fail();
			} catch (IllegalArgumentException e) {
				assertTrue(true);
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		
	}
	
}
