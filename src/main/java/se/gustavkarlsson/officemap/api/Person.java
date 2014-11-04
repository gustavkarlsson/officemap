package se.gustavkarlsson.officemap.api;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@Entity
@Table(name = "Person")
@DiscriminatorValue(Person.TYPE)
public final class Person extends Item {
	
	public static final String TYPE = "Person";
	
	@JsonProperty
	@NotBlank
	@Column(name = "username", nullable = false)
	private final String username;
	
	@JsonProperty
	@NotBlank
	@Column(name = "firstName", nullable = false)
	private final String firstName;

	@JsonProperty
	@NotBlank
	@Column(name = "lastName", nullable = false)
	private final String lastName;

	@JsonProperty
	@Email
	@NotBlank
	@Column(name = "email", nullable = false)
	private final String email;
	
	private Person() {
		// Required by Hibernate
		super(null, null, false);
		this.username = null;
		this.firstName = null;
		this.lastName = null;
		this.email = null;
	}
	
	private Person(@JsonProperty("id") final Long id, @JsonProperty("reference") final Long reference,
			@JsonProperty("deleted") final boolean deleted, @JsonProperty("username") final String username,
			@JsonProperty("firstName") final String firstName, @JsonProperty("lastName") final String lastName,
			@JsonProperty("email") final String email) {
		super(id, reference, deleted);
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
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
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", getId()).add("reference", getReference())
				.add("deleted", isDeleted()).add("username", username).add("firstName", firstName)
				.add("lastName", lastName).add("email", email).toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(username, firstName, lastName, email);
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
				.append(lastName, rhs.lastName).append(email, rhs.email).isEquals();
	}
	
	public static class Builder {
		
		private Long id;
		
		private Long reference;
		
		private boolean deleted;
		
		private String username;
		
		private String firstName;
		
		private String lastName;
		
		private String email;
		
		protected Builder(final Long id, final Long reference, final boolean deleted, final String username,
				final String firstName, final String lastName, final String email) {
			this.id = id;
			this.reference = reference;
			this.deleted = deleted;
			this.username = username;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
		}
		
		public static Builder fromNothing() {
			return new Builder(null, null, false, null, null, null, null);
		}
		
		public static Builder withFields(final Long id, final Long reference, final boolean deleted,
				final String username, final String firstName, final String lastName, final String email) {
			return new Builder(id, reference, deleted, username, firstName, lastName, email);
		}
		
		public static Builder fromPerson(final Person person) {
			return new Builder(person.getId(), person.getReference(), person.isDeleted(), person.getUsername(),
					person.getFirstName(), person.getLastName(), person.getEmail());
		}
		
		public Person build() {
			return new Person(id, reference, deleted, username, firstName, lastName, email);
		}
		
		public Long getId() {
			return id;
		}
		
		public Builder withId(final Long id) {
			this.id = id;
			return this;
		}
		
		public Long getReference() {
			return reference;
		}
		
		public Builder withReference(final Long reference) {
			this.reference = reference;
			return this;
		}
		
		public boolean isDeleted() {
			return deleted;
		}
		
		public Builder withDeleted(final boolean deleted) {
			this.deleted = deleted;
			return this;
		}
		
		public String getUsername() {
			return username;
		}
		
		public Builder withUsername(final String username) {
			this.username = username;
			return this;
		}
		
		public String getFirstName() {
			return firstName;
		}
		
		public Builder withFirstName(final String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public String getLastName() {
			return lastName;
		}
		
		public Builder withLastName(final String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public String getEmail() {
			return email;
		}
		
		public Builder withEmail(final String email) {
			this.email = email;
			return this;
		}
	}
}
