package se.gustavkarlsson.officemap;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.gustavkarlsson.officemap.api.Person;
import se.gustavkarlsson.officemap.dao.PersonDao;
import se.gustavkarlsson.officemap.health.DummyHealthCheck;
import se.gustavkarlsson.officemap.resources.PersonResource;

public class OfficeMap extends Application<OfficeMapConfiguration> {
	
	HibernateBundle<OfficeMapConfiguration> hibernate = createHibernateBundle();
	MigrationsBundle<OfficeMapConfiguration> migrations = createMigrationsBundle();
	
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
	}
	
	@Override
	public void run(final OfficeMapConfiguration configuration, final Environment environment) throws Exception {
		environment.healthChecks().register("person", new DummyHealthCheck());
		final PersonDao personDao = new PersonDao(hibernate.getSessionFactory());
		environment.jersey().register(new PersonResource(personDao));
	}
	
	private HibernateBundle<OfficeMapConfiguration> createHibernateBundle() {
		final HibernateBundle<OfficeMapConfiguration> bundle = new HibernateBundle<OfficeMapConfiguration>(Person.class) {
			@Override
			public DataSourceFactory getDataSourceFactory(final OfficeMapConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		};
		return bundle;
	}
	
	private MigrationsBundle<OfficeMapConfiguration> createMigrationsBundle() {
		final MigrationsBundle<OfficeMapConfiguration> bundle = new MigrationsBundle<OfficeMapConfiguration>() {
			@Override
			public DataSourceFactory getDataSourceFactory(final OfficeMapConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		};
		return bundle;
	}
}
