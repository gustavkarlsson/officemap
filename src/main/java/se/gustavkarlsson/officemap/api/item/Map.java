package se.gustavkarlsson.officemap.api.item;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class Map implements Buildable<Map> {

	@NotBlank
	private final String name;

	@NotNull
	private final Sha1 image;

	private Map(final String name, final Sha1 image) {
		this.name = name;
		this.image = image;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("image")
	public Sha1 getImage() {
		return image;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("name", name).add("image", image).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name, image);
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
		return new EqualsBuilder().append(name, rhs.name).append(image, rhs.image).isEquals();
	}
	
	@Override
	public MapBuilder toBuilder() {
		return builder().with(name, image);
	}
	
	public static MapBuilder builder() {
		return new MapBuilder();
	}
	
	public static class MapBuilder implements Builder<Map> {
		
		private String name;
		
		private Sha1 image;
		
		protected MapBuilder() {
		}
		
		@Override
		public Map build() {
			return new Map(name, image);
		}
		
		public MapBuilder with(final String name, final Sha1 image) {
			this.name = name;
			this.image = image;
			return this;
		}
		
		public MapBuilder withName(final String name) {
			this.name = name;
			return this;
		}
		
		public MapBuilder withImage(final Sha1 image) {
			this.image = image;
			return this;
		}
	}

}
