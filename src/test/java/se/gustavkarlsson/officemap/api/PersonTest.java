package se.gustavkarlsson.officemap.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PersonTest {

	private static final ObjectMapper mapper = Jackson.newObjectMapper();

	private static String fixture;
	private Person person;

	@BeforeClass
	public static void setUp() throws Exception {
		fixture = fixture("fixtures/person.json");
	}

	@Before
	public void before() {
		person = new Person(1, "johndoe", "John", "Doe", "john.doe@company.com", null);
	}

	@Test
	public void serializesToJSON() throws Exception {
		final String serialized = mapper.writeValueAsString(person);
		assertEquals(fixture, serialized, false);
	}

	@Test
	public void deserializesFromJSON() throws Exception {
		final Person deserialized = mapper.readValue(fixture, Person.class);
		assertThat(deserialized).isEqualTo(person);
	}

	@Test
	public void validPersonValidates() throws Exception {
		assertThat(person).isValid();
	}

	@Test
	public void blankUsernameIsInvalid() throws Exception {
		assertInvalidUsername(null);
		assertInvalidUsername("");
		assertInvalidUsername(" \t\n");
	}

	@Test
	public void blankFirstNameIsInvalid() throws Exception {
		assertInvalidFirstName(null);
		assertInvalidFirstName("");
		assertInvalidFirstName(" \t\n");
	}

	@Test
	public void blankLastNameIsInvalid() throws Exception {
		assertInvalidLastName(null);
		assertInvalidLastName("");
		assertInvalidLastName(" \t\n");
	}

	@Test
	public void invalidEmailIsInvalid() throws Exception {
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
	public void nullGroupBecomesEmptyGroup() throws Exception {
		Assertions.assertThat(person.getGroups()).isEmpty();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void groupsIsImmutable() throws Exception {
		person.getGroups().add(new Group());
	}

	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(Person.class).usingGetClass().allFieldsShouldBeUsedExcept("id", "groups").verify();
	}

	private void assertInvalidUsername(final String username) {
		final Person person = new Person(1, username, "John", "Doe", "john.doe@company.com", null);
		assertThatPersonHasInvalid(person, "username");
	}

	private void assertInvalidFirstName(final String firstName) {
		final Person person = new Person(1, "johndoe", firstName, "Doe", "john.doe@company.com", null);
		assertThatPersonHasInvalid(person, "firstName");
	}

	private void assertInvalidLastName(final String lastName) {
		final Person person = new Person(1, "johndoe", "John", lastName, "john.doe@company.com", null);
		assertThatPersonHasInvalid(person, "lastName");
	}

	private void assertInvalidEmail(final String email) {
		final Person person = new Person(1, "johndoe", "John", "Doe", email, null);
		assertThatPersonHasInvalid(person, "email");
	}

	private void assertThatPersonHasInvalid(final Person person, final String property) {
		assertThat(person).hasInvalid(property);
	}

}
