package se.gustavkarlsson.officemap.api.item.person;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.item.Item;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;

@JsonSerialize(using = PersonSerializer.class)
@JsonDeserialize(using = PersonDeserializer.class)
public final class Person extends Item {
	
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

	private Person(final Long id, final String username, final String firstName, final String lastName,
			final String email, final Sha1 portrait) {
		super(id);
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.portrait = portrait;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public Sha1 getPortrait() {
		return portrait;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", getId()).add("username", username).add("firstName", firstName)
				.add("lastName", lastName).add("email", email).add("portrait", portrait).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(username, firstName, lastName, email, portrait);
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
				.append(lastName, rhs.lastName).append(email, rhs.email).append(portrait, rhs.getPortrait()).isEquals();
	}

	public Builder toBuilder() {
		return builder().with(getId(), username, firstName, lastName, email, portrait);
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private Long id;

		private String username;

		private String firstName;

		private String lastName;

		private String email;

		private Sha1 portrait;

		protected Builder() {
		}

		public Person build() {
			return new Person(id, username, firstName, lastName, email, portrait);
		}
		
		public Builder with(final Long id, final String username, final String firstName, final String lastName,
				final String email, final Sha1 portrait) {
			this.id = id;
			this.username = username;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.portrait = portrait;
			return this;
		}

		public Builder withId(final Long id) {
			this.id = id;
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
	}
}
