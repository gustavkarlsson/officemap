package se.gustavkarlsson.officemap.api.item.area;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotBlank;

import se.gustavkarlsson.officemap.api.fileentry.FileEntry;
import se.gustavkarlsson.officemap.api.item.Item;
import se.gustavkarlsson.officemap.api.item.Reference;
import se.gustavkarlsson.officemap.api.item.person.Person;
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
	@OneToMany
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@PrimaryKeyJoinColumn
	private final FileEntry map;
	
	// TODO Make sure only one head can have the same persons
	@NotNull
	@OneToMany(fetch = FetchType.EAGER, targetEntity = Reference.class)
	@JoinTable(name = "Area_PersonReferences", joinColumns = @JoinColumn(name = "area_fk"),
			inverseJoinColumns = @JoinColumn(name = "personReference_fk"))
	private final Set<Reference<Person>> persons;
	
	private Area() {
		// Required by Hibernate
		super(null, null, null, false);
		this.name = null;
		this.map = null;
		this.persons = null;
	}
	
	private Area(final Long id, final Long timestamp, final Reference<Area> reference, final boolean deleted,
			final String name, final FileEntry map, final Set<Reference<Person>> persons) {
		super(id, timestamp, reference, deleted);
		this.name = name;
		this.map = map;
		this.persons = CollectionsUtils.unmodifiableSetWithoutNull(persons);
	}
	
	public String getName() {
		return name;
	}
	
	public FileEntry getMap() {
		return map;
	}
	
	public final Set<Reference<Person>> getPersons() {
		return persons;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", getId()).add("reference", getReference())
				.add("deleted", isDeleted()).add("name", name).add("map", map).add("persons", persons).toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(isDeleted(), name, map, persons);
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
		return new EqualsBuilder().append(isDeleted(), rhs.isDeleted()).append(name, rhs.name).append(map, rhs.map)
				.append(persons, rhs.getPersons()).isEquals();
	}

	public Builder toBuilder() {
		return builder().with(getId(), getTimestamp(), getReference(), isDeleted(), name, map, persons);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private Long id;

		private Reference<Area> reference;

		private Long timestamp;

		private Boolean deleted;

		private String name;

		private FileEntry map;

		private Set<Reference<Person>> persons;

		protected Builder() {
		}

		public Area build() {
			return new Area(id, timestamp, reference, deleted, name, map, persons);
		}

		public Builder with(final Long id, final Long timestamp, final Reference<Area> reference,
				final boolean deleted, final String name, final FileEntry map,
				final Collection<Reference<Person>> persons) {
			this.id = id;
			this.timestamp = timestamp;
			this.reference = reference;
			this.deleted = deleted;
			this.name = name;
			this.map = map;
			this.persons = persons == null ? new HashSet<Reference<Person>>() : new HashSet<>(persons);
			return this;
		}

		public Builder withId(final Long id) {
			this.id = id;
			return this;
		}

		public Builder withReference(final Reference<Area> reference) {
			this.reference = reference;
			return this;
		}

		public Builder withTimestamp(final Long timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder withDeleted(final boolean deleted) {
			this.deleted = deleted;
			return this;
		}

		public Builder withName(final String name) {
			this.name = name;
			return this;
		}

		public Builder withMap(final FileEntry map) {
			this.map = map;
			return this;
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
