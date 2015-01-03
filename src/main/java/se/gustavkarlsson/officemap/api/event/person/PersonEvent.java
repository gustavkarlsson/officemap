package se.gustavkarlsson.officemap.api.event.person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Range;

import se.gustavkarlsson.officemap.api.event.Event;

@Entity
@Table(name = PersonEvent.TYPE)
public abstract class PersonEvent extends Event {
	public static final String TYPE = "PersonEvent";
	
	@Range(min = 0)
	@Column(name = "ref")
	protected final int ref;
	
	protected PersonEvent(final long timestamp, final int ref) {
		super(timestamp);
		this.ref = ref;
	}

	public final int getRef() {
		return ref;
	}
	
}
