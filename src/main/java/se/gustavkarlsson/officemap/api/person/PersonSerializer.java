package se.gustavkarlsson.officemap.api.person;

import java.io.IOException;

import se.gustavkarlsson.officemap.api.ItemSerializer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class PersonSerializer extends ItemSerializer<Person> {

	protected PersonSerializer() {
		super(Person.class);
	}
	
	@Override
	protected void writeLeafFields(final Person value, final JsonGenerator jgen) throws JsonGenerationException,
			IOException {
		jgen.writeStringField("username", value.getUsername());
		jgen.writeStringField("firstName", value.getFirstName());
		jgen.writeStringField("lastName", value.getLastName());
		jgen.writeStringField("email", value.getEmail());
	}
	
}
