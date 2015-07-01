package se.gustavkarlsson.officemap.api.items;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gustavkarlsson.officemap.test.TestLocationBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LocationTest {
	
	private static final ObjectMapper mapper = Jackson.newObjectMapper();

	private static final Location Location = TestLocationBuilder.withTestParameters().build();
	
	private static String fixture;

	@BeforeClass
	public static void setUp() throws Exception {
		fixture = fixture("fixtures/location.json");
	}
	
	@Test
	public void serializesToJSON() throws Exception {
		final String serialized = mapper.writeValueAsString(Location);
		assertEquals(fixture, serialized, true);
	}
	
	@Test
	public void deserializesFromJSON() throws Exception {
		final Location deserialized = mapper.readValue(fixture, Location.class);
		assertThat(deserialized).isEqualTo(Location);
	}

	@Test
	public void validLocationValidates() throws Exception {
		assertThat(Location).isValid();
	}

	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(Location.class).usingGetClass().allFieldsShouldBeUsed().verify();
	}

}
