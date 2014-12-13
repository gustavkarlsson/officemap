package se.gustavkarlsson.officemap.api;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.common.base.Objects;

@Embeddable
public final class Sha1 {

	private static final int SHA1_BYTE_SIZE = 20;
	
	@Column(name = "sha1")
	private final byte[] value;
	
	private Sha1() {
		// Required by Hibernate
		this.value = null;
	}
	
	private Sha1(final byte[] sha1) {
		checkNotNull(sha1);
		checkArgument(sha1.length == SHA1_BYTE_SIZE, "sha1 is not 20 bytes");
		this.value = Arrays.copyOf(sha1, sha1.length);
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
		return Objects.toStringHelper(this).add("SHA-1", getHex()).toString();
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
		return new EqualsBuilder().append(value, rhs.value).isEquals();
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private byte[] sha1;

		protected Builder() {
		}
		
		public Sha1 build() {
			return new Sha1(sha1);
		}
		
		public Builder withSha1(final byte[] sha1) {
			this.sha1 = sha1;
			return this;
		}
		
		public Builder withSha1(final String sha1Hex) {
			try {
				this.sha1 = Hex.decodeHex(sha1Hex.toCharArray());
			} catch (final DecoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new IllegalArgumentException(e);
			}
			return this;
		}
	}
}
