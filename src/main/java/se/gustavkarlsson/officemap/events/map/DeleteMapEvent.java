package se.gustavkarlsson.officemap.events.map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = DeleteMapEvent.TYPE)
@DiscriminatorValue(DeleteMapEvent.TYPE)
public final class DeleteMapEvent extends MapEvent {
	public static final String TYPE = "DeleteMapEvent";

	public DeleteMapEvent(final long timestamp, final int mapId) {
		super(timestamp, mapId);
	}
	
	@Override
	public String toString() {
		return "DeleteMapEvent [id=" + id + ", timestamp=" + timestamp + ", mapId=" + mapId + "]";
	}

}
