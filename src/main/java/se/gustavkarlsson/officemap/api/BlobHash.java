package se.gustavkarlsson.officemap.api;

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
import org.hibernate.validator.constraints.Range;

import com.google.common.base.Objects;

@Entity
@Table(name = BlobHash.TYPE)
public final class BlobHash {

	public static final String TYPE = "BlobHash";

	@Range(min = 0)
	@Id
	@Column(name = "id")
	@TableGenerator(name = "TABLE_GEN", table = "Sequence", pkColumnName = "name", valueColumnName = "count",
			pkColumnValue = "BlobHashId")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private final Long id;

	@Size(min = 20, max = 20)
	@NotNull
	@Column(name = "sha1")
	private final byte[] sha1;
	
	private BlobHash() {
		// Required by Hibernate
		this.id = null;
		this.sha1 = null;
	}
	
	private BlobHash(final Long id, final byte[] sha1) {
		checkNotNull(sha1);
		this.id = id;
		this.sha1 = Arrays.copyOf(sha1, sha1.length);
	}
	
	public final Long getId() {
		return id;
	}

	public final byte[] getSha1() {
		final byte[] copy = Arrays.copyOf(sha1, sha1.length);
		return copy;
	}

	public final String getSha1Hexadecimal() {
		final String encoded = Hex.encodeHexString(sha1);
		return encoded;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).add("sha1", sha1).toString();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(sha1);
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
		final BlobHash rhs = (BlobHash) obj;
		return new EqualsBuilder().append(sha1, rhs.getSha1()).isEquals();
	}

	public static class Builder {
		
		private Long id;
		
		private byte[] sha1;
		
		protected Builder(final Long id, final byte[] sha1) {
			this.id = id;
			this.sha1 = sha1;
		}
		
		public static Builder fromNothing() {
			return new Builder(null, null);
		}
		
		public static Builder withFields(final Long id, final byte[] sha1) {
			return new Builder(id, sha1);
		}
		
		public static Builder fromBlobHash(final BlobHash blobHash) {
			return new Builder(blobHash.getId(), blobHash.getSha1());
		}
		
		public BlobHash build() {
			return new BlobHash(id, sha1);
		}
		
		public Long getId() {
			return id;
		}
		
		public Builder withId(final Long id) {
			this.id = id;
			return this;
		}
		
		public byte[] getSha1() {
			return sha1;
		}
		
		public Builder withSha1(final byte[] sha1) {
			this.sha1 = sha1;
			return this;
		}
		
		public Builder withSha1(final String sha1Hexadecimal) throws DecoderException {
			final byte[] decoded = Hex.decodeHex(sha1Hexadecimal.toCharArray());
			this.sha1 = decoded;
			return this;
		}
	}
}
