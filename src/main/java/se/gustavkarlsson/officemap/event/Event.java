package se.gustavkarlsson.officemap.event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import se.gustavkarlsson.officemap.State;

@Entity
@Table(name = Event.TYPE)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Event {
	public static final String TYPE = "Event";

	@Range(min = 0)
	@Id
	@Column(name = "id", nullable = false)
	@GenericGenerator(name = "idgen", strategy = "increment")
	@GeneratedValue(generator = "idgen")
	protected final Long id;

	@Range(min = 1)
	@NotNull
	@Column(name = "timestamp", nullable = false)
	protected final long timestamp;

	protected Event(final long timestamp) {
		this.id = null;
		this.timestamp = timestamp;
	}

	public final Long getId() {
		return id;
	}

	public final long getTimestamp() {
		return timestamp;
	}
	
	public abstract void process(State state) throws ProcessEventException;

}
