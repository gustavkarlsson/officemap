package se.gustavkarlsson.officemap.events.map;

import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Before;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.items.Map;
import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.test.TestMapBuilder;

public class CreateMapEventTest {

	private Map map;

	@Before
	public void setUp() {
		map = TestMapBuilder.withTestParameters().build();
	}

	@Test
	public void testCreateWithValidMapIsValid() throws Exception {
		final CreateMapEvent event = new CreateMapEvent(0, 0, map);
		assertThat(event).isValid();
	}

	@Test
	public void testCreateWithNullMapIsInvalid() throws Exception {
		final CreateMapEvent event = new CreateMapEvent(0, 0, null);
		assertThat(event).isInvalid();
	}

	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(CreateMapEvent.class).usingGetClass().allFieldsShouldBeUsedExcept("id").verify();
	}

	@Test
	public void testProcessCreatesMap() throws Exception {
		// FIXME mock state
		final State state = new State();
		final int ref = state.getMaps().getNextRef();
		new CreateMapEvent(0, ref, map).process(state);
		final Map savedMap = state.getMaps().get(ref);
		assertThat(savedMap).isEqualTo(map);
	}

}
