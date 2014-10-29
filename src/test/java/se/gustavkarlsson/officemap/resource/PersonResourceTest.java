package se.gustavkarlsson.officemap.resource;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.Person;
import se.gustavkarlsson.officemap.dao.PersonDao;
import se.gustavkarlsson.officemap.resources.PersonResource;
import se.gustavkarlsson.officemap.test.TestPersonBuilder;

import com.sun.jersey.api.client.UniformInterfaceException;

public class PersonResourceTest {
	
	private static final PersonDao dao = mock(PersonDao.class);
	
	private final Person person1 = TestPersonBuilder.withTestParameters().withId(1l).build();
	private final Person person2 = TestPersonBuilder.withTestParameters().withId(2l).build();
	private final Person[] persons = { person1, person2 };
	
	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder().addResource(new PersonResource(dao))
			.build();
	
	@Before
	public void setup() {
		when(dao.findById(eq(1l))).thenReturn(person1);
		when(dao.list()).thenReturn(Arrays.asList(persons));
	}
	
	@After
	public void teardown() {
		reset(dao);
	}
	
	@Test
	public void getPerson() {
		final Person fetchedPerson = resources.client().resource("/persons/1").get(Person.class);
		assertThat(fetchedPerson).isEqualTo(person1);
		verify(dao).findById(1l);
	}
	
	@Test
	public void getInvalidPerson() {
		try {
			resources.client().resource("/persons/3").get(Person.class);
			fail("No exception thrown. Expected UniformInterfaceException");
		} catch (UniformInterfaceException e) {
			final String errorMessage = e.getMessage();
			assertThat(errorMessage).contains("404");
		}
	}
	
	@Test
	public void listPersons() {
		final Person[] fetchedPersons = resources.client().resource("/persons").get(Person[].class);
		assertThat(fetchedPersons).containsExactly(persons);
		verify(dao).list();
	}
}