package se.gustavkarlsson.officemap.api;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Person")
public class Person implements Identifiable {
	
	@JsonProperty
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private final Long id;
	
	@NotBlank
	@JsonProperty
	@Column(name = "username", nullable = false, unique = true)
	private final String username;
	
	@NotBlank
	@JsonProperty
	@Column(name = "firstName", nullable = false)
	private final String firstName;
	
	@NotBlank
	@JsonProperty
	@Column(name = "lastName", nullable = false)
	private final String lastName;
	
	@Email
	@NotBlank
	@JsonProperty
	@Column(name = "email", nullable = false, unique = true)
	private final String email;
	
	private Person() {
		// Required by Hibernate
		// Hibernate sets the parameters even though they are final.
		this.id = null;
		this.username = null;
		this.firstName = null;
		this.lastName = null;
		this.email = null;
	}
	
	@JsonCreator
	private Person(@JsonProperty("id") final Long id, @JsonProperty("username") final String username,
			@JsonProperty("firstName") final String firstName, @JsonProperty("lastName") final String lastName,
			@JsonProperty("email") final String email) {
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	@Override
	public Long getId() {
		return id;
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
		return "Person [id=" + id + ", username=" + username + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Person other = (Person) obj;
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!lastName.equals(other.lastName)) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}
	
	public static class Builder {
		
		private Long id;
		
		private String username;
		
		private String firstName;
		
		private String lastName;
		
		private String email;
		
		protected Builder(final Long id, final String username, final String firstName, final String lastName,
				final String email) {
			this.id = id;
			this.username = username;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
		}
		
		public static Builder fromNothing() {
			return new Builder(null, null, null, null, null);
		}
		
		public static Builder withFields(final Long id, final String username, final String firstName,
				final String lastName, final String email, final Set<Group> groups) {
			return new Builder(id, username, firstName, lastName, email);
		}
		
		public static Builder fromPerson(final Person person) {
			return new Builder(person.getId(), person.getUsername(), person.getFirstName(), person.getLastName(),
					person.getEmail());
		}
		
		public Person build() {
			return new Person(id, username, firstName, lastName, email);
		}
		
		public Long getId() {
			return id;
		}
		
		public Builder withId(final Long id) {
			this.id = id;
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
