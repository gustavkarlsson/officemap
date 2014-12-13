package se.gustavkarlsson.officemap.dao;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.testing.junit.DropwizardAppRule;

import java.util.Arrays;
import java.util.Properties;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import se.gustavkarlsson.officemap.OfficeMap;
import se.gustavkarlsson.officemap.OfficeMapConfiguration;
import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.item.Item;
import se.gustavkarlsson.officemap.api.item.Reference;
import se.gustavkarlsson.officemap.api.item.area.Area;
import se.gustavkarlsson.officemap.api.item.area.AreaReference;
import se.gustavkarlsson.officemap.api.item.person.Person;
import se.gustavkarlsson.officemap.api.item.person.PersonReference;

import com.google.common.io.Resources;

public abstract class AbstractDaoTest {

	@ClassRule
	public static final DropwizardAppRule<OfficeMapConfiguration> RULE = new DropwizardAppRule<OfficeMapConfiguration>(
			OfficeMap.class, Resources.getResource("testconfiguration.yml").getPath());

	private static Liquibase liquibase;
	private static SessionFactory sessionFactory;
	private Session session;

	@BeforeClass
	public static void setupDatabase() throws Exception {
		liquibase = createLiquibase(RULE.getConfiguration().getDataSourceFactory());
		sessionFactory = new SessionFactoryFactory().build(createHibernateBundle(), RULE.getEnvironment(), RULE
				.getConfiguration().getDataSourceFactory(), Arrays.<Class<?>> asList(Item.class, Person.class,
				Area.class, Reference.class, PersonReference.class, AreaReference.class, Sha1.class));
	}
	
	@Before
	public void prepare() throws Exception {
		liquibase.dropAll();
		liquibase.update(new Contexts());
		session = getSessionFactory().openSession();
		ManagedSessionContext.bind(session);
		session.createSQLQuery("delete from Sequence").executeUpdate();
	}

	@After
	public void finish() throws Exception {
		session.close();
		ManagedSessionContext.unbind(getSessionFactory());
	}

	protected static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private static Liquibase createLiquibase(final DataSourceFactory dataSourceFactory) throws Exception {
		final Properties info = new Properties();
		info.setProperty("user", dataSourceFactory.getUser());
		info.setProperty("password", dataSourceFactory.getPassword());
		final org.h2.jdbc.JdbcConnection h2Conn = new org.h2.jdbc.JdbcConnection(dataSourceFactory.getUrl(), info);
		final JdbcConnection connection = new JdbcConnection(h2Conn);
		final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);
		final Liquibase liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), database);
		return liquibase;
	}

	private static HibernateBundle<?> createHibernateBundle() {
		final HibernateBundle<OfficeMapConfiguration> bundle = new HibernateBundle<OfficeMapConfiguration>(Person.class) {
			@Override
			public DataSourceFactory getDataSourceFactory(final OfficeMapConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		};
		return bundle;
	}
}
