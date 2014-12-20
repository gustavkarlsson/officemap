package se.gustavkarlsson.officemap.events.map.set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.events.map.MapEvent;

@Entity
@Table(name = SetMapImageEvent.TYPE)
@DiscriminatorValue(SetMapImageEvent.TYPE)
public final class SetMapImageEvent extends MapEvent {
	public static final String TYPE = "SetMapImageEvent";
	
	private final Sha1 image;

	public SetMapImageEvent(final long timestamp, final int mapId, final Sha1 image) {
		super(timestamp, mapId);
		this.image = image;
	}
	
	@Override
	public String toString() {
		return "SetMapImageEvent [id=" + id + ", timestamp=" + timestamp + ", mapId=" + mapId + ", image=" + image
				+ "]";
	}
	
}
