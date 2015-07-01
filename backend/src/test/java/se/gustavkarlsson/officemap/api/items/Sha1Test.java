package se.gustavkarlsson.officemap.api.items;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.fail;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.items.Sha1;
import se.gustavkarlsson.officemap.test.TestSha1Builder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Sha1Test {
	
	private static final ObjectMapper mapper = Jackson.newObjectMapper();

	private static final Sha1 sha1 = TestSha1Builder.withTestParameters().build();
	
	private static String fixture;

	@BeforeClass
	public static void setUp() throws Exception {
		fixture = fixture("fixtures/sha1.json");
	}
	
	@Test
	public void serializesToJSON() throws Exception {
		final String serialized = mapper.writeValueAsString(sha1);
		assertEquals(fixture, serialized, true);
	}
	
	@Test
	public void deserializesFromJSON() throws Exception {
		final Sha1 deserialized = mapper.readValue(fixture, Sha1.class);
		assertThat(deserialized).isEqualTo(sha1);
	}

	@Test
	public void validSha1Validates() throws Exception {
		assertThat(sha1).isValid();
	}

	@Test
	public void invalidSha1Sizes() throws Exception {
		assertIllegalArgumentExceptionForBytes(new byte[0]);
		assertIllegalArgumentExceptionForBytes(new byte[1]);
		assertIllegalArgumentExceptionForBytes(new byte[19]);
		assertIllegalArgumentExceptionForBytes(new byte[21]);
	}

	@Test
	public void invalidSha1Hexes() throws Exception {
		assertIllegalArgumentExceptionForText("");
		assertIllegalArgumentExceptionForText("cf23df2207d99a74fbe169e3eba035e633b65d9");
		assertIllegalArgumentExceptionForText("cf23df2207d99a74fbe169e3eba035e633b65d947");
		assertIllegalArgumentExceptionForText("cf23df2207d99a74fbe169e3eba035e633b65d9i");
	}

	@Test(expected = NullPointerException.class)
	public void nullBytesFails() throws Exception {
		final byte[] nullValue = null;
		Sha1.builder().withBytes(nullValue).build();
	}

	@Test(expected = NullPointerException.class)
	public void nullHexFails() throws Exception {
		final String nullValue = null;
		Sha1.builder().withHex(nullValue).build();
	}

	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(Sha1.class).usingGetClass().allFieldsShouldBeUsed().verify();
	}

	private void assertIllegalArgumentExceptionForBytes(final byte[] bytes) {
		try {
			Sha1.builder().withBytes(bytes).build();
			fail("Expected IllegalArgumentException");
		} catch (final IllegalArgumentException e) {
		}
	}

	private void assertIllegalArgumentExceptionForText(final String text) {
		try {
			Sha1.builder().withHex(text).build();
			fail("Expected IllegalArgumentException");
		} catch (final IllegalArgumentException e) {
		}
	}

}
