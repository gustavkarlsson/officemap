package se.gustavkarlsson.officemap.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.area.Area;
import se.gustavkarlsson.officemap.api.area.AreaReference;
import se.gustavkarlsson.officemap.api.person.Person;
import se.gustavkarlsson.officemap.api.person.PersonReference;

public class TestAreaBuilder extends Area.Builder {

	public TestAreaBuilder(final Long id, final Reference<Area> reference, final Long timestamp, final boolean deleted,
			final String name, final Collection<Reference<Person>> persons) {
		super(id, reference, timestamp, deleted, name, persons);
	}

	public static TestAreaBuilder withTestParameters() {
		return new TestAreaBuilder(3l, new AreaReference(1l, new ArrayList<Area>()), 1_000_000l, false, "Floor 1",
				new HashSet<Reference<Person>>(Arrays.asList(new PersonReference(1l, null), new PersonReference(3l,
						null), new PersonReference(5l, null))));
	}

}
