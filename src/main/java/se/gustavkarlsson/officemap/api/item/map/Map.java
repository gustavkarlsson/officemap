package se.gustavkarlsson.officemap.api.item.map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.item.Item;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;

@JsonSerialize(using = MapSerializer.class)
@JsonDeserialize(using = MapDeserializer.class)
public class Map extends Item {

	@NotBlank
	private final String name;

	@NotNull
	private final Sha1 map;

	private Map(final Long id, final String name, final Sha1 map) {
		super(id);
		this.name = name;
		this.map = map;
	}

	public String getName() {
		return name;
	}

	public Sha1 getMap() {
		return map;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", getId()).add("name", name).add("map", map).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name, map);
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
		final Map rhs = (Map) obj;
		return new EqualsBuilder().append(name, rhs.name).append(map, rhs.map).isEquals();
	}
	
	public Builder toBuilder() {
		return builder().with(getId(), name, map);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private Long id;
		
		private String name;
		
		private Sha1 map;
		
		protected Builder() {
		}
		
		public Map build() {
			return new Map(id, name, map);
		}
		
		public Builder with(final Long id, final String name, final Sha1 map) {
			this.id = id;
			this.name = name;
			this.map = map;
			return this;
		}
		
		public Builder withId(final Long id) {
			this.id = id;
			return this;
		}
		
		public Builder withName(final String name) {
			this.name = name;
			return this;
		}
		
		public Builder withMap(final Sha1 map) {
			this.map = map;
			return this;
		}
	}

}
