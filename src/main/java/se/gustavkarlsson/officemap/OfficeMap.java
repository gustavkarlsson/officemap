package se.gustavkarlsson.officemap;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.gustavkarlsson.officemap.health.DummyHealthCheck;
import se.gustavkarlsson.officemap.resources.PersonResource;

public class OfficeMap extends Application<OfficeMapConfiguration> {
	public static void main(final String[] args) throws Exception {
		new OfficeMap().run(args);
	}
	
	@Override
	public String getName() {
		return "OfficeMap";
	}
	
	@Override
	public void initialize(final Bootstrap<OfficeMapConfiguration> bootstrap) {
	}
	
	@Override
	public void run(final OfficeMapConfiguration configuration, final Environment environment) throws Exception {
		environment.healthChecks().register("person", new DummyHealthCheck());
		
		environment.jersey().register(new PersonResource());
	}
	
}
