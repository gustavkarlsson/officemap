package se.gustavkarlsson.officemap.api.item.map;

import java.io.IOException;

import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.item.ItemDeserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("serial")
class MapDeserializer extends ItemDeserializer<Map> {
	
	MapDeserializer() {
		super(Map.class);
	}
	
	@Override
	protected Map deserialize(final JsonParser jp, final JsonNode node) throws JsonParseException,
			JsonMappingException, IOException {
		final String name = node.get("name").asText();
		final Sha1 image = Sha1.builder().withSha1(node.get("image").asText()).build();
		final Map map = Map.builder().with(null, name, image).build();
		return map;
	}
	
}
