package se.gustavkarlsson.officemap.resource;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;

import se.gustavkarlsson.officemap.api.Person;
import se.gustavkarlsson.officemap.dao.PersonDao;
import se.gustavkarlsson.officemap.resources.PersonResource;
import se.gustavkarlsson.officemap.test.TestPersonBuilder;

import com.google.common.base.Optional;

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
		when(dao.findHeadByRef(eq(1l))).thenReturn(Optional.of(person1));
		when(dao.findAllHeads()).thenReturn(Arrays.asList(persons));
	}

	@After
	public void teardown() {
		reset(dao);
	}
}