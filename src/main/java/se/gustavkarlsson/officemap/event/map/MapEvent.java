package se.gustavkarlsson.officemap.event.map;

import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.event.ItemEvent;

@Entity
@Table(name = MapEvent.TYPE)
public abstract class MapEvent extends ItemEvent {
	public static final String TYPE = "MapEvent";

	protected MapEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}

}
