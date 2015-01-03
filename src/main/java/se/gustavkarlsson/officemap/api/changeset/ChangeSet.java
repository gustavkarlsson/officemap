package se.gustavkarlsson.officemap.api.changeset;

import java.util.List;

import se.gustavkarlsson.officemap.event.Event;

public abstract class ChangeSet {

	public abstract List<Event> generateEvents(long timestamp, int ref);

}