package se.gustavkarlsson.officemap.core;

import se.gustavkarlsson.officemap.api.items.Map;
import se.gustavkarlsson.officemap.api.items.Person;

public class State {

	private final ItemStore<Person> persons = new ItemStore<>();
	private final ItemStore<Map> maps = new ItemStore<>();

	public final ItemStore<Person> getPersons() {
		return persons;
	}
	
	public ItemStore<Map> getMaps() {
		return maps;
	}
}
