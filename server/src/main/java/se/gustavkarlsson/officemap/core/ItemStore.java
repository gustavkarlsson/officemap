package se.gustavkarlsson.officemap.core;

import se.gustavkarlsson.officemap.api.items.Searchable;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ItemStore<T extends Searchable> {

	private final Map<Integer, T> backedUpItems = new HashMap<>();
	private final Map<Integer, T> items = new HashMap<>();

	private final Index index;

	public ItemStore(Index index) {
		this.index = index;
	}
	
	public void create(final int ref, final T item) {
		checkNotNull(item);
		items.put(ref, item);
		index.add(ref, item);
	}
	
	public void replace(final int ref, final T item) {
		checkNotNull(item);
		checkItemExists(ref);
		items.put(ref, item);
		index.update(ref, item);
	}
	
	public void delete(final int ref) {
		checkItemExists(ref);
		items.remove(ref);
		index.remove(ref);
	}

	private void checkItemExists(final int ref) {
		if (!contains(ref)) {
			throw new ItemNotFoundException(ref);
		}
	}
	
	public T get(final int ref) {
		checkItemExists(ref);
		return items.get(ref);
	}
	
	public boolean contains(final int ref) {
		return items.containsKey(ref);
	}
	
	public Map<Integer, T> getAll() {
		return new HashMap<>(items);
	}
	
	void backup() {
		backedUpItems.clear();
		backedUpItems.putAll(items);
	}

	void restore() {
		items.clear();
		items.putAll(backedUpItems);
	}
}
