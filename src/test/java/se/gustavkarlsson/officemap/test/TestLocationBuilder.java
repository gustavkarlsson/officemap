package se.gustavkarlsson.officemap.test;

import se.gustavkarlsson.officemap.api.items.Location.LocationBuilder;

public class TestLocationBuilder extends LocationBuilder {
	
	public static LocationBuilder withTestParameters() {
		return new TestLocationBuilder().with(2, 3.4, 1.05);
	}

}
