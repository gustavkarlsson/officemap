package se.gustavkarlsson.officemap.api.event.map;

import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.State;

@Entity
@Table(name = CreateMapEvent.TYPE)
public final class CreateMapEvent extends MapEvent {
	public static final String TYPE = "CreateMapEvent";

	// Required by Hibernate
	private CreateMapEvent() {
		super(0, 0);
	}
	
	public CreateMapEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}
	
	@Override
	public void process(final State state) {
		// TODO Auto-generated method stub
	}

	@Override
	public String toString() {
		return "CreateMapEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + "]";
	}
	
}
