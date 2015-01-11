package se.gustavkarlsson.officemap;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;

import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.events.Event;
import se.gustavkarlsson.officemap.health.DummyHealthCheck;
import se.gustavkarlsson.officemap.resources.FilesResource;
import se.gustavkarlsson.officemap.resources.MapsResource;
import se.gustavkarlsson.officemap.resources.PersonsResource;
import se.gustavkarlsson.officemap.resources.SearchResource;
import se.gustavkarlsson.officemap.util.FileHandler;

import com.codahale.metrics.health.HealthCheckRegistry;

public class OfficeMap extends Application<OfficeMapConfiguration> {

	private final HibernateBundle<OfficeMapConfiguration> hibernate = new ScanningHibernateBundle<OfficeMapConfiguration>(
			"se.gustavkarlsson.officemap.events") {
		@Override
		public DataSourceFactory getDataSourceFactory(final OfficeMapConfiguration config) {
			return config.getDataSourceFactory();
		}
	};

	private final MigrationsBundle<OfficeMapConfiguration> migrations = new MigrationsBundle<OfficeMapConfiguration>() {
		@Override
		public DataSourceFactory getDataSourceFactory(final OfficeMapConfiguration config) {
			return config.getDataSourceFactory();
		}
	};

	private final AssetsBundle assets = new AssetsBundle("/assets", "/", "index.html");

	private SessionFactory sessionFactory;
	private EventDao dao;
	private FileHandler fileHandler;
	private State state;

	public static void main(final String[] args) throws Exception {
		new OfficeMap().run(args);
	}

	@Override
	public String getName() {
		return "OfficeMap";
	}

	@Override
	public void initialize(final Bootstrap<OfficeMapConfiguration> bootstrap) {
		bootstrap.addBundle(hibernate);
		bootstrap.addBundle(migrations);
		bootstrap.addBundle(assets);
	}

	@Override
	public void run(final OfficeMapConfiguration config, final Environment environment) throws Exception {
		sessionFactory = hibernate.getSessionFactory();
		dao = new EventDao(sessionFactory);
		fileHandler = new FileHandler(config.getDataPath());
		state = initState(sessionFactory, dao);

		setupHealthChecks(environment.healthChecks());
		setupJersey(environment.jersey());
	}

	private void setupHealthChecks(final HealthCheckRegistry healthChecks) {
		healthChecks.register("dummy", new DummyHealthCheck());
	}

	private void setupJersey(final JerseyEnvironment jersey) {
		jersey.setUrlPattern("/api/*");

		jersey.register(new FilesResource(fileHandler));
		jersey.register(new PersonsResource(state, dao));
		jersey.register(new MapsResource(state, dao));
		jersey.register(new SearchResource(state));
	}

	private State initState(final SessionFactory sessionFactory, final EventDao dao) {
		final State state = new State();
		final List<Event> events;
		final Session session = sessionFactory.openSession();
		try {
			// The following line is important, otherwise the session isn't known to the DAO.
			ManagedSessionContext.bind(session);
			events = dao.list();
		} finally {
			session.close();
		}
		for (final Event event : events) {
			event.process(state);
		}
		return state;
	}
}
