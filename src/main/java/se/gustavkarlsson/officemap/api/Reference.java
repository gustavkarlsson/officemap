package se.gustavkarlsson.officemap.api;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Range;

import com.google.common.base.Objects;

@Entity
@Table(name = Reference.TYPE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Reference<T extends Item<T>> {
	
	public static final String TYPE = "Reference";

	@Range(min = 0)
	@Id
	@Column(name = "id")
	@TableGenerator(name = "TABLE_GEN", table = "Sequence", pkColumnName = "name", valueColumnName = "count",
			pkColumnValue = "ReferenceId")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private final Long id;

	@Size(min = 1)
	@OneToMany(mappedBy = "reference", fetch = FetchType.LAZY, targetEntity = Item.class)
	@Cascade(CascadeType.SAVE_UPDATE)
	private final List<T> items;
	
	protected Reference() {
		this.id = null;
		this.items = null;
	}
	
	protected Reference(final Long id, final List<T> items) {
		this.id = id;
		this.items = items;
	}
	
	public final Long getId() {
		return id;
	}

	public final List<T> getItems() {
		return items;
	}
	
	public T getHead() {
		return items.get(items.size() - 1);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", getId()).toString();
	}
}
