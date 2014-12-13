package se.gustavkarlsson.officemap.api.item.area;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.item.ItemDeserializer;
import se.gustavkarlsson.officemap.api.item.Reference;
import se.gustavkarlsson.officemap.api.item.person.Person;
import se.gustavkarlsson.officemap.api.item.person.PersonReference;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("serial")
class AreaDeserializer extends ItemDeserializer<Area> {
	
	AreaDeserializer() {
		super(Area.class);
	}
	
	@Override
	protected Area deserialize(final JsonParser jp, final JsonNode node, final boolean deleted)
			throws JsonParseException, JsonMappingException, IOException {
		final String name = node.get("name").asText();
		final Sha1 map = Sha1.builder().withSha1(node.get("map").asText()).build();
		final List<Reference<Person>> personReferences = getPersonReferences(node);
		final Area area = Area.builder().with(null, null, null, deleted, name, map, personReferences).build();
		return area;
	}
	
	private List<Reference<Person>> getPersonReferences(final JsonNode node) {
		final List<Reference<Person>> personReferences = new ArrayList<>();
		for (final JsonNode personReferenceNode : node.get("persons")) {
			final long personReferenceId = personReferenceNode.asLong();
			final PersonReference personReference = new PersonReference(personReferenceId, null);
			personReferences.add(personReference);
		}
		return personReferences;
	}
	
}
