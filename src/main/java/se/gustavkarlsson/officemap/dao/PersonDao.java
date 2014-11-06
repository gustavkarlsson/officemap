package se.gustavkarlsson.officemap.dao;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.Person;

public final class PersonDao extends AbstractItemDao<Person> {

	public PersonDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Person prepareForInsertion(final Person item) {
		final long reference = getFreeReference();
		final long timestamp = System.currentTimeMillis();
		final Person prepared = Person.Builder.fromPerson(item).withId(null).withTimestamp(timestamp)
				.withReference(reference).withDeleted(false).build();
		return prepared;
	}
	
	@Override
	protected Person prepareForUpdate(final Person item) {
		final long timestamp = System.currentTimeMillis();
		final Person prepared = Person.Builder.fromPerson(item).withId(null).withTimestamp(timestamp)
				.withDeleted(false).build();
		return prepared;
	}
	
	@Override
	protected Person prepareForUndelete(final Person item) {
		final long timestamp = System.currentTimeMillis();
		final Person prepared = Person.Builder.fromPerson(item).withId(null).withTimestamp(timestamp)
				.withDeleted(false).build();
		return prepared;
	}
	
	@Override
	protected Person prepareForDelete(final Person item) {
		final long timestamp = System.currentTimeMillis();
		final Person prepared = Person.Builder.fromPerson(item).withId(null).withTimestamp(timestamp).withDeleted(true)
				.build();
		return prepared;
	}
}
