package se.gustavkarlsson.officemap.event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = ItemEvent.TYPE)
public abstract class ItemEvent extends Event {
	public static final String TYPE = "ItemEvent";
	
	@Range(min = 0)
	@Column(name = "ref")
	protected final int ref;

	protected ItemEvent(final long timestamp, final int ref) {
		super(timestamp);
		this.ref = ref;
	}

	public final int getRef() {
		return ref;
	}

}
