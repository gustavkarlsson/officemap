package se.gustavkarlsson.officemap.events.person.set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.events.person.PersonEvent;

@Entity
@Table(name = SetPersonFirstNameEvent.TYPE)
@DiscriminatorValue(SetPersonFirstNameEvent.TYPE)
public final class SetPersonFirstNameEvent extends PersonEvent {
	public static final String TYPE = "SetPersonFirstNameEvent";

	private final String firstName;
	
	public SetPersonFirstNameEvent(final long timestamp, final int personId, final String firstName) {
		super(timestamp, personId);
		this.firstName = firstName;
	}

	@Override
	public String toString() {
		return "SetPersonFirstNameEvent [id=" + id + ", timestamp=" + timestamp + ", personId=" + personId
				+ ", firstName=" + firstName + "]";
	}

}
