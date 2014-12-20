package se.gustavkarlsson.officemap.events.person;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = DeletePersonEvent.TYPE)
@DiscriminatorValue(DeletePersonEvent.TYPE)
public final class DeletePersonEvent extends PersonEvent {
	public static final String TYPE = "DeletePersonEvent";
	
	public DeletePersonEvent(final long timestamp, final int personId) {
		super(timestamp, personId);
	}

	@Override
	public String toString() {
		return "DeletePersonEvent [id=" + id + ", timestamp=" + timestamp + ", personId=" + personId + "]";
	}
	
}
