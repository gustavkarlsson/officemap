package se.gustavkarlsson.officemap.api.fileentry;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

class FileEntrySerializer extends StdSerializer<FileEntry> {

	FileEntrySerializer() {
		super(FileEntry.class);
	}
	
	@Override
	public void serialize(final FileEntry value, final JsonGenerator jgen, final SerializerProvider provider)
			throws IOException, JsonGenerationException {
		jgen.writeStartObject();
		jgen.writeStringField("sha1", value.getSha1Hex());
		jgen.writeStringField("mimeType", value.getMimeType());
		jgen.writeEndObject();
	}
	
}
