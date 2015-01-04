package se.gustavkarlsson.officemap.event.map;

import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.item.Location;
import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.event.ItemEvent;

@Entity
@Table(name = DeleteMapEvent.TYPE)
public final class DeleteMapEvent extends ItemEvent {
	public static final String TYPE = "DeleteMapEvent";

	// Required by Hibernate
	private DeleteMapEvent() {
		super(0, 0);
	}

	public DeleteMapEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}

	@Override
	public void process(final State state) {
		for (final Entry<Integer, Person> entry : state.getPersons().getAll().entrySet()) {
			final int personRef = entry.getKey();
			final Person person = entry.getValue();
			if (isOnMap(person)) {
				final Person newPerson = person.toBuilder().withLocation(null).build();
				state.getPersons().replace(personRef, newPerson);
			}
		}
		state.getMaps().delete(ref);
	}

	protected boolean isOnMap(final Person person) {
		final Location location = person.getLocation();
		return location != null && location.getMapRef() == ref;
	}
	
	@Override
	public String toString() {
		return "DeleteMapEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + "]";
	}

}
