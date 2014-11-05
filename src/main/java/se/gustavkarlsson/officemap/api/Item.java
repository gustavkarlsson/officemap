package se.gustavkarlsson.officemap.api;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@Entity
@Table(name = "Item")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Item {
	
	@Range(min = 0)
	@JsonProperty
	@Id
	@Column(name = "id")
	@TableGenerator(name = "TABLE_GEN", table = "Sequence", pkColumnName = "name", valueColumnName = "count",
			pkColumnValue = "ItemId")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private final Long id;

	@Range(min = 0)
	@Column(name = "timestamp", nullable = false)
	private final Long timestamp;

	@Range(min = 0)
	@JsonProperty
	@Column(name = "reference", nullable = false)
	private final Long reference;
	
	@JsonProperty
	@Column(name = "deleted", nullable = false)
	private final boolean deleted;

	protected Item(final Long id, final Long timestamp, final Long reference, final boolean deleted) {
		this.id = id;
		this.timestamp = timestamp;
		this.reference = reference;
		this.deleted = deleted;
	}

	public final Long getId() {
		return id;
	}
	
	public final Long getTimestamp() {
		return timestamp;
	}
	
	public final Long getReference() {
		return reference;
	}
	
	public final boolean isDeleted() {
		return deleted;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).add("timestamp", timestamp).add("reference", reference)
				.add("deleted", deleted).toString();
	}
}
