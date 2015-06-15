package se.gustavkarlsson.officemap;

import com.codahale.metrics.health.HealthCheckRegistry;
import io.dropwizard.Application;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlet.FilterHolder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;
import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.events.Event;
import se.gustavkarlsson.officemap.health.DummyHealthCheck;
import se.gustavkarlsson.officemap.resources.FilesResource;
import se.gustavkarlsson.officemap.resources.MapsResource;
import se.gustavkarlsson.officemap.resources.PersonsResource;
import se.gustavkarlsson.officemap.resources.SearchResource;
import se.gustavkarlsson.officemap.util.FileHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.List;

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

	private final ConfiguredAssetsBundle assets = new ConfiguredAssetsBundle("/web", "/", "index.html");

	private final MultiPartBundle multipart = new MultiPartBundle();

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
		bootstrap.addBundle(multipart);
	}

	@Override
	public void run(final OfficeMapConfiguration config, final Environment environment) throws Exception {
		sessionFactory = hibernate.getSessionFactory();
		dao = new EventDao(sessionFactory);
		fileHandler = new FileHandler(config.getDataPath());
		state = initState(sessionFactory, dao);

		setupHealthChecks(environment.healthChecks());
		setupJersey(environment.jersey());
		setupUrlRewriting(environment);
	}

	private void setupHealthChecks(final HealthCheckRegistry healthChecks) {
		healthChecks.register("dummy", new DummyHealthCheck());
	}

	private void setupJersey(final JerseyEnvironment jersey) {
		jersey.register(new FilesResource(fileHandler));
		jersey.register(new PersonsResource(state, dao));
		jersey.register(new MapsResource(state, dao));
		jersey.register(new SearchResource(state));
	}

	private void setupUrlRewriting(final Environment environment) {
		final FilterHolder filter = environment.getApplicationContext().addFilter(UrlRewriteFilter.class, "/*",
				EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
		filter.setInitParameter("confPath", "urlrewrite.xml");
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
