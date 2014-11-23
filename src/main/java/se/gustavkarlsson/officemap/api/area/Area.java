package se.gustavkarlsson.officemap.api.area;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

import se.gustavkarlsson.officemap.api.Item;
import se.gustavkarlsson.officemap.api.Reference;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;

@Entity
@Table(name = Area.TYPE)
@DiscriminatorValue(Area.TYPE)
@JsonSerialize(using = AreaSerializer.class)
@JsonDeserialize(using = AreaDeserializer.class)
public class Area extends Item<Area> {
	
	public static final String TYPE = "Area";

	@JsonProperty
	@NotBlank
	@Column(name = "name", nullable = false)
	private final String name;

	private Area() {
		// Required by Hibernate
		super(null, null, null, false);
		this.name = null;
	}

	private Area(@JsonProperty("id") final Long id, @JsonProperty("timestamp") final Long timestamp,
			@JsonProperty("reference") final Reference<Area> reference, @JsonProperty("deleted") final boolean deleted,
			@JsonProperty("name") final String name) {
		super(id, timestamp, reference, deleted);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", getId()).add("reference", getReference())
				.add("deleted", isDeleted()).add("name", name).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(isDeleted(), name);
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
		final Area rhs = (Area) obj;
		return new EqualsBuilder().append(isDeleted(), rhs.isDeleted()).append(name, rhs.name).isEquals();
	}
	
	public static class Builder {
		
		private Long id;
		
		private Reference<Area> reference;
		
		private Long timestamp;
		
		private boolean deleted;
		
		private String name;
		
		protected Builder(final Long id, final Long timestamp, final Reference<Area> reference, final boolean deleted,
				final String name) {
			this.id = id;
			this.timestamp = timestamp;
			this.reference = reference;
			this.deleted = deleted;
			this.name = name;
		}
		
		public static Builder fromNothing() {
			return new Builder(null, null, null, false, null);
		}
		
		public static Builder withFields(final Long id, final Long timestamp, final Reference<Area> reference,
				final boolean deleted, final String name) {
			return new Builder(id, timestamp, reference, deleted, name);
		}
		
		public static Builder fromArea(final Area area) {
			return new Builder(area.getId(), area.getTimestamp(), area.getReference(), area.isDeleted(), area.getName());
		}
		
		public Area build() {
			return new Area(id, timestamp, reference, deleted, name);
		}
		
		public Long getId() {
			return id;
		}
		
		public Builder withId(final Long id) {
			this.id = id;
			return this;
		}
		
		public Long getTimestamp() {
			return timestamp;
		}
		
		public Builder withTimestamp(final Long timestamp) {
			this.timestamp = timestamp;
			return this;
		}
		
		public Reference<Area> getReference() {
			return reference;
		}
		
		public Builder withReference(final Reference<Area> reference) {
			this.reference = reference;
			return this;
		}
		
		public boolean isDeleted() {
			return deleted;
		}
		
		public Builder withDeleted(final boolean deleted) {
			this.deleted = deleted;
			return this;
		}
		
		public String getName() {
			return name;
		}
		
		public Builder withName(final String name) {
			this.name = name;
			return this;
		}
	}

}
