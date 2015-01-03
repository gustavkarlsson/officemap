package se.gustavkarlsson.officemap.api.event.person.update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import se.gustavkarlsson.officemap.api.item.person.Person;
import se.gustavkarlsson.officemap.api.item.person.Person.Builder;

@Entity
@Table(name = UpdatePersonUsernameEvent.TYPE)
public final class UpdatePersonUsernameEvent extends UpdatePersonEvent {
	public static final String TYPE = "UpdatePersonUsernameEvent";
	
	@NotNull
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
	protected Person updateProperty(final Builder builder) {
		return builder.withUsername(username).build();
	}
	
	@Override
	public String toString() {
		return "UpdatePersonUsernameEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", username="
				+ username + "]";
	}

}
