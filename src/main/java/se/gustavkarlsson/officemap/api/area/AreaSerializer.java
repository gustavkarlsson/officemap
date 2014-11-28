package se.gustavkarlsson.officemap.api.area;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import se.gustavkarlsson.officemap.api.ItemSerializer;
import se.gustavkarlsson.officemap.api.Reference;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class AreaSerializer extends ItemSerializer<Area> {
	
	protected AreaSerializer() {
		super(Area.class);
	}
	
	@Override
	protected void writeLeafFields(final Area value, final JsonGenerator jgen) throws IOException,
			JsonGenerationException {
		jgen.writeStringField("name", value.getName());
		jgen.writeArrayFieldStart("persons");
		final List<Long> sortedPersons = getSortedReferenceIds(value.getPersons());
		for (final Long personRef : sortedPersons) {
			jgen.writeNumber(personRef);
		}
		jgen.writeEndArray();
	}

	private List<Long> getSortedReferenceIds(final Collection<? extends Reference<?>> references) {
		final List<Long> sortedReferenceIds = new ArrayList<>();
		for (final Reference<?> ref : references) {
			sortedReferenceIds.add(ref.getId());
		}
		Collections.sort(sortedReferenceIds);
		return sortedReferenceIds;
	}

}
