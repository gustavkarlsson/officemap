package se.gustavkarlsson.officemap.dao;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.Person;

public final class PersonDao extends AbstractEventDao<Person> {
	
	public PersonDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
	protected Person assignReference(final long reference, final Person person) {
		final Person personWithReference = Person.Builder.fromPerson(person).withReference(reference).build();
		return personWithReference;
	}
}
