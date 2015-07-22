package se.gustavkarlsson.officemap.resources;

import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.events.Event;

import java.util.List;

public abstract class ItemsResource<T> extends Resource {
	
	protected final State state;
	private final EventDao dao;
	
	public ItemsResource(final State state, final EventDao dao) {
		this.state = state;
		this.dao = dao;
	}
	
	protected void processEvents(final List<? extends Event> events) {
		state.backup();
		try {
			for (final Event event : events) {
				event.process(state);
				dao.store(event);
			}
			dao.flush();
		} catch (final Exception e) {
			state.restore();
			throw e;
		}
	}
	
	protected void processEvent(final Event event) {
		state.backup();
		try {
			event.process(state);
			dao.store(event);
			dao.flush();
		} catch (final Exception e) {
			state.restore();
			throw e;
		}
	}
	
}