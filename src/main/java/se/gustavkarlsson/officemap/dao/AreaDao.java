package se.gustavkarlsson.officemap.dao;

import java.util.ArrayList;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.area.Area;
import se.gustavkarlsson.officemap.api.area.AreaReference;

public final class AreaDao extends AbstractItemDao<Area> {
	
	public AreaDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
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
