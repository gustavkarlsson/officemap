package se.gustavkarlsson.officemap.dao;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.Person;

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
	protected Person setReference(final Person item, final Long reference) {
		final Person prepared = Person.Builder.fromPerson(item).withReference(reference).build();
		return prepared;
	}
	
	@Override
	protected Person setTimestamp(final Person item, final Long timestamp) {
		final Person prepared = Person.Builder.fromPerson(item).withTimestamp(timestamp).build();
		return prepared;
	}
}
