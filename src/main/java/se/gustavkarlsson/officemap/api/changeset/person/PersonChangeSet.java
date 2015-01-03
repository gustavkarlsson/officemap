package se.gustavkarlsson.officemap.api.changeset.person;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import se.gustavkarlsson.officemap.api.event.person.PersonEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonEmailEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonFirstNameEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonLastNameEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonLocationEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonPortraitEvent;
import se.gustavkarlsson.officemap.api.event.person.update.UpdatePersonUsernameEvent;
import se.gustavkarlsson.officemap.api.item.Location;
import se.gustavkarlsson.officemap.api.item.Sha1;
import se.gustavkarlsson.officemap.util.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PersonChangeSetDeserializer.class)
public class PersonChangeSet {

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
	
	public List<PersonEvent> generateEvents(final long timestamp, final int ref) {
		final List<PersonEvent> events = new ArrayList<>();
		
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
	
}
