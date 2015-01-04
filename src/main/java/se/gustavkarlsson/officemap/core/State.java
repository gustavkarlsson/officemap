package se.gustavkarlsson.officemap.core;

import se.gustavkarlsson.officemap.api.item.Map;
import se.gustavkarlsson.officemap.api.item.Person;

public class State {

	private final Items<Person> persons = new Items<Person>();
	private final Items<Map> maps = new Items<Map>();

	public final Items<Person> getPersons() {
		return persons;
	}
	
	public Items<Map> getMaps() {
		return maps;
	}
}
