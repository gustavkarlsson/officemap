package se.gustavkarlsson.officemap.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.item.Reference;
import se.gustavkarlsson.officemap.api.item.area.Area;
import se.gustavkarlsson.officemap.api.item.person.Person;
import se.gustavkarlsson.officemap.test.TestAreaBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AreaTest {

	private static final ObjectMapper mapper = Jackson.newObjectMapper();

	private static final Area area = TestAreaBuilder.withTestParameters().build();
	
	private static String fixture;

	@BeforeClass
	public static void setUp() throws Exception {
		fixture = fixture("fixtures/area.json");
	}

	@Test
	public void serializesToJSON() throws Exception {
		final String serialized = mapper.writeValueAsString(area);
		assertEquals(fixture, serialized, true);
	}

	@Test
	public void deserializesFromJSON() throws Exception {
		final Area deserialized = mapper.readValue(fixture, Area.class);
		assertThat(deserialized).isEqualTo(area);
	}

	@Test
	public void validAreaValidates() throws Exception {
		assertThat(area).isValid();
	}

	@Test
	public void invalidId() throws Exception {
		assertInvalidId(-1l);
		assertInvalidId(Long.MIN_VALUE);
	}

	@Test
	public void invalidTimestamp() throws Exception {
		assertInvalidTimestamp(-1l);
		assertInvalidTimestamp(Long.MIN_VALUE);
	}

	@Test
	public void invalidName() throws Exception {
		assertInvalidName(null);
		assertInvalidName("");
		assertInvalidName(" \t\n");
	}

	@Test
	public void nullPersonsCreatesEmptyPersons() throws Exception {
		final Area areaToTest = area.toBuilder().withPersons(null).build();
		assertThat(areaToTest).isValid();
	}

	@Test
	public void nullPersonsAreRemoved() throws Exception {
		final Reference<Person> nullPerson = null;
		final Area areaToTest = area.toBuilder().withPersonsVararg(nullPerson).build();
		org.fest.assertions.api.Assertions.assertThat(areaToTest.getPersons()).isEmpty();
	}

	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(Area.class).usingGetClass().allFieldsShouldBeUsedExcept("id", "timestamp", "reference")
				.verify();
	}

	private void assertInvalidId(final Long id) {
		final Area invalidArea = area.toBuilder().withId(id).build();
		assertThatAreaHasInvalid(invalidArea, "id");
	}

	private void assertInvalidTimestamp(final Long timestamp) {
		final Area invalidArea = area.toBuilder().withTimestamp(timestamp).build();
		assertThatAreaHasInvalid(invalidArea, "timestamp");
	}

	private void assertInvalidName(final String username) {
		final Area invalidArea = area.toBuilder().withName(username).build();
		assertThatAreaHasInvalid(invalidArea, "name");
	}

	private void assertThatAreaHasInvalid(final Area area, final String property) {
		assertThat(area).hasInvalid(property);
	}

}
