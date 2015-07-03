package se.gustavkarlsson.officemap.api.items;

public interface Buildable<T> {

	Builder<T> toBuilder();
}
