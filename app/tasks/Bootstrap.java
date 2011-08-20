package tasks;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import models.SharedUrl;

import org.apache.log4j.Logger;
import org.cloudfoundry.runtime.env.MysqlServiceInfo;

import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import siena.PersistenceManagerFactory;
import siena.jdbc.JdbcPersistenceManager;
import utils.C3poConnectionManager;
import utils.CloudEnvironment;

@OnApplicationStart
public class Bootstrap extends Job {

	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public CloudEnvironment cloudEnvironment = new CloudEnvironment();
	public List<MysqlServiceInfo> mysqlServices = cloudEnvironment.getServiceInfos(MysqlServiceInfo.class);
	static Logger logger = Logger.getLogger(Bootstrap.class);
	//Configure database connection
	public void doJob() {
		logger.info("Doing bootstrap");
		Properties p = new Properties();
		p.setProperty("driver", MYSQL_DRIVER);
		if(Play.mode == Play.Mode.PROD) {
			setCloundFoundryProperties(p);
		} else {
			setDevProperties(p);
		}

		createDatabasePool(p);
	}

	private void setDevProperties(Properties p) {
		logger.info("Getting configration from application.conf");
		p.setProperty("url", Play.configuration.getProperty("db.url"));
		p.setProperty("user", Play.configuration.getProperty("db.user"));
		p.setProperty("password", Play.configuration.getProperty("db.pass"));
	}
	
	private void setCloundFoundryProperties(Properties p) {
		logger.info("Getting configration from cloudfoundry");
		// Check that only one MySQL service is available. It is a non-blocking check.
		if (mysqlServices.size() > 1) {
			logger.warn("[CloudFoundry] There is more than one MySQL service bind to this application instance. Only the first will be used.");
		}

		MysqlServiceInfo mysqlServiceInfo = mysqlServices.get(0);

		// Update of Play configuration. Theses properties will be used by the DBPlugin.

		p.put("driver", MYSQL_DRIVER);
		p.put("url", mysqlServiceInfo.getUrl());
		p.put("user", mysqlServiceInfo.getUserName());
		p.put("password", mysqlServiceInfo.getPassword());


	}

	public void createDatabasePool(Properties p) {
		C3poConnectionManager cm = new C3poConnectionManager();
		cm.init(p);
		
		JdbcPersistenceManager pm = new JdbcPersistenceManager(cm, null);
		PersistenceManagerFactory.install(pm, SharedUrl.class);
	}
	


}
