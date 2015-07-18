package se.gustavkarlsson.officemap.events.person;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import se.gustavkarlsson.officemap.api.items.Location;
import se.gustavkarlsson.officemap.api.items.Person;
import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.events.ItemEvent;
import se.gustavkarlsson.officemap.events.person.update.MapRefNotFoundException;

@Entity
@Table(name = CreatePersonEvent.TYPE)
public final class CreatePersonEvent extends ItemEvent {
	public static final String TYPE = "CreatePersonEvent";

	@NotNull
	@Valid
	@Embedded
	private Person person;

	// Required by Hibernate
	private CreatePersonEvent() {
		super(0, 0);
	}

	public CreatePersonEvent(final long timestamp, final int ref, final Person person) {
		super(timestamp, ref);
		this.person = person;
	}

	@Override
	public void process(final State state) {
		final Location location = person.getLocation();
		if (location != null && !state.getMaps().contains(location.getMapRef())) {
			throw new MapRefNotFoundException(location.getMapRef());
		}
		state.getPersons().create(ref, person);
	}
	
	@Override
	public String toString() {
		return "CreatePersonEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", person=" + person + "]";
	}

}
