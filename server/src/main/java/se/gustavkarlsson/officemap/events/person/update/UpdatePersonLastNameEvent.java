package se.gustavkarlsson.officemap.events.person.update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import se.gustavkarlsson.officemap.api.items.Person;
import se.gustavkarlsson.officemap.api.items.Person.PersonBuilder;

@Entity
@Table(name = UpdatePersonLastNameEvent.TYPE)
public final class UpdatePersonLastNameEvent extends UpdatePersonEvent {
	public static final String TYPE = "UpdatePersonLastNameEvent";
	
	@NotBlank
	@Column(name = "lastName", nullable = false)
	private final String lastName;

	// Required by Hibernate
	private UpdatePersonLastNameEvent() {
		super(0, 0);
		this.lastName = null;
	}
	
	public UpdatePersonLastNameEvent(final long timestamp, final int ref, final String lastName) {
		super(timestamp, ref);
		this.lastName = lastName;
	}

	public final String getLastName() {
		return lastName;
	}

	@Override
	protected Person updateProperty(final PersonBuilder builder) {
		return builder.withLastName(lastName).build();
	}
	
	@Override
	public String toString() {
		return "UpdatePersonLastNameEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", lastName="
				+ lastName + "]";
	}

}
