package se.gustavkarlsson.officemap;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheckRegistry;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.events.Event;
import se.gustavkarlsson.officemap.health.LocationMapsExist;
import se.gustavkarlsson.officemap.resources.FilesResource;
import se.gustavkarlsson.officemap.resources.MapsResource;
import se.gustavkarlsson.officemap.resources.PersonsResource;
import se.gustavkarlsson.officemap.resources.SearchResource;
import se.gustavkarlsson.officemap.util.FileHandler;
import se.gustavkarlsson.officemap.util.ThumbnailHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

	private final MultiPartBundle multipart = new MultiPartBundle();

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
		bootstrap.addBundle(multipart);
	}

	@Override
	public void run(final OfficeMapConfiguration config, final Environment environment) throws Exception {
		final SessionFactory sessionFactory = hibernate.getSessionFactory();
		dao = new EventDao(sessionFactory);
		fileHandler = new FileHandler(config.getDataPath(), new ThumbnailHandler(config.getThumbsCachePath()));
		state = initState(sessionFactory, dao, environment.metrics());

		setupHealthChecks(environment.healthChecks());
		setupJersey(environment.jersey());
	}

	private void setupHealthChecks(final HealthCheckRegistry healthChecks) {
		healthChecks.register("location maps exist", new LocationMapsExist(state));
	}

	private void setupJersey(final JerseyEnvironment jersey) {
		jersey.register(new FilesResource(fileHandler));
		jersey.register(new PersonsResource(state, dao));
		jersey.register(new MapsResource(state, dao));
		jersey.register(new SearchResource(state));
	}

	private State initState(final SessionFactory sessionFactory, final EventDao dao, MetricRegistry metrics) {
		Counter counter = metrics.counter(MetricRegistry.name(this.getClass(), "initState", "events"));
		Timer timer = metrics.timer(MetricRegistry.name(this.getClass(), "initState"));
		long startTime = System.nanoTime();

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
			counter.inc();
		}
		timer.update(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
		return state;
	}
}
