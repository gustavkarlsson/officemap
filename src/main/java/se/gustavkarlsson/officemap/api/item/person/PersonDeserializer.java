package se.gustavkarlsson.officemap.api.item.person;

import java.io.IOException;

import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.item.ItemDeserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("serial")
class PersonDeserializer extends ItemDeserializer<Person> {
	
	PersonDeserializer() {
		super(Person.class);
	}
	
	@Override
	protected Person deserialize(final JsonParser jp, final JsonNode node, final boolean deleted)
			throws JsonParseException, JsonMappingException, IOException {
		final String username = node.get("username").asText();
		final String firstName = node.get("firstName").asText();
		final String lastName = node.get("lastName").asText();
		final String email = node.get("email").asText();
		final Sha1 portrait = Sha1.builder().withSha1(node.get("portrait").asText()).build();
		final Person person = Person.builder()
				.with(null, null, null, deleted, username, firstName, lastName, email, portrait).build();
		return person;
	}
}
