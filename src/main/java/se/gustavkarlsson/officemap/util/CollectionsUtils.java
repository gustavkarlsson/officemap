package se.gustavkarlsson.officemap.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class CollectionsUtils {

	public static final <T> Set<T> unmodifiableSetWithoutNull(final Collection<T> collection) {
		final Set<T> preparedSet = collection == null ? new HashSet<T>() : new HashSet<>(collection);
		preparedSet.remove(null);
		final Set<T> unmodifiableSetWithoutNull = Collections.unmodifiableSet(preparedSet);
		return unmodifiableSetWithoutNull;
	}
}
