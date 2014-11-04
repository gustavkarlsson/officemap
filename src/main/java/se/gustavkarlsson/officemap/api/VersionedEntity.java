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

@Entity
@Table(name = "VersionedEntity")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class VersionedEntity {

	@Range(min = 0)
	@JsonProperty
	@Id
	@Column(name = "id")
	@TableGenerator(name = "TABLE_GEN", table = "Sequence", pkColumnName = "name", valueColumnName = "count",
			pkColumnValue = "VersionedEntityId")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private final Long id;
	
	@Range(min = 0)
	@JsonProperty
	@Column(name = "reference", nullable = false)
	private final Long reference;

	@JsonProperty
	@Column(name = "deleted", nullable = false)
	private final boolean deleted;
	
	protected VersionedEntity(final Long id, final Long reference, final boolean deleted) {
		this.id = id;
		this.reference = reference;
		this.deleted = deleted;
	}
	
	public final Long getId() {
		return id;
	}

	public final Long getReference() {
		return reference;
	}

	public final boolean isDeleted() {
		return deleted;
	}
}
