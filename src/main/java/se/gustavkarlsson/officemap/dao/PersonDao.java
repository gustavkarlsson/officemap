package se.gustavkarlsson.officemap.dao;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.Person;

public final class PersonDao extends AbstractItemDao<Person> {
	
	public PersonDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
	protected Person prepareForInsertion(final Person person, final long reference, final long timestamp) {
		final Person prepared = Person.Builder.fromPerson(person).withId(null).withReference(reference)
				.withTimestamp(timestamp).build();
		return prepared;
	}

	@Override
	protected Person prepareForUpdate(final Person person, final long timestamp) {
		final Person prepared = Person.Builder.fromPerson(person).withId(null).withTimestamp(timestamp).build();
		return prepared;
	}
}
