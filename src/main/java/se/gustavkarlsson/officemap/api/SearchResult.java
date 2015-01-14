package se.gustavkarlsson.officemap.api;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult<T> implements Comparable<SearchResult<?>> {

	private final T object;

	private final String url;

	private final double score;

	public SearchResult(final T object, final String url, final double score) {
		this.object = checkNotNull(object);
		this.url = checkNotNull(url);
		this.score = score;
	}

	@JsonProperty("object")
	public final T getObject() {
		return object;
	}

	@JsonProperty("url")
	public final String getUrl() {
		return url;
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
		return "SearchResult [object=" + object + ", url=" + url + ", score=" + score + "]";
	}
}
