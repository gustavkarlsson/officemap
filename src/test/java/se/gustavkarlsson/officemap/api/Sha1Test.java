package se.gustavkarlsson.officemap.api;

import static org.fest.assertions.api.Assertions.fail;
import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import se.gustavkarlsson.officemap.api.fileentry.FileEntry;

public class Sha1Test {
	
	@Test
	public void validSha1Validates() throws Exception {
		assertThat(
				FileEntry
						.builder()
						.with(null, "application/pdf",
								new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 })
						.build()).isValid();
		assertThat(
				FileEntry.builder().with(null, "application/text", "cf23df2207d99a74fbe169e3eba035e633b65d94").build())
				.isValid();
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
	public void nullSha1Fails() throws Exception {
		final byte[] nullValue = null;
		FileEntry.builder().with(null, "application/text", nullValue).build();
	}

	@Test(expected = NullPointerException.class)
	public void nullSha1HexFails() throws Exception {
		final String nullValue = null;
		FileEntry.builder().with(null, "application/text", nullValue).build();
	}
	
	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(FileEntry.class).usingGetClass().allFieldsShouldBeUsedExcept("id")
				.suppress(Warning.NULL_FIELDS).verify();
	}
	
	private void assertIllegalArgumentExceptionForBytes(final byte[] bytes) {
		try {
			FileEntry.builder().with(null, "application/text", bytes).build();
			fail("Expected IllegalArgumentException");
		} catch (final IllegalArgumentException e) {
		}
	}
	
	private void assertIllegalArgumentExceptionForText(final String text) {
		try {
			FileEntry.builder().with(null, "application/text", text).build();
			fail("Expected IllegalArgumentException");
		} catch (final IllegalArgumentException e) {
		}
	}
	
}
