package se.gustavkarlsson.officemap;

import se.gustavkarlsson.officemap.api.Items;
import se.gustavkarlsson.officemap.api.item.map.Map;
import se.gustavkarlsson.officemap.api.item.person.Person;

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
