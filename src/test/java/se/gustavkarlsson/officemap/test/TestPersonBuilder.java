package se.gustavkarlsson.officemap.test;

import se.gustavkarlsson.officemap.api.item.Location;
import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.api.item.Sha1;

public class TestPersonBuilder extends Person.Builder {

	public static Person.Builder withTestParameters() {
		return new TestPersonBuilder().with("johndoe", "John", "Doe", "john.doe@company.com",
				Sha1.builder().withSha1("cf23df2207d99a74fbe169e3eba035e633b65d94").build(),
				Location.builder().with(2, 3.4, 1.05).build());
	}
	
}
