package se.gustavkarlsson.officemap.api.area;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

import se.gustavkarlsson.officemap.api.Item;
import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.person.Person;
import se.gustavkarlsson.officemap.util.CollectionsUtils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

@Entity
@Table(name = Area.TYPE)
@DiscriminatorValue(Area.TYPE)
@JsonSerialize(using = AreaSerializer.class)
@JsonDeserialize(using = AreaDeserializer.class)
public class Area extends Item<Area> {
	
	public static final String TYPE = "Area";

	@NotBlank
	@Column(name = "name", nullable = false)
	private final String name;

	@NotNull
	@ManyToMany(fetch = FetchType.LAZY, targetEntity = Reference.class)
	@JoinTable(name = "Area_PersonReferences", joinColumns = @JoinColumn(name = "areaFK"),
			inverseJoinColumns = @JoinColumn(name = "personReferenceFK"))
	private final Set<Reference<Person>> persons;

	private Area() {
		// Required by Hibernate
		super(null, null, null, false);
		this.name = null;
		this.persons = null;
	}

	private Area(final Long id, final Long timestamp, final Reference<Area> reference, final boolean deleted,
			final String name, final Set<Reference<Person>> persons) {
		super(id, timestamp, reference, deleted);
		this.name = name;
		this.persons = CollectionsUtils.unmodifiableSetWithoutNull(persons);
	}

	public String getName() {
		return name;
	}

	public final Set<Reference<Person>> getPersons() {
		return persons;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", getId()).add("reference", getReference())
				.add("deleted", isDeleted()).add("name", name).add("persons", persons).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(isDeleted(), name, persons);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		final Area rhs = (Area) obj;
		return new EqualsBuilder().append(isDeleted(), rhs.isDeleted()).append(name, rhs.name)
				.append(persons, rhs.getPersons()).isEquals();
	}
	
	public static class Builder {
		
		private Long id;
		
		private Reference<Area> reference;
		
		private Long timestamp;
		
		private boolean deleted;
		
		private String name;
		
		private Set<Reference<Person>> persons;
		
		protected Builder(final Long id, final Reference<Area> reference, final Long timestamp, final boolean deleted,
				final String name, final Collection<Reference<Person>> persons) {
			this.id = id;
			this.timestamp = timestamp;
			this.reference = reference;
			this.deleted = deleted;
			this.name = name;
			this.persons = persons == null ? new HashSet<Reference<Person>>() : new HashSet<>(persons);
		}
		
		public static Builder fromNothing() {
			return new Builder(null, null, null, false, null, null);
		}
		
		public static Builder withFields(final Long id, final Long timestamp, final Reference<Area> reference,
				final boolean deleted, final String name, final Collection<Reference<Person>> persons) {
			return new Builder(id, reference, timestamp, deleted, name, persons);
		}
		
		public static Builder fromArea(final Area area) {
			return new Builder(area.getId(), area.getReference(), area.getTimestamp(), area.isDeleted(),
					area.getName(), area.getPersons());
		}
		
		public Area build() {
			return new Area(id, timestamp, reference, deleted, name, persons);
		}
		
		public Long getId() {
			return id;
		}
		
		public Builder withId(final Long id) {
			this.id = id;
			return this;
		}
		
		public Reference<Area> getReference() {
			return reference;
		}
		
		public Builder withReference(final Reference<Area> reference) {
			this.reference = reference;
			return this;
		}
		
		public Long getTimestamp() {
			return timestamp;
		}
		
		public Builder withTimestamp(final Long timestamp) {
			this.timestamp = timestamp;
			return this;
		}
		
		public boolean isDeleted() {
			return deleted;
		}
		
		public Builder withDeleted(final boolean deleted) {
			this.deleted = deleted;
			return this;
		}
		
		public String getName() {
			return name;
		}
		
		public Builder withName(final String name) {
			this.name = name;
			return this;
		}
		
		public Set<Reference<Person>> getPersons() {
			return persons;
		}
		
		public Builder withPersons(final Set<Reference<Person>> persons) {
			this.persons = persons;
			return this;
		}
		
		@SafeVarargs
		public final Builder withPersonsVararg(final Reference<Person>... persons) {
			this.persons = Sets.newHashSet(persons);
			return this;
		}
	}

}
