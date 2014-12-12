package se.gustavkarlsson.officemap.api.fileentry;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;

@Entity
@Table(name = FileEntry.TYPE)
@JsonSerialize(using = FileEntrySerializer.class)
@JsonDeserialize(using = FileEntryDeserializer.class)
public final class FileEntry {

	public static final String TYPE = "FileEntry";
	
	private static final int SHA1_BYTE_SIZE = 20;
	
	@Range(min = 0)
	@Id
	@Column(name = "id")
	@TableGenerator(name = "TABLE_GEN", table = "Sequence", pkColumnName = "name", valueColumnName = "count",
			pkColumnValue = "FileEntryId")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private final Long id;

	@NotBlank
	@Column(name = "mimeType")
	private final String mimeType;

	@Size(min = 20, max = 20)
	@NotNull
	@Column(name = "sha1")
	private final byte[] sha1;

	private FileEntry() {
		// Required by Hibernate
		this.id = null;
		this.mimeType = null;
		this.sha1 = null;
	}

	private FileEntry(final Long id, final String mimeType, final byte[] sha1) {
		checkNotNull(mimeType);
		checkNotNull(sha1);
		checkArgument(sha1.length == SHA1_BYTE_SIZE, "sha1 is not 20 bytes");
		this.id = id;
		this.mimeType = mimeType;
		this.sha1 = Arrays.copyOf(sha1, sha1.length);
	}
	
	public final Long getId() {
		return id;
	}
	
	public final String getMimeType() {
		return mimeType;
	}
	
	public final byte[] getSha1() {
		final byte[] copy = Arrays.copyOf(sha1, sha1.length);
		return copy;
	}
	
	public final String getSha1Hex() {
		final String encoded = Hex.encodeHexString(sha1);
		return encoded;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("MIME", mimeType).add("SHA-1", getSha1Hex()).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(Arrays.hashCode(sha1), mimeType);
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
		final FileEntry rhs = (FileEntry) obj;
		return new EqualsBuilder().append(sha1, rhs.sha1).append(mimeType, rhs.mimeType).isEquals();
	}
	
	public Builder toBuilder() {
		return builder().with(getId(), mimeType, sha1);
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private Long id;
		
		private String mimeType;
		
		private byte[] sha1;

		protected Builder() {
		}
		
		public FileEntry build() {
			return new FileEntry(id, mimeType, sha1);
		}
		
		public Builder with(final Long id, final String mimeType, final byte[] sha1) {
			this.id = id;
			this.mimeType = mimeType;
			this.sha1 = sha1;
			return this;
		}
		
		public Builder with(final Long id, final String mimeType, final String sha1Hex) {
			try {
				this.sha1 = Hex.decodeHex(sha1Hex.toCharArray());
				return with(id, mimeType, sha1);
			} catch (final DecoderException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		
		public Builder withId(final Long id) {
			this.id = id;
			return this;
		}
		
		public Builder withMimeType(final String mimeType) {
			this.mimeType = mimeType;
			return this;
		}
		
		public Builder withSha1(final byte[] sha1) {
			this.sha1 = sha1;
			return this;
		}
		
		public Builder withSha1Hex(final String sha1Hex) {
			try {
				this.sha1 = Hex.decodeHex(sha1Hex.toCharArray());
			} catch (final DecoderException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
			return this;
		}
	}
}
