package se.gustavkarlsson.officemap.dao;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.Person;

import com.google.common.base.Optional;

public class PersonDaoTest extends AbstractDaoTest {
	
	private static final Person validPerson1 = Person.Builder.withFields(7l, null, null, false, "gustavkarlsson",
			"Gustav", "Karlsson", "gustav.karlsson@gmail.com").build();
	private static final Person validPerson2 = Person.Builder.withFields(null, 500l, null, false, "mickeymouse",
			"Mickey", "Mouse", "mickey.mouse@disney.com").build();
	private static final Person validPerson3 = Person.Builder.withFields(null, null, 6l, true, "michaeljackson",
			"Michael", "Jackson", "michael.jackson@neverland.com").build();

	private static PersonDao dao;

	@BeforeClass
	public static void initDao() {
		dao = new PersonDao(getSessionFactory());
	}

	@Test
	public void insertValidPersonsSucceeds() throws Exception {
		dao.insert(validPerson1);
		dao.insert(validPerson2);
		dao.insert(validPerson3);
	}

	@Test
	public void personEqualsAfterPersistence() throws Exception {
		final Optional<Long> ref = dao.insert(validPerson1);
		assertThat(ref.isPresent()).isTrue();
		final Optional<Person> fetchedPerson = dao.findHeadByRef(ref.get());
		assertThat(fetchedPerson.isPresent()).isTrue();
		assertThat(fetchedPerson.get()).isEqualTo(validPerson1);
	}

	@Test
	public void refIncrementsAfterInsert() throws Exception {
		final Optional<Long> ref1 = dao.insert(validPerson1);
		final Optional<Long> ref2 = dao.insert(validPerson2);
		final Optional<Long> ref3 = dao.insert(validPerson3);
		assertThat(ref1.get()).isEqualTo(1l);
		assertThat(ref2.get()).isEqualTo(2l);
		assertThat(ref3.get()).isEqualTo(3l);
	}

	@Test
	public void updateSetsHead() throws Exception {
		final Optional<Long> ref = dao.insert(validPerson1);
		final Person fetchedPerson = dao.findHeadByRef(ref.get()).get();
		assertThat(fetchedPerson.getId()).isEqualTo(1l);
		final Person modifiedPerson = Person.Builder.fromPerson(fetchedPerson).withFirstName("Gustiff").build();
		dao.update(modifiedPerson.getReference(), modifiedPerson);
		final Person fetchedModifiedPerson = dao.findHeadByRef(ref.get()).get();
		assertThat(fetchedModifiedPerson).isEqualTo(modifiedPerson);
		assertThat(fetchedModifiedPerson.getId()).isEqualTo(2l);
	}

	// TODO rename and extract into more tests
	@Test
	public void find() throws Exception {
		dao.insert(validPerson1);
		final Long person2Ref = dao.insert(validPerson2).get();
		final Person person2Fetched = dao.findHeadByRef(person2Ref).get();
		final Person person2Updated = Person.Builder.fromPerson(person2Fetched).withEmail("no@email.com").build();
		dao.update(person2Updated.getReference(), person2Updated);

		final List<Person> heads = dao.findAllHeads();
		assertThat(heads).containsExactly(validPerson1, person2Updated);

		final List<Person> allWithPerson2Ref = dao.findAllByRef(person2Ref);
		assertThat(allWithPerson2Ref).containsExactly(validPerson2, person2Updated);

		final List<Person> all = dao.findAll();
		assertThat(all).containsExactly(validPerson1, validPerson2, person2Updated);
	}

	@Test(expected = NullPointerException.class)
	public void insertNullFails() throws Exception {
		dao.insert(null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidUsernameFails() throws Exception {
		final Person person = Person.Builder.fromPerson(validPerson1).withUsername("").build();
		dao.insert(person);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidFirstNameFails() throws Exception {
		final Person person = Person.Builder.fromPerson(validPerson1).withFirstName("").build();
		dao.insert(person);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidLastNameFails() throws Exception {
		final Person person = Person.Builder.fromPerson(validPerson1).withLastName("").build();
		dao.insert(person);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidEmailFails() throws Exception {
		final Person person = Person.Builder.fromPerson(validPerson1).withEmail("gustav.karlsson at gmail.com").build();
		dao.insert(person);
	}
	
	@Test
	public void timestampUpdatesOnInsert() throws Exception {
		final long presetTimestamp = -1l;
		final Person person = Person.Builder.fromPerson(validPerson1).withTimestamp(presetTimestamp).build();
		final Optional<Long> reference = dao.insert(person);
		final Person fetchedPerson = dao.findHeadByRef(reference.get()).get();
		assertThat(fetchedPerson.getTimestamp()).isNotEqualTo(presetTimestamp);
	}
	
	@Test
	public void referenceUpdatesOnInsert() throws Exception {
		final long presetReference = -1l;
		final Person person = Person.Builder.fromPerson(validPerson1).withReference(presetReference).build();
		final Optional<Long> reference = dao.insert(person);
		assertThat(reference.get()).isNotEqualTo(presetReference);
	}
}
