package se.gustavkarlsson.officemap.api.item.map;

import java.io.IOException;

import se.gustavkarlsson.officemap.api.item.ItemSerializer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class MapSerializer extends ItemSerializer<Map> {
	
	MapSerializer() {
		super(Map.class);
	}
	
	@Override
	protected void writeLeafFields(final Map value, final JsonGenerator jgen) throws IOException,
			JsonGenerationException {
		jgen.writeStringField("name", value.getName());
		jgen.writeStringField("image", value.getMap().getHex());
	}

}
