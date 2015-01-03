package se.gustavkarlsson.officemap.event.map.update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import se.gustavkarlsson.officemap.api.item.Map;
import se.gustavkarlsson.officemap.api.item.Map.MapBuilder;

@Entity
@Table(name = UpdateMapNameEvent.TYPE)
public final class UpdateMapNameEvent extends UpdateMapEvent {
	public static final String TYPE = "UpdateMapNameEvent";

	@NotNull
	@Column(name = "name", nullable = false)
	private final String name;

	// Required by Hibernate
	private UpdateMapNameEvent() {
		super(0, 0);
		this.name = null;
	}

	public UpdateMapNameEvent(final long timestamp, final int ref, final String name) {
		super(timestamp, ref);
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	@Override
	protected Map updateProperty(final MapBuilder builder) {
		return builder.withName(name).build();
	}

	@Override
	public String toString() {
		return "UpdateMapNameEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", name=" + name + "]";
	}

}
