package se.gustavkarlsson.officemap.api.changesets.person;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import se.gustavkarlsson.officemap.api.changesets.ChangeSet;
import se.gustavkarlsson.officemap.api.changesets.ChangeSetDeserializer;
import se.gustavkarlsson.officemap.api.changesets.ValueMappingException;
import se.gustavkarlsson.officemap.api.changesets.person.PersonChangeSet.PersonChangeSetDeserializer;
import se.gustavkarlsson.officemap.api.items.Location;
import se.gustavkarlsson.officemap.api.items.Sha1;
import se.gustavkarlsson.officemap.events.Event;
import se.gustavkarlsson.officemap.events.person.update.UpdatePersonEmailEvent;
import se.gustavkarlsson.officemap.events.person.update.UpdatePersonFirstNameEvent;
import se.gustavkarlsson.officemap.events.person.update.UpdatePersonLastNameEvent;
import se.gustavkarlsson.officemap.events.person.update.UpdatePersonLocationEvent;
import se.gustavkarlsson.officemap.events.person.update.UpdatePersonPortraitEvent;
import se.gustavkarlsson.officemap.events.person.update.UpdatePersonUsernameEvent;
import se.gustavkarlsson.officemap.util.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PersonChangeSetDeserializer.class)
public class PersonChangeSet extends ChangeSet {
	
	@NotNull
	private final Value<String> username;
	
	@NotNull
	private final Value<String> firstName;

	@NotNull
	private final Value<String> lastName;

	@NotNull
	private final Value<String> email;

	@NotNull
	private final Value<Sha1> portrait;

	@NotNull
	private final Value<Location> location;
	
	public PersonChangeSet(final Value<String> username, final Value<String> firstName, final Value<String> lastName,
			final Value<String> email, final Value<Sha1> portrait, final Value<Location> location) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.portrait = portrait;
		this.location = location;
	}
	
	public final Value<String> getUsername() {
		return username;
	}

	public final Value<String> getFirstName() {
		return firstName;
	}

	public final Value<String> getLastName() {
		return lastName;
	}

	public final Value<String> getEmail() {
		return email;
	}

	public final Value<Sha1> getPortrait() {
		return portrait;
	}

	public final Value<Location> getLocation() {
		return location;
	}

	@Override
	public List<Event> generateEvents(final long timestamp, final int ref) {
		final List<Event> events = new ArrayList<>();

		if (username.isPresent()) {
			events.add(new UpdatePersonUsernameEvent(timestamp, ref, username.get()));
		}

		if (firstName.isPresent()) {
			events.add(new UpdatePersonFirstNameEvent(timestamp, ref, firstName.get()));
		}

		if (lastName.isPresent()) {
			events.add(new UpdatePersonLastNameEvent(timestamp, ref, lastName.get()));
		}

		if (email.isPresent()) {
			events.add(new UpdatePersonEmailEvent(timestamp, ref, email.get()));
		}

		if (portrait.isPresent()) {
			events.add(new UpdatePersonPortraitEvent(timestamp, ref, portrait.get()));
		}

		if (location.isPresent()) {
			events.add(new UpdatePersonLocationEvent(timestamp, ref, location.get()));
		}

		return events;
	}

	@SuppressWarnings("serial")
	static class PersonChangeSetDeserializer extends ChangeSetDeserializer<PersonChangeSet> {
		
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

}
