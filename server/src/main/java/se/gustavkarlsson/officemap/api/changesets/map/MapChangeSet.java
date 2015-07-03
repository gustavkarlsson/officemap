package se.gustavkarlsson.officemap.api.changesets.map;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import se.gustavkarlsson.officemap.api.changesets.ChangeSet;
import se.gustavkarlsson.officemap.api.changesets.ChangeSetDeserializer;
import se.gustavkarlsson.officemap.api.changesets.ValueMappingException;
import se.gustavkarlsson.officemap.api.changesets.map.MapChangeSet.MapChangeSetDeserializer;
import se.gustavkarlsson.officemap.api.items.Sha1;
import se.gustavkarlsson.officemap.events.Event;
import se.gustavkarlsson.officemap.events.map.update.UpdateMapImageEvent;
import se.gustavkarlsson.officemap.events.map.update.UpdateMapNameEvent;
import se.gustavkarlsson.officemap.util.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = MapChangeSetDeserializer.class)
public class MapChangeSet extends ChangeSet {

	@NotNull
	private final Value<String> name;

	@NotNull
	private final Value<Sha1> image;
	
	public MapChangeSet(final Value<String> name, final Value<Sha1> image) {
		this.name = name;
		this.image = image;
	}

	public final Value<String> getName() {
		return name;
	}
	
	public final Value<Sha1> getImage() {
		return image;
	}
	
	@Override
	public List<Event> generateEvents(final long timestamp, final int ref) {
		final List<Event> events = new ArrayList<>();
		
		if (name.isPresent()) {
			events.add(new UpdateMapNameEvent(timestamp, ref, name.get()));
		}
		
		if (image.isPresent()) {
			events.add(new UpdateMapImageEvent(timestamp, ref, image.get()));
		}
		
		return events;
	}
	
	@SuppressWarnings("serial")
	static class MapChangeSetDeserializer extends ChangeSetDeserializer<MapChangeSet> {

		protected MapChangeSetDeserializer() {
			super(MapChangeSet.class);
		}

		@Override
		public MapChangeSet deserialize(final JsonNode root) throws ValueMappingException {
			final Value<String> name = readStringValue(root, "name");
			final Value<Sha1> image = readSha1Value(root, "image");
			return new MapChangeSet(name, image);
		}
	}
	
}
