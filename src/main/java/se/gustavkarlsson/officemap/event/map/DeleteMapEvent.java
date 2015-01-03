package se.gustavkarlsson.officemap.event.map;

import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.State;
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
		state.getMaps().delete(ref);
	}
	
	@Override
	public String toString() {
		return "DeleteMapEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + "]";
	}

}
