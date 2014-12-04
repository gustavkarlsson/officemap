package se.gustavkarlsson.officemap.test;

import java.util.ArrayList;

import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.person.Person;
import se.gustavkarlsson.officemap.api.person.PersonReference;

public class TestPersonBuilder extends Person.Builder {

	public TestPersonBuilder(final Long id, final Reference<Person> reference, final Long timestamp,
			final boolean deleted, final String username, final String firstName, final String lastName,
			final String email, final Sha1 portrait) {
		super(id, reference, timestamp, deleted, username, firstName, lastName, email, portrait);
	}

	public static TestPersonBuilder withTestParameters() {
		return new TestPersonBuilder(3l, new PersonReference(1l, new ArrayList<Person>()), 1_000_000l, false,
				"johndoe", "John", "Doe", "john.doe@company.com", new Sha1("cf23df2207d99a74fbe169e3eba035e633b65d94"));
	}

}
