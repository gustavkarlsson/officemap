package se.gustavkarlsson.officemap;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfficeMapConfiguration extends Configuration {

	@NotBlank
	@JsonProperty
	private String dataPath = "data/";

	@NotBlank
	@JsonProperty
	private String tempPath = "temp/";
	
	@Valid
	@NotNull
	@JsonProperty
	private final DataSourceFactory database = createDefaultDataSourceFactory();

	public final Path getDataPath() {
		return Paths.get(dataPath);
	}
	
	public final void setDataPath(final String dataPath) {
		this.dataPath = dataPath;
	}
	
	public final Path getTempPath() {
		return Paths.get(tempPath);
	}
	
	public final void setTempPath(final String tempPath) {
		this.tempPath = tempPath;
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
