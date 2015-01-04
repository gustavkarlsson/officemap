package se.gustavkarlsson.officemap.test;

import se.gustavkarlsson.officemap.api.item.Map.MapBuilder;
import se.gustavkarlsson.officemap.api.item.Sha1;

public class TestMapBuilder extends MapBuilder {

	public static MapBuilder withTestParameters() {
		return new TestMapBuilder().with("Floor 1", Sha1.builder().withHex("cf23df2207d99a74fbe169e3eba045e633b65d94")
				.build());
	}

}
