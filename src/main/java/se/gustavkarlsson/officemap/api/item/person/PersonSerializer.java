package se.gustavkarlsson.officemap.api.item.person;

import java.io.IOException;

import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.item.ItemSerializer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

class PersonSerializer extends ItemSerializer<Person> {
	
	PersonSerializer() {
		super(Person.class);
	}

	@Override
	protected void writeLeafFields(final Person value, final JsonGenerator jgen) throws JsonGenerationException,
			IOException {
		jgen.writeStringField("username", value.getUsername());
		jgen.writeStringField("firstName", value.getFirstName());
		jgen.writeStringField("lastName", value.getLastName());
		jgen.writeStringField("email", value.getEmail());
		writePortrait(value, jgen);
	}
	
	private void writePortrait(final Person value, final JsonGenerator jgen) throws IOException,
			JsonProcessingException {
		final Sha1 portrait = value.getPortrait();
		if (portrait == null) {
			return;
		}
		jgen.writeObjectField("portrait", portrait.getHex());
	}

}
