package se.gustavkarlsson.officemap.api.area;

import se.gustavkarlsson.officemap.api.ItemDeserializer;

import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("serial")
class AreaDeserializer extends ItemDeserializer<Area> {

	protected AreaDeserializer() {
		super(Area.class);
	}

	@Override
	protected Area deserialize(final JsonNode node, final boolean deleted) {
		final String name = node.get("name").asText();
		final Area area = Area.Builder.withFields(null, null, null, deleted, name).build();
		return area;
	}

}
