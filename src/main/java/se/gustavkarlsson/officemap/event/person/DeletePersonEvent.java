package se.gustavkarlsson.officemap.event.person;

import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.State;

@Entity
@Table(name = DeletePersonEvent.TYPE)
public final class DeletePersonEvent extends PersonEvent {
	public static final String TYPE = "DeletePersonEvent";

	// Required by Hibernate
	private DeletePersonEvent() {
		super(0, 0);
	}
	
	public DeletePersonEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}

	@Override
	public void process(final State state) {
		state.getPersons().delete(ref);
	}

	@Override
	public String toString() {
		return "DeletePersonEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + "]";
	}
	
}
