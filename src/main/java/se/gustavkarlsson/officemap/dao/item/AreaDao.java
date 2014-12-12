package se.gustavkarlsson.officemap.dao.item;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.item.Reference;
import se.gustavkarlsson.officemap.api.item.area.Area;
import se.gustavkarlsson.officemap.api.item.area.AreaReference;
import se.gustavkarlsson.officemap.api.item.person.Person;

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
		for (final Area area : areas) {
			if (!area.getId().equals(item.getId())) {
				for (final Reference<Person> personRef : persons) {
					if (area.getPersons().contains(personRef)) {
						throw new IllegalStateException("Person with ID: " + personRef.getId()
								+ " is already in area with ID: " + area.getId());
					}
				}
			}
		}
	}
	
	@Override
	protected Area setId(final Area item, final Long id) {
		final Area prepared = item.toBuilder().withId(id).build();
		return prepared;
	}
	
	@Override
	protected Area setReference(final Area item, final Reference<Area> reference) {
		final Area prepared = item.toBuilder().withReference(reference).build();
		return prepared;
	}
	
	@Override
	protected Area setTimestamp(final Area item, final Long timestamp) {
		final Area prepared = item.toBuilder().withTimestamp(timestamp).build();
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
