package se.gustavkarlsson.officemap.event.person;

import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.event.ItemEvent;

@Entity
@Table(name = PersonEvent.TYPE)
public abstract class PersonEvent extends ItemEvent {
	public static final String TYPE = "PersonEvent";
	
	protected PersonEvent(final long timestamp, final int ref) {
		super(timestamp, ref);
	}

}
