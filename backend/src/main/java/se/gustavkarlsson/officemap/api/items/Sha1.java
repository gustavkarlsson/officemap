package se.gustavkarlsson.officemap.api.items;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.base.MoreObjects;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.builder.EqualsBuilder;
import se.gustavkarlsson.officemap.api.items.Sha1.Sha1Deserializer;
import se.gustavkarlsson.officemap.api.items.Sha1.Sha1Serializer;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Embeddable
@JsonSerialize(using = Sha1Serializer.class)
@JsonDeserialize(using = Sha1Deserializer.class)
public final class Sha1 implements Buildable<Sha1> {
	
	private static final int SHA1_STRING_SIZE = 40;
	
	@NotNull
	@Column(name = "value")
	private final byte[] value;
	
	// Required by Hibernate
	private Sha1() {
		value = null;
	}
	
	private Sha1(final byte[] bytes) {
		this.value = Arrays.copyOf(bytes, bytes.length);
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
		return MoreObjects.toStringHelper(this).add("SHA-1", getHex()).toString();
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
		
		private String hex;
		
		protected Sha1Builder() {
		}
		
		@Override
		public Sha1 build() {
			checkNotNull(hex);
			checkArgument(hex.length() == SHA1_STRING_SIZE, "SHA-1 hex is not " + SHA1_STRING_SIZE + " chars: " + hex);
			try {
				return new Sha1(Hex.decodeHex(hex.toCharArray()));
			} catch (final DecoderException e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		public Sha1Builder withBytes(final byte[] bytes) {
			if (bytes == null) {
				this.hex = null;
			} else {
				this.hex = Hex.encodeHexString(bytes);
			}
			return this;
		}
		
		public Sha1Builder withHex(final String hex) {
			this.hex = hex;
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
	
	@SuppressWarnings("serial")
	static class Sha1Deserializer extends StdDeserializer<Sha1> {
		
		public Sha1Deserializer() {
			super(Sha1.class);
		}

		@Override
		public Sha1 deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException,
				JsonProcessingException {
			final JsonNode root = jp.getCodec().readTree(jp);
			return Sha1.builder().withHex(root.asText()).build();
		}
		
	}
}
