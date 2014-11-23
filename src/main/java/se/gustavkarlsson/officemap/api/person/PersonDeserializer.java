package se.gustavkarlsson.officemap.api.person;

import se.gustavkarlsson.officemap.api.ItemDeserializer;

import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("serial")
class PersonDeserializer extends ItemDeserializer<Person> {
	
	protected PersonDeserializer() {
		super(Person.class);
	}
	
	@Override
	protected Person deserialize(final JsonNode node, final boolean deleted) {
		final String username = node.get("username").asText();
		final String firstName = node.get("firstName").asText();
		final String lastName = node.get("lastName").asText();
		final String email = node.get("email").asText();
		final Person person = Person.Builder
				.withFields(null, null, null, deleted, username, firstName, lastName, email).build();
		return person;
	}
	
}
