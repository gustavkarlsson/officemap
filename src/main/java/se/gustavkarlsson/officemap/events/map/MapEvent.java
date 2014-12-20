package se.gustavkarlsson.officemap.events.map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.events.Event;

@Entity
@Table(name = MapEvent.TYPE)
public abstract class MapEvent extends Event {
	public static final String TYPE = "MapEvent";
	
	@Column(name = "mapId")
	protected final int mapId;

	protected MapEvent(final long timestamp, final int mapId) {
		super(timestamp);
		this.mapId = mapId;
	}

}
