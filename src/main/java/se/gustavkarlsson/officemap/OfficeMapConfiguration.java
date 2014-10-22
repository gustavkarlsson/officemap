package se.gustavkarlsson.officemap;

import io.dropwizard.Configuration;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfficeMapConfiguration extends Configuration {
	@NotEmpty
	private String databasePath = ".";

	@JsonProperty
	public String getDatabasePath() {
		return databasePath;
	}

	@JsonProperty
	public void setDatabasePath(final String databasePath) {
		this.databasePath = databasePath;
	}
}
