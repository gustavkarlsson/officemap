package se.gustavkarlsson.officemap.api.item;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public abstract class ItemSerializer<T extends Item> extends StdSerializer<T> {

	protected ItemSerializer(final Class<T> t) {
		super(t);
	}

	@Override
	public void serialize(final T value, final JsonGenerator jgen, final SerializerProvider provider)
			throws IOException, JsonGenerationException {
		jgen.writeStartObject();
		writeLeafFields(value, jgen);
		jgen.writeEndObject();
	}

	protected abstract void writeLeafFields(T value, JsonGenerator jgen) throws IOException, JsonGenerationException;
}