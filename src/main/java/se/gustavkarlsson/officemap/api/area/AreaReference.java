package se.gustavkarlsson.officemap.api.area;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import se.gustavkarlsson.officemap.api.Reference;

@Entity
@DiscriminatorValue(AreaReference.TYPE)
public final class AreaReference extends Reference<Area> {

	public static final String TYPE = "AreaReference";

	public AreaReference() {
		// Required by Hibernate
		super();
	}

	public AreaReference(final Long id, final List<Area> items) {
		super(id, items);
	}
	
}
