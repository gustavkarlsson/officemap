package se.gustavkarlsson.officemap.event.person.update;

import static com.google.common.base.Preconditions.checkNotNull;
import se.gustavkarlsson.officemap.Items;
import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.api.item.Person.PersonBuilder;
import se.gustavkarlsson.officemap.event.ItemEvent;

abstract class UpdatePersonEvent extends ItemEvent {

	public UpdatePersonEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}

	@Override
	public void process(final State state) {
		checkNotNull(state);
		final Items<Person> persons = state.getPersons();
		final Person person = persons.get(ref);
		final Person updatedPerson = updateProperty(person.toBuilder());
		persons.replace(ref, updatedPerson);
	}
	
	protected abstract Person updateProperty(PersonBuilder builder);

}