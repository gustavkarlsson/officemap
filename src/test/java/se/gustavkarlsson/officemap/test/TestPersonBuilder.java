package se.gustavkarlsson.officemap.test;

import java.util.ArrayList;

import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.person.Person;
import se.gustavkarlsson.officemap.api.person.Person.Builder;
import se.gustavkarlsson.officemap.api.person.PersonReference;

public class TestPersonBuilder extends Builder {

	public TestPersonBuilder(final Long id, final Long timestamp, final Reference<Person> reference,
			final String username, final String firstName, final String lastName, final String email,
			final boolean deleted) {
		super(id, timestamp, reference, deleted, username, firstName, lastName, email);
	}

	public static TestPersonBuilder withTestParameters() {
		return new TestPersonBuilder(3l, 1_000_000l, new PersonReference(1l, new ArrayList<Person>()), "johndoe",
				"John", "Doe", "john.doe@company.com", false);
	}

}
