package se.gustavkarlsson.officemap.api.item;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public abstract class Item {

	@Range(min = 0)
	@JsonProperty
	private final Long id;
	
	protected Item(final Long id) {
		this.id = id;
	}
	
	public final Long getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).toString();
	}
}
