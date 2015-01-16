package youapp;


//import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import javax.sql.DataSource;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;

public class JdbcDbInitializer {

	public static final String TEST_DATASET_PATH = "test-dataset.xml";
	
	/**
	 * Logger.
	 */
	static final Log log = LogFactory.getLog(JdbcDbInitializer.class);
	
	/**
	 * Provides access to the database.
	 */
	private DataSource dataSource;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void resetDatabase() throws Exception {
		IDatabaseConnection connection = null;
		try {
			IDataSet dataset = new FlatXmlDataSetBuilder().build(new FileInputStream(TEST_DATASET_PATH));
			connection = new DatabaseConnection(dataSource.getConnection());
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
		} catch (DataSetException e) {
			if (log.isErrorEnabled()) {
				log.error(e);
			}
			throw e;
		} catch (FileNotFoundException e) {
			if (log.isErrorEnabled()) {
				log.error(e);
			}
			throw e;
		} catch (DatabaseUnitException e) {
			if (log.isErrorEnabled()) {
				log.error(e);
			}
			throw e;
		} catch (SQLException e) {
			if (log.isErrorEnabled()) {
				log.error(e);
			}
			throw e;
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				if (log.isErrorEnabled()) {
					log.error(e);
				}
				throw e;
				// Stupid!!
			}
		}
	}
	
}
