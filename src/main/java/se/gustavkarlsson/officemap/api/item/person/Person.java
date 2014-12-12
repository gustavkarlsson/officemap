package se.gustavkarlsson.officemap.api.item.person;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import se.gustavkarlsson.officemap.api.fileentry.FileEntry;
import se.gustavkarlsson.officemap.api.item.Item;
import se.gustavkarlsson.officemap.api.item.Reference;

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

	@OneToMany
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@PrimaryKeyJoinColumn
	private final FileEntry portrait;
	
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
			final FileEntry portrait) {
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
	
	public FileEntry getPortrait() {
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
	
	public Builder toBuilder() {
		return builder().with(getId(), getTimestamp(), getReference(), isDeleted(), username, firstName, lastName,
				email, portrait);
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private Long id;
		
		private Reference<Person> reference;
		
		private Long timestamp;
		
		private Boolean deleted;
		
		private String username;
		
		private String firstName;
		
		private String lastName;
		
		private String email;
		
		private FileEntry portrait;
		
		protected Builder() {
		}
		
		public Person build() {
			return new Person(id, timestamp, reference, deleted, username, firstName, lastName, email, portrait);
		}

		public Builder with(final Long id, final Long timestamp, final Reference<Person> reference,
				final boolean deleted, final String username, final String firstName, final String lastName,
				final String email, final FileEntry portrait) {
			this.id = id;
			this.timestamp = timestamp;
			this.reference = reference;
			this.deleted = deleted;
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
		
		public Builder withReference(final Reference<Person> reference) {
			this.reference = reference;
			return this;
		}
		
		public Builder withTimestamp(final Long timestamp) {
			this.timestamp = timestamp;
			return this;
		}
		
		public Builder withDeleted(final boolean deleted) {
			this.deleted = deleted;
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
		
		public Builder withPortrait(final FileEntry portrait) {
			this.portrait = portrait;
			return this;
		}
	}
}
