package se.gustavkarlsson.officemap.api.changesets;

import java.util.List;

import se.gustavkarlsson.officemap.events.Event;

public abstract class ChangeSet {

	public abstract List<Event> generateEvents(long timestamp, int ref);

}