package se.gustavkarlsson.officemap.event.person.update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.api.item.Person.PersonBuilder;

@Entity
@Table(name = UpdatePersonEmailEvent.TYPE)
public final class UpdatePersonEmailEvent extends UpdatePersonEvent {
	public static final String TYPE = "UpdatePersonEmailEvent";
	
	@NotNull
	@Email
	@Column(name = "email", nullable = false)
	private final String email;

	// Required by Hibernate
	private UpdatePersonEmailEvent() {
		super(0, 0);
		this.email = null;
	}
	
	public UpdatePersonEmailEvent(final long timestamp, final int ref, final String email) {
		super(timestamp, ref);
		this.email = email;
	}

	public final String getEmail() {
		return email;
	}

	@Override
	protected Person updateProperty(final PersonBuilder builder) {
		return builder.withEmail(email).build();
	}
	
	@Override
	public String toString() {
		return "UpdatePersonEmailEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", email=" + email
				+ "]";
	}

}
