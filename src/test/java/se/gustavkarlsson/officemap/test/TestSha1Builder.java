package se.gustavkarlsson.officemap.test;

import se.gustavkarlsson.officemap.api.item.Sha1.Sha1Builder;

public class TestSha1Builder extends Sha1Builder {

	public static Sha1Builder withTestParameters() {
		return new TestSha1Builder().withHex("cf23df2207d99a74fbe169e3eba035e633b65d94");
	}
	
}
