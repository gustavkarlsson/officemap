package se.gustavkarlsson.officemap.event.map.update;

import se.gustavkarlsson.officemap.Items;
import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.item.Map;
import se.gustavkarlsson.officemap.api.item.Map.MapBuilder;
import se.gustavkarlsson.officemap.event.ItemEvent;

abstract class UpdateMapEvent extends ItemEvent {

	public UpdateMapEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}

	@Override
	public void process(final State state) {
		final Items<Map> maps = state.getMaps();
		final Map map = maps.get(ref);
		final Map updatedMap = updateProperty(map.toBuilder());
		maps.replace(ref, updatedMap);
	}
	
	protected abstract Map updateProperty(MapBuilder builder);

}