package se.gustavkarlsson.officemap.api.fileentry;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
class FileEntryDeserializer extends StdDeserializer<FileEntry> {
	
	FileEntryDeserializer() {
		super(FileEntry.class);
	}
	
	@Override
	public FileEntry deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		final JsonNode node = jp.getCodec().readTree(jp);
		final String sha1 = node.get("sha1").asText();
		final String mimeType = node.get("mimeType").asText();
		final FileEntry item = FileEntry.builder().with(null, mimeType, sha1).build();
		return item;
	}
}