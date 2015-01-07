package se.gustavkarlsson.officemap.events.map;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import se.gustavkarlsson.officemap.api.items.Map;
import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.events.ItemEvent;

@Entity
@Table(name = CreateMapEvent.TYPE)
public final class CreateMapEvent extends ItemEvent {
	public static final String TYPE = "CreateMapEvent";

	@NotNull
	@Valid
	@Embedded
	private Map map;

	// Required by Hibernate
	private CreateMapEvent() {
		super(0, 0);
	}
	
	public CreateMapEvent(final long timestamp, final int ref, final Map map) {
		super(timestamp, ref);
		this.map = map;
	}
	
	@Override
	public void process(final State state) {
		state.getMaps().create(ref, map);
	}

	@Override
	public String toString() {
		return "CreateMapEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", map=" + map + "]";
	}
	
}
