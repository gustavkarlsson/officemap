package se.gustavkarlsson.officemap.api.item;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public abstract class ItemSerializer<T extends Item<T>> extends StdSerializer<T> {

	protected ItemSerializer(final Class<T> t) {
		super(t);
	}

	@Override
	public void serialize(final T value, final JsonGenerator jgen, final SerializerProvider provider)
			throws IOException, JsonGenerationException {
		jgen.writeStartObject();
		writeItemFields(value, jgen);
		writeLeafFields(value, jgen);
		jgen.writeEndObject();
	}
	
	private void writeItemFields(final T value, final JsonGenerator jgen) throws IOException, JsonGenerationException {
		jgen.writeNumberField("timestamp", value.getTimestamp());
		jgen.writeNumberField("reference", value.getReference().getId());
		jgen.writeBooleanField("deleted", value.isDeleted());
	}

	protected abstract void writeLeafFields(T value, JsonGenerator jgen) throws IOException, JsonGenerationException;
}