package se.gustavkarlsson.officemap.api.area;

import java.io.IOException;

import se.gustavkarlsson.officemap.api.ItemSerializer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class AreaSerializer extends ItemSerializer<Area> {
	
	protected AreaSerializer() {
		super(Area.class);
	}
	
	@Override
	protected void writeLeafFields(final Area value, final JsonGenerator jgen) throws IOException,
			JsonGenerationException {
		jgen.writeStringField("name", value.getName());
	}

}
