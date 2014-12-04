package se.gustavkarlsson.officemap.api;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.common.base.Objects;

@Embeddable
public final class Sha1 {

	private static final int SHA1_HEX_SIZE = 40;
	private static final int SHA1_BYTE_SIZE = 20;
	@Size(min = 20, max = 20)
	@NotNull
	@Column(name = "value")
	private final byte[] value;
	
	public Sha1(final byte[] sha1) {
		checkNotNull(sha1);
		checkArgument(sha1.length == SHA1_BYTE_SIZE, "sha1 is not 20 bytes");
		this.value = Arrays.copyOf(sha1, sha1.length);
	}
	
	@SuppressWarnings("unused")
	private Sha1() {
		// Required by Hibernate
		this.value = null;
	}
	
	public Sha1(final String sha1) {
		checkNotNull(sha1);
		checkArgument(sha1.length() == SHA1_HEX_SIZE, "sha1 is not 20 chars");
		try {
			this.value = Hex.decodeHex(sha1.toCharArray());
		} catch (final DecoderException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public final byte[] get() {
		final byte[] copy = Arrays.copyOf(value, value.length);
		return copy;
	}

	public final String getHex() {
		final String encoded = Hex.encodeHexString(value);
		return encoded;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).addValue(getHex()).toString();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(value);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		final Sha1 rhs = (Sha1) obj;
		return new EqualsBuilder().append(value, rhs.get()).isEquals();
	}
}
