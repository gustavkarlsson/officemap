package se.gustavkarlsson.officemap.events.map.set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.events.map.MapEvent;

@Entity
@Table(name = SetMapNameEvent.TYPE)
@DiscriminatorValue(SetMapNameEvent.TYPE)
public final class SetMapNameEvent extends MapEvent {
	public static final String TYPE = "SetMapNameEvent";

	private final String name;
	
	public SetMapNameEvent(final long timestamp, final int mapId, final String name) {
		super(timestamp, mapId);
		this.name = name;
	}

	@Override
	public String toString() {
		return "SetMapNameEvent [id=" + id + ", timestamp=" + timestamp + ", mapId=" + mapId + ", name=" + name + "]";
	}

}
