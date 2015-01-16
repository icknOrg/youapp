package youapp.dataaccess.dao.jdbc;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import youapp.JdbcDbInitializer;
import youapp.dataaccess.dto.QuestionDto;
import youapp.dataaccess.dto.ReplyDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml", "classpath:dao-context.xml", "classpath:service-context.xml" })
public class JdbcQuestionDaoTest {

	private ApplicationContext applicationContext;

	private JdbcDbInitializer jdbcDbInitializer;

	private JdbcQuestionDao questionDao;

	private JdbcReplyDao replyDao;

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
		questionDao = (JdbcQuestionDao)applicationContext.getBean("questionDao", JdbcQuestionDao.class);
		replyDao = (JdbcReplyDao)applicationContext.getBean("replyDao", JdbcReplyDao.class);
	}

	@After
	public void tearDown() {
		questionDao = null;
		replyDao = null;
	}

	@Test
	public void testGetById() {
		QuestionDto question = questionDao.getById(1L);
		assertEquals(new Long(1L), question.getId());
		assertEquals(new Long(1L), question.getAuthor());
		Calendar cal = Calendar.getInstance();
		cal.setTime(question.getCreated());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(15, cal.get(Calendar.DATE));
		assertEquals("Cats or dogs - which do you like more?", question.getQuestion());
		assertEquals("Cats", question.getAnswerA());
		assertEquals("Dogs", question.getAnswerB());
		assertNull(question.getAnswerC());
		assertNull(question.getAnswerD());
		assertNull(question.getAnswerE());

		// Exception testing.
		try {
			questionDao.getById(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionDao.getById(9999L);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}

	}

	@Test
	public void testGetByAuthor() {
		// Check for existent author which has questions.
		List<QuestionDto> questions = questionDao.getByAuthor(1L);
		assertEquals(3, questions.size());
		QuestionDto q = questions.get(0);
		assertEquals(new Long(1L), q.getId());
		assertEquals(new Long(1L), q.getAuthor());
		assertEquals("Cats or dogs - which do you like more?", q.getQuestion());
		assertEquals("Cats", q.getAnswerA());
		assertEquals("Dogs", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		q = null;
		q = questions.get(1);
		assertEquals(new Long(2L), q.getId());
		assertEquals(new Long(1L), q.getAuthor());
		assertEquals("Would you consider a long-distance relationship?", q.getQuestion());
		assertEquals("Yes, why not?", q.getAnswerA());
		assertEquals("Depends, if I would be totally in love maybe.", q.getAnswerB());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());

		// Check for existent author which has no questions.
		questions = null;
		questions = questionDao.getByAuthor(7L);
		assertEquals(0, questions.size());

		// Add a new question for the above author and repeat the above test.
		questions = null;
		q = new QuestionDto();
		q.setAuthor(7L);
		q.setCreated(new Date(System.currentTimeMillis()));
		q.setQuestion("Which is your favourite season?");
		q.setAnswerA("Spring");
		q.setAnswerB("Summer");
		q.setAnswerC("Autumn");
		q.setAnswerD("Winter");
		Long id = questionDao.create(q);
		questions = questionDao.getByDate(new Date(System.currentTimeMillis()));
		assertNotNull(questions);
		assertEquals(1, questions.size());
		assertEquals(id, questions.get(0).getId());

		// Check for inexistent author.
		questions = null;
		questions = questionDao.getByAuthor(9999L);
		assertEquals(0, questions.size());

		// Exception checking.
		try {
			questionDao.getByAuthor(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetByAuthorLimited() {
		// Check for existent author which has created questions.
		List<QuestionDto> questions = questionDao.getByAuthor(1L, 0, 1);
		assertEquals(1, questions.size());
		QuestionDto q = questions.get(0);
		assertEquals(new Long(1L), q.getId());
		assertEquals(new Long(1L), q.getAuthor());
		assertEquals("Cats or dogs - which do you like more?", q.getQuestion());
		assertEquals("Cats", q.getAnswerA());
		assertEquals("Dogs", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());

		questions = null;
		q = null;
		questions = questionDao.getByAuthor(1L, 0, 10);
		assertEquals(3, questions.size());
		q = questions.get(0);
		assertEquals(new Long(1L), q.getId());
		assertEquals(new Long(1L), q.getAuthor());
		assertEquals("Cats or dogs - which do you like more?", q.getQuestion());
		assertEquals("Cats", q.getAnswerA());
		assertEquals("Dogs", q.getAnswerB());
		assertNull(q.getAnswerC());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());
		q = null;
		q = questions.get(1);
		assertEquals(new Long(2L), q.getId());
		assertEquals(new Long(1L), q.getAuthor());
		assertEquals("Would you consider a long-distance relationship?", q.getQuestion());
		assertEquals("Yes, why not?", q.getAnswerA());
		assertEquals("Depends, if I would be totally in love maybe.", q.getAnswerB());
		assertNull(q.getAnswerD());
		assertNull(q.getAnswerE());

		// Check for existent author which has not created questions.
		questions = null;
		questions = questionDao.getByAuthor(7L);
		assertEquals(0, questions.size());

		// Add a new question for the above author and repeat the above test.
		questions = null;
		q = new QuestionDto();
		q.setAuthor(7L);
		q.setCreated(new Date(System.currentTimeMillis()));
		q.setQuestion("Which is your favourite season?");
		q.setAnswerA("Spring");
		q.setAnswerB("Summer");
		q.setAnswerC("Autumn");
		q.setAnswerD("Winter");
		Long id = questionDao.create(q);
		questions = questionDao.getByDate(new Date(System.currentTimeMillis()));
		assertNotNull(questions);
		assertEquals(1, questions.size());
		assertEquals(id, questions.get(0).getId());

		// Check for inexistent author.
		questions = null;
		questions = questionDao.getByAuthor(9999L);
		assertEquals(0, questions.size());

		// Exception checking.
		try {
			questionDao.getByAuthor(null, 0, 2);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionDao.getByAuthor(1L, null, 2);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionDao.getByAuthor(1L, 0, null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionDao.getByAuthor(1L, 0, 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionDao.getByAuthor(1L, 0, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionDao.getByAuthor(1L, -1, 2);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetByDate() {
		Calendar cal = Calendar.getInstance();

		// Retrieve existent questions.
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, 4);
		cal.set(Calendar.DATE, 25);
		List<QuestionDto> questions = questionDao.getByDate(cal.getTime());
		assertNotNull(questions);
		assertEquals(2, questions.size());
		assertEquals(new Long(4L), questions.get(0).getId());
		assertEquals(new Long(5L), questions.get(1).getId());

		// Provide a date for which no questions exist.
		questions = null;
		questions = questionDao.getByDate(new Date(System.currentTimeMillis()));
		assertNotNull(questions);
		assertEquals(0, questions.size());

		// Provide strange dates.
		questions = null;
		cal.set(Calendar.YEAR, 129);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 0);
		questions = questionDao.getByDate(cal.getTime());
		assertNotNull(questions);
		assertEquals(0, questions.size());

		// Create question and retrieve it via date.
		questions = null;
		QuestionDto q = new QuestionDto();
		q.setAuthor(1L);
		q.setCreated(new Date(System.currentTimeMillis()));
		q.setQuestion("Which is your favourite season?");
		q.setAnswerA("Spring");
		q.setAnswerB("Summer");
		q.setAnswerC("Autumn");
		q.setAnswerD("Winter");
		Long id = questionDao.create(q);
		questions = questionDao.getByDate(new Date(System.currentTimeMillis()));
		assertNotNull(questions);
		assertEquals(1, questions.size());
		assertEquals(id, questions.get(0).getId());

		// Exception testing.
		try {
			questionDao.getByDate(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetMostRecentByDays() {
//		// Provide number of days for which recent questions exist.
//		List<QuestionDto> questions = questionDao.getMostRecentByDays(158);
//		assertNotNull(questions);
//		assertEquals(4, questions.size());
//
//		// Provide number of days for which no recent questions exist.
//		questions = null;
//		questions = questionDao.getMostRecentByDays(1);
//		assertNotNull(questions);
//		assertEquals(0, questions.size());
//
//		// Create question and repeat the above test.
//		questions = null;
//		QuestionDto q = new QuestionDto();
//		q.setAuthor(1L);
//		q.setCreated(new Date(System.currentTimeMillis()));
//		q.setQuestion("Which is your favourite season?");
//		q.setAnswerA("Spring");
//		q.setAnswerB("Summer");
//		q.setAnswerC("Autumn");
//		q.setAnswerD("Winter");
//		Long id = questionDao.create(q);
//		questions = questionDao.getMostRecentByDays(0);
//		assertNotNull(questions);
//		assertEquals(1, questions.size());
//		assertEquals(id, questions.get(0).getId());
//
//		// Exception testing.
//		try {
//			questionDao.getMostRecentByDays(-1);
//			fail();
//		} catch (Exception e) {
//			assertTrue(true);
//		}
	}

	@Test
	public void testGetMostRecentBySize() {
		// Check for an existing number of questions.
		List<QuestionDto> questions = questionDao.getMostRecentBySize(2);
		assertEquals(2, questions.size());
		assertEquals(new Long(6L), questions.get(0).getId());
		assertEquals(new Long(4L), questions.get(1).getId());

		// Check for more than the existing number of questions.
		questions = null;
		questions = questionDao.getMostRecentBySize(10);
		assertEquals(6, questions.size());
		assertEquals(new Long(6L), questions.get(0).getId());
		assertEquals(new Long(4L), questions.get(1).getId());
		assertEquals(new Long(5L), questions.get(2).getId());
		assertEquals(new Long(3L), questions.get(3).getId());
		assertEquals(new Long(2L), questions.get(4).getId());
		assertEquals(new Long(1L), questions.get(5).getId());
		
		// Create a new question and check whether it shows up.
		questions = null;
		QuestionDto q = new QuestionDto();
		q.setAuthor(1L);
		q.setCreated(new Date(System.currentTimeMillis()));
		q.setQuestion("Which is your favourite season?");
		q.setAnswerA("Spring");
		q.setAnswerB("Summer");
		q.setAnswerC("Autumn");
		q.setAnswerD("Winter");
		Long id = questionDao.create(q);
		questions = questionDao.getMostRecentBySize(1);
		assertNotNull(questions);
		assertEquals(1, questions.size());
		assertEquals(id, questions.get(0).getId());

		// Exception testing.
		try {
			questionDao.getMostRecentBySize(0);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			questionDao.getMostRecentBySize(-1);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
	}

	@Test
	public void testFetchNext() {
		List<QuestionDto> questions = questionDao.fetchNext(1L, 2);
		assertNotNull(questions);
		assertEquals(2, questions.size());
		assertNotSame(new Long(1L), questions.get(0).getId());
		assertNotSame(new Long(1L), questions.get(1).getId());

		// Replied questions should not be fetched.
		ReplyDto reply = new ReplyDto();
		reply.setPersonId(1L);
		reply.setQuestionId(2L);
		reply.setSkipped(false);
		reply.setInPrivate(false);
		reply.setCritical(false);
		reply.setLastUpdate(new Date(System.currentTimeMillis()));
		reply.setImportance(250);
		reply.setAnswerA(true);
		reply.setAnswerB(false);
		reply.setAnswerC(false);
		reply.setAnswerD(false);
		reply.setAnswerE(false);
		replyDao.create(reply);

		questions = null;
		questions = questionDao.fetchNext(1L, 2);
		assertEquals(2, questions.size());
		assertNotSame(new Long(2L), questions.get(0).getId());
		assertNotSame(new Long(2L), questions.get(1).getId());

		// When providing an inexistent person id, questions should still be retrieved.
		questions = null;
		questions = questionDao.fetchNext(9999L, 2);
		assertEquals(2, questions.size());

		// Exception testing.
		try {
			questionDao.fetchNext(null, 2);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionDao.fetchNext(1L, 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionDao.fetchNext(1L, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionDao.fetchNext(null, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testCreate() {
		
		// Create a new question and check whether it's added.
		QuestionDto qCreated = new QuestionDto();
		qCreated.setAuthor(1L);
		qCreated.setCreated(new Date(System.currentTimeMillis()));
		qCreated.setQuestion("Which is your favourite season?");
		qCreated.setAnswerA("Spring");
		qCreated.setAnswerB("Summer");
		qCreated.setAnswerC("Autumn");
		qCreated.setAnswerD("Winter");
		Long id = questionDao.create(qCreated);
		QuestionDto qRetrieved = questionDao.getById(id);
		Calendar cal = Calendar.getInstance();
		cal.setTime(qCreated.getCreated());
		Calendar calCreated = Calendar.getInstance();
		calCreated.setTime(qRetrieved.getCreated());
		assertNotNull(qRetrieved);
		assertEquals(qCreated.getId(), qRetrieved.getId());
		assertEquals(qCreated.getAuthor(), qRetrieved.getAuthor());
		assertEquals(cal.get(Calendar.YEAR), calCreated.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calCreated.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calCreated.get(Calendar.DATE));
		assertEquals(qCreated.getQuestion(), qRetrieved.getQuestion());
		assertEquals(qCreated.getAnswerA(), qRetrieved.getAnswerA());
		assertEquals(qCreated.getAnswerB(), qRetrieved.getAnswerB());
		assertEquals(qCreated.getAnswerC(), qRetrieved.getAnswerC());
		assertEquals(qCreated.getAnswerD(), qRetrieved.getAnswerD());
		assertEquals(qCreated.getAnswerE(), qRetrieved.getAnswerE());
		
		// Exception testing.
		try {
			questionDao.create(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			qCreated = new QuestionDto();
			qCreated.setAuthor(null);
			qCreated.setCreated(new Date(System.currentTimeMillis()));
			qCreated.setQuestion("Which is your favourite season?");
			qCreated.setAnswerA("Spring");
			qCreated.setAnswerB("Summer");
			qCreated.setAnswerC("Autumn");
			qCreated.setAnswerD("Winter");
			questionDao.create(qCreated);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			qCreated = new QuestionDto();
			qCreated.setAuthor(9999L);
			qCreated.setCreated(new Date(System.currentTimeMillis()));
			qCreated.setQuestion("Which is your favourite season?");
			qCreated.setAnswerA("Spring");
			qCreated.setAnswerB("Summer");
			qCreated.setAnswerC("Autumn");
			qCreated.setAnswerD("Winter");
			questionDao.create(qCreated);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			qCreated = new QuestionDto();
			qCreated.setAuthor(1L);
			qCreated.setCreated(null);
			qCreated.setQuestion("Which is your favourite season?");
			qCreated.setAnswerA("Spring");
			qCreated.setAnswerB("Summer");
			qCreated.setAnswerC("Autumn");
			qCreated.setAnswerD("Winter");
			questionDao.create(qCreated);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			qCreated = new QuestionDto();
			qCreated.setAuthor(1L);
			qCreated.setCreated(new Date(System.currentTimeMillis()));
			qCreated.setQuestion(null);
			qCreated.setAnswerA("Spring");
			qCreated.setAnswerB("Summer");
			qCreated.setAnswerC("Autumn");
			qCreated.setAnswerD("Winter");
			questionDao.create(qCreated);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			qCreated = new QuestionDto();
			qCreated.setAuthor(1L);
			qCreated.setCreated(new Date(System.currentTimeMillis()));
			qCreated.setQuestion("Which is your favourite season?");
			qCreated.setAnswerA(null);
			qCreated.setAnswerB("Summer");
			qCreated.setAnswerC("Autumn");
			qCreated.setAnswerD("Winter");
			questionDao.create(qCreated);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			qCreated = new QuestionDto();
			qCreated.setAuthor(1L);
			qCreated.setCreated(new Date(System.currentTimeMillis()));
			qCreated.setQuestion("Which is your favourite season?");
			qCreated.setAnswerA("Spring");
			qCreated.setAnswerB(null);
			qCreated.setAnswerC("Autumn");
			qCreated.setAnswerD("Winter");
			questionDao.create(qCreated);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testUpdate() {
		
		Long questionId = 1L;
		Long authorId = 2L;
		Date created = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		String question = "Question!";
		String answerA = "AnswerA";
		String answerB = "AnswerB";
		
		// Update author id.
		QuestionDto q = questionDao.getById(questionId);
		cal.setTime(q.getCreated());
		q.setAuthor(authorId);
		questionDao.update(q);
		QuestionDto qUpdated = questionDao.getById(questionId);
		Calendar calUpdated = Calendar.getInstance();
		calUpdated.setTime(qUpdated.getCreated());
		assertEquals(q.getId(), qUpdated.getId());
		assertEquals(cal.get(Calendar.YEAR), calUpdated.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calUpdated.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calUpdated.get(Calendar.DATE));
		assertEquals(q.getAuthor(), qUpdated.getAuthor());
		assertEquals(q.getQuestion(), qUpdated.getQuestion());
		assertEquals(q.getAnswerA(), qUpdated.getAnswerA());
		assertEquals(q.getAnswerB(), qUpdated.getAnswerB());
		assertEquals(q.getAnswerC(), qUpdated.getAnswerC());
		assertEquals(q.getAnswerD(), qUpdated.getAnswerD());
		assertEquals(q.getAnswerE(), qUpdated.getAnswerE());
		
		// Update created date.
		q = null;
		qUpdated = null;
		q = questionDao.getById(questionId);
		q.setCreated(created);
		cal.setTime(created);
		questionDao.update(q);
		qUpdated = questionDao.getById(questionId);
		calUpdated.setTime(qUpdated.getCreated());
		assertEquals(q.getId(), qUpdated.getId());
		assertEquals(cal.get(Calendar.YEAR), calUpdated.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calUpdated.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calUpdated.get(Calendar.DATE));
		assertEquals(q.getAuthor(), qUpdated.getAuthor());
		assertEquals(q.getQuestion(), qUpdated.getQuestion());
		assertEquals(q.getAnswerA(), qUpdated.getAnswerA());
		assertEquals(q.getAnswerB(), qUpdated.getAnswerB());
		assertEquals(q.getAnswerC(), qUpdated.getAnswerC());
		assertEquals(q.getAnswerD(), qUpdated.getAnswerD());
		assertEquals(q.getAnswerE(), qUpdated.getAnswerE());
		
		// Update question.
		q = null;
		qUpdated = null;
		q = questionDao.getById(questionId);
		q.setQuestion(question);
		questionDao.update(q);
		qUpdated = questionDao.getById(questionId);
		calUpdated.setTime(qUpdated.getCreated());
		assertEquals(q.getId(), qUpdated.getId());
		assertEquals(cal.get(Calendar.YEAR), calUpdated.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calUpdated.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calUpdated.get(Calendar.DATE));
		assertEquals(q.getAuthor(), qUpdated.getAuthor());
		assertEquals(q.getQuestion(), qUpdated.getQuestion());
		assertEquals(q.getAnswerA(), qUpdated.getAnswerA());
		assertEquals(q.getAnswerB(), qUpdated.getAnswerB());
		assertEquals(q.getAnswerC(), qUpdated.getAnswerC());
		assertEquals(q.getAnswerD(), qUpdated.getAnswerD());
		assertEquals(q.getAnswerE(), qUpdated.getAnswerE());
		
		// Update answer A.
		q = null;
		qUpdated = null;
		q = questionDao.getById(questionId);
		q.setAnswerA(answerA);
		questionDao.update(q);
		qUpdated = questionDao.getById(questionId);
		calUpdated.setTime(qUpdated.getCreated());
		assertEquals(q.getId(), qUpdated.getId());
		assertEquals(cal.get(Calendar.YEAR), calUpdated.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calUpdated.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calUpdated.get(Calendar.DATE));
		assertEquals(q.getAuthor(), qUpdated.getAuthor());
		assertEquals(q.getQuestion(), qUpdated.getQuestion());
		assertEquals(q.getAnswerA(), qUpdated.getAnswerA());
		assertEquals(q.getAnswerB(), qUpdated.getAnswerB());
		assertEquals(q.getAnswerC(), qUpdated.getAnswerC());
		assertEquals(q.getAnswerD(), qUpdated.getAnswerD());
		assertEquals(q.getAnswerE(), qUpdated.getAnswerE());
		
		// Update answer B.
		q = null;
		qUpdated = null;
		q = questionDao.getById(questionId);
		q.setAnswerB(answerB);
		questionDao.update(q);
		qUpdated = questionDao.getById(questionId);
		calUpdated.setTime(qUpdated.getCreated());
		assertEquals(q.getId(), qUpdated.getId());
		assertEquals(cal.get(Calendar.YEAR), calUpdated.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), calUpdated.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), calUpdated.get(Calendar.DATE));
		assertEquals(q.getAuthor(), qUpdated.getAuthor());
		assertEquals(q.getQuestion(), qUpdated.getQuestion());
		assertEquals(q.getAnswerA(), qUpdated.getAnswerA());
		assertEquals(q.getAnswerB(), qUpdated.getAnswerB());
		assertEquals(q.getAnswerC(), qUpdated.getAnswerC());
		assertEquals(q.getAnswerD(), qUpdated.getAnswerD());
		assertEquals(q.getAnswerE(), qUpdated.getAnswerE());
		
		// Exception testing.
		try {
			questionDao.update((QuestionDto)null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			q = questionDao.getById(questionId);
			q.setAuthor(null);
			questionDao.update(q);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			q = questionDao.getById(questionId);
			q.setAuthor(9999L);
			questionDao.update(q);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			q = questionDao.getById(questionId);
			q.setCreated(null);
			questionDao.update(q);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			q = questionDao.getById(questionId);
			q.setQuestion(null);
			questionDao.update(q);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			q = questionDao.getById(questionId);
			q.setAnswerA(null);
			questionDao.update(q);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			q = questionDao.getById(questionId);
			q.setAnswerB(null);
			questionDao.update(q);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testUpdateMultiple() {
		
		Long qFirstId = 1L;
		Long qFirstAuthor = 1L;
		Calendar qFirstCal = Calendar.getInstance();
		qFirstCal.set(Calendar.YEAR, 2010);
		qFirstCal.set(Calendar.MONTH, 0);
		qFirstCal.set(Calendar.DATE, 1);
		String qFirstQuestion = "First!";
		String qFirstA = "FirstA";
		String qFirstB = "FirstB";
		
		Long qSecondId = 2L;
		Long qSecondAuthor = 1L;
		Calendar qSecondCal = Calendar.getInstance();
		qSecondCal.set(Calendar.YEAR, 2010);
		qSecondCal.set(Calendar.MONTH, 0);
		qSecondCal.set(Calendar.DATE, 1);
		String qSecondQuestion = "Second!";
		String qSecondA = "SecondA";
		String qSecondB = "SecondB";
		
		List<QuestionDto> toUpdate = new LinkedList<QuestionDto>();
		
		// Update questions.
		QuestionDto qFirst = questionDao.getById(qFirstId);
		qFirst.setCreated(qFirstCal.getTime());
		qFirst.setAuthor(qFirstAuthor);
		qFirst.setQuestion(qFirstQuestion);
		qFirst.setAnswerA(qFirstA);
		qFirst.setAnswerB(qFirstB);
		
		QuestionDto qSecond = questionDao.getById(qSecondId);
		qSecond.setCreated(qSecondCal.getTime());
		qSecond.setAuthor(qSecondAuthor);
		qSecond.setQuestion(qSecondQuestion);
		qSecond.setAnswerA(qSecondA);
		qSecond.setAnswerB(qSecondB);
		
		toUpdate.add(qFirst);
		toUpdate.add(qSecond);
		questionDao.update(toUpdate);
		
		QuestionDto firstUpdated = questionDao.getById(qFirstId);
		assertNotNull(firstUpdated);
		assertEquals(qFirstId, firstUpdated.getId());
		Calendar firstUpdatedCal = Calendar.getInstance();
		firstUpdatedCal.setTime(firstUpdated.getCreated());
		assertEquals(qFirstCal.get(Calendar.YEAR), firstUpdatedCal.get(Calendar.YEAR));
		assertEquals(qFirstCal.get(Calendar.MONTH), firstUpdatedCal.get(Calendar.MONTH));
		assertEquals(qFirstCal.get(Calendar.DATE), firstUpdatedCal.get(Calendar.DATE));
		assertEquals(qFirstAuthor, firstUpdated.getAuthor());
		assertEquals(qFirstQuestion, firstUpdated.getQuestion());
		assertEquals(qFirstA, firstUpdated.getAnswerA());
		assertEquals(qFirstB, firstUpdated.getAnswerB());
		
		QuestionDto secondUpdated = questionDao.getById(qSecondId);
		assertNotNull(secondUpdated);
		assertEquals(qSecondId, secondUpdated.getId());
		Calendar secondUpdatedCal = Calendar.getInstance();
		secondUpdatedCal.setTime(secondUpdated.getCreated());
		assertEquals(qSecondCal.get(Calendar.YEAR), secondUpdatedCal.get(Calendar.YEAR));
		assertEquals(qSecondCal.get(Calendar.MONTH), secondUpdatedCal.get(Calendar.MONTH));
		assertEquals(qSecondCal.get(Calendar.DATE), secondUpdatedCal.get(Calendar.DATE));
		assertEquals(qSecondAuthor, secondUpdated.getAuthor());
		assertEquals(qSecondQuestion, secondUpdated.getQuestion());
		assertEquals(qSecondA, secondUpdated.getAnswerA());
		assertEquals(qSecondB, secondUpdated.getAnswerB());
		
		try {
			questionDao.update((List<QuestionDto>)null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testDelete() {
		// Delete an existent question.
		replyDao.deleteQuestionReplies(1L);
		questionDao.delete(1L);
		try {
			questionDao.getById(1L);
			fail();
		} catch (Exception e){
			assertTrue(true);
		}
		
		// Delete a inexistent question.
		questionDao.delete(9999L);
		try {
			questionDao.getById(9999L);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		
		// Exception testing.
		try {
			questionDao.delete(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetMatchingQuestionsByPair() {
		List<QuestionDto> questions = questionDao.getMatchingQuestionsByPair(1L, 2L);
		assertNotNull(questions);
		assertEquals(1, questions.size());
		
		QuestionDto question = questionDao.getById(1L);
		QuestionDto matchingQuestion = questions.get(0);
		assertEquals(question.getId(), matchingQuestion.getId());
		assertEquals(question.getAuthor(), matchingQuestion.getAuthor());
		assertEquals(question.getQuestion(), matchingQuestion.getQuestion());
		assertEquals(question.getAnswerA(), matchingQuestion.getAnswerA());
		assertEquals(question.getAnswerB(), matchingQuestion.getAnswerB());
		assertEquals(question.getAnswerC(), matchingQuestion.getAnswerC());
		assertEquals(question.getAnswerD(), matchingQuestion.getAnswerD());
		assertEquals(question.getAnswerE(), matchingQuestion.getAnswerE());
		
		questions = null;
		questions = questionDao.getMatchingQuestionsByPair(1L, 6L);
		assertNotNull(questions);
		assertEquals(0, questions.size());
		
		questions = null;
		questions = questionDao.getMatchingQuestionsByPair(1L, 9999L);
		assertNotNull(questions);
		assertEquals(0, questions.size());
		
		questions = null;
		questions = questionDao.getMatchingQuestionsByPair(9999L, 1L);
		assertNotNull(questions);
		assertEquals(0, questions.size());
		
		questions = null;
		questions = questionDao.getMatchingQuestionsByPair(9999L, 9999L);
		assertNotNull(questions);
		assertEquals(0, questions.size());
		
		try {
			questionDao.getMatchingQuestionsByPair(null, 1L);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionDao.getMatchingQuestionsByPair(1L, null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionDao.getMatchingQuestionsByPair(null, null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetNumberOfMatchingQuestionsByPair() {
		
		Long idPersonA = 3L;
		Long idPersonB = 5L;
		
		int nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonA, idPersonB, null, null, null);
		assertEquals(2, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonA, idPersonB, false, null, null);
		assertEquals(1, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonA, idPersonB, null, false, null);
		assertEquals(1, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonA, idPersonB, null, null, false);
		assertEquals(1, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonA, idPersonB, false, false, null);
		assertEquals(1, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonA, idPersonB, false, null, false);
		assertEquals(0, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonA, idPersonB, false, false, false);
		assertEquals(0, nofMatchingQuestions);
		
		
		// It should be bidirectional.
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonB, idPersonA, null, null, null);
		assertEquals(2, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonB, idPersonA, false, null, null);
		assertEquals(1, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonB, idPersonA, null, false, null);
		assertEquals(1, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonB, idPersonA, null, null, false);
		assertEquals(1, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonB, idPersonA, false, false, null);
		assertEquals(1, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonB, idPersonA, false, null, false);
		assertEquals(0, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonB, idPersonA, false, false, false);
		assertEquals(0, nofMatchingQuestions);
		
		
		// Persons without replies.
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(8L, idPersonB, null, null, null);
		assertEquals(0, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonA, 8L, null, null, null);
		assertEquals(0, nofMatchingQuestions);
		
		
		// Not existing persons.
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(9999L, idPersonB, null, null, null);
		assertEquals(0, nofMatchingQuestions);
		
		nofMatchingQuestions = 0;
		nofMatchingQuestions = questionDao.getNumberOfMatchingQuestionsByPair(idPersonA, 9999L, null, null, null);
		assertEquals(0, nofMatchingQuestions);
		
		
	}
	
}
