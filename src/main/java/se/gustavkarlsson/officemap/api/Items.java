package se.gustavkarlsson.officemap.api;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;

public class Items<T> {

	private int highestRef = 0;

	private final Map<Integer, T> items = new HashMap<>();
	
	public void create(final int ref, final T item) {
		checkNotNull(item);
		checkState(items.get(ref) != null, "An Item with ref " + ref + " already exists");
		items.put(ref, item);
		ensureHighestRef(ref);
	}
	
	private void ensureHighestRef(final int ref) {
		highestRef = Math.max(highestRef, ref);
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
		checkState(items.get(ref) == null, "No Item with ref " + ref + " exists");
	}
	
	public Optional<T> get(final int ref) {
		final T item = items.get(ref);
		return Optional.fromNullable(item);
	}
	
	public boolean exists(final int ref) {
		return get(ref).isPresent();
	}
	
	public Map<Integer, T> getAll() {
		return new HashMap<>(items);
	}

	public final int getNextRef() {
		return highestRef + 1;
	}
}
