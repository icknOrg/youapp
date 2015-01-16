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
import youapp.dataaccess.dto.ReplyDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml", "classpath:dao-context.xml", "classpath:service-context.xml" })
public class JdbcReplyDaoTest {

	private ApplicationContext applicationContext;

	private JdbcDbInitializer jdbcDbInitializer;
	
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
		replyDao = (JdbcReplyDao)applicationContext.getBean("replyDao", JdbcReplyDao.class);
	}
	
	@After
	public void tearDown() {
		replyDao = null;
	}
	
	@Test
	public void testGetById() {
		// Check for existent reply.
		ReplyDto reply = replyDao.getById(1L, 1L);
		assertNotNull(reply);
		assertEquals(new Long(1L), reply.getPersonId());
		assertEquals(new Long(1L), reply.getQuestionId());
		Calendar cal = Calendar.getInstance();
		cal.setTime(reply.getLastUpdate());
		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(3, cal.get(Calendar.MONTH));
		assertEquals(15, cal.get(Calendar.DATE));
		assertFalse(reply.getSkipped());
		assertFalse(reply.getInPrivate());
		assertFalse(reply.getCritical());
		assertEquals(new Integer(50), reply.getImportance());
		assertNull(reply.getExplanation());
		assertTrue(reply.getAnswerA());
		assertFalse(reply.getAnswerB());
		assertFalse(reply.getAnswerC());
		assertFalse(reply.getAnswerD());
		assertFalse(reply.getAnswerE());
		
		// Check for inexistent reply.
		try {
			replyDao.getById(1L, 2L);
		} catch (Exception e) {
			assertTrue(true);
		}
		
		// Create new reply and repeat the above test.
		ReplyDto rCreated = new ReplyDto();
		rCreated.setPersonId(1L);
		rCreated.setQuestionId(2L);
		rCreated.setLastUpdate(new Date(System.currentTimeMillis()));
		rCreated.setSkipped(false);
		rCreated.setInPrivate(false);
		rCreated.setCritical(false);
		rCreated.setImportance(50);
		rCreated.setAnswerA(true);
		rCreated.setAnswerB(false);
		rCreated.setAnswerC(false);
		rCreated.setAnswerD(false);
		rCreated.setAnswerE(false);
		replyDao.create(rCreated);
		ReplyDto rRetrieved = replyDao.getById(1L, 2L);
		assertEquals(rCreated.getPersonId(), rRetrieved.getPersonId());
		assertEquals(rCreated.getQuestionId(), rRetrieved.getQuestionId());
		Calendar calCreated = Calendar.getInstance();
		calCreated.setTime(rCreated.getLastUpdate());
		Calendar calRetrieved = Calendar.getInstance();
		calRetrieved.setTime(rRetrieved.getLastUpdate());
		assertEquals(calCreated.get(Calendar.YEAR), calRetrieved.get(Calendar.YEAR));
		assertEquals(calCreated.get(Calendar.MONTH), calRetrieved.get(Calendar.MONTH));
		assertEquals(calCreated.get(Calendar.DATE), calRetrieved.get(Calendar.DATE));
		assertEquals(rCreated.getSkipped(), rRetrieved.getSkipped());
		assertEquals(rCreated.getInPrivate(), rRetrieved.getInPrivate());
		assertEquals(rCreated.getCritical(), rRetrieved.getCritical());
		assertEquals(rCreated.getImportance(), rRetrieved.getImportance());
		assertEquals(rCreated.getAnswerA(), rRetrieved.getAnswerA());
		assertEquals(rCreated.getAnswerB(), rRetrieved.getAnswerB());
		assertEquals(rCreated.getAnswerC(), rRetrieved.getAnswerC());
		assertEquals(rCreated.getAnswerD(), rRetrieved.getAnswerD());
		assertEquals(rCreated.getAnswerE(), rRetrieved.getAnswerE());
		
		// Exception testing.
		try {
			replyDao.getById(null, 1L);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getById(1L, null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getById(null, null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getById(9999L, 1L);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getById(1L, 9999L);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getById(9999L, 9999L);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetByIds() {
		List<Long> questionIds = new LinkedList<Long>();
		questionIds.add(1L);
		List<ReplyDto> replies = replyDao.getByIds(1L, questionIds);
		assertNotNull(replies);
		assertEquals(1, replies.size());
		
		ReplyDto foundReply = replies.get(0);
		ReplyDto reply = replyDao.getById(1L, 1L);
		assertEquals(reply.getPersonId(), foundReply.getPersonId());
		assertEquals(reply.getQuestionId(), foundReply.getQuestionId());
		assertEquals(reply.getSkipped(), foundReply.getSkipped());
		assertEquals(reply.getInPrivate(), foundReply.getInPrivate());
		assertEquals(reply.getCritical(), foundReply.getCritical());
		assertEquals(reply.getAnswerA(), foundReply.getAnswerA());
		assertEquals(reply.getAnswerB(), foundReply.getAnswerB());
		assertEquals(reply.getAnswerC(), foundReply.getAnswerC());
		assertEquals(reply.getAnswerD(), foundReply.getAnswerD());
		assertEquals(reply.getAnswerE(), foundReply.getAnswerE());
		
		replies = null;
		questionIds.clear();
		questionIds.add(3L);
		replies = replyDao.getByIds(1L, questionIds);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		replies = null;
		questionIds.clear();
		questionIds.add(null);
		questionIds.add(1L);
		replies = replyDao.getByIds(1L, questionIds);
		assertNotNull(replies);
		assertEquals(1, replies.size());
		assertEquals(new Long(1L), replies.get(0).getQuestionId());
		
		replies = null;
		questionIds.clear();
		questionIds.add(9999L);
		replies = replyDao.getByIds(1L, questionIds);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		try {
			questionIds.clear();
			questionIds.add(1L);
			replyDao.getByIds(null, questionIds);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			questionIds.clear();
			questionIds.add(1L);
			replyDao.getByIds(1L, new LinkedList<Long>());
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByIds(1L, null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByIds(null, null);
			fail();
		} catch (Exception e){
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testGetByPerson() {
		// Check for person with existent replies.
		List<ReplyDto> replies = replyDao.getByPerson(1L, null, null, null);
		assertNotNull(replies);
		assertEquals(1, replies.size());
		assertEquals(new Long(1L), replies.get(0).getPersonId());
		assertEquals(new Long(1L), replies.get(0).getQuestionId());
		
		// Add new reply for the person and check again.
		replies = null;
		ReplyDto rCreated = new ReplyDto();
		rCreated.setPersonId(1L);
		rCreated.setQuestionId(2L);
		rCreated.setLastUpdate(new Date(System.currentTimeMillis()));
		rCreated.setSkipped(false);
		rCreated.setInPrivate(false);
		rCreated.setCritical(false);
		rCreated.setImportance(50);
		rCreated.setAnswerA(true);
		rCreated.setAnswerB(false);
		rCreated.setAnswerC(false);
		rCreated.setAnswerD(false);
		rCreated.setAnswerE(false);
		replyDao.create(rCreated);
		replies = replyDao.getByPerson(1L, null, null, null);
		assertNotNull(replies);
		assertEquals(2, replies.size());
		assertEquals(new Long(1L), replies.get(0).getPersonId());
		assertEquals(new Long(1L), replies.get(1).getPersonId());
		assertEquals(new Long(1L), replies.get(0).getQuestionId());
		assertEquals(new Long(2L), replies.get(1).getQuestionId());
		
		// Check for person without replies.
		replies = null;
		replies = replyDao.getByPerson(7L, null, null, null);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Check for inexistent person.
		replies = null;
		replies = replyDao.getByPerson(9999L, null, null, null);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Exception testing.
		try {
			replyDao.getByPerson(null, null, null, null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetByPersonLimited() {
		// Check for person with existent replies.
		List<ReplyDto> replies = replyDao.getByPerson(1L, 0, 10);
		assertNotNull(replies);
		assertEquals(1, replies.size());
		assertEquals(new Long(1L), replies.get(0).getPersonId());
		assertEquals(new Long(1L), replies.get(0).getQuestionId());
		
		// Add new reply for the person and check again.
		replies = null;
		ReplyDto rCreated = new ReplyDto();
		rCreated.setPersonId(1L);
		rCreated.setQuestionId(2L);
		rCreated.setLastUpdate(new Date(System.currentTimeMillis()));
		rCreated.setSkipped(false);
		rCreated.setInPrivate(false);
		rCreated.setCritical(false);
		rCreated.setImportance(50);
		rCreated.setAnswerA(true);
		rCreated.setAnswerB(false);
		rCreated.setAnswerC(false);
		rCreated.setAnswerD(false);
		rCreated.setAnswerE(false);
		replyDao.create(rCreated);
		replies = replyDao.getByPerson(1L, 0, 10);
		assertNotNull(replies);
		assertEquals(2, replies.size());
		assertEquals(new Long(1L), replies.get(0).getPersonId());
		assertEquals(new Long(1L), replies.get(1).getPersonId());
		assertEquals(new Long(1L), replies.get(0).getQuestionId());
		assertEquals(new Long(2L), replies.get(1).getQuestionId());
		
		// Check for person with existing replies with different offset.
		replies = null;
		replies = replyDao.getByPerson(1L, 1, 10);
		assertNotNull(replies);
		assertEquals(1, replies.size());
		assertEquals(new Long(1L), replies.get(0).getPersonId());
		assertEquals(new Long(2L), replies.get(0).getQuestionId());
		
		// Check for person without replies.
		replies = null;
		replies = replyDao.getByPerson(7L, 0, 10);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Check for inexistent person.
		replies = null;
		replies = replyDao.getByPerson(9999L, 0, 10);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Exception testing.
		try {
			replyDao.getByPerson(null, 0, 1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByPerson(null, -1, 1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByPerson(null, -1, 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByPerson(null, -1, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByPerson(null, 0, 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByPerson(null, 0, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByPerson(1L, -1, 1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByPerson(1L, -1, 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByPerson(1L, -1, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByPerson(1L, 0, 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByPerson(1L, 0, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetByQuestion() {
		
		// Retrieve replies for existent question with existing replies.
		List<ReplyDto> replies = replyDao.getByQuestion(1L);
		assertNotNull(replies);
		assertEquals(3, replies.size());
		assertEquals(new Long(1L), replies.get(0).getPersonId());
		assertEquals(new Long(2L), replies.get(1).getPersonId());
		assertEquals(new Long(3L), replies.get(2).getPersonId());
		assertEquals(new Long(1L), replies.get(0).getQuestionId());
		assertEquals(new Long(1L), replies.get(1).getQuestionId());
		assertEquals(new Long(1L), replies.get(2).getQuestionId());
		
		// Add new reply for the question and check again.
		replies = null;
		ReplyDto rCreated = new ReplyDto();
		rCreated.setPersonId(4L);
		rCreated.setQuestionId(1L);
		rCreated.setLastUpdate(new Date(System.currentTimeMillis()));
		rCreated.setSkipped(false);
		rCreated.setInPrivate(false);
		rCreated.setCritical(false);
		rCreated.setImportance(50);
		rCreated.setAnswerA(true);
		rCreated.setAnswerB(false);
		rCreated.setAnswerC(false);
		rCreated.setAnswerD(false);
		rCreated.setAnswerE(false);
		replyDao.create(rCreated);
		replies = replyDao.getByQuestion(1L);
		assertNotNull(replies);
		assertEquals(4, replies.size());
		assertEquals(new Long(1L), replies.get(0).getPersonId());
		assertEquals(new Long(2L), replies.get(1).getPersonId());
		assertEquals(new Long(3L), replies.get(2).getPersonId());
		assertEquals(new Long(4L), replies.get(3).getPersonId());
		assertEquals(new Long(1L), replies.get(0).getQuestionId());
		assertEquals(new Long(1L), replies.get(1).getQuestionId());
		assertEquals(new Long(1L), replies.get(2).getQuestionId());
		assertEquals(new Long(1L), replies.get(3).getQuestionId());
		
		// Retrieve replies for existent question without replies.
		replies = null;
		replies = replyDao.getByQuestion(4L);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Retrieve replies for inexistent question.
		replies = null;
		replies = replyDao.getByQuestion(9999L);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Exception testing.
		try {
			replyDao.getByQuestion(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetByQuestionLimited() {
		
		// Retrieve replies for existent question with existing replies.
		List<ReplyDto> replies = replyDao.getByQuestion(1L, 0, 10);
		assertNotNull(replies);
		assertEquals(3, replies.size());
		assertEquals(new Long(1L), replies.get(0).getPersonId());
		assertEquals(new Long(2L), replies.get(1).getPersonId());
		assertEquals(new Long(3L), replies.get(2).getPersonId());
		assertEquals(new Long(1L), replies.get(0).getQuestionId());
		assertEquals(new Long(1L), replies.get(1).getQuestionId());
		assertEquals(new Long(1L), replies.get(2).getQuestionId());
		
		// Add new reply for the question and check again.
		replies = null;
		ReplyDto rCreated = new ReplyDto();
		rCreated.setPersonId(4L);
		rCreated.setQuestionId(1L);
		rCreated.setLastUpdate(new Date(System.currentTimeMillis()));
		rCreated.setSkipped(false);
		rCreated.setInPrivate(false);
		rCreated.setCritical(false);
		rCreated.setImportance(50);
		rCreated.setAnswerA(true);
		rCreated.setAnswerB(false);
		rCreated.setAnswerC(false);
		rCreated.setAnswerD(false);
		rCreated.setAnswerE(false);
		replyDao.create(rCreated);
		replies = replyDao.getByQuestion(1L, 0, 10);
		assertNotNull(replies);
		assertEquals(4, replies.size());
		assertEquals(new Long(1L), replies.get(0).getPersonId());
		assertEquals(new Long(2L), replies.get(1).getPersonId());
		assertEquals(new Long(3L), replies.get(2).getPersonId());
		assertEquals(new Long(4L), replies.get(3).getPersonId());
		assertEquals(new Long(1L), replies.get(0).getQuestionId());
		assertEquals(new Long(1L), replies.get(1).getQuestionId());
		assertEquals(new Long(1L), replies.get(2).getQuestionId());
		assertEquals(new Long(1L), replies.get(3).getQuestionId());
		
		// Retrieve for person with existing replies with different offset.
		replies = null;
		replies = replyDao.getByQuestion(1L, 2, 10);
		assertNotNull(replies);
		assertEquals(2, replies.size());
		assertEquals(new Long(3L), replies.get(0).getPersonId());
		assertEquals(new Long(4L), replies.get(1).getPersonId());
		assertEquals(new Long(1L), replies.get(0).getQuestionId());
		assertEquals(new Long(1L), replies.get(1).getQuestionId());
		
		// Retrieve replies for existent question without replies.
		replies = null;
		replies = replyDao.getByQuestion(4L, 0, 10);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Retrieve replies for inexistent question.
		replies = null;
		replies = replyDao.getByQuestion(9999L, 0, 10);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Exception testing.
		try {
			replyDao.getByQuestion(null, 0, 1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByQuestion(null, -1, 1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByQuestion(null, -1, 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByQuestion(null, -1, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByQuestion(null, 0, 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByQuestion(null, 0, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByQuestion(1L, -1, 1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByQuestion(1L, -1, 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByPerson(1L, -1, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByQuestion(1L, 0, 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			replyDao.getByQuestion(1L, 0, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testExists() {
		assertTrue(replyDao.exists(1L, 1L));
		assertTrue(replyDao.exists(2L, 1L));
		assertTrue(replyDao.exists(3L, 1L));
		assertFalse(replyDao.exists(4L, 1L));
	}
	
	@Test
	public void testGetMostRecentByDays() {
		// Calculate number of days between now and the most recent reply.
		ReplyDto mostRecent = replyDao.getById(6L, 2L);
		long days = (System.currentTimeMillis() - mostRecent.getLastUpdate().getTime())/(1000 * 60 * 60 * 24);
		
		// Check for existent replies.
		List<ReplyDto> replies = replyDao.getMostRecentByDays((int)days, false);
		assertEquals(1, replies.size());
		assertEquals(false, replies.get(0).getInPrivate());
		
		replies = null;
		replies = replyDao.getMostRecentByDays((int)days, true);
		assertEquals(1, replies.size());
		assertEquals(false, replies.get(0).getInPrivate());
		
		// Check for existent replies with different privacy settings.
		replies = null;
		replies = replyDao.getMostRecentByDays((int)days + 25, false);
		assertEquals(2, replies.size());
		assertEquals(true, replies.get(0).getInPrivate());
		assertEquals(false, replies.get(1).getInPrivate());
		
		replies = null;
		replies = replyDao.getMostRecentByDays((int)days + 25, true);
		assertEquals(1, replies.size());
		assertEquals(false, replies.get(0).getInPrivate());
		
		// Exception testing.
		try {
			replyDao.getMostRecentByDays(-1, false);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			replyDao.getMostRecentByDays(-2, true);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetMostRecentBySize() {
		List<ReplyDto> replies = replyDao.getMostRecentBySize(2, false);
		assertEquals(2, replies.size());
		ReplyDto r = replies.get(0);
		assertEquals(false, r.getInPrivate());
		assertEquals(new Long(6L), r.getPersonId());
		assertEquals(new Long(2L), r.getQuestionId());
		r = replies.get(1);
		assertEquals(true, r.getInPrivate());
		assertEquals(new Long(5L), r.getPersonId());
		assertEquals(new Long(2L), r.getQuestionId());
		
		replies = null;
		r = null;
		replies = replyDao.getMostRecentBySize(2, true);
		assertEquals(2, replies.size());
		r = replies.get(0);
		assertEquals(false, r.getInPrivate());
		assertEquals(new Long(6L), r.getPersonId());
		assertEquals(new Long(2L), r.getQuestionId());
		r = replies.get(1);
		assertEquals(false, r.getInPrivate());
		assertEquals(new Long(2L), r.getPersonId());
		assertEquals(new Long(1L), r.getQuestionId());
		
		// Exception testing.
		try {
			replyDao.getMostRecentBySize(0, true);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			replyDao.getMostRecentBySize(0, false);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			replyDao.getMostRecentBySize(-1, true);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}
		try {
			replyDao.getMostRecentBySize(-1, false);
			fail();
		} catch (Exception e) {
			// Exception expected.
			assertTrue(true);
		}	
	}
	
	@Test
	public void testGetByDate() {
		// TODO
	}
	
	@Test
	public void testGetByDateLimited() {
		// TODO
	}
	
	@Test
	public void testGetByDatePerson() {
		// TODO
	}
	
	@Test
	public void testGetAll() {
		// TODO
	}
	
	@Test
	public void testCreate() {
		// TODO
	}
	
	@Test
	public void testEdit() {
		// TODO
	}
	
	@Test
	public void testDelete() {
		// TODO
	}
	
	@Test
	public void testDeletePersonReplies() {
		// TODO
	}
	
	@Test
	public void testDeleteQuestionReplies() {
		// Delete replies of a question which has replies.
		Long questionId = 1L;
		List<ReplyDto> replies = replyDao.getByQuestion(questionId);
		assertNotNull(replies);
		assertEquals(3, replies.size());
		replyDao.deleteQuestionReplies(questionId);
		replies = null;
		replies = replyDao.getByQuestion(questionId);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Delete replies of a question which has no replies.
		questionId = 4L;
		replies = null;
		replies = replyDao.getByQuestion(questionId);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		replyDao.deleteQuestionReplies(questionId);
		replies = null;
		replies = replyDao.getByQuestion(questionId);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Exception testing.
		try {
			replyDao.deleteQuestionReplies(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetNumberOfRepliesByPerson() {
		long nofReplies = replyDao.getNumberOfRepliesByPerson(1L, null, null, null);
		assertEquals(1, nofReplies);
		
		nofReplies = 0;
		nofReplies = replyDao.getNumberOfRepliesByPerson(1L, false, null, null);
		assertEquals(1, nofReplies);
		
		nofReplies = 0;
		nofReplies = replyDao.getNumberOfRepliesByPerson(3L, null, null, null);
		assertEquals(3, nofReplies);
		
		nofReplies = 0;
		nofReplies = replyDao.getNumberOfRepliesByPerson(3L, false, null, null);
		assertEquals(2, nofReplies);
		
		nofReplies = replyDao.getNumberOfRepliesByPerson(9999L, null, null, null);
		assertEquals(0, nofReplies);
		
	}
	
}
