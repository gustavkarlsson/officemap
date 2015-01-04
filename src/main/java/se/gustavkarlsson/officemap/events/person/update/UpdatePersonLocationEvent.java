package se.gustavkarlsson.officemap.events.person.update;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.api.item.Location;
import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.api.item.Person.PersonBuilder;
import se.gustavkarlsson.officemap.core.State;

@Entity
@Table(name = UpdatePersonLocationEvent.TYPE)
public final class UpdatePersonLocationEvent extends UpdatePersonEvent {
	public static final String TYPE = "UpdatePersonLocationEvent";

	@Embedded
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
		if (location != null && !state.getMaps().exists(location.getMapRef())) {
			throw new MapRefNotFoundException(location.getMapRef());
		}
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
