package se.gustavkarlsson.officemap.api;

import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import se.gustavkarlsson.officemap.api.BlobHash.Builder;

public class BlobHashTest {
	
	private static final BlobHash blobHash = BlobHash.Builder.withFields(1l,
			new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 }).build();
	
	@Test
	public void validBlobHashValidates() throws Exception {
		assertThat(blobHash).isValid();
	}
	
	@Test
	public void invalidId() throws Exception {
		assertInvalidId(-1l);
		assertInvalidId(Long.MIN_VALUE);
	}
	
	@Test
	public void invalidSha1() throws Exception {
		assertInvalidSha1(new byte[0]);
		assertInvalidSha1(new byte[1]);
		assertInvalidSha1(new byte[19]);
		assertInvalidSha1(new byte[21]);
	}

	@Test(expected = NullPointerException.class)
	public void nullSha1Fails() throws Exception {
		final byte[] nullSha1 = null;
		Builder.fromBlobHash(blobHash).withSha1(nullSha1).build();
	}
	
	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(BlobHash.class).usingGetClass().allFieldsShouldBeUsedExcept("id")
				.suppress(Warning.NULL_FIELDS).verify();
	}
	
	private void assertInvalidId(final Long id) {
		final BlobHash invalidBlobHash = Builder.fromBlobHash(blobHash).withId(id).build();
		assertThatAreaHasInvalid(invalidBlobHash, "id");
	}
	
	private void assertInvalidSha1(final byte[] sha1) {
		final BlobHash invalidBlobHash = Builder.fromBlobHash(blobHash).withSha1(sha1).build();
		assertThatAreaHasInvalid(invalidBlobHash, "sha1");
	}
	
	private void assertThatAreaHasInvalid(final BlobHash area, final String property) {
		assertThat(area).hasInvalid(property);
	}
	
}
