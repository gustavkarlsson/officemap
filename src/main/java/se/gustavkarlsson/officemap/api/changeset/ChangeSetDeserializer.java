package se.gustavkarlsson.officemap.api.changeset;

import java.io.IOException;

import se.gustavkarlsson.officemap.api.Location;
import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.util.Value;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public abstract class ChangeSetDeserializer<T> extends StdDeserializer<T> {
	
	protected ChangeSetDeserializer(final Class<T> vc) {
		super(vc);
	}

	@Override
	public T deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		final JsonNode root = jp.getCodec().readTree(jp);
		return deserialize(root);
	}
	
	protected Value<Location> readLocationValue(final JsonNode root, final String path) throws ValueMappingException {
		final JsonNode node = root.path(path);
		if (node.isMissingNode()) {
			return Value.absent();
		}
		if (node.isNull()) {
			return Value.ofNull();
		}
		if (!node.isObject()) {
			throw new ValueMappingException(path, node, "Location");
		}

		final JsonNode mapRefNode = node.path("mapRef");
		final JsonNode latitudeNode = node.path("latitude");
		final JsonNode longitudeNode = node.path("longitude");

		if (!mapRefNode.isIntegralNumber() || !latitudeNode.isNumber() || !longitudeNode.isNumber()) {
			throw new ValueMappingException(path, node, "Location",
					"mapRef, latitude or longitude value missing or invalid.");
		}
		final int mapRef = mapRefNode.asInt();
		final double latitude = latitudeNode.asDouble();
		final double longitude = longitudeNode.asDouble();
		final Location location = Location.builder().with(mapRef, latitude, longitude).build();
		return Value.of(location);
	}

	protected Value<Sha1> readSha1Value(final JsonNode root, final String path) throws ValueMappingException {
		final JsonNode node = root.path(path);
		if (node.isMissingNode()) {
			return Value.absent();
		}
		if (node.isNull()) {
			return Value.ofNull();
		}
		if (!node.isTextual()) {
			throw new ValueMappingException(path, node, "Sha1");
		}
		try {
			final Sha1 sha1 = Sha1.builder().withSha1(node.asText()).build();
			return Value.of(sha1);
		} catch (final IllegalArgumentException e) {
			throw new ValueMappingException(path, node, "Sha1", e.getCause().getMessage());
		}
	}

	protected Value<String> readStringValue(final JsonNode root, final String path) throws ValueMappingException {
		final JsonNode node = root.path(path);
		if (node.isMissingNode()) {
			return Value.absent();
		}
		if (node.isNull()) {
			return Value.ofNull();
		}
		if (!node.isTextual()) {
			throw new ValueMappingException(path, node, "String");
		}
		return Value.of(node.asText());
	}
	
	protected abstract T deserialize(final JsonNode root) throws ValueMappingException;
	
}