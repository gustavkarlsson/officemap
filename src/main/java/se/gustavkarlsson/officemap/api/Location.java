package se.gustavkarlsson.officemap.api;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.common.base.Objects;

@Embeddable
public final class Location {

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

	public final int getMapRef() {
		return mapRef;
	}

	public final double getLatitude() {
		return latitude;
	}

	public final double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("mapRef", mapRef).add("latitude", latitude).add("longitude", longitude)
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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private Integer mapRef;

		private Double latitude;

		private Double longitude;

		protected Builder() {
		}

		public Location build() {
			return new Location(mapRef, latitude, longitude);
		}

		public Builder with(final Integer mapRef, final Double latitude, final Double longitude) {
			this.mapRef = mapRef;
			this.latitude = latitude;
			this.longitude = longitude;
			return this;
		}

		public Builder withMapRef(final Integer mapRef) {
			this.mapRef = mapRef;
			return this;
		}

		public Builder withCoordinates(final Double latitude, final Double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
			return this;
		}
	}
}
