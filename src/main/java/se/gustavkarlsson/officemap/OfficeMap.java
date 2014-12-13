package se.gustavkarlsson.officemap;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.item.Reference;
import se.gustavkarlsson.officemap.api.item.area.Area;
import se.gustavkarlsson.officemap.api.item.area.AreaReference;
import se.gustavkarlsson.officemap.api.item.person.Person;
import se.gustavkarlsson.officemap.api.item.person.PersonReference;
import se.gustavkarlsson.officemap.dao.item.AreaDao;
import se.gustavkarlsson.officemap.dao.item.PersonDao;
import se.gustavkarlsson.officemap.health.DummyHealthCheck;
import se.gustavkarlsson.officemap.resources.api.AreaResource;
import se.gustavkarlsson.officemap.resources.api.FileResource;
import se.gustavkarlsson.officemap.resources.api.PersonResource;
import se.gustavkarlsson.officemap.util.FileHandler;

public class OfficeMap extends Application<OfficeMapConfiguration> {
	
	private final HibernateBundle<OfficeMapConfiguration> hibernate = new HibernateBundle<OfficeMapConfiguration>(
			Person.class, Area.class, Reference.class, PersonReference.class, AreaReference.class, Sha1.class) {
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
		final FileHandler fileHandler = new FileHandler(config.getDataDirectory(), config.getTempDirectory());
		
		final PersonDao personDao = new PersonDao(hibernate.getSessionFactory());
		environment.jersey().register(new PersonResource(personDao));
		
		final AreaDao areaDao = new AreaDao(hibernate.getSessionFactory());
		environment.jersey().register(new AreaResource(areaDao, personDao));
		
		environment.jersey().register(new FileResource(fileHandler));
		
		environment.jersey().setUrlPattern("/api/*");
	}
}
