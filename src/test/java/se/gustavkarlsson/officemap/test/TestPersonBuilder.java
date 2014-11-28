package se.gustavkarlsson.officemap.test;

import java.util.ArrayList;

import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.person.Person;
import se.gustavkarlsson.officemap.api.person.PersonReference;

public class TestPersonBuilder extends Person.Builder {
	
	public TestPersonBuilder(final Long id, final Reference<Person> reference, final Long timestamp,
			final boolean deleted, final String username, final String firstName, final String lastName,
			final String email) {
		super(id, reference, timestamp, deleted, username, firstName, lastName, email);
	}
	
	public static TestPersonBuilder withTestParameters() {
		return new TestPersonBuilder(3l, new PersonReference(1l, new ArrayList<Person>()), 1_000_000l, false,
				"johndoe", "John", "Doe", "john.doe@company.com");
	}
	
}
