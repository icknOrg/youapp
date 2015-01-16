package youapp.dataaccess.dao.jdbc;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import youapp.dataaccess.dto.NameDto;
import youapp.dataaccess.dto.PersonDto;
import youapp.dataaccess.dto.QuestionDto;
import youapp.dataaccess.dto.ReplyDto;
import youapp.dataaccess.dto.TagDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml", "classpath:dao-context.xml", "classpath:service-context.xml" })
public class JdbcPersonDaoTest {

	private ApplicationContext applicationContext;

	private JdbcDbInitializer jdbcDbInitializer;
	
	private JdbcPersonDao personDao;
	
	private JdbcTagDao tagDao;
	
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
		personDao = (JdbcPersonDao)applicationContext.getBean("personDao", JdbcPersonDao.class);
		tagDao = (JdbcTagDao)applicationContext.getBean("tagDao", JdbcTagDao.class);
		questionDao = (JdbcQuestionDao)applicationContext.getBean("questionDao", JdbcQuestionDao.class);
		replyDao = (JdbcReplyDao)applicationContext.getBean("replyDao", JdbcReplyDao.class);
	}
	
	@After
	public void tearDown() {
		personDao = null;
		tagDao = null;
		questionDao = null;
		replyDao = null;
	}
	
	@Test
    @Ignore
	public void testGetAllFriends() {
		List<PersonDto> friends = personDao.getAllFriends(5L, 0, 10, true);
		assertEquals(2, friends.size());
		int counter = 0;
		boolean found4L = false;
		boolean found6L = false;
		for (PersonDto person : friends) {
			if (person.getId() == 4L) {
				found4L = true;
			}
			if (person.getId() == 6L) {
				found6L = true;
			}
			counter = counter + 1;
		}
		assertEquals(2, counter);
		assertTrue(found4L);
		assertTrue(found6L);
		
		friends = null;
		friends = personDao.getAllFriends(5L, 0, 10, false);
		assertEquals(1, friends.size());
		assertEquals(new Long(4L), friends.get(0).getId());
	}
	
	@Test
    @Ignore
	public void testGetRecentFriends() {
		List<PersonDto> friends = personDao.getRecentFriends(5L, 10, true);
		assertEquals(2, friends.size());
		int counter = 0;
		boolean found4L = false;
		boolean found6L = false;
		for (PersonDto person : friends) {
			if (person.getId() == 4L) {
				found4L = true;
			}
			if (person.getId() == 6L) {
				found6L = true;
			}
			counter = counter + 1;
		}
		assertEquals(2, counter);
		assertTrue(found4L);
		assertTrue(found6L);
		assertEquals(new Long(4L), friends.get(0).getId());
		assertEquals(new Long(6L), friends.get(1).getId());
		
		friends = null;
		friends = personDao.getRecentFriends(5L, 1, true);
		assertEquals(1, friends.size());
		assertEquals(new Long(4L), friends.get(0).getId());
		
		friends = null;
		friends = personDao.getRecentFriends(5L, 10, false);
		assertEquals(1, friends.size());
		assertEquals(new Long(4L), friends.get(0).getId());
		
		friends = null;
		friends = personDao.getRecentFriends(5L, 1, false);
		assertEquals(1, friends.size());
		assertEquals(new Long(4L), friends.get(0).getId());
	}
	
	@Test
	public void testGetName() {
		// Check activated person (nick must be available).
		PersonDto person = personDao.getById(1L);
		NameDto name = personDao.getName(1L);
		assertEquals(person.getId(), name.getPersonId());
		assertEquals(person.getFirstName(), name.getFirstName());
		assertEquals(person.getLastName(), name.getLastName());
		assertEquals(person.getNickName(), name.getNickName());
		assertNotNull(name.getNickName());
		
		// Check deactivated person (nick must be null).
		person = personDao.getById(8L);
		name = personDao.getName(8L);
		assertEquals(person.getId(), name.getPersonId());
		assertEquals(person.getFirstName(), name.getFirstName());
		assertEquals(person.getLastName(), name.getLastName());
		assertNull(name.getNickName());
		
		// Exception testing.
		try {
			personDao.getName(9999L);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		
		try {
			personDao.getName(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetNickName() {
		// Check activated person (nick must be available).
		PersonDto person = personDao.getById(1L);
		String nick = personDao.getNickName(1L);
		assertEquals(person.getNickName(), nick);
		
		// Check deactivated person (nick must be null).
		person = personDao.getById(8L);
		nick = personDao.getNickName(8L);
		assertEquals(person.getNickName(), nick);
		assertNull(nick);
		
		// Exception testing.
		try {
			personDao.getNickName(9999L);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		
		try {
			personDao.getNickName(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
    @Ignore
	public void testDeactivate() {
		// Deactivate existent person.
		long personId = 1L;
		PersonDto person = personDao.getById(personId);
		assertNotNull(person);
		// Tags
		List<TagDto> medications = tagDao.getByPerson(personId);
		assertNotNull(medications);
		// Questions
		List<QuestionDto> questions = questionDao.getByAuthor(personId);
		assertNotNull(questions);
		// Replies
		List<ReplyDto> replies = replyDao.getByPerson(personId, null, null, null);
		assertNotNull(replies);
		// Deactivate
		personDao.deactivate(personId);
		PersonDto deactivated = personDao.getById(personId);
		assertNotNull(deactivated);
		assertEquals(person.getId(), deactivated.getId());
		assertEquals(person.getAccessLevel(), deactivated.getAccessLevel());
		assertEquals(person.getFbId(), deactivated.getFbId());
		assertEquals(person.getFirstName(), deactivated.getFirstName());
		assertEquals(person.getLastName(), deactivated.getLastName());
		assertEquals(person.getGender(), deactivated.getGender());
		assertNull(deactivated.getNickName());
		assertNull(deactivated.getMemberSince());
		assertNull(deactivated.getLastOnline());
		assertNull(deactivated.getBirthday());
		/*assertNull(deactivated.getCountry());
		assertNull(deactivated.getState());
		assertNull(deactivated.getCity());*/
		assertNull(deactivated.getEducation());
		assertNull(deactivated.getJob());
		assertNull(deactivated.getDiet());
		assertNull(deactivated.getDietStrict());
		assertNull(deactivated.getReligion());
		assertNull(deactivated.getReligionSerious());
		assertNull(deactivated.getSign());
		assertNull(deactivated.getSignSerious());
		
		// Deactivate inexistent person.
		try {
			personDao.getByFbId(9999L);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		personDao.deactivate(9999L);
		try {
			personDao.getByFbId(9999L);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		
		// Exception testing.
		try {
			personDao.deactivate(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
}
