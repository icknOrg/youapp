package youapp.dataaccess.dao.jdbc;

import static org.junit.Assert.*;

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
import youapp.dataaccess.dao.IFacebookGroupDao;
import youapp.dataaccess.dto.FacebookGroupDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml", "classpath:dao-context.xml", "classpath:service-context.xml" })
public class JdbcFacebookGroupDaoTest {

	static final Log log = LogFactory.getLog(JdbcFacebookGroupDaoTest.class);

	private ApplicationContext applicationContext;

	private JdbcDbInitializer jdbcDbInitializer;
	
	private IFacebookGroupDao groupDao;
	
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
		groupDao = (JdbcFacebookGroupDao)applicationContext.getBean("facebookGroupDao", JdbcFacebookGroupDao.class);
	}
	
	@After
	public void tearDown() {
		groupDao = null;
	}
	
	@Test
	public void testGetById() {
		FacebookGroupDto group = groupDao.getById(1L, false);
		assertNotNull(group);
		assertEquals(new Long(1), group.getId());
		assertEquals(new Long(1111), group.getgId());
	}
	
	@Test
	public void testGetByPerson() {
		List<FacebookGroupDto> groups = groupDao.getByPerson(1L);
		assertNotNull(groups);
		assertEquals(2, groups.size());
		assertEquals(new Long(1), groups.get(0).getId());
		assertEquals(new Long(4), groups.get(1).getId());
	}
	
}
