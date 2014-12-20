package se.gustavkarlsson.officemap.api;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.common.base.Objects;

public final class Coordinates {
	
	private final double latitude;
	
	private final double longitude;
	
	private Coordinates(final double latitude, final double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public final double getLatitude() {
		return latitude;
	}

	public final double getLongitude() {
		return longitude;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("latitude", latitude).add("longitude", longitude).toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(latitude, longitude);
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
		final Coordinates rhs = (Coordinates) obj;
		return new EqualsBuilder().append(latitude, rhs.latitude).append(longitude, rhs.longitude).isEquals();
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private double latitude;

		private double longitude;

		protected Builder() {
		}
		
		public Coordinates build() {
			return new Coordinates(latitude, longitude);
		}
		
		public Builder withLatLng(final double latitude, final double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
			return this;
		}
	}
}
