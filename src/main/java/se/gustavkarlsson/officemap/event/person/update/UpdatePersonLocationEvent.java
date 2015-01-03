package se.gustavkarlsson.officemap.event.person.update;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.item.Location;
import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.api.item.Person.PersonBuilder;

@Entity
@Table(name = UpdatePersonLocationEvent.TYPE)
public final class UpdatePersonLocationEvent extends UpdatePersonEvent {
	public static final String TYPE = "UpdatePersonLocationEvent";

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "mapRef", column = @Column(name = "mapRef")),
			@AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
			@AttributeOverride(name = "longitude", column = @Column(name = "longitude")) })
	private final Location location;

	// Required by Hibernate
	private UpdatePersonLocationEvent() {
		super(0, 0);
		this.location = null;
	}

	public UpdatePersonLocationEvent(final long timestamp, final int ref, final Location location) {
		super(timestamp, ref);
		this.location = location;
	}

	public final Location getLocation() {
		return location;
	}

	@Override
	public void process(final State state) {
		checkNotNull(state);
		// TODO better error handling than checkState
		checkState(location != null && !state.getMaps().exists(location.getMapRef()),
				"No Map with ref: " + location.getMapRef() + " exists");
		super.process(state);
	}
	
	@Override
	protected Person updateProperty(final PersonBuilder builder) {
		return builder.withLocation(location).build();
	}

	@Override
	public String toString() {
		return "UpdatePersonLocationEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", location="
				+ location + "]";
	}

}
