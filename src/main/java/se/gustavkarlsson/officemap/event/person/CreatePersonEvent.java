package se.gustavkarlsson.officemap.event.person;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.event.ItemEvent;

@Entity
@Table(name = CreatePersonEvent.TYPE)
public final class CreatePersonEvent extends ItemEvent {
	public static final String TYPE = "CreatePersonEvent";

	@NotNull
	@Embedded
	private Person person;

	// Required by Hibernate
	private CreatePersonEvent() {
		super(0, 0);
	}

	public CreatePersonEvent(final long timestamp, final int ref, final Person person) {
		super(timestamp, ref);
		this.person = person;
	}

	@Override
	public void process(final State state) {
		state.getPersons().create(ref, person);
	}
	
	@Override
	public String toString() {
		return "CreatePersonEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", person=" + person + "]";
	}

}
