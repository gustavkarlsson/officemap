package se.gustavkarlsson.officemap.core;

import se.gustavkarlsson.officemap.api.items.Map;
import se.gustavkarlsson.officemap.api.items.Person;

import java.util.concurrent.atomic.AtomicInteger;

public class State {

	private final AtomicInteger nextFreeRef = new AtomicInteger(0);

	private final Index index = new Index();

	private final ItemStore<Person> persons = new ItemStore<>(index);
	private final ItemStore<Map> maps = new ItemStore<>(index);

	public final int getUniqueRef() {
		return nextFreeRef.getAndIncrement();
	}

	public final void backup() {
		persons.backup();
		maps.backup();
	}

	public final void restore() {
		persons.restore();
		maps.restore();
	}

	public final Index getIndex() {
		return index;
	}

	public final ItemStore<Person> getPersons() {
		return persons;
	}
	
	public final ItemStore<Map> getMaps() {
		return maps;
	}
}
