package se.gustavkarlsson.officemap.representations;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {

	private long id;

	@NotBlank
	private String username;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@Email
	private String email;

	public Person() {
		// Default constructor for Jackson deserialization
	}

	public Person(final long id, final String username, final String firstName, final String lastName,
			final String email) {
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	@JsonProperty
	public long getId() {
		return id;
	}
	
	@JsonProperty
	public String getUsername() {
		return username;
	}
	
	@JsonProperty
	public String getFirstName() {
		return firstName;
	}
	
	@JsonProperty
	public String getLastName() {
		return lastName;
	}
	
	@JsonProperty
	public String getEmail() {
		return email;
	}
}
