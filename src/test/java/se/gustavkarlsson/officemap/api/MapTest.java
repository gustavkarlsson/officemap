package se.gustavkarlsson.officemap.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.item.Map;
import se.gustavkarlsson.officemap.api.item.Sha1;
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
	public void invalidName() throws Exception {
		assertInvalidName(null);
		assertInvalidName("");
		assertInvalidName(" \t\n");
	}
	
	@Test
	public void nullImageInvalid() throws Exception {
		assertInvalidImage(null);
	}
	
	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(Map.class).usingGetClass().allFieldsShouldBeUsed().verify();
	}
	
	private void assertInvalidName(final String username) {
		final Map invalidMap = map.toBuilder().withName(username).build();
		assertThatMapHasInvalid(invalidMap, "name");
	}
	
	private void assertInvalidImage(final Sha1 image) {
		final Map invalidMap = map.toBuilder().withImage(image).build();
		assertThatMapHasInvalid(invalidMap, "image");
	}
	
	private void assertThatMapHasInvalid(final Map map, final String property) {
		assertThat(map).hasInvalid(property);
	}
	
}
