package se.gustavkarlsson.officemap.dao;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.Person;

public class PersonDao extends GenericDao<Person> {
	
	public PersonDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}
