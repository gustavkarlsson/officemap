package se.gustavkarlsson.officemap.events.person.set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.events.person.PersonEvent;

@Entity
@Table(name = SetPersonUsernameEvent.TYPE)
@DiscriminatorValue(SetPersonUsernameEvent.TYPE)
public final class SetPersonUsernameEvent extends PersonEvent {
	public static final String TYPE = "SetPersonUsernameEvent";
	
	private final String username;

	public SetPersonUsernameEvent(final long timestamp, final int personId, final String username) {
		super(timestamp, personId);
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "SetPersonUsernameEvent [id=" + id + ", timestamp=" + timestamp + ", personId=" + personId
				+ ", username=" + username + "]";
	}
	
}
