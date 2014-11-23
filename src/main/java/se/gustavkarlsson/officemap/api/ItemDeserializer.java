package se.gustavkarlsson.officemap.api;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public abstract class ItemDeserializer<T extends Item<T>> extends StdDeserializer<T> {

	protected ItemDeserializer(final Class<T> t) {
		super(t);
	}

	@Override
	public T deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		final JsonNode node = jp.getCodec().readTree(jp);
		final boolean deleted = node.get("deleted").asBoolean();
		final T item = deserialize(node, deleted);
		return item;
	}

	protected abstract T deserialize(JsonNode node, boolean deleted);
}