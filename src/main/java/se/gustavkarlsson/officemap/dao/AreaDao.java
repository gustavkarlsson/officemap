package se.gustavkarlsson.officemap.dao;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.area.Area;
import se.gustavkarlsson.officemap.api.area.AreaReference;
import se.gustavkarlsson.officemap.api.person.Person;

import com.google.common.base.Optional;

public final class AreaDao extends AbstractItemDao<Area> {
	
	public AreaDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
	public Optional<Long> insert(final Area item) {
		checkNotNull(item);
		checkPersonAvailability(item);
		return super.insert(item);
	}
	
	@Override
	public UpdateResponse update(final long referenceId, final Area item) {
		checkNotNull(item);
		checkPersonAvailability(item);
		return super.update(referenceId, item);
	}
	
	private void checkPersonAvailability(final Area item) {
		final Set<Reference<Person>> persons = item.getPersons();
		final List<Area> areas = findAllHeads();
		areas.remove(item);
		for (final Area area : areas) {
			for (final Reference<Person> personRef : persons) {
				if (area.getPersons().contains(personRef)) {
					throw new IllegalStateException("Person with ID: " + personRef.getId()
							+ " is already in area with ID: " + area.getId());
				}
			}
		}
	}
	
	@Override
	protected Area setId(final Area item, final Long id) {
		final Area prepared = Area.Builder.fromArea(item).withId(id).build();
		return prepared;
	}
	
	@Override
	protected Area setReference(final Area item, final Reference<Area> reference) {
		final Area prepared = Area.Builder.fromArea(item).withReference(reference).build();
		return prepared;
	}
	
	@Override
	protected Area setTimestamp(final Area item, final Long timestamp) {
		final Area prepared = Area.Builder.fromArea(item).withTimestamp(timestamp).build();
		return prepared;
	}
	
	@Override
	protected Reference<Area> createNewReference() {
		return new AreaReference(null, new ArrayList<Area>());
	}
	
	@Override
	protected Class<? extends Reference<Area>> getReferenceClass() {
		return AreaReference.class;
	}
}
