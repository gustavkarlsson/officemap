package se.gustavkarlsson.officemap.core;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;

public class ItemStore<T> {

	private int refCount = 0;

	private final Map<Integer, T> backedUpItems = new HashMap<>();
	private final Map<Integer, T> items = new HashMap<>();
	
	public void create(final int ref, final T item) {
		checkNotNull(item);
		checkState(ref == getNextRef(), "Ref " + ref + " is not the next free ref. Should be " + getNextRef());
		refCount = ref;
		items.put(ref, item);
	}
	
	public void replace(final int ref, final T item) {
		checkNotNull(item);
		checkItemExists(ref);
		items.put(ref, item);
	}
	
	public void delete(final int ref) {
		checkItemExists(ref);
		items.remove(ref);
	}

	private void checkItemExists(final int ref) {
		if (!exists(ref)) {
			throw new ItemNotFoundException(ref);
		}
	}
	
	public T get(final int ref) {
		checkItemExists(ref);
		return items.get(ref);
	}
	
	public boolean exists(final int ref) {
		return items.containsKey(ref);
	}
	
	public Map<Integer, T> getAll() {
		return new HashMap<>(items);
	}

	public final int getNextRef() {
		return refCount + 1;
	}
	
	public void backup() {
		backedUpItems.clear();
		backedUpItems.putAll(items);
	}

	public void restore() {
		items.clear();
		items.putAll(backedUpItems);
	}
}
