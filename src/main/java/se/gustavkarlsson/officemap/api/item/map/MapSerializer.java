package se.gustavkarlsson.officemap.api.item.map;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

class MapSerializer extends StdSerializer<Map> {

	MapSerializer() {
		super(Map.class);
	}

	@Override
	public void serialize(final Map value, final JsonGenerator jgen, final SerializerProvider provider)
			throws IOException, JsonGenerationException {
		jgen.writeStartObject();
		jgen.writeStringField("name", value.getName());
		jgen.writeStringField("image", value.getMap().getHex());
		jgen.writeEndObject();
	}
	
}
