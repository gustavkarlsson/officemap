package se.gustavkarlsson.officemap.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.test.TestPersonBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PersonTest {
	
	private static final ObjectMapper mapper = Jackson.newObjectMapper();

	private static final Person person = TestPersonBuilder.withTestParameters().build();
	
	private static String fixture;
	
	@BeforeClass
	public static void setUp() throws Exception {
		fixture = fixture("fixtures/person.json");
	}
	
	@Test
	public void serializesToJSON() throws Exception {
		final String serialized = mapper.writeValueAsString(person);
		assertEquals(fixture, serialized, true);
	}
	
	@Test
	public void validPersonValidates() throws Exception {
		assertThat(person).isValid();
	}
	
	@Test
	public void invalidUsername() throws Exception {
		assertInvalidUsername(null);
		assertInvalidUsername("");
		assertInvalidUsername(" \t\n");
	}
	
	@Test
	public void invalidFirstName() throws Exception {
		assertInvalidFirstName(null);
		assertInvalidFirstName("");
		assertInvalidFirstName(" \t\n");
	}
	
	@Test
	public void invalidLastName() throws Exception {
		assertInvalidLastName(null);
		assertInvalidLastName("");
		assertInvalidLastName(" \t\n");
	}
	
	@Test
	public void invalidEmail() throws Exception {
		assertInvalidEmail(null);
		assertInvalidEmail("");
		assertInvalidEmail("apa");
		assertInvalidEmail("apa@");
		assertInvalidEmail("@kaka.com");
		assertInvalidEmail("apa @kaka.com");
		assertInvalidEmail("apa@ kaka.com");
		assertInvalidEmail("apa.kaka");
	}
	
	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(Person.class).usingGetClass().allFieldsShouldBeUsed().verify();
	}
	
	private void assertInvalidUsername(final String username) {
		final Person invalidPerson = person.toBuilder().withUsername(username).build();
		assertThatPersonHasInvalid(invalidPerson, "username");
	}
	
	private void assertInvalidFirstName(final String firstName) {
		final Person invalidPerson = person.toBuilder().withFirstName(firstName).build();
		assertThatPersonHasInvalid(invalidPerson, "firstName");
	}
	
	private void assertInvalidLastName(final String lastName) {
		final Person invalidPerson = person.toBuilder().withLastName(lastName).build();
		assertThatPersonHasInvalid(invalidPerson, "lastName");
	}
	
	private void assertInvalidEmail(final String email) {
		final Person invalidPerson = person.toBuilder().withEmail(email).build();
		assertThatPersonHasInvalid(invalidPerson, "email");
	}
	
	private void assertThatPersonHasInvalid(final Person person, final String property) {
		assertThat(person).hasInvalid(property);
	}
	
}
