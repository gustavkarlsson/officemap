package se.gustavkarlsson.officemap.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import se.gustavkarlsson.officemap.api.fileentry.FileEntry;
import se.gustavkarlsson.officemap.api.item.Reference;
import se.gustavkarlsson.officemap.api.item.area.Area;
import se.gustavkarlsson.officemap.api.item.area.AreaReference;
import se.gustavkarlsson.officemap.api.item.person.Person;
import se.gustavkarlsson.officemap.api.item.person.PersonReference;

public class TestAreaBuilder extends Area.Builder {
	
	public static Area.Builder withTestParameters() {
		return new TestAreaBuilder().with(
				3l,
				1_000_000l,
				new AreaReference(1l, new ArrayList<Area>()),
				false,
				"Floor 1",
				FileEntry.builder().withSha1Hex("cf23df2207d99a74fbe169e3eba045e633b65d94")
						.withMimeType("application/png").build(),
				new HashSet<Reference<Person>>(Arrays.asList(new PersonReference(1l, null), new PersonReference(3l,
						null), new PersonReference(5l, null))));
	}
	
}
