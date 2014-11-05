package se.gustavkarlsson.officemap.test;

import se.gustavkarlsson.officemap.api.Person.Builder;

public class TestPersonBuilder extends Builder {
	
	public TestPersonBuilder(final Long id, final Long timestamp, final Long reference, final String username,
			final String firstName, final String lastName, final String email, final boolean deleted) {
		super(id, timestamp, reference, deleted, username, firstName, lastName, email);
	}
	
	public static TestPersonBuilder withTestParameters() {
		return new TestPersonBuilder(3l, 1_000_000l, 1l, "johndoe", "John", "Doe", "john.doe@company.com", false);
	}
	
}
