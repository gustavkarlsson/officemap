package se.gustavkarlsson.officemap.api.person;

import se.gustavkarlsson.officemap.api.ItemDeserializer;
import se.gustavkarlsson.officemap.api.Sha1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;

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
		final Sha1 portrait = readPortraitSha1(node);
		final Person person = Person.Builder.withFields(null, null, null, deleted, username, firstName, lastName,
				email, portrait).build();
		return person;
	}

	private Sha1 readPortraitSha1(final JsonNode node) {
		final JsonNode portraitNode = node.get("portrait");
		if (portraitNode == null || portraitNode instanceof NullNode) {
			return null;
		}
		final String sha1Hex = portraitNode.asText();
		final Sha1 sha1 = new Sha1(sha1Hex);
		return sha1;
	}

}
