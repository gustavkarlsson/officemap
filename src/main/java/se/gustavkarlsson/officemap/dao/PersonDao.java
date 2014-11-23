package se.gustavkarlsson.officemap.dao;

import java.util.ArrayList;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.person.Person;
import se.gustavkarlsson.officemap.api.person.PersonReference;

public final class PersonDao extends AbstractItemDao<Person> {

	public PersonDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Person setId(final Person item, final Long id) {
		final Person prepared = Person.Builder.fromPerson(item).withId(id).build();
		return prepared;
	}

	@Override
	protected Person setReference(final Person item, final Reference<Person> reference) {
		final Person prepared = Person.Builder.fromPerson(item).withReference(reference).build();
		return prepared;
	}

	@Override
	protected Person setTimestamp(final Person item, final Long timestamp) {
		final Person prepared = Person.Builder.fromPerson(item).withTimestamp(timestamp).build();
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
