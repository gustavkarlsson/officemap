package se.gustavkarlsson.officemap.dao;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.Person;

import com.google.common.base.Optional;

public class PersonDaoTest extends AbstractDaoTest {

	private static PersonDao dao;

	@BeforeClass
	public static void initDao() {
		dao = new PersonDao(getSessionFactory());
	}

	@Test
	public void persistenceEquals() throws Exception {
		final Person person = Person.Builder.withFields(null, null, false, "gustavkarlsson", "Gustav", "Karlsson",
				"gustav.karlsson@gmail.com").build();
		final Optional<Long> ref = dao.insert(person);
		assertThat(ref.isPresent()).isTrue();
		final Optional<Person> fetchedPerson = dao.findHeadByRef(ref.get());
		assertThat(fetchedPerson.isPresent()).isTrue();
		assertThat(fetchedPerson.get()).isEqualTo(person);
	}

	@Test
	public void insertRefIncrement() throws Exception {
		final Person person1 = Person.Builder.withFields(null, null, false, "gustavkarlsson", "Gustav", "Karlsson",
				"gustav.karlsson@gmail.com").build();
		final Person person2 = Person.Builder.withFields(null, null, false, "mickeymouse", "Mickey", "Mouse",
				"mickey.mouse@disney.com").build();
		final Person person3 = Person.Builder.withFields(null, null, true, "michaeljackson", "Michael", "Jackson",
				"michael.jackson@neverland.com").build();
		final Optional<Long> ref1 = dao.insert(person1);
		final Optional<Long> ref2 = dao.insert(person2);
		final Optional<Long> ref3 = dao.insert(person3);
		assertThat(ref1.get()).isEqualTo(1l);
		assertThat(ref2.get()).isEqualTo(2l);
		assertThat(ref3.get()).isEqualTo(3l);
	}

	@Test
	public void updateSetsHead() throws Exception {
		final Person person = Person.Builder.withFields(null, null, false, "gustavkarlsson", "Gustav", "Karlsson",
				"gustav.karlsson@gmail.com").build();
		final Optional<Long> ref = dao.insert(person);
		final Person fetchedPerson = dao.findHeadByRef(ref.get()).get();
		assertThat(fetchedPerson.getId()).isEqualTo(1l);
		final Person modifiedPerson = Person.Builder.fromPerson(fetchedPerson).withFirstName("Gustiff").build();
		dao.update(modifiedPerson.getReference(), modifiedPerson);
		final Person fetchedModifiedPerson = dao.findHeadByRef(ref.get()).get();
		assertThat(fetchedModifiedPerson).isEqualTo(modifiedPerson);
		assertThat(fetchedModifiedPerson.getId()).isEqualTo(2l);
	}

	@Test
	public void find() throws Exception {
		final Person person1 = Person.Builder.withFields(null, null, false, "gustavkarlsson", "Gustav", "Karlsson",
				"gustav.karlsson@gmail.com").build();
		final Person person2 = Person.Builder.withFields(null, null, false, "mickeymouse", "Mickey", "Mouse",
				"mickey.mouse@disney.com").build();
		dao.insert(person1);
		final Long person2Ref = dao.insert(person2).get();
		final Person person2Fetched = dao.findHeadByRef(person2Ref).get();
		final Person person2Updated = Person.Builder.fromPerson(person2Fetched).withEmail("no@email.com").build();
		dao.update(person2Updated.getReference(), person2Updated);

		final List<Person> heads = dao.findAllHeads();
		assertThat(heads).containsExactly(person1, person2Updated);

		final List<Person> allWithPerson2Ref = dao.findAllByRef(person2Ref);
		assertThat(allWithPerson2Ref).containsExactly(person2, person2Updated);

		final List<Person> all = dao.findAll();
		assertThat(all).containsExactly(person1, person2, person2Updated);
	}

	@Test(expected = NullPointerException.class)
	public void insertWithNull() throws Exception {
		dao.insert(null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidUsername() throws Exception {
		final Person person = Person.Builder.withFields(null, null, false, "", "Gustav", "Karlsson",
				"gustav.karlsson@gmail.com").build();
		dao.insert(person);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidFirstName() throws Exception {
		final Person person = Person.Builder.withFields(null, null, false, "gustavkarlsson", "", "Karlsson",
				"gustav.karlsson@gmail.com").build();
		dao.insert(person);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidLastName() throws Exception {
		final Person person = Person.Builder.withFields(null, null, false, "gustavkarlsson", "Gustav", "",
				"gustav.karlsson@gmail.com").build();
		dao.insert(person);
	}

	@Test(expected = IllegalStateException.class)
	public void insertWithReference() throws Exception {
		final Person person = Person.Builder.withFields(null, 1l, false, "gustavkarlsson", "Gustav", "Karlsson",
				"gustav.karlsson@gmail.com").build();
		dao.insert(person);
	}

	@Test(expected = IllegalStateException.class)
	public void insertWithId() throws Exception {
		final Person person = Person.Builder.withFields(1l, null, false, "gustavkarlsson", "Gustav", "Karlsson",
				"gustav.karlsson@gmail.com").build();
		dao.insert(person);
	}
}
