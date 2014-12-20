package se.gustavkarlsson.officemap.events.person.set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.events.person.PersonEvent;

@Entity
@Table(name = SetPersonEmailEvent.TYPE)
@DiscriminatorValue(SetPersonEmailEvent.TYPE)
public final class SetPersonEmailEvent extends PersonEvent {
	public static final String TYPE = "SetPersonEmailEvent";

	private final String email;
	
	public SetPersonEmailEvent(final long timestamp, final int personId, final String email) {
		super(timestamp, personId);
		this.email = email;
	}

	@Override
	public String toString() {
		return "SetPersonEmailEvent [id=" + id + ", timestamp=" + timestamp + ", personId=" + personId + ", email="
				+ email + "]";
	}

}
