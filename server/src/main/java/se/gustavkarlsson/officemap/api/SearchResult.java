package se.gustavkarlsson.officemap.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

public class SearchResult<T> {

	private final int ref;

	private final String type;

	private final T item;

	public SearchResult(final int ref, final String type, final T item) {
		this.ref = ref;
		this.type = checkNotNull(type);
		this.item = checkNotNull(item);
	}

	@JsonProperty("ref")
	public final int getRef() {
		return ref;
	}

	@JsonProperty("type")
	public final String getType() {
		return type;
	}

	@JsonProperty("item")
	public final T getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "SearchResult [ref=" + ref + ", type=" + type + ", item=" + item + "]";
	}
}
