package se.gustavkarlsson.officemap.api.person;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.EqualsBuilder;

import se.gustavkarlsson.officemap.api.Reference;

import com.google.common.base.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		final PersonReference rhs = (PersonReference) obj;
		return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
	}
	
}
