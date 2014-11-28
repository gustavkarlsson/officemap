package se.gustavkarlsson.officemap.api.area;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.EqualsBuilder;

import se.gustavkarlsson.officemap.api.Reference;

import com.google.common.base.Objects;

@Entity
@DiscriminatorValue(AreaReference.TYPE)
public final class AreaReference extends Reference<Area> {
	
	public static final String TYPE = "AreaReference";
	
	public AreaReference() {
		// Required by Hibernate
		super();
	}
	
	public AreaReference(final Long id, final List<Area> items) {
		super(id, items);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
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
		final AreaReference rhs = (AreaReference) obj;
		return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
	}

}
