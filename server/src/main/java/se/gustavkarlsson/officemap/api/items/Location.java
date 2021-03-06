package se.gustavkarlsson.officemap.api.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public final class Location implements Buildable<Location> {

	@Column(name = "mapRef")
	private final int mapRef;

	@Column(name = "latitude")
	private final double latitude;

	@Column(name = "longitude")
	private final double longitude;

	// Required by Hibernate
	private Location() {
		this.mapRef = 0;
		this.latitude = 0;
		this.longitude = 0;
	}

	private Location(final int mapRef, final double latitude, final double longitude) {
		this.mapRef = mapRef;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@JsonProperty("mapRef")
	public final int getMapRef() {
		return mapRef;
	}

	@JsonProperty("latitude")
	public final double getLatitude() {
		return latitude;
	}

	@JsonProperty("longitude")
	public final double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("mapRef", mapRef).add("latitude", latitude).add("longitude", longitude)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(mapRef, latitude, longitude);
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
		final Location rhs = (Location) obj;
		return new EqualsBuilder().append(mapRef, rhs.mapRef).append(latitude, rhs.latitude)
				.append(longitude, rhs.longitude).isEquals();
	}

	@Override
	public LocationBuilder toBuilder() {
		return builder().with(mapRef, latitude, longitude);
	}

	public static LocationBuilder builder() {
		return new LocationBuilder();
	}

	public static class LocationBuilder implements Builder<Location> {

		private Integer mapRef;

		private Double latitude;

		private Double longitude;

		protected LocationBuilder() {
		}

		@Override
		public Location build() {
			return new Location(mapRef, latitude, longitude);
		}

		public LocationBuilder with(final Integer mapRef, final Double latitude, final Double longitude) {
			this.mapRef = mapRef;
			this.latitude = latitude;
			this.longitude = longitude;
			return this;
		}

		public LocationBuilder withMapRef(final Integer mapRef) {
			this.mapRef = mapRef;
			return this;
		}

		public LocationBuilder withCoordinates(final Double latitude, final Double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
			return this;
		}
	}
}
