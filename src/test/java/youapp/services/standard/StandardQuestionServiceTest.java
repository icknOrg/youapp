package youapp.services.standard;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import youapp.JdbcDbInitializer;
import youapp.dataaccess.dao.ISearchableQuestionDao;
import youapp.dataaccess.dao.solr.SolrSearchableQuestionDao;
import youapp.dataaccess.dto.QuestionDto;
import youapp.model.Name;
import youapp.model.Question;
import youapp.model.Reply;
import youapp.services.PersonService;
import youapp.services.QuestionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml", "classpath:dao-context.xml", "classpath:service-context.xml" })
public class StandardQuestionServiceTest {

	private ApplicationContext applicationContext;

	private JdbcDbInitializer jdbcDbInitializer;
	
	private PersonService personService;
	
	private QuestionService questionService;
	
	private ISearchableQuestionDao searchableQuestionDao;

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Autowired
	public void setJdbcDbInitializer(JdbcDbInitializer jdbcDbInitializer) {
		this.jdbcDbInitializer = jdbcDbInitializer;
	}
	
	@Before
	public void setUp() throws Exception {
		jdbcDbInitializer.resetDatabase();
		personService = (PersonService)applicationContext.getBean("personService", PersonService.class);
		questionService = (QuestionService)applicationContext.getBean("questionService", QuestionService.class);
		searchableQuestionDao = (ISearchableQuestionDao)applicationContext.getBean("searchableQuestionDao", SolrSearchableQuestionDao.class);
	}
	
	@After
	public void tearDown() {
		personService = null;
		questionService = null;
		searchableQuestionDao = null;
	}
	
	@Test
	public void testGetQuestionById() throws Exception {
		Question q = questionService.getQuestionById(1L);
		assertNotNull(q);
		assertEquals(new Long(1L), q.getId());
		assertEquals(new Long(1L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		Name authorName = q.getAuthorName();
		assertEquals("Micheline", authorName.getFirstName());
		assertEquals("Calmy-Rey", authorName.getLastName());
		assertEquals("Micheline", authorName.getNickName());
		assertEquals("Cats or dogs - which do you like more?", q.getQuestion());
		assertEquals("Cats", q.getAnswerA());
		assertEquals("Dogs", q.getAnswerB());
		
		// Check for inexistent questions.
		q = questionService.getQuestionById(-1L);
		assertNull(q);
		q = questionService.getQuestionById(7L);
		assertNull(q);
		q = questionService.getQuestionById(99L);
		assertNull(q);
		
		// Exception testing.
		try {
			questionService.getQuestionById(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testGetQuestionsByAuthor() throws Exception {
		// Check for existent author.
		List<Question> questions = questionService.getQuestionsByAuthor(1L);
		assertEquals(3, questions.size());
		Question q = questions.get(0);
		assertEquals(new Long(1L), q.getId());
		assertEquals(new Long(1L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		Name authorName = q.getAuthorName();
		assertEquals("Micheline", authorName.getFirstName());
		assertEquals("Calmy-Rey", authorName.getLastName());
		assertEquals("Micheline", authorName.getNickName());
		assertEquals("Cats or dogs - which do you like more?", q.getQuestion());
		assertEquals("Cats", q.getAnswerA());
		assertEquals("Dogs", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		q = null;
		q = questions.get(1);
		assertEquals(new Long(2L), q.getId());
		assertEquals(new Long(1L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		authorName = null;
		authorName = q.getAuthorName();
		assertEquals("Micheline", authorName.getFirstName());
		assertEquals("Calmy-Rey", authorName.getLastName());
		assertEquals("Micheline", authorName.getNickName());
		assertEquals("Would you consider a long-distance relationship?", q.getQuestion());
		assertEquals("Yes, why not?", q.getAnswerA());
		assertEquals("Depends, if I would be totally in love maybe.", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		
		// Check for inexistent author.
		questions = null;
		questions = questionService.getQuestionsByAuthor(3L);
		assertEquals(0, questions.size());
	}
	
	@Test
	public void testGetQuestionsByAuthorLimited() throws Exception {
		// Check for existent author.
		List<Question> questions = questionService.getQuestionsByAuthor(1L, 0, 1);
		assertEquals(1, questions.size());
		Question q = questions.get(0);
		assertEquals(new Long(1L), q.getId());
		assertEquals(new Long(1L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		Name authorName = q.getAuthorName();
		assertEquals("Micheline", authorName.getFirstName());
		assertEquals("Calmy-Rey", authorName.getLastName());
		assertEquals("Micheline", authorName.getNickName());
		assertEquals("Cats or dogs - which do you like more?", q.getQuestion());
		assertEquals("Cats", q.getAnswerA());
		assertEquals("Dogs", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		
		questions = null;
		q = null;
		questions = questionService.getQuestionsByAuthor(1L, 0, 10);
		assertEquals(3, questions.size());
		q = questions.get(0);
		assertEquals(new Long(1L), q.getId());
		assertEquals(new Long(1L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		authorName = null;
		authorName = q.getAuthorName();
		assertEquals("Micheline", authorName.getFirstName());
		assertEquals("Calmy-Rey", authorName.getLastName());
		assertEquals("Micheline", authorName.getNickName());
		assertEquals("Cats or dogs - which do you like more?", q.getQuestion());
		assertEquals("Cats", q.getAnswerA());
		assertEquals("Dogs", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		q = null;
		q = questions.get(1);
		assertEquals(new Long(2L), q.getId());
		assertEquals(new Long(1L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		authorName = null;
		authorName = q.getAuthorName();
		assertEquals("Micheline", authorName.getFirstName());
		assertEquals("Calmy-Rey", authorName.getLastName());
		assertEquals("Micheline", authorName.getNickName());
		assertEquals("Would you consider a long-distance relationship?", q.getQuestion());
		assertEquals("Yes, why not?", q.getAnswerA());
		assertEquals("Depends, if I would be totally in love maybe.", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		
		// Check for inexistent author.
		questions = null;
		questions = questionService.getQuestionsByAuthor(3L, 0, 15);
		assertEquals(0, questions.size());
	}
	@Test
	public void testGetQuestionsByDate() throws Exception {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2011);
		c.set(Calendar.MONTH, 4); // Month starts at 0.
		c.set(Calendar.DATE, 25);
		
		// Check for existent date.
		List<Question> questions = questionService.getQuestionsByDate(c.getTime());
		assertEquals(2, questions.size());
		Question q = questions.get(0);
		assertEquals(new Long(4L), q.getId());
		assertEquals(new Long(5L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		Name authorName = q.getAuthorName();
		assertEquals("Didier", authorName.getFirstName());
		assertEquals("Burkhalter", authorName.getLastName());
		assertEquals("B.Diddy", authorName.getNickName());
		assertEquals("Are you an animal person?", q.getQuestion());
		assertEquals("Yes, I love animals and I have some domestic animals myself.", q.getAnswerA());
		assertEquals("Yes, I like them but I don't have domestic animals myself.", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		q = null;
		q = questions.get(1);
		assertEquals(new Long(5L), q.getId());
		assertEquals(new Long(6L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		authorName = null;
		authorName = q.getAuthorName();
		assertEquals("Simonetta", authorName.getFirstName());
		assertEquals("Sommaruga", authorName.getLastName());
		assertEquals("SimSom", authorName.getNickName());
		assertEquals("You find a 100 dollar bill on the floor. What do you do with it?", q.getQuestion());
		assertEquals("I take it to the police.", q.getAnswerA());
		assertEquals("I take it for myself.", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		
		
		questions = null;
		q = null;
		c.set(Calendar.YEAR, 2011);
		c.set(Calendar.MONTH, 4); // Month starts at 0.
		c.set(Calendar.DATE, 9);
		questions = questionService.getQuestionsByDate(c.getTime());
		assertEquals(1, questions.size());
		q = questions.get(0);
		assertEquals(new Long(3L), q.getId());
		assertEquals(new Long(5L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		authorName = null;
		authorName = q.getAuthorName();
		assertEquals("Didier", authorName.getFirstName());
		assertEquals("Burkhalter", authorName.getLastName());
		assertEquals("B.Diddy", authorName.getNickName());
		assertEquals("Do you have a smartphone?", q.getQuestion());
		assertEquals("Yes, I'm addicted.", q.getAnswerA());
		assertEquals("Yes, I have to.", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		
		
		// Check for inexistent date.
		questions = questionService.getQuestionsByDate(new Date(System.currentTimeMillis()));
		assertEquals(0, questions.size());
		
		// Exception checking.
		try {
			questionService.getQuestionsByDate(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetRecentQuestionsByDay() throws Exception {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2011);
		c.set(Calendar.MONTH, 4); // Month starts at 0.
		c.set(Calendar.DATE, 25);
		int days = (int)getDayDifference(c.getTime(), new Date(System.currentTimeMillis()));
		List<Question> questions = questionService.getRecentQuestionsByDays(days);
		assertEquals(3, questions.size());
		Question q = questions.get(0);
		assertEquals(new Long(4L), q.getId());
		assertEquals(new Long(5L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		Name authorName = q.getAuthorName();
		assertEquals("Didier", authorName.getFirstName());
		assertEquals("Burkhalter", authorName.getLastName());
		assertEquals("B.Diddy", authorName.getNickName());
		assertEquals("Are you an animal person?", q.getQuestion());
		assertEquals("Yes, I love animals and I have some domestic animals myself.", q.getAnswerA());
		assertEquals("Yes, I like them but I don't have domestic animals myself.", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		q = null;
		q = questions.get(1);
		assertEquals(new Long(5L), q.getId());
		assertEquals(new Long(6L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		authorName = null;
		authorName = q.getAuthorName();
		assertEquals("Simonetta", authorName.getFirstName());
		assertEquals("Sommaruga", authorName.getLastName());
		assertEquals("SimSom", authorName.getNickName());
		assertEquals("You find a 100 dollar bill on the floor. What do you do with it?", q.getQuestion());
		assertEquals("I take it to the police.", q.getAnswerA());
		assertEquals("I take it for myself.", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		
		questions = questionService.getRecentQuestionsByDays(0);
		assertEquals(0, questions.size());
	}
	
	@Test
	public void testGetRecentQuestionsBySize() throws Exception {
		
		List<Question> questions = questionService.getRecentQuestionsBySize(2);
		assertEquals(2, questions.size());
		Question q = questions.get(0);
		assertEquals(new Long(6L), q.getId());
		assertEquals(new Long(1L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		Name authorName = q.getAuthorName();
		assertEquals("Micheline", authorName.getFirstName());
		assertEquals("Calmy-Rey", authorName.getLastName());
		assertEquals("Micheline", authorName.getNickName());
		assertEquals("Cats or dogs - which do you prefer?", q.getQuestion());
		assertEquals("Cats", q.getAnswerA());
		assertEquals("Dogs", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		q = null;
		q = questions.get(1);
		assertEquals(new Long(4L), q.getId());
		assertEquals(new Long(5L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		authorName = null;
		authorName = q.getAuthorName();
		assertEquals("Didier", authorName.getFirstName());
		assertEquals("Burkhalter", authorName.getLastName());
		assertEquals("B.Diddy", authorName.getNickName());
		assertEquals("Are you an animal person?", q.getQuestion());
		assertEquals("Yes, I love animals and I have some domestic animals myself.", q.getAnswerA());
		assertEquals("Yes, I like them but I don't have domestic animals myself.", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		
		questions = null;
		q = null;
		questions = questionService.getRecentQuestionsBySize(3);
		assertEquals(3, questions.size());
		q = questions.get(0);
		assertEquals(new Long(6L), q.getId());
		q = questions.get(1);
		assertEquals(new Long(4L), q.getId());
		q = questions.get(2);
		assertEquals(new Long(5L), q.getId());
		
		questions = null;
		q = null;
		questions = questionService.getRecentQuestionsBySize(100);
		assertEquals(6, questions.size());
		
		questions = null;
		q = null;
		questions = questionService.getRecentQuestionsBySize(1);
		assertEquals(1, questions.size());
		q = questions.get(0);
		assertEquals(new Long(6L), q.getId());
		assertEquals(new Long(1L), q.getAuthorId());
		assertNotNull(q.getAuthorName());
		authorName = null;
		authorName = q.getAuthorName();
		assertEquals("Micheline", authorName.getFirstName());
		assertEquals("Calmy-Rey", authorName.getLastName());
		assertEquals("Micheline", authorName.getNickName());
		assertEquals("Cats or dogs - which do you prefer?", q.getQuestion());
		assertEquals("Cats", q.getAnswerA());
		assertEquals("Dogs", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		
		// Exception testing.
		try {
			questionService.getRecentQuestionsBySize(0);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionService.getRecentQuestionsBySize(-1);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
	}
	
	@Test
	public void testFetchNextQuestions() throws Exception {
		
		List<Question> questions = questionService.fetchNextQuestions(1L, 2);
		assertEquals(2, questions.size());
		for (Question q : questions) {
			assertNotSame(new Long(1L), q.getId());
		}
		
		Question quest = questions.get(0);
		Reply reply = new Reply();
		reply.setQuestionId(quest.getId());
		reply.setPersonId(1L);
		reply.setSkipped(false);
		reply.setInPrivate(false);
		reply.setCritical(false);
		reply.setLastUpdate(new Date(System.currentTimeMillis()));
		reply.setImportance(0);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		questionService.createReply(reply);
		
		questions = null;
		questions = questionService.fetchNextQuestions(1L, 2);
		assertEquals(2, questions.size());
		for (Question q : questions) {
			assertNotSame(new Long(1L), q.getId());
			assertNotSame(quest.getId(), q.getId());
		}
		
		// Fetch questions for inexistent person (no exception should be thrown).
		questions = null;
		questions = questionService.fetchNextQuestions(9999L, 2);
		assertNotNull(questions);
		assertEquals(0, questions.size());
		
		// Exception testing.
		try {
			questionService.fetchNextQuestions(1L, 0);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionService.fetchNextQuestions(1L, -1);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
	}
	
	@Test
	public void testCreateQuestion() throws Exception {
		Question q = new Question();
		Date created = new Date(System.currentTimeMillis());
		Calendar createdCal = Calendar.getInstance();
		createdCal.setTime(created);
		Long authorId = 1L;
		Name name = personService.getName(authorId);
		String question = "Question!";
		String answerA = "AnswerA";
		String answerB = "AnswerB";
		String answerC = "AnswerC";
		
		q.setCreated(created);
		q.setAuthorId(authorId);
		q.setAuthorName(name);
		q.setQuestion(question);
		q.setAnswerA(answerA);
		q.setAnswerB(answerB);
		q.setAnswerC(answerC);
		
		// Checking the database part.
		Long questionId = questionService.createQuestion(q);
		q = questionService.getQuestionById(questionId);
		assertNotNull(q);
		Calendar cal = Calendar.getInstance();
		cal.setTime(q.getCreated());
		assertEquals(questionId, q.getId());
		assertEquals(createdCal.get(Calendar.YEAR), cal.get(Calendar.YEAR));
		assertEquals(createdCal.get(Calendar.MONTH), cal.get(Calendar.MONTH));
		assertEquals(createdCal.get(Calendar.DATE), cal.get(Calendar.DATE));
		assertEquals(authorId, q.getAuthorId());
		assertEquals(question, q.getQuestion());
		assertEquals(answerA, q.getAnswerA());
		assertEquals(answerB, q.getAnswerB());
		assertEquals(answerC, q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		
		
		// Exception checking.
		q = new Question();
		//q.setCreated(created);
		q.setAuthorId(authorId);
		q.setAuthorName(name);
		q.setQuestion(question);
		q.setAnswerA(answerA);
		q.setAnswerB(answerB);
		try {
			questionService.createQuestion(q);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		
		q = new Question();
		q.setCreated(created);
		q.setAuthorId(99L);
		q.setAuthorName(name);
		q.setQuestion(question);
		q.setAnswerA(answerA);
		q.setAnswerB(answerB);
		try {
			questionService.createQuestion(q);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		
		q = new Question();
		q.setCreated(created);
		q.setAuthorId(authorId);
		q.setAuthorName(name);
		// q.setQuestion(question);
		q.setAnswerA(answerA);
		q.setAnswerB(answerB);
		try {
			questionService.createQuestion(q);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		
		q = new Question();
		q.setCreated(created);
		q.setAuthorId(authorId);
		q.setAuthorName(name);
		q.setQuestion(question);
		q.setAnswerA(answerA);
		//q.setAnswerB(answerB);
		try {
			questionService.createQuestion(q);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
	}
	
	@Test
	@Ignore
	public void testDeleteQuestion() throws Exception {
		// Delete existent question.
		questionService.deleteRepliesByQuestion(1L);
		questionService.deleteQuestion(1L);
		Question q = questionService.getQuestionById(1L);
		assertNull(q);
		QuestionDto qSolr = searchableQuestionDao.getById(1L);
		assertNull(qSolr);
		
		// Delete inexistent question.
		q = questionService.getQuestionById(99L);
		questionService.deleteQuestion(99L);
		q = questionService.getQuestionById(99L);
		assertNull(q);
		qSolr = searchableQuestionDao.getById(1L);
		assertNull(qSolr);
		
		// Exception testing.
		try {
			questionService.deleteQuestion(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testDeleteQuestionWithReplies() throws Exception {
		// Delete existent question.
		Long questionId = 1L;
		Question q = questionService.getQuestionById(questionId);
		assertNotNull(q);
		List<Reply> replies = questionService.getRepliesByQuestion(questionId);
		assertNotNull(replies);
		assertEquals(3, replies.size());
		questionService.deleteQuestionWithReplies(questionId);
		
		q = null;
		replies = null;
		q = questionService.getQuestionById(questionId);
		assertNull(q);
		replies = questionService.getRepliesByQuestion(questionId);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Delete inexistent question.
		questionId = 9999L;
		q = null;
		replies = null;
		q = questionService.getQuestionById(questionId);
		assertNull(q);
		replies = questionService.getRepliesByQuestion(questionId);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		questionService.deleteQuestionWithReplies(questionId);
		
		q = null;
		replies = null;
		q = questionService.getQuestionById(questionId);
		assertNull(q);
		replies = questionService.getRepliesByQuestion(questionId);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Exception testing.
		try {
			questionService.deleteQuestionWithReplies(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetReplyById() throws Exception {
		
		// Check for existent reply.
		Reply reply = questionService.getReplyById(1L, 1L);
		assertEquals(new Long(1L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		Calendar cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(15, cal.get(Calendar.DATE));
		assertEquals(new Integer(50), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		// Check for inexistent reply.
		reply = null;
		reply = questionService.getReplyById(1L, 2L);
		assertNull(reply);
		
		// Check for inexistent person.
		reply = null;
		reply = questionService.getReplyById(99L, 100L);
		assertNull(reply);
		
	}
	
	@Test
	public void testGetReplyByPerson() throws Exception {
		
		// Check for existent person.
		List<Reply> replies = questionService.getRepliesByPerson(1L, false, null, null, null);
		assertNotNull(replies);
		assertEquals(1, replies.size());
		Reply reply = replies.get(0);
		assertEquals(new Long(1L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		Calendar cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(15, cal.get(Calendar.DATE));
		assertEquals(new Integer(50), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		// Check for existent person without replies.
		replies = null;
		replies = questionService.getRepliesByPerson(7L, false, null, null, null);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Check for inexistent person.
		replies = null;
		replies = questionService.getRepliesByPerson(9999L, false, null, null, null);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
	}
	
	@Test
	public void testGetReplyByPersonLimited() throws Exception {
		
		// Check for existent person with replies.
		List<Reply> replies = questionService.getRepliesByPerson(1L, 0, 10);
		assertEquals(1, replies.size());
		Reply reply = replies.get(0);
		assertEquals(new Long(1L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		Calendar cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(15, cal.get(Calendar.DATE));
		assertEquals(new Integer(50), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		replies = null;
		reply = null;
		replies = questionService.getRepliesByPerson(1L, 0, 1);
		assertEquals(1, replies.size());
		reply = replies.get(0);
		assertEquals(new Long(1L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(15, cal.get(Calendar.DATE));
		assertEquals(new Integer(50), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		// Check for person without replies.
		replies = null;
		replies = questionService.getRepliesByPerson(7L, 0, 10);
		assertEquals(0, replies.size());
		
		// Check for inexistent person.
		replies = null;
		replies = questionService.getRepliesByPerson(9999L, 0, 10);
		assertEquals(0, replies.size());
		
		// Exception checking.
		try {
			questionService.getRepliesByPerson(1L, -1, 10);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionService.getRepliesByPerson(1L, -1, 0);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionService.getRepliesByPerson(1L, -1, -1);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionService.getRepliesByPerson(1L, 0, -1);
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetReplyByQuestion() throws Exception {
		
		// Check for existent question.
		List<Reply> replies = questionService.getRepliesByQuestion(1L);
		assertEquals(3, replies.size());
		Reply reply = replies.get(0);
		assertEquals(new Long(1L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		Calendar cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(15, cal.get(Calendar.DATE));
		assertEquals(new Integer(50), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		reply = replies.get(1);
		assertEquals(new Long(2L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(16, cal.get(Calendar.DATE));
		assertEquals(new Integer(0), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		reply = replies.get(2);
		assertEquals(new Long(3L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertTrue(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(20, cal.get(Calendar.DATE));
		assertEquals(new Integer(10), reply.getImportance());
		assertFalse(reply.getAnswerA());
		assertTrue(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		// Check for inexistent question.
		replies = null;
		reply = null;
		replies = questionService.getRepliesByQuestion(99L);
		assertEquals(0, replies.size());
		
	}
	
	@Test
	public void testGetReplyByQuestionLimited() throws Exception {
		
		// Check for existent question.
		List<Reply> replies = questionService.getRepliesByQuestion(1L, 0, 10);
		assertEquals(3, replies.size());
		Reply reply = replies.get(0);
		assertEquals(new Long(1L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		Calendar cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(15, cal.get(Calendar.DATE));
		assertEquals(new Integer(50), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		reply = replies.get(1);
		assertEquals(new Long(2L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(16, cal.get(Calendar.DATE));
		assertEquals(new Integer(0), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		reply = replies.get(2);
		assertEquals(new Long(3L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertTrue(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(20, cal.get(Calendar.DATE));
		assertEquals(new Integer(10), reply.getImportance());
		assertFalse(reply.getAnswerA());
		assertTrue(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		replies = null;
		reply = null;
		replies = questionService.getRepliesByQuestion(1L, 0, 1);
		assertEquals(1, replies.size());
		reply = replies.get(0);
		assertEquals(new Long(1L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(15, cal.get(Calendar.DATE));
		assertEquals(new Integer(50), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		replies = null;
		reply = null;
		replies = questionService.getRepliesByQuestion(1L, 1, 1);
		assertEquals(1, replies.size());
		reply = replies.get(0);
		assertEquals(new Long(2L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(16, cal.get(Calendar.DATE));
		assertEquals(new Integer(0), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		// Check for existent question without replies.
		replies = null;
		replies = questionService.getRepliesByQuestion(4L, 0, 10);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Check for inexistent question.
		replies = null;
		replies = questionService.getRepliesByQuestion(99L, 0, 10);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Exception testing.
		try {
			questionService.getRepliesByQuestion(1L, -1, 10);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionService.getRepliesByQuestion(1L, -1, 0);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionService.getRepliesByQuestion(1L, -1, -1);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionService.getRepliesByQuestion(1L, 0, -1);
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetRecentRepliesByDays() throws Exception {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2011);
		c.set(Calendar.MONTH, 3); // Month starts at 0.
		c.set(Calendar.DATE, 30);
		int days = (int)getDayDifference(c.getTime(), new Date(System.currentTimeMillis()));
		List<Reply> replies = questionService.getRecentRepliesByDays(days, false);
		assertEquals(2, replies.size());
		
		Reply reply = replies.get(0);
		assertEquals(new Long(5L), reply.getPersonId());
		assertEquals(new Long(2L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertTrue(reply.getInPrivate());
		assertFalse(reply.getCritical());
		Calendar cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(c.get(Calendar.YEAR), cal.get(Calendar.YEAR));
		assertEquals(c.get(Calendar.MONTH), cal.get(Calendar.MONTH));
		assertEquals(c.get(Calendar.DATE), cal.get(Calendar.DATE));
		assertEquals(new Integer(250), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		reply = replies.get(1);
		assertEquals(new Long(6L), reply.getPersonId());
		assertEquals(new Long(2L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(4, cal.get(Calendar.MONTH));
		assertEquals(21, cal.get(Calendar.DATE));
		assertEquals(new Integer(250), reply.getImportance());
		assertFalse(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertTrue(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		
		replies = null;
		reply = null;
		replies = questionService.getRecentRepliesByDays(days, true);
		assertEquals(1, replies.size());
		reply = replies.get(0);
		assertEquals(new Long(6L), reply.getPersonId());
		assertEquals(new Long(2L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(4, cal.get(Calendar.MONTH));
		assertEquals(21, cal.get(Calendar.DATE));
		assertEquals(new Integer(250), reply.getImportance());
		assertFalse(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertTrue(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
	}
	
	@Test
	public void testGetRecentRepliesBySize() throws Exception {
		
		List<Reply> replies = questionService.getRecentRepliesBySize(1, false);
		assertEquals(1, replies.size());
		Reply reply = replies.get(0);
		assertEquals(new Long(6L), reply.getPersonId());
		assertEquals(new Long(2L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		Calendar cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(4, cal.get(Calendar.MONTH));
		assertEquals(21, cal.get(Calendar.DATE));
		assertEquals(new Integer(250), reply.getImportance());
		assertFalse(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertTrue(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		replies = null;
		reply = null;
		replies = questionService.getRecentRepliesBySize(1, true);
		assertEquals(1, replies.size());
		reply = replies.get(0);
		assertEquals(new Long(6L), reply.getPersonId());
		assertEquals(new Long(2L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(4, cal.get(Calendar.MONTH));
		assertEquals(21, cal.get(Calendar.DATE));
		assertEquals(new Integer(250), reply.getImportance());
		assertFalse(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertTrue(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		replies = null;
		reply = null;
		replies = questionService.getRecentRepliesBySize(2, false);
		reply = replies.get(0);
		assertEquals(new Long(6L), reply.getPersonId());
		assertEquals(new Long(2L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(4, cal.get(Calendar.MONTH)); // Month starts at 0.
		assertEquals(21, cal.get(Calendar.DATE));
		assertEquals(new Integer(250), reply.getImportance());
		assertFalse(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertTrue(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		reply = replies.get(1);
		assertEquals(new Long(5L), reply.getPersonId());
		assertEquals(new Long(2L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertTrue(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH)); // Month starts at 0.
		assertEquals(30, cal.get(Calendar.DATE));
		assertEquals(new Integer(250), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		replies = null;
		reply = null;
		replies = questionService.getRecentRepliesBySize(2, true);
		assertEquals(2, replies.size());
		reply = replies.get(0);
		assertEquals(new Long(6L), reply.getPersonId());
		assertEquals(new Long(2L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(4, cal.get(Calendar.MONTH));
		assertEquals(21, cal.get(Calendar.DATE));
		assertEquals(new Integer(250), reply.getImportance());
		assertFalse(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertTrue(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		reply = replies.get(1);
		assertEquals(new Long(2L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH)); // Month starts at 0.
		assertEquals(16, cal.get(Calendar.DATE));
		assertEquals(new Integer(0), reply.getImportance());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		// Exception testing.
		try {
			questionService.getRecentRepliesBySize(0, false);
			fail();
		} catch (Exception e ){
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionService.getRecentRepliesBySize(0, true);
			fail();
		} catch (Exception e ){
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionService.getRecentRepliesBySize(-1, false);
			fail();
		} catch (Exception e ){
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionService.getRecentRepliesBySize(-1, true);
			fail();
		} catch (Exception e ){
			// Exception expected.
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testExistsReply() throws Exception {
		assertTrue(questionService.existsReply(1L, 1L));
		assertTrue(questionService.existsReply(2L, 1L));
		assertTrue(questionService.existsReply(3L, 1L));
		assertFalse(questionService.existsReply(1L, 2L));
		assertFalse(questionService.existsReply(99L, 1L));
		assertFalse(questionService.existsReply(1L, 99L));
		assertFalse(questionService.existsReply(99L, 99L));
	}
	
	@Test
	public void testCreateReply() throws Exception {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(System.currentTimeMillis()));
		
		Reply reply = new Reply();
		reply.setPersonId(1L);
		reply.setQuestionId(2L);
		reply.setSkipped(false);
		reply.setInPrivate(false);
		reply.setCritical(false);
		reply.setLastUpdate(cal.getTime());
		reply.setImportance(250);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		
		questionService.createReply(reply);
		Reply createdReply = questionService.getReplyById(1L, 2L);
		Calendar calCreated = Calendar.getInstance();
		calCreated.setTime(createdReply.getLastUpdate());
		assertEquals(reply.getPersonId(), createdReply.getPersonId());
		assertEquals(reply.getQuestionId(), createdReply.getQuestionId());
		assertEquals(reply.getSkipped(), createdReply.getSkipped());
		assertEquals(reply.getInPrivate(), createdReply.getInPrivate());
		assertEquals(reply.getCritical(), createdReply.getCritical());
		assertEquals(cal.get(Calendar.YEAR), calCreated.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calCreated.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calCreated.get(Calendar.DATE));
		assertEquals(reply.getImportance(), createdReply.getImportance());
		assertEquals(reply.getExplanation(), createdReply.getExplanation());
		assertEquals(reply.getAnswerA(), createdReply.getAnswerA());
		assertEquals(reply.getAnswerB(), createdReply.getAnswerB());
		assertEquals(reply.getAnswerC(), createdReply.getAnswerC());
		assertEquals(reply.getAnswerD(), createdReply.getAnswerD());
		assertEquals(reply.getAnswerE(), createdReply.getAnswerE());
		
		// Create an already existent reply.
		reply = new Reply();
		reply.setPersonId(1L);
		reply.setQuestionId(1L);
		reply.setSkipped(true);
		reply.setInPrivate(true);
		reply.setCritical(true);
		reply.setLastUpdate(cal.getTime());
		reply.setImportance(250);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		questionService.createReply(reply);
		// Nothing should have changed.
		createdReply = questionService.getReplyById(1L, 1L);
		assertEquals(new Long(1L), createdReply.getPersonId());
		assertEquals(new Long(1L), createdReply.getQuestionId());
		assertFalse(createdReply.getSkipped());
		assertFalse(createdReply.getInPrivate());
		assertFalse(createdReply.getCritical());
		cal = Calendar.getInstance();
		cal.setTime(createdReply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(15, cal.get(Calendar.DATE));
		assertEquals(new Integer(50), createdReply.getImportance());
		assertTrue(createdReply.getAnswerA());
		assertFalse(createdReply.getAnswerB());
		assertFalse(createdReply.getAnswerC());
		assertFalse(createdReply.getAnswerD());
		assertFalse(createdReply.getAnswerE());
		
		
		reply = new Reply();
		//reply.setPersonId(1L);
		reply.setQuestionId(3L);
		reply.setSkipped(false);
		reply.setInPrivate(false);
		reply.setCritical(false);
		reply.setLastUpdate(cal.getTime());
		reply.setImportance(250);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		try {
			questionService.createReply(reply);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		
		reply = new Reply();
		reply.setPersonId(1L);
		//reply.setQuestionId(3L);
		reply.setSkipped(false);
		reply.setInPrivate(false);
		reply.setCritical(false);
		reply.setLastUpdate(cal.getTime());
		reply.setImportance(250);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		try {
			questionService.createReply(reply);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		
		reply = new Reply();
		reply.setPersonId(1L);
		reply.setQuestionId(3L);
		//reply.setSkipped(false);
		reply.setInPrivate(false);
		reply.setCritical(false);
		reply.setLastUpdate(cal.getTime());
		reply.setImportance(250);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		try {
			questionService.createReply(reply);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		
		reply = new Reply();
		reply.setPersonId(1L);
		reply.setQuestionId(3L);
		reply.setSkipped(false);
		//reply.setInPrivate(false);
		reply.setCritical(false);
		reply.setLastUpdate(cal.getTime());
		reply.setImportance(250);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		try {
			questionService.createReply(reply);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		
		reply = new Reply();
		reply.setPersonId(1L);
		reply.setQuestionId(3L);
		reply.setSkipped(false);
		reply.setInPrivate(false);
		//reply.setCritical(false);
		reply.setLastUpdate(cal.getTime());
		reply.setImportance(250);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		try {
			questionService.createReply(reply);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		
		reply = new Reply();
		reply.setPersonId(1L);
		reply.setQuestionId(3L);
		reply.setSkipped(false);
		reply.setInPrivate(false);
		reply.setCritical(false);
		//reply.setLastUpdate(cal.getTime());
		reply.setImportance(250);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		try {
			questionService.createReply(reply);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		
		reply = new Reply();
		reply.setPersonId(1L);
		reply.setQuestionId(3L);
		reply.setSkipped(false);
		reply.setInPrivate(false);
		reply.setCritical(false);
		reply.setLastUpdate(cal.getTime());
		//reply.setImportance(250);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		try {
			questionService.createReply(reply);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		
		reply = new Reply();
		reply.setPersonId(1L);
		reply.setQuestionId(3L);
		reply.setSkipped(false);
		reply.setInPrivate(false);
		reply.setCritical(false);
		reply.setLastUpdate(cal.getTime());
		reply.setImportance(250000);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		try {
			questionService.createReply(reply);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
	}
	
	@Test
	public void testUpdateReply() throws Exception {
		
		Boolean skipped = true;
		Boolean inPrivate = true;
		Boolean critical = true;
		Date lastUpdate = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastUpdate);
		Integer importance = 250;
		String explanation = "Blah.";
		Boolean answerA = false;
		Boolean answerB = true;
		Boolean answerC = false;
		Boolean answerD = false;
		Boolean answerE = false;
		Boolean expectedA = false;
		Boolean expectedB = true;
		Boolean expectedC = false;
		Boolean expectedD = false;
		Boolean expectedE = false;
		
		
		// Update skipped etc.
		Reply reply = questionService.getReplyById(1L, 1L);
		reply.setSkipped(skipped);
		reply.setInPrivate(inPrivate);
		reply.setCritical(critical);
		questionService.updateReply(reply);
		
		Reply updated = questionService.getReplyById(1L, 1L);
		Calendar calPrevious = Calendar.getInstance();
		calPrevious.setTime(reply.getLastUpdate());
		Calendar calUpdated = Calendar.getInstance();
		calUpdated.setTime(updated.getLastUpdate());
		assertEquals(skipped, updated.getSkipped());
		assertEquals(inPrivate, updated.getInPrivate());
		assertEquals(critical, updated.getCritical());
		assertEquals(calPrevious.get(Calendar.YEAR), calUpdated.get(Calendar.YEAR));
		assertEquals(calPrevious.get(Calendar.MONTH), calUpdated.get(Calendar.MONTH));
		assertEquals(calPrevious.get(Calendar.DATE), calUpdated.get(Calendar.DATE));
		assertEquals(reply.getImportance(), updated.getImportance());
		assertEquals(reply.getExplanation(), updated.getExplanation());
		assertEquals(reply.getAnswerA(), updated.getAnswerA());
		assertEquals(reply.getAnswerB(), updated.getAnswerB());
		assertEquals(reply.getAnswerC(), updated.getAnswerC());
		assertEquals(reply.getAnswerD(), updated.getAnswerD());
		assertEquals(reply.getAnswerE(), updated.getAnswerE());
		
		// Update last update value.
		reply = null;
		updated = null;
		reply = questionService.getReplyById(1L, 1L);
		reply.setLastUpdate(lastUpdate);
		questionService.updateReply(reply);
		
		updated = questionService.getReplyById(1L, 1L);
		calUpdated = Calendar.getInstance();
		calUpdated.setTime(updated.getLastUpdate());
		assertEquals(skipped, updated.getSkipped());
		assertEquals(inPrivate, updated.getInPrivate());
		assertEquals(critical, updated.getCritical());
		assertEquals(cal.get(Calendar.YEAR), calUpdated.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calUpdated.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calUpdated.get(Calendar.DATE));
		assertEquals(reply.getImportance(), updated.getImportance());
		assertEquals(reply.getExplanation(), updated.getExplanation());
		assertEquals(reply.getAnswerA(), updated.getAnswerA());
		assertEquals(reply.getAnswerB(), updated.getAnswerB());
		assertEquals(reply.getAnswerC(), updated.getAnswerC());
		assertEquals(reply.getAnswerD(), updated.getAnswerD());
		assertEquals(reply.getAnswerE(), updated.getAnswerE());
		
		// Update importance.
		reply = null;
		updated = null;
		reply = questionService.getReplyById(1L, 1L);
		reply.setImportance(importance);
		questionService.updateReply(reply);
		
		updated = questionService.getReplyById(1L, 1L);
		calUpdated = Calendar.getInstance();
		calUpdated.setTime(updated.getLastUpdate());
		assertEquals(skipped, updated.getSkipped());
		assertEquals(inPrivate, updated.getInPrivate());
		assertEquals(critical, updated.getCritical());
		assertEquals(cal.get(Calendar.YEAR), calUpdated.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calUpdated.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calUpdated.get(Calendar.DATE));
		assertEquals(importance, updated.getImportance());
		assertEquals(reply.getExplanation(), updated.getExplanation());
		assertEquals(reply.getAnswerA(), updated.getAnswerA());
		assertEquals(reply.getAnswerB(), updated.getAnswerB());
		assertEquals(reply.getAnswerC(), updated.getAnswerC());
		assertEquals(reply.getAnswerD(), updated.getAnswerD());
		assertEquals(reply.getAnswerE(), updated.getAnswerE());
		
		// Update explanation.
		reply = null;
		updated = null;
		reply = questionService.getReplyById(1L, 1L);
		reply.setExplanation(explanation);
		questionService.updateReply(reply);
		
		updated = questionService.getReplyById(1L, 1L);
		calUpdated = Calendar.getInstance();
		calUpdated.setTime(updated.getLastUpdate());
		assertEquals(skipped, updated.getSkipped());
		assertEquals(inPrivate, updated.getInPrivate());
		assertEquals(critical, updated.getCritical());
		assertEquals(cal.get(Calendar.YEAR), calUpdated.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calUpdated.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calUpdated.get(Calendar.DATE));
		assertEquals(importance, updated.getImportance());
		assertEquals(explanation, updated.getExplanation());
		assertEquals(reply.getAnswerA(), updated.getAnswerA());
		assertEquals(reply.getAnswerB(), updated.getAnswerB());
		assertEquals(reply.getAnswerC(), updated.getAnswerC());
		assertEquals(reply.getAnswerD(), updated.getAnswerD());
		assertEquals(reply.getAnswerE(), updated.getAnswerE());
		
		// Update answers and expected answers.
		reply = null;
		updated = null;
		reply = questionService.getReplyById(1L, 1L);
		reply.setAnswerA(answerA);
		reply.setAnswerB(answerB);
		reply.setAnswerC(answerC);
		reply.setAnswerD(answerD);
		reply.setAnswerE(answerE);
		questionService.updateReply(reply);
		
		updated = questionService.getReplyById(1L, 1L);
		calUpdated = Calendar.getInstance();
		calUpdated.setTime(updated.getLastUpdate());
		assertEquals(skipped, updated.getSkipped());
		assertEquals(inPrivate, updated.getInPrivate());
		assertEquals(critical, updated.getCritical());
		assertEquals(cal.get(Calendar.YEAR), calUpdated.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calUpdated.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calUpdated.get(Calendar.DATE));
		assertEquals(importance, updated.getImportance());
		assertEquals(explanation, updated.getExplanation());
		assertEquals(answerA, updated.getAnswerA());
		assertEquals(answerB, updated.getAnswerB());
		assertEquals(answerC, updated.getAnswerC());
		assertEquals(answerD, updated.getAnswerD());
		assertEquals(answerE, updated.getAnswerE());
	}
	
	@Test
	public void testDeleteReply() throws Exception {
		Reply reply = questionService.getReplyById(1L, 1L);
		assertNotNull(reply);
		questionService.deleteReply(1L, 1L);
		reply = questionService.getReplyById(1L, 1L);
		assertNull(reply);
		
		reply = questionService.getReplyById(99L, 99L);
		assertNull(reply);
		questionService.deleteReply(99L, 99L);
		reply = questionService.getReplyById(99L, 99L);
		assertNull(reply);
	}
	
	@Test
	public void testDeleteRepliesByPerson() throws Exception {
		List<Reply> replies = questionService.getRepliesByPerson(1L, false, null, null, null);
		assertEquals(1, replies.size());
		questionService.deleteRepliesByPerson(1L);
		replies = questionService.getRepliesByPerson(1L, false, null, null, null);
		assertEquals(0, replies.size());
	}
	
	@Test
	public void testDeleteRepliesByQuestion() throws Exception {
		List<Reply> replies = questionService.getRepliesByQuestion(1L);
		assertEquals(3, replies.size());
		questionService.deleteRepliesByQuestion(1L);
		replies = questionService.getRepliesByPerson(1L, false, null, null, null);
		assertEquals(0, replies.size());
	}
	
	@Test
	@Ignore
	public void testGetSimilarQuestions() throws Exception {
		// Search for existing similar questions.
		Question question = questionService.getQuestionById(1L);
		List<Question> questions = questionService.getSimilarQuestions(question);
		assertNotNull(questions);
		assertEquals(2, questions.size());
		
		// Search for not existing similar questions.
		question = new Question();
		question.setQuestion("Do you support nuclear power reactors?");
		question.setAnswerA("Yes, it is a good way to produce energy.");
		question.setAnswerB("No, they should all be stopped.");
		questions = null;
		questions = questionService.getSimilarQuestions(question);
		assertNotNull(questions);
		assertEquals(0, questions.size());
		
		// Exception testing.
		try {
			questionService.getSimilarQuestions(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			question = new Question();
			questionService.getSimilarQuestions(question);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			question = new Question();
			question.setQuestion("");
			questionService.getSimilarQuestions(question);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	public long getDayDifference(Date a, Date b) {
		long diff;
		if (a.getTime() > b.getTime()) {
			diff = a.getTime() - b.getTime();
		} else {
			diff = b.getTime() - a.getTime();
		}
		long diffDays = diff / (long)(24 * 60 * 60 * 1000); // Calculate difference in days
		return diffDays;
	}
	
}
