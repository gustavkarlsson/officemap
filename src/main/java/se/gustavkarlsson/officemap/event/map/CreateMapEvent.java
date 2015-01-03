package se.gustavkarlsson.officemap.event.map;

import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.item.Map;
import se.gustavkarlsson.officemap.event.ItemEvent;

@Entity
@Table(name = CreateMapEvent.TYPE)
public final class CreateMapEvent extends ItemEvent {
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
		state.getMaps().create(ref, Map.builder().build());
	}

	@Override
	public String toString() {
		return "CreateMapEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + "]";
	}
	
}
