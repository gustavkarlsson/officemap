package se.gustavkarlsson.officemap.resources;

import java.util.List;

import se.gustavkarlsson.officemap.core.ItemStore;
import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.events.Event;

public abstract class ItemsResource<T> extends Resource {
	
	private final State state;
	private final EventDao dao;
	
	protected final ItemStore<T> items;
	
	public ItemsResource(final State state, final EventDao dao, final ItemStore<T> items) {
		this.state = state;
		this.dao = dao;
		this.items = items;
	}
	
	protected void processEvents(final List<? extends Event> events) {
		items.backup();
		try {
			for (final Event event : events) {
				event.process(state);
				dao.store(event);
			}
			dao.flush();
		} catch (final Exception e) {
			items.restore();
			throw e;
		}
	}
	
	protected void processEvent(final Event event) {
		items.backup();
		try {
			event.process(state);
			dao.store(event);
			dao.flush();
		} catch (final Exception e) {
			items.restore();
			throw e;
		}
	}
	
}