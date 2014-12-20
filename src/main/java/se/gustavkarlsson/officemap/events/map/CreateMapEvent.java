package se.gustavkarlsson.officemap.events.map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = CreateMapEvent.TYPE)
@DiscriminatorValue(CreateMapEvent.TYPE)
public final class CreateMapEvent extends MapEvent {
	public static final String TYPE = "CreateMapEvent";
	
	public CreateMapEvent(final long timestamp, final int mapId) {
		super(timestamp, mapId);
	}

	@Override
	public String toString() {
		return "CreateMapEvent [id=" + id + ", timestamp=" + timestamp + ", mapId=" + mapId + "]";
	}
	
}
