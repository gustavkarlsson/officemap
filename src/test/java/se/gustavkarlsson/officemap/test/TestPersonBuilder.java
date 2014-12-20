package se.gustavkarlsson.officemap.test;

import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.item.person.Person;

public class TestPersonBuilder extends Person.Builder {
	
	public static Person.Builder withTestParameters() {
		return new TestPersonBuilder().with(3l, "johndoe", "John", "Doe", "john.doe@company.com", Sha1.builder()
				.withSha1("cf23df2207d99a74fbe169e3eba035e633b65d94").build());
	}

}
