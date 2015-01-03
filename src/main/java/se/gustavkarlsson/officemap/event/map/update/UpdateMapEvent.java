package se.gustavkarlsson.officemap.event.map.update;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import se.gustavkarlsson.officemap.Items;
import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.item.Map;
import se.gustavkarlsson.officemap.api.item.Map.Builder;
import se.gustavkarlsson.officemap.event.ProcessEventException;
import se.gustavkarlsson.officemap.event.map.MapEvent;

import com.google.common.base.Optional;

abstract class UpdateMapEvent extends MapEvent {
	
	public UpdateMapEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}
	
	@Override
	public void process(final State state) throws ProcessEventException {
		checkNotNull(state);
		try {
			final Items<Map> maps = state.getMaps();
			final Optional<Map> map = maps.get(ref);
			checkState(map.isPresent(), "No Map with ref " + ref + " exists");
			final Map updatedMap = updateProperty(map.get().toBuilder());
			maps.replace(ref, updatedMap);
		} catch (final Exception e) {
			throw new ProcessEventException(e);
		}
	}

	protected abstract Map updateProperty(Builder builder);
	
}