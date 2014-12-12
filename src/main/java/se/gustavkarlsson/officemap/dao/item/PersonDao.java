package se.gustavkarlsson.officemap.dao.item;

import java.util.ArrayList;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.item.Reference;
import se.gustavkarlsson.officemap.api.item.person.Person;
import se.gustavkarlsson.officemap.api.item.person.PersonReference;

public final class PersonDao extends AbstractItemDao<Person> {

	public PersonDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Person setId(final Person item, final Long id) {
		final Person prepared = item.toBuilder().withId(id).build();
		return prepared;
	}

	@Override
	protected Person setReference(final Person item, final Reference<Person> reference) {
		final Person prepared = item.toBuilder().withReference(reference).build();
		return prepared;
	}

	@Override
	protected Person setTimestamp(final Person item, final Long timestamp) {
		final Person prepared = item.toBuilder().withTimestamp(timestamp).build();
		return prepared;
	}

	@Override
	protected Reference<Person> createNewReference() {
		return new PersonReference(null, new ArrayList<Person>());
	}
	
	@Override
	protected Class<? extends Reference<Person>> getReferenceClass() {
		return PersonReference.class;
	}
}
