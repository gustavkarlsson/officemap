package se.gustavkarlsson.officemap.test;

import java.util.ArrayList;

import se.gustavkarlsson.officemap.api.fileentry.FileEntry;
import se.gustavkarlsson.officemap.api.item.person.Person;
import se.gustavkarlsson.officemap.api.item.person.PersonReference;

public class TestPersonBuilder extends Person.Builder {
	
	public static Person.Builder withTestParameters() {
		return new TestPersonBuilder().with(3l, 1_000_000l, new PersonReference(1l, new ArrayList<Person>()), false,
				"johndoe", "John", "Doe", "john.doe@company.com",
				FileEntry.builder().with(2l, "application/png", "cf23df2207d99a74fbe169e3eba035e633b65d94").build());
	}

}
