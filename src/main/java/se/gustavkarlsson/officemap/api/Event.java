package se.gustavkarlsson.officemap.api;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "Event")
public final class Event<T extends Item> {

	@Id
	@Column(name = "id")
	@TableGenerator(name = "TABLE_GEN", table = "Sequence", pkColumnName = "name", valueColumnName = "count",
			pkColumnValue = "EventId")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private final Long id;

	@Column(name = "timestamp", nullable = false)
	private final Long timestamp;

	@OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Column(name = "item_fk")
	private final T item;
	
	public Event() {
		// Required by Hibernate
		this.id = null;
		this.timestamp = null;
		this.item = null;
	}

	public Event(final Long id, final Long timestamp, final T item) {
		this.id = id;
		this.timestamp = timestamp;
		this.item = item;
	}
	
	public final Long getId() {
		return id;
	}
	
	public final Long getTimestamp() {
		return timestamp;
	}
	
	public final T getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", timestamp=" + timestamp + "]";
	}
}
