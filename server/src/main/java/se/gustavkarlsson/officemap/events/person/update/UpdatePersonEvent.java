package se.gustavkarlsson.officemap.events.person.update;

import se.gustavkarlsson.officemap.api.items.Person;
import se.gustavkarlsson.officemap.api.items.Person.PersonBuilder;
import se.gustavkarlsson.officemap.core.ItemStore;
import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.events.ItemEvent;

abstract class UpdatePersonEvent extends ItemEvent {
	
	public UpdatePersonEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}
	
	@Override
	public void process(final State state) {
		final ItemStore<Person> persons = state.getPersons();
		final Person person = persons.get(ref);
		final Person updatedPerson = updateProperty(person.toBuilder());
		persons.replace(ref, updatedPerson);
	}

	protected abstract Person updateProperty(PersonBuilder builder);
	
}