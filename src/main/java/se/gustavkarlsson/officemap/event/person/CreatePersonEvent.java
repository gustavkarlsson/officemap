package se.gustavkarlsson.officemap.event.person;

import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.event.ItemEvent;

@Entity
@Table(name = CreatePersonEvent.TYPE)
public final class CreatePersonEvent extends ItemEvent {
	public static final String TYPE = "CreatePersonEvent";

	// Required by Hibernate
	private CreatePersonEvent() {
		super(0, 0);
	}

	public CreatePersonEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}

	@Override
	public void process(final State state) {
		state.getPersons().create(ref, Person.builder().build());
	}
	
	@Override
	public String toString() {
		return "CreatePersonEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + "]";
	}

}