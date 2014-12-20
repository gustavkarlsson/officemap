package se.gustavkarlsson.officemap.events.person.set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.events.person.PersonEvent;

@Entity
@Table(name = SetPersonLastNameEvent.TYPE)
@DiscriminatorValue(SetPersonLastNameEvent.TYPE)
public final class SetPersonLastNameEvent extends PersonEvent {
	public static final String TYPE = "SetPersonLastNameEvent";
	
	private final String lastName;

	public SetPersonLastNameEvent(final long timestamp, final int personId, final String lastName) {
		super(timestamp, personId);
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return "SetPersonLastNameEvent [id=" + id + ", timestamp=" + timestamp + ", personId=" + personId
				+ ", lastName=" + lastName + "]";
	}
	
}
