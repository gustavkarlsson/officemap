package se.gustavkarlsson.officemap.api.item;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public final class Person {

	@NotBlank
	private final String username;

	@NotBlank
	private final String firstName;

	@NotBlank
	private final String lastName;

	@Email
	@NotBlank
	private final String email;

	private final Sha1 portrait;

	private final Location location;

	private Person(final String username, final String firstName, final String lastName, final String email,
			final Sha1 portrait, final Location location) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.portrait = portrait;
		this.location = location;
	}

	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("portrait")
	public Sha1 getPortrait() {
		return portrait;
	}

	@JsonProperty("location")
	public Location getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("username", username).add("firstName", firstName)
				.add("lastName", lastName).add("email", email).add("portrait", portrait).add("location", location)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(username, firstName, lastName, email, portrait, location);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		final Person rhs = (Person) obj;
		return new EqualsBuilder().append(username, rhs.username).append(firstName, rhs.firstName)
				.append(lastName, rhs.lastName).append(email, rhs.email).append(portrait, rhs.portrait)
				.append(location, rhs.location).isEquals();
	}

	public Builder toBuilder() {
		return builder().with(username, firstName, lastName, email, portrait, location);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String username;

		private String firstName;

		private String lastName;

		private String email;

		private Sha1 portrait;

		private Location location;

		protected Builder() {
		}

		public Person build() {
			return new Person(username, firstName, lastName, email, portrait, location);
		}

		public Builder with(final String username, final String firstName, final String lastName, final String email,
				final Sha1 portrait, final Location location) {
			this.username = username;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.portrait = portrait;
			this.location = location;
			return this;
		}

		public Builder withUsername(final String username) {
			this.username = username;
			return this;
		}

		public Builder withFirstName(final String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder withLastName(final String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder withEmail(final String email) {
			this.email = email;
			return this;
		}

		public Builder withPortrait(final Sha1 portrait) {
			this.portrait = portrait;
			return this;
		}

		public Builder withLocation(final Location location) {
			this.location = location;
			return this;
		}
	}
}
