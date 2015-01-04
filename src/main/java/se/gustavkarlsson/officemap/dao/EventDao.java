package se.gustavkarlsson.officemap.dao;

import static com.google.common.base.Preconditions.checkArgument;
import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.event.Event;

public class EventDao extends AbstractDAO<Event> {
	
	public EventDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public void store(@Valid @NotNull final Event event) {
		checkArgument(event.getId() == null, "id is not null: " + event.getId());
		persist(event);
	}
	
	public List<Event> list() {
		@SuppressWarnings("unchecked")
		final List<Event> list = criteria().list();
		return list;
	}

	public void flush() {
		currentSession().flush();
	}
}
