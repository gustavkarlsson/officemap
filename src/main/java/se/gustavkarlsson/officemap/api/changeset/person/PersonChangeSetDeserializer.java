package se.gustavkarlsson.officemap.api.changeset.person;

import se.gustavkarlsson.officemap.api.Location;
import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.changeset.ChangeSetDeserializer;
import se.gustavkarlsson.officemap.api.changeset.ValueMappingException;
import se.gustavkarlsson.officemap.util.Value;

import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("serial")
public class PersonChangeSetDeserializer extends ChangeSetDeserializer<PersonChangeSet> {

	protected PersonChangeSetDeserializer() {
		super(PersonChangeSet.class);
	}

	@Override
	public PersonChangeSet deserialize(final JsonNode root) throws ValueMappingException {
		final Value<String> username = readStringValue(root, "username");
		final Value<String> firstName = readStringValue(root, "firstName");
		final Value<String> lastName = readStringValue(root, "lastName");
		final Value<String> email = readStringValue(root, "email");
		final Value<Sha1> portrait = readSha1Value(root, "portrait");
		final Value<Location> location = readLocationValue(root, "location");
		return new PersonChangeSet(username, firstName, lastName, email, portrait, location);
	}
}
