package se.gustavkarlsson.officemap.test;

import se.gustavkarlsson.officemap.api.items.Location;
import se.gustavkarlsson.officemap.api.items.Sha1;
import se.gustavkarlsson.officemap.api.items.Person.PersonBuilder;

public class TestPersonBuilder extends PersonBuilder {
	
	public static PersonBuilder withTestParameters() {
		return new TestPersonBuilder().with("johndoe", "John", "Doe", "john.doe@company.com",
				Sha1.builder().withHex("cf23df2207d99a74fbe169e3eba035e633b65d94").build(),
				Location.builder().with(2, 3.4, 1.05).build());
	}

}
