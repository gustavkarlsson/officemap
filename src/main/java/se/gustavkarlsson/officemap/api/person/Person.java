package se.gustavkarlsson.officemap.api.person;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import se.gustavkarlsson.officemap.api.Item;
import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.Sha1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;

@Entity
@Table(name = Person.TYPE)
@DiscriminatorValue(Person.TYPE)
@JsonSerialize(using = PersonSerializer.class)
@JsonDeserialize(using = PersonDeserializer.class)
public final class Person extends Item<Person> {
	
	public static final String TYPE = "Person";

	@NotBlank
	@Column(name = "username", nullable = false)
	private final String username;

	@NotBlank
	@Column(name = "firstName", nullable = false)
	private final String firstName;

	@NotBlank
	@Column(name = "lastName", nullable = false)
	private final String lastName;

	@Email
	@NotBlank
	@Column(name = "email", nullable = false)
	private final String email;

	@Embedded
	@AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "portrait")))
	private final Sha1 portrait;
	
	private Person() {
		// Required by Hibernate
		super(null, null, null, false);
		this.username = null;
		this.firstName = null;
		this.lastName = null;
		this.email = null;
		this.portrait = null;
	}
	
	private Person(final Long id, final Long timestamp, final Reference<Person> reference, final boolean deleted,
			final String username, final String firstName, final String lastName, final String email,
			final Sha1 portrait) {
		super(id, timestamp, reference, deleted);
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
		return Objects.toStringHelper(this).add("id", getId()).add("reference", getReference())
				.add("deleted", isDeleted()).add("username", username).add("firstName", firstName)
				.add("lastName", lastName).add("email", email).add("portrait", portrait).toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(isDeleted(), username, firstName, lastName, email, portrait);
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
		return new EqualsBuilder().append(isDeleted(), rhs.isDeleted()).append(username, rhs.username)
				.append(firstName, rhs.firstName).append(lastName, rhs.lastName).append(email, rhs.email)
				.append(portrait, rhs.getPortrait()).isEquals();
	}
	
	public static class Builder {
		
		private Long id;
		
		private Reference<Person> reference;
		
		private Long timestamp;
		
		private boolean deleted;
		
		private String username;
		
		private String firstName;
		
		private String lastName;
		
		private String email;
		
		private Sha1 portrait;
		
		protected Builder(final Long id, final Reference<Person> reference, final Long timestamp,
				final boolean deleted, final String username, final String firstName, final String lastName,
				final String email, final Sha1 portrait) {
			this.id = id;
			this.timestamp = timestamp;
			this.reference = reference;
			this.deleted = deleted;
			this.username = username;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.portrait = portrait;
		}
		
		public static Builder fromNothing() {
			return new Builder(null, null, null, false, null, null, null, null, null);
		}
		
		public static Builder withFields(final Long id, final Long timestamp, final Reference<Person> reference,
				final boolean deleted, final String username, final String firstName, final String lastName,
				final String email, final Sha1 portrait) {
			return new Builder(id, reference, timestamp, deleted, username, firstName, lastName, email, portrait);
		}
		
		public static Builder fromPerson(final Person person) {
			return new Builder(person.getId(), person.getReference(), person.getTimestamp(), person.isDeleted(),
					person.getUsername(), person.getFirstName(), person.getLastName(), person.getEmail(),
					person.getPortrait());
		}
		
		public Person build() {
			return new Person(id, timestamp, reference, deleted, username, firstName, lastName, email, portrait);
		}
		
		public Long getId() {
			return id;
		}
		
		public Builder withId(final Long id) {
			this.id = id;
			return this;
		}
		
		public Reference<Person> getReference() {
			return reference;
		}
		
		public Builder withReference(final Reference<Person> reference) {
			this.reference = reference;
			return this;
		}
		
		public Long getTimestamp() {
			return timestamp;
		}
		
		public Builder withTimestamp(final Long timestamp) {
			this.timestamp = timestamp;
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
		
		public Sha1 getPortrait() {
			return portrait;
		}
		
		public Builder withPortrait(final Sha1 portrait) {
			this.portrait = portrait;
			return this;
		}
	}
}
