package se.gustavkarlsson.officemap.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

public class SearchResult<T> implements Comparable<SearchResult<?>> {

	private final T object;

	private final int ref;

	private final double score;

	public SearchResult(final T object, final int ref, final double score) {
		this.object = checkNotNull(object);
		this.ref = ref;
		this.score = score;
	}

	@JsonProperty("object")
	public final T getObject() {
		return object;
	}

	@JsonProperty("ref")
	public final int getRef() {
		return ref;
	}

	@JsonIgnore
	public final double getScore() {
		return score;
	}

	@Override
	public int compareTo(final SearchResult<?> o) {
		return Double.compare(score, o.score);
	}

	@Override
	public String toString() {
		return "SearchResult [object=" + object + ", ref=" + ref + ", score=" + score + "]";
	}
}
