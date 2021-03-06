package se.gustavkarlsson.officemap.events.person.update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import se.gustavkarlsson.officemap.api.items.Person;
import se.gustavkarlsson.officemap.api.items.Person.PersonBuilder;

@Entity
@Table(name = UpdatePersonFirstNameEvent.TYPE)
public final class UpdatePersonFirstNameEvent extends UpdatePersonEvent {
	public static final String TYPE = "UpdatePersonFirstNameEvent";
	
	@NotBlank
	@Column(name = "firstName", nullable = false)
	private final String firstName;

	// Required by Hibernate
	private UpdatePersonFirstNameEvent() {
		super(0, 0);
		this.firstName = null;
	}
	
	public UpdatePersonFirstNameEvent(final long timestamp, final int ref, final String firstName) {
		super(timestamp, ref);
		this.firstName = firstName;
	}

	public final String getFirstName() {
		return firstName;
	}
	
	@Override
	protected Person updateProperty(final PersonBuilder builder) {
		return builder.withFirstName(firstName).build();
	}

	@Override
	public String toString() {
		return "UpdatePersonFirstNameEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", firstName="
				+ firstName + "]";
	}

}
