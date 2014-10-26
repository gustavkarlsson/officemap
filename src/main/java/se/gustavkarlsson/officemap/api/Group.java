package se.gustavkarlsson.officemap.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Group {

	@JsonProperty
	private long id;

	@NotBlank
	@JsonProperty
	private String name;

	@NotNull
	@JsonProperty
	private Set<Person> members = new HashSet<Person>();
	
	public Group() {
		// Default constructor for Jackson deserialization
	}

	public Group(final long id, final String name, final Collection<Person> members) {
		this.id = id;
		this.name = name;
		this.members.addAll(members);
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Set<Person> getMembers() {
		return members;
	}

	public void setMembers(final Set<Person> members) {
		this.members = members;
	}
}
