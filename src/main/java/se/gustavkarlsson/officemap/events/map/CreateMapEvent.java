package se.gustavkarlsson.officemap.events.map;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.common.base.Objects;

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
		return "CreateMapEvent [id=" + id + ", timestamp=" + timestamp
				+ ", ref=" + ref + ", map=" + map + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(timestamp, ref, map);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		final CreateMapEvent rhs = (CreateMapEvent) obj;
		return new EqualsBuilder().append(timestamp, rhs.timestamp)
				.append(ref, rhs.ref).append(map, rhs.map).isEquals();
	}

}
