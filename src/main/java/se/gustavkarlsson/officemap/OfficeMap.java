package se.gustavkarlsson.officemap;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.area.Area;
import se.gustavkarlsson.officemap.api.area.AreaReference;
import se.gustavkarlsson.officemap.api.person.Person;
import se.gustavkarlsson.officemap.api.person.PersonReference;
import se.gustavkarlsson.officemap.dao.AreaDao;
import se.gustavkarlsson.officemap.dao.PersonDao;
import se.gustavkarlsson.officemap.health.DummyHealthCheck;
import se.gustavkarlsson.officemap.resources.api.AreaResource;
import se.gustavkarlsson.officemap.resources.api.PersonResource;

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
		bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
	}

	@Override
	public void run(final OfficeMapConfiguration configuration, final Environment environment) throws Exception {
		environment.healthChecks().register("person", new DummyHealthCheck());
		environment.jersey().register(new PersonResource(new PersonDao(hibernate.getSessionFactory())));
		environment.jersey().register(new AreaResource(new AreaDao(hibernate.getSessionFactory())));
		environment.jersey().setUrlPattern("/api/*");
	}

	private HibernateBundle<OfficeMapConfiguration> createHibernateBundle() {
		final HibernateBundle<OfficeMapConfiguration> bundle = new HibernateBundle<OfficeMapConfiguration>(
				Person.class, Area.class, Reference.class, PersonReference.class, AreaReference.class) {
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
