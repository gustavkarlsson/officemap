package se.gustavkarlsson.officemap.api.item;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.builder.EqualsBuilder;

import se.gustavkarlsson.officemap.api.item.Sha1.Sha1Serializer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.base.Objects;

@Embeddable
@JsonSerialize(using = Sha1Serializer.class)
public final class Sha1 implements Buildable<Sha1> {

	private static final int SHA1_BYTE_SIZE = 20;

	@NotNull
	@Column(name = "value")
	private final byte[] value;

	// Required by Hibernate
	private Sha1() {
		value = null;
	}

	private Sha1(final byte[] sha1) {
		checkNotNull(sha1);
		checkArgument(sha1.length == SHA1_BYTE_SIZE, "sha1 is not 20 bytes");
		this.value = Arrays.copyOf(sha1, sha1.length);
	}

	public final byte[] getBytes() {
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

	@Override
	public Sha1Builder toBuilder() {
		return new Sha1Builder();
	}

	public static Sha1Builder builder() {
		return new Sha1Builder();
	}

	public static class Sha1Builder implements Builder<Sha1> {

		private byte[] sha1;

		protected Sha1Builder() {
		}

		@Override
		public Sha1 build() {
			return new Sha1(sha1);
		}

		public Sha1Builder withSha1(final byte[] sha1) {
			this.sha1 = sha1;
			return this;
		}

		public Sha1Builder withSha1(final String sha1Hex) {
			try {
				this.sha1 = Hex.decodeHex(sha1Hex.toCharArray());
			} catch (final DecoderException e) {
				throw new IllegalArgumentException(e);
			}
			return this;
		}
	}

	static class Sha1Serializer extends StdSerializer<Sha1> {

		public Sha1Serializer() {
			super(Sha1.class);
		}
		
		@Override
		public void serialize(final Sha1 value, final JsonGenerator jgen, final SerializerProvider provider)
				throws IOException, JsonGenerationException {
			jgen.writeString(value.getHex());
		}

	}
}
