package se.gustavkarlsson.officemap.events.person;

import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.events.ItemEvent;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = DeletePersonEvent.TYPE)
public final class DeletePersonEvent extends ItemEvent {
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
