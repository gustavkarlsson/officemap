package se.gustavkarlsson.officemap.api.event.person.update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.api.item.Person.Builder;

@Entity
@Table(name = UpdatePersonFirstNameEvent.TYPE)
public final class UpdatePersonFirstNameEvent extends UpdatePersonEvent {
	public static final String TYPE = "UpdatePersonFirstNameEvent";
	
	@NotNull
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
	protected Person updateProperty(final Builder builder) {
		return builder.withFirstName(firstName).build();
	}
	
	@Override
	public String toString() {
		return "UpdatePersonFirstNameEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", firstName="
				+ firstName + "]";
	}
	
}
