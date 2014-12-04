package se.gustavkarlsson.officemap;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfficeMapConfiguration extends Configuration {

	@NotBlank
	@JsonProperty
	private String dataDirectory = "./data/";

	@NotBlank
	@JsonProperty
	private String tempDirectory = "./temp/";
	
	@Valid
	@NotNull
	@JsonProperty
	private final DataSourceFactory database = createDefaultDataSourceFactory();

	public final String getDataDirectory() {
		return dataDirectory;
	}
	
	public final void setDataDirectory(final String dataDirectory) {
		this.dataDirectory = dataDirectory;
	}
	
	public final String getTempDirectory() {
		return tempDirectory;
	}
	
	public final void setTempDirectory(final String tempDirectory) {
		this.tempDirectory = tempDirectory;
	}
	
	public final DataSourceFactory getDataSourceFactory() {
		return database;
	}
	
	private DataSourceFactory createDefaultDataSourceFactory() {
		final DataSourceFactory factory = new DataSourceFactory();
		factory.setDriverClass("org.h2.Driver");
		factory.setUrl("jdbc:h2:./database");
		factory.setUser("sa");
		factory.setPassword("sa");
		return factory;
	}
}
