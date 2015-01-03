package se.gustavkarlsson.officemap.test;

import se.gustavkarlsson.officemap.api.item.Sha1;
import se.gustavkarlsson.officemap.api.item.map.Map;

public class TestMapBuilder extends Map.Builder {

	public static Map.Builder withTestParameters() {
		return new TestMapBuilder().with("Floor 1", Sha1.builder().withSha1("cf23df2207d99a74fbe169e3eba045e633b65d94")
				.build());
	}

}
