package se.gustavkarlsson.officemap.api.item.map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

import se.gustavkarlsson.officemap.api.Sha1;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;

@JsonSerialize(using = MapSerializer.class)
public class Map {
	
	@NotBlank
	private final String name;
	
	@NotNull
	private final Sha1 map;
	
	private Map(final String name, final Sha1 map) {
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
		return Objects.toStringHelper(this).add("name", name).add("map", map).toString();
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
		return builder().with(name, map);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String name;

		private Sha1 map;

		protected Builder() {
		}

		public Map build() {
			return new Map(name, map);
		}

		public Builder with(final String name, final Sha1 map) {
			this.name = name;
			this.map = map;
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
