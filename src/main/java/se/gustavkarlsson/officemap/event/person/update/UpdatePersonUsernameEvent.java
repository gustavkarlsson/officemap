package se.gustavkarlsson.officemap.event.person.update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.api.item.Person.PersonBuilder;

@Entity
@Table(name = UpdatePersonUsernameEvent.TYPE)
public final class UpdatePersonUsernameEvent extends UpdatePersonEvent {
	public static final String TYPE = "UpdatePersonUsernameEvent";
	
	@Column(name = "username", nullable = false)
	private final String username;

	// Required by Hibernate
	private UpdatePersonUsernameEvent() {
		super(0, 0);
		this.username = null;
	}
	
	public UpdatePersonUsernameEvent(final long timestamp, final int ref, final String username) {
		super(timestamp, ref);
		this.username = username;
	}

	public final String getUsername() {
		return username;
	}
	
	@Override
	protected Person updateProperty(final PersonBuilder builder) {
		return builder.withUsername(username).build();
	}
	
	@Override
	public String toString() {
		return "UpdatePersonUsernameEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", username="
				+ username + "]";
	}

}
