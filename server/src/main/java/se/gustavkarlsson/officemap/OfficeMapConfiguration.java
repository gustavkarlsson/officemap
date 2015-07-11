package se.gustavkarlsson.officemap;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OfficeMapConfiguration extends Configuration {

	@Valid
	@NotNull
	private final DataSourceFactory dataSourceFactory = createDefaultDataSourceFactory();
	
	@NotBlank
	private final String dataPath = "data/";

	@NotBlank
	private final String thumbsCachePath = "thumbs_cache/";

	@JsonProperty("database")
	public final DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	@JsonProperty("dataPath")
	public final Path getDataPath() {
		return Paths.get(dataPath);
	}

	@JsonProperty("thumbsCachePath")
	public Path getThumbsCachePath() {
		return Paths.get(thumbsCachePath);
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
