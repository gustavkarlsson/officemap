package se.gustavkarlsson.officemap.api;

import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;

import java.util.ArrayList;
import java.util.List;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import se.gustavkarlsson.officemap.api.area.Area;
import se.gustavkarlsson.officemap.api.area.AreaReference;
import se.gustavkarlsson.officemap.test.TestAreaBuilder;

import com.google.common.collect.Lists;

public class AreaReferenceTest {
	
	private static final Area area = TestAreaBuilder.withTestParameters().build();
	private static final AreaReference areaReference = new AreaReference(1l, Lists.newArrayList(area));
	
	@Test
	public void validAreaReferenceValidates() throws Exception {
		assertThat(areaReference).isValid();
	}
	
	@Test
	public void invalidId() throws Exception {
		assertInvalidId(-1l);
		assertInvalidId(Long.MIN_VALUE);
	}
	
	@Test
	public void invalidItems() throws Exception {
		assertInvalidItems(null);
		assertInvalidItems(new ArrayList<Area>());
	}
	
	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(AreaReference.class).usingGetClass().allFieldsShouldBeUsedExcept("items").verify();
	}
	
	private void assertInvalidId(final Long id) {
		final AreaReference invalidAreaReference = new AreaReference(id, Lists.newArrayList(area));
		assertThatPersonHasInvalid(invalidAreaReference, "id");
	}
	
	private void assertInvalidItems(final List<Area> items) {
		final AreaReference invalidAreaReference = new AreaReference(1l, items);
		assertThatPersonHasInvalid(invalidAreaReference, "items");
	}
	
	private void assertThatPersonHasInvalid(final AreaReference areaReference, final String property) {
		assertThat(areaReference).hasInvalid(property);
	}
	
}
