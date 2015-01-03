package se.gustavkarlsson.officemap;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;

import se.gustavkarlsson.officemap.api.event.Event;
import se.gustavkarlsson.officemap.api.event.ProcessEventException;
import se.gustavkarlsson.officemap.api.event.person.CreatePersonEvent;
import se.gustavkarlsson.officemap.api.event.person.DeletePersonEvent;
import se.gustavkarlsson.officemap.api.event.person.PersonEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonEmailEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonFirstNameEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonLastNameEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonLocationEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonPortraitEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonUsernameEvent;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.health.DummyHealthCheck;
import se.gustavkarlsson.officemap.resources.api.FileResource;
import se.gustavkarlsson.officemap.resources.api.PersonResource;
import se.gustavkarlsson.officemap.util.FileHandler;

public class OfficeMap extends Application<OfficeMapConfiguration> {

	private final HibernateBundle<OfficeMapConfiguration> hibernate = new HibernateBundle<OfficeMapConfiguration>(
			CreatePersonEvent.class, DeletePersonEvent.class, UpdatePersonEmailEvent.class,
			UpdatePersonUsernameEvent.class, UpdatePersonFirstNameEvent.class, UpdatePersonLastNameEvent.class,
			UpdatePersonPortraitEvent.class, UpdatePersonLocationEvent.class, PersonEvent.class, Event.class) {
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
		setupHealthChecks(environment);
		setupJersey(config, environment);
	}

	private void setupHealthChecks(final Environment environment) {
		environment.healthChecks().register("person", new DummyHealthCheck());
	}

	private void setupJersey(final OfficeMapConfiguration config, final Environment environment) {
		final SessionFactory sessionFactory = hibernate.getSessionFactory();
		final EventDao dao = new EventDao(sessionFactory);
		final State state = initState(sessionFactory, dao);
		final FileHandler fileHandler = new FileHandler(config.getDataDirectory(), config.getTempDirectory());
		
		environment.jersey().register(new FileResource(fileHandler));
		environment.jersey().register(new PersonResource(state, dao));
		
		environment.jersey().setUrlPattern("/api/*");
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
			try {
				event.process(state);
			} catch (final ProcessEventException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return state;
	}
}
