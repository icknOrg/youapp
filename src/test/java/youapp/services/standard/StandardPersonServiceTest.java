package youapp.services.standard;


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
import youapp.SolrIndexInitializer;
import youapp.dataaccess.dao.ISearchableQuestionDao;
import youapp.dataaccess.dao.solr.SolrSearchableQuestionDao;
import youapp.model.Name;
import youapp.model.Person;
import youapp.model.Question;
import youapp.model.Reply;
import youapp.model.TagSet;
import youapp.services.PersonService;
import youapp.services.QuestionService;
import youapp.services.TagService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml", "classpath:dao-context.xml", "classpath:service-context.xml" })
public class StandardPersonServiceTest {

	private ApplicationContext applicationContext;

	private JdbcDbInitializer jdbcDbInitializer;
	
	//private SolrIndexInitializer solrIndexInitializer;

	private PersonService personService;
	
	private TagService tagService;
	
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
	
	@Autowired
	public void setSolrIndexInitializer(SolrIndexInitializer solrIndexInitializer) {
		//this.solrIndexInitializer = solrIndexInitializer;
	}

	@Before
	public void setUp() throws Exception {
		jdbcDbInitializer.resetDatabase();
		//solrIndexInitializer.resetIndex();
		personService = (PersonService)applicationContext.getBean("personService", PersonService.class);
		tagService = (TagService)applicationContext.getBean("tagService", TagService.class);
		questionService = (QuestionService)applicationContext.getBean("questionService", QuestionService.class);
		searchableQuestionDao = (SolrSearchableQuestionDao)applicationContext.getBean("searchableQuestionDao", SolrSearchableQuestionDao.class);
	}

	@After
	public void tearDown() {
		personService = null;
		tagService = null;
		questionService = null;
		searchableQuestionDao = null;
	}

	@Test
	@Ignore
	public void testGetAllFriends() throws Exception {
		List<Person> friends = personService.getAllFriends(5L, 0, 10, true);
		assertEquals(2, friends.size());
		int counter = 0;
		boolean found4L = false;
		boolean found6L = false;
		for (Person person : friends) {
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
		friends = personService.getAllFriends(5L, 0, 10, false);
		assertEquals(1, friends.size());
		assertEquals(new Long(4L), friends.get(0).getId());
	}

	@Test
    @Ignore
	public void testGetRecentFriends() throws Exception {
		List<Person> friends = personService.getRecentFriends(5L, 10, true);
		assertEquals(2, friends.size());
		int counter = 0;
		boolean found4L = false;
		boolean found6L = false;
		for (Person person : friends) {
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
		friends = personService.getRecentFriends(5L, 1, true);
		assertEquals(1, friends.size());
		assertEquals(new Long(4L), friends.get(0).getId());

		friends = null;
		friends = personService.getRecentFriends(5L, 10, false);
		assertEquals(1, friends.size());
		assertEquals(new Long(4L), friends.get(0).getId());

		friends = null;
		friends = personService.getRecentFriends(5L, 1, false);
		assertEquals(1, friends.size());
		assertEquals(new Long(4L), friends.get(0).getId());
	}

	@Test
	public void testGetName() throws Exception {
		// Check activated person (nick must be available).
		Person person = personService.getById(1L);
		Name name = personService.getName(1L);
		assertEquals(person.getId(), name.getPersonId());
		assertEquals(person.getFirstName(), name.getFirstName());
		assertEquals(person.getLastName(), name.getLastName());
		assertEquals(person.getNickName(), name.getNickName());
		assertNotNull(name.getNickName());

		// Check deactivated person (nick must be null).
		person = personService.getById(8L);
		name = personService.getName(8L);
		assertEquals(person.getId(), name.getPersonId());
		assertEquals(person.getFirstName(), name.getFirstName());
		assertEquals(person.getLastName(), name.getLastName());
		assertNull(name.getNickName());

		// Retrieving an inexistent's person's name must not throw an exception!
		name = null;
		name = personService.getName(9999L);
		assertNull(name);

		// Exception testing.
		try {
			personService.getName(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetNickName() throws Exception {
		// Check activated person (nick must be available).
		Person person = personService.getById(1L);
		String nick = personService.getNickName(1L);
		assertEquals(person.getNickName(), nick);

		// Check deactivated person (nick must be null).
		person = personService.getById(8L);
		nick = personService.getNickName(8L);
		assertEquals(person.getNickName(), nick);
		assertNull(nick);

		// Retrieving an inexistent's person's nick must not throw an exception!
		nick = null;
		nick = personService.getNickName(9999L);
		assertNull(nick);

		// Exception testing.
		try {
			personService.getNickName(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	@Ignore
	public void testDeactivate() throws Exception {
		// Deactivate existent person.
		Long personId = 1L;
		Person person = personService.getById(personId);
		assertNotNull(person);
		// Tags
		TagSet tags = tagService.getByPerson(personId);
		assertNotNull(tags);
		// Questions
		List<Question> questions = questionService.getQuestionsByAuthor(personId);
		assertNotNull(questions);
		// Replies
		List<Reply> replies = questionService.getRepliesByPerson(personId, false, null, null, null);
		assertNotNull(replies);
		
		// Deactivate
		personService.deactivate(personId);
		Person deactivated = personService.getById(personId);
		assertNotNull(deactivated);
		assertEquals(person.getId(), deactivated.getId());
		assertEquals(person.getAccessLevel().getId(), deactivated.getAccessLevel().getId());
		assertEquals(person.getFbId(), deactivated.getFbId());
		assertEquals(person.getFirstName(), deactivated.getFirstName());
		assertEquals(person.getLastName(), deactivated.getLastName());
		assertEquals(person.getGender(), deactivated.getGender());
		assertNull(deactivated.getNickName());
		assertNull(deactivated.getMemberSince());
		assertNull(deactivated.getLastOnline());
		assertNull(deactivated.getBirthday());
		/*
		assertNull(deactivated.getCountry());
		assertNull(deactivated.getState());
		assertNull(deactivated.getCity());*/
		
		// Tags
		tags = null;
		tags = tagService.getByPerson(personId);
		assertNotNull(tags);
		assertEquals(0, tags.size());
		// Questions
		questions = null;
		questions = questionService.getQuestionsByAuthor(personId);
		assertNotNull(questions);
		assertThat(questions.size(), greaterThan(0));
		// Replies
		replies = null;
		replies = questionService.getRepliesByPerson(personId, false, null, null, null);
		assertNotNull(replies);
		assertEquals(0, replies.size());
		
		// Deactivate inexistent person.
		personService.deactivate(9999L);
		
		// Exception testing.
		try {
			personService.deactivate(null);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
}
