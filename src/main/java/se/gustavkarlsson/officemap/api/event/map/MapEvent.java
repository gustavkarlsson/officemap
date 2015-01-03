package se.gustavkarlsson.officemap.api.event.map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Range;

import se.gustavkarlsson.officemap.api.event.Event;

@Entity
@Table(name = MapEvent.TYPE)
public abstract class MapEvent extends Event {
	public static final String TYPE = "MapEvent";

	@Range(min = 0)
	@Column(name = "ref")
	protected final int ref;
	
	protected MapEvent(final long timestamp, final int ref) {
		super(timestamp);
		this.ref = ref;
	}
	
	public final int getRef() {
		return ref;
	}
	
}
