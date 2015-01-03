package se.gustavkarlsson.officemap.api.item.person;

import java.io.IOException;

import se.gustavkarlsson.officemap.api.Location;
import se.gustavkarlsson.officemap.api.Sha1;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

class PersonSerializer extends StdSerializer<Person> {
	
	PersonSerializer() {
		super(Person.class);
	}

	@Override
	public void serialize(final Person value, final JsonGenerator jgen, final SerializerProvider provider)
			throws IOException, JsonGenerationException {
		jgen.writeStartObject();
		jgen.writeStringField("username", value.getUsername());
		jgen.writeStringField("firstName", value.getFirstName());
		jgen.writeStringField("lastName", value.getLastName());
		jgen.writeStringField("email", value.getEmail());
		writePortrait(value, jgen);
		writeLocation(value, jgen);
		jgen.writeEndObject();
	}

	private void writePortrait(final Person value, final JsonGenerator jgen) throws IOException,
			JsonProcessingException {
		final Sha1 portrait = value.getPortrait();
		if (portrait == null) {
			return;
		}
		jgen.writeStringField("portrait", portrait.getHex());
	}
	
	private void writeLocation(final Person person, final JsonGenerator jgen) throws JsonGenerationException,
			IOException {
		final Location location = person.getLocation();
		if (location == null) {
			jgen.writeNullField("location");
		}
		jgen.writeObjectFieldStart("location");
		jgen.writeNumberField("mapRef", location.getMapRef());
		jgen.writeNumberField("latitude", location.getLatitude());
		jgen.writeNumberField("longitude", location.getLongitude());
		jgen.writeEndObject();
	}

}
