package se.gustavkarlsson.officemap.api.event.person.update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import se.gustavkarlsson.officemap.api.item.person.Person;
import se.gustavkarlsson.officemap.api.item.person.Person.Builder;

@Entity
@Table(name = UpdatePersonLastNameEvent.TYPE)
public final class UpdatePersonLastNameEvent extends UpdatePersonEvent {
	public static final String TYPE = "UpdatePersonLastNameEvent";

	@NotNull
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
	protected Person updateProperty(final Builder builder) {
		return builder.withLastName(lastName).build();
	}

	@Override
	public String toString() {
		return "UpdatePersonLastNameEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", lastName="
				+ lastName + "]";
	}
	
}
