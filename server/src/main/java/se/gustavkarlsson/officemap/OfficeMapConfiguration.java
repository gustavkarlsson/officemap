package se.gustavkarlsson.officemap;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.server.ServerFactory;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OfficeMapConfiguration extends Configuration implements AssetsBundleConfiguration {

	@Valid
	@NotNull
	private final ServerFactory serverFactory = createDefaultServerFactory();

	@Valid
	@NotNull
	private final DataSourceFactory dataSourceFactory = createDefaultDataSourceFactory();
	
	@NotBlank
	private final String dataPath = "data/";

	@NotBlank
	private final String thumbsCachePath = "thumbs_cache/";

	@Valid
	@NotNull
	@JsonProperty
	private final AssetsConfiguration assets = new AssetsConfiguration();

	@Override
	@JsonProperty("server")
	public ServerFactory getServerFactory() {
		return serverFactory;
	}

	@JsonProperty("database")
	public final DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	@JsonProperty("dataPath")
	public final Path getDataPath() {
		return Paths.get(dataPath);
	}

	@Override
	@JsonProperty("assets")
	public AssetsConfiguration getAssetsConfiguration() {
		return assets;
	}

	@JsonProperty("thumbsCachePath")
	public Path getThumbsCachePath() {
		return Paths.get(thumbsCachePath);
	}

	private ServerFactory createDefaultServerFactory() {
		DefaultServerFactory factory = new DefaultServerFactory();
		factory.setJerseyRootPath("/api/*");
		return factory;
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
