package se.gustavkarlsson.officemap.api.items;

import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

@Embeddable
public final class Person implements Buildable<Person>, Searchable {
	
	@NotBlank
	@Column(name = "username")
	private final String username;
	
	@NotBlank
	@Column(name = "firstName")
	private final String firstName;
	
	@NotBlank
	@Column(name = "lastName")
	private final String lastName;
	
	@Email
	@NotBlank
	@Column(name = "email")
	private final String email;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "portrait")) })
	private final Sha1 portrait;
	
	@Embedded
	private final Location location;

	// Required by Hibernate
	private Person() {
		this.username = null;
		this.firstName = null;
		this.lastName = null;
		this.email = null;
		this.portrait = null;
		this.location = null;
	}
	
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
	
	@Override
	public PersonBuilder toBuilder() {
		return builder().with(username, firstName, lastName, email, portrait, location);
	}
	
	@Override
	@JsonIgnore
	public Set<String> getKeywords() {
		return Sets.newHashSet(username, firstName, lastName, email);
	}
	
	public static PersonBuilder builder() {
		return new PersonBuilder();
	}
	
	public static class PersonBuilder implements Builder<Person> {
		
		private String username;
		
		private String firstName;
		
		private String lastName;
		
		private String email;
		
		private Sha1 portrait;
		
		private Location location;
		
		protected PersonBuilder() {
		}
		
		@Override
		public Person build() {
			return new Person(username, firstName, lastName, email, portrait, location);
		}
		
		public PersonBuilder with(final String username, final String firstName, final String lastName,
				final String email, final Sha1 portrait, final Location location) {
			this.username = username;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.portrait = portrait;
			this.location = location;
			return this;
		}
		
		public PersonBuilder withUsername(final String username) {
			this.username = username;
			return this;
		}
		
		public PersonBuilder withFirstName(final String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public PersonBuilder withLastName(final String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public PersonBuilder withEmail(final String email) {
			this.email = email;
			return this;
		}
		
		public PersonBuilder withPortrait(final Sha1 portrait) {
			this.portrait = portrait;
			return this;
		}
		
		public PersonBuilder withLocation(final Location location) {
			this.location = location;
			return this;
		}
	}
}
