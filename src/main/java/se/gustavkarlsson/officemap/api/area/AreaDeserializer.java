package se.gustavkarlsson.officemap.api.area;

import java.util.ArrayList;
import java.util.List;

import se.gustavkarlsson.officemap.api.ItemDeserializer;
import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.person.Person;
import se.gustavkarlsson.officemap.api.person.PersonReference;

import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("serial")
class AreaDeserializer extends ItemDeserializer<Area> {

	protected AreaDeserializer() {
		super(Area.class);
	}

	@Override
	protected Area deserialize(final JsonNode node, final boolean deleted) {
		final String name = node.get("name").asText();
		final List<Reference<Person>> personReferences = new ArrayList<>();
		for (final JsonNode personReferenceNode : node.get("persons")) {
			final long personReferenceId = personReferenceNode.asLong();
			final PersonReference personReference = new PersonReference(personReferenceId, null);
			personReferences.add(personReference);
		}
		final Area area = Area.Builder.withFields(null, null, null, deleted, name, personReferences).build();
		return area;
	}

}
