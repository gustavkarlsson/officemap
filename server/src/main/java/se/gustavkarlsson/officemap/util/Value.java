package se.gustavkarlsson.officemap.util;

public class Value<T> {

	public static <T> Value<T> of(final T value) {
		return new Value<>(value, true);
	}

	public static <T> Value<T> ofNull() {
		return new Value<>(null, true);
	}

	public static <T> Value<T> absent() {
		return new Value<>(null, false);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (present ? 1231 : 1237);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Value<?> other = (Value<?>) obj;
		if (present != other.present) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "Value [value=" + value + ", present=" + present + "]";
	}

}
