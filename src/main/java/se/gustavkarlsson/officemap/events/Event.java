package se.gustavkarlsson.officemap.events;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = Event.TYPE)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class Event {
	public static final String TYPE = "Event";
	
	@Range(min = 0)
	@Id
	@Column(name = "id")
	@TableGenerator(name = "TABLE_GEN", table = "Sequence", pkColumnName = "name", valueColumnName = "count",
			pkColumnValue = "EventId")
	protected final Long id;

	@Range(min = 0)
	@NotNull
	@Column(name = "timestamp")
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
	
}
