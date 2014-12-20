package se.gustavkarlsson.officemap.events.person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.events.Event;

@Entity
@Table(name = PersonEvent.TYPE)
public abstract class PersonEvent extends Event {
	public static final String TYPE = "PersonEvent";

	@Column(name = "personId")
	protected final int personId;
	
	protected PersonEvent(final long timestamp, final int personId) {
		super(timestamp);
		this.personId = personId;
	}
	
}
