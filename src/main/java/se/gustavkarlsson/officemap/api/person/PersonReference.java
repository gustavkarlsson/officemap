package se.gustavkarlsson.officemap.api.person;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import se.gustavkarlsson.officemap.api.Reference;

@Entity
@DiscriminatorValue(PersonReference.TYPE)
public final class PersonReference extends Reference<Person> {

	public static final String TYPE = "PersonReference";

	public PersonReference() {
		// Required by Hibernate
		super();
	}

	public PersonReference(final Long id, final List<Person> items) {
		super(id, items);
	}
	
}
