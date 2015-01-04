package se.gustavkarlsson.officemap.events.map.update;

import se.gustavkarlsson.officemap.api.items.Map;
import se.gustavkarlsson.officemap.api.items.Map.MapBuilder;
import se.gustavkarlsson.officemap.core.ItemStore;
import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.events.ItemEvent;

abstract class UpdateMapEvent extends ItemEvent {

	public UpdateMapEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}

	@Override
	public void process(final State state) {
		final ItemStore<Map> maps = state.getMaps();
		final Map map = maps.get(ref);
		final Map updatedMap = updateProperty(map.toBuilder());
		maps.replace(ref, updatedMap);
	}
	
	protected abstract Map updateProperty(MapBuilder builder);

}