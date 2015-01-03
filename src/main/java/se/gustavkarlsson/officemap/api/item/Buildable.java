package se.gustavkarlsson.officemap.api.item;

public interface Buildable<T> {

	Builder<T> toBuilder();
}
