package se.gustavkarlsson.officemap.event.person.update;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import se.gustavkarlsson.officemap.Items;
import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.api.item.Person.PersonBuilder;
import se.gustavkarlsson.officemap.event.ProcessEventException;
import se.gustavkarlsson.officemap.event.person.PersonEvent;

import com.google.common.base.Optional;

abstract class UpdatePersonEvent extends PersonEvent {

	public UpdatePersonEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}

	@Override
	public void process(final State state) throws ProcessEventException {
		checkNotNull(state);
		try {
			final Items<Person> persons = state.getPersons();
			final Optional<Person> person = persons.get(ref);
			checkState(person.isPresent(), "No Person with ref " + ref + " exists");
			final Person updatedPerson = updateProperty(person.get().toBuilder());
			persons.replace(ref, updatedPerson);
		} catch (final Exception e) {
			throw new ProcessEventException(e);
		}
	}
	
	protected abstract Person updateProperty(PersonBuilder builder);

}