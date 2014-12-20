package se.gustavkarlsson.officemap.events.person;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = CreatePersonEvent.TYPE)
@DiscriminatorValue(CreatePersonEvent.TYPE)
public final class CreatePersonEvent extends PersonEvent {
	public static final String TYPE = "CreatePersonEvent";

	public CreatePersonEvent(final long timestamp, final int personId) {
		super(timestamp, personId);
	}
	
	@Override
	public String toString() {
		return "CreatePersonEvent [id=" + id + ", timestamp=" + timestamp + ", personId=" + personId + "]";
	}

}
