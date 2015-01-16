package youapp.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import youapp.JdbcDbInitializer;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.model.FacebookGroup;
import youapp.services.FacebookGroupService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml", "classpath:dao-context.xml", "classpath:service-context.xml" })
public class StandardFacebookGroupTest {
	
	static final Log log = LogFactory.getLog(StandardFacebookGroupTest.class);

	private ApplicationContext applicationContext;

	private JdbcDbInitializer jdbcDbInitializer;
	
	private FacebookGroupService groupService;
	
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
		groupService = (StandardFacebookGroupService)applicationContext.getBean("facebookGroupService", StandardFacebookGroupService.class);
	}
	
	@After
	public void tearDown() {
		groupService = null;
	}
	
	@Test
	public void testGetById() {
		FacebookGroup group = groupService.getById(1L, false);
		assertNotNull(group);
		assertEquals(new Long(1), group.getId());
		assertEquals(new Long(1111), group.getgId());
	}
	
	@Test
	public void testGetByPerson() throws DataAccessLayerException {
		List<FacebookGroup> groups = groupService.getByPerson(1L, false);
		assertNotNull(groups);
		assertEquals(2, groups.size());
	}

}
