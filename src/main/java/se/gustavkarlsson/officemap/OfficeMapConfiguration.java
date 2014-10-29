package se.gustavkarlsson.officemap;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfficeMapConfiguration extends Configuration {

	@Valid
	@NotNull
	@JsonProperty("database")
	private final DataSourceFactory database = createDefaultDataSourceFactory();
	
	private DataSourceFactory createDefaultDataSourceFactory() {
		final DataSourceFactory factory = new DataSourceFactory();
		factory.setDriverClass("org.h2.Driver");
		factory.setUrl("jdbc:h2:./database");
		factory.setUser("sa");
		factory.setPassword("sa");
		return factory;
	}

	public DataSourceFactory getDataSourceFactory() {
		return database;
	}
}
