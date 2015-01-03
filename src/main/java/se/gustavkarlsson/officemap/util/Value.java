package se.gustavkarlsson.officemap.util;

public class Value<T> {

	public static <T> Value<T> of(final T value) {
		return new Value<T>(value, true);
	}

	public static <T> Value<T> ofNull() {
		return new Value<T>(null, true);
	}

	public static <T> Value<T> absent() {
		return new Value<T>(null, false);
	}

	private final T value;
	private final boolean present;

	private Value(final T value, final boolean present) {
		this.value = value;
		this.present = present;
	}

	public boolean isPresent() {
		return present;
	}

	public boolean isAbsent() {
		return !isPresent();
	}

	public boolean isNull() {
		if (isAbsent()) {
			throw new IllegalStateException("Value.isNull() cannot be called on an absent value");
		}
		return value == null;
	}

	public boolean isNotNull() {
		if (isAbsent()) {
			throw new IllegalStateException("Value.isNotNull() cannot be called on an absent value");
		}
		return !isNull();
	}

	public T get() {
		if (isAbsent()) {
			throw new IllegalStateException(getClass().getSimpleName()
					+ "Value.get() cannot be called on an absent value");
		}
		return value;
	}

}
