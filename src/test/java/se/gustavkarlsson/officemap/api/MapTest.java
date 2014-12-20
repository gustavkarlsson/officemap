package se.gustavkarlsson.officemap.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.item.map.Map;
import se.gustavkarlsson.officemap.test.TestMapBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MapTest {
	
	private static final ObjectMapper mapper = Jackson.newObjectMapper();
	
	private static final Map map = TestMapBuilder.withTestParameters().build();

	private static String fixture;
	
	@BeforeClass
	public static void setUp() throws Exception {
		fixture = fixture("fixtures/map.json");
	}
	
	@Test
	public void serializesToJSON() throws Exception {
		final String serialized = mapper.writeValueAsString(map);
		assertEquals(fixture, serialized, true);
	}
	
	@Test
	public void deserializesFromJSON() throws Exception {
		final Map deserialized = mapper.readValue(fixture, Map.class);
		assertThat(deserialized).isEqualTo(map);
	}
	
	@Test
	public void validMapValidates() throws Exception {
		assertThat(map).isValid();
	}
	
	@Test
	public void invalidId() throws Exception {
		assertInvalidId(-1l);
		assertInvalidId(Long.MIN_VALUE);
	}
	
	@Test
	public void invalidName() throws Exception {
		assertInvalidName(null);
		assertInvalidName("");
		assertInvalidName(" \t\n");
	}
	
	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(Map.class).usingGetClass().allFieldsShouldBeUsedExcept("id").verify();
	}
	
	private void assertInvalidId(final Long id) {
		final Map invalidMap = map.toBuilder().withId(id).build();
		assertThatMapHasInvalid(invalidMap, "id");
	}
	
	private void assertInvalidName(final String username) {
		final Map invalidMap = map.toBuilder().withName(username).build();
		assertThatMapHasInvalid(invalidMap, "name");
	}
	
	private void assertThatMapHasInvalid(final Map map, final String property) {
		assertThat(map).hasInvalid(property);
	}
	
}
