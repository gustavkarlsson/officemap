package se.gustavkarlsson.officemap.dao;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.item.Reference;
import se.gustavkarlsson.officemap.api.item.person.Person;
import se.gustavkarlsson.officemap.api.item.person.PersonReference;
import se.gustavkarlsson.officemap.dao.item.ItemDao;
import se.gustavkarlsson.officemap.dao.item.ItemDao.UpdateResponse;
import se.gustavkarlsson.officemap.dao.item.PersonDao;

import com.google.common.base.Optional;

public class PersonDaoTest extends AbstractDaoTest {
	
	private static final Person validPerson1 = Person
			.builder()
			.with(7l, null, new PersonReference(1l, Collections.<Person> emptyList()), false, "gustavkarlsson",
					"Gustav", "Karlsson", "gustav.karlsson@gmail.com",
					Sha1.builder().withSha1("cf23df2207d99a74fbe169e3eba035e633b65d94").build()).build();
	private static final Person validPerson2 = Person
			.builder()
			.with(null, 500l, new PersonReference(2l, Collections.<Person> emptyList()), false, "mickeymouse",
					"Mickey", "Mouse", "mickey.mouse@disney.com",
					Sha1.builder().withSha1("cf23df2207d99a74fbe169e3eba035e633b65d9a").build()).build();
	private static final Person validPerson3 = Person
			.builder()
			.with(null, null, new PersonReference(3l, Collections.<Person> emptyList()), true, "michaeljackson",
					"Michael", "Jackson", "michael.jackson@neverland.com", null).build();

	private static ItemDao<Person> dao;

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
		final Optional<Person> fetchedPerson = dao.findHeadByReference(ref.get());
		assertThat(fetchedPerson.isPresent()).isTrue();
		assertThat(fetchedPerson.get()).isEqualTo(validPerson1);
	}

	@Test
	public void nullPortraitPersists() throws Exception {
		final Person person = validPerson1.toBuilder().withPortrait(null).build();
		final Optional<Long> ref = dao.insert(person);
		assertThat(ref.isPresent()).isTrue();
		final Optional<Person> fetchedPerson = dao.findHeadByReference(ref.get());
		assertThat(fetchedPerson.isPresent()).isTrue();
		assertThat(fetchedPerson.get().getPortrait()).isNull();
	}

	@Test
	public void refIncrementsAfterInsert() throws Exception {
		final Optional<Long> ref1 = dao.insert(validPerson1);
		final Optional<Long> ref2 = dao.insert(validPerson2);
		final Optional<Long> ref3 = dao.insert(validPerson3);
		final Long ref1Id = ref1.get();
		assertThat(ref2.get()).isEqualTo(ref1Id + 1);
		assertThat(ref3.get()).isEqualTo(ref1Id + 2);
	}

	@Test
	public void updateSetsHead() throws Exception {
		final Optional<Long> ref = dao.insert(validPerson1);
		final Person fetchedPerson = dao.findHeadByReference(ref.get()).get();
		final Person modifiedPerson = fetchedPerson.toBuilder().withFirstName("Gustiff").build();
		final UpdateResponse updateResponse = dao.update(modifiedPerson.getReference().getId(), modifiedPerson);
		assertThat(updateResponse).isEqualTo(UpdateResponse.UPDATED);
		final Person fetchedModifiedPerson = dao.findHeadByReference(ref.get()).get();
		assertThat(fetchedModifiedPerson).isEqualTo(modifiedPerson);
		assertThat(fetchedModifiedPerson.getId()).isEqualTo(fetchedPerson.getId() + 1);
	}

	@Test
	public void findAllHeads() throws Exception {
		dao.insert(validPerson1);
		dao.insert(validPerson2);

		final List<Person> heads = dao.findAllHeads();
		assertThat(heads).containsExactly(validPerson1, validPerson2);
	}

	@Test
	public void findAllByReference() throws Exception {
		dao.insert(validPerson1);
		final Long person2Ref = dao.insert(validPerson2).get();
		final Person person2Fetched = dao.findHeadByReference(person2Ref).get();
		final Person person2Updated = person2Fetched.toBuilder().withEmail("no@email.com").build();
		dao.update(person2Updated.getReference().getId(), person2Updated);

		final List<Person> allWithPerson2Ref = dao.findAllByReference(person2Ref);
		assertThat(allWithPerson2Ref).containsExactly(validPerson2, person2Updated);
	}

	@Test(expected = NullPointerException.class)
	public void insertNullFails() throws Exception {
		dao.insert(null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidUsernameFails() throws Exception {
		final Person person = validPerson1.toBuilder().withUsername("").build();
		dao.insert(person);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidFirstNameFails() throws Exception {
		final Person person = validPerson1.toBuilder().withFirstName("").build();
		dao.insert(person);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidLastNameFails() throws Exception {
		final Person person = validPerson1.toBuilder().withLastName("").build();
		dao.insert(person);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidEmailFails() throws Exception {
		final Person person = validPerson1.toBuilder().withEmail("gustav.karlsson at gmail.com").build();
		dao.insert(person);
	}
	
	@Test
	public void timestampUpdatesOnInsert() throws Exception {
		final long presetTimestamp = -1l;
		final Person person = validPerson1.toBuilder().withTimestamp(presetTimestamp).build();
		final Optional<Long> reference = dao.insert(person);
		final Person fetchedPerson = dao.findHeadByReference(reference.get()).get();
		assertThat(fetchedPerson.getTimestamp()).isNotEqualTo(presetTimestamp);
	}
	
	@Test
	public void referenceUpdatesOnInsert() throws Exception {
		final Reference<Person> presetReference = new PersonReference();
		final Person person = validPerson1.toBuilder().withReference(presetReference).build();
		final Optional<Long> referenceId = dao.insert(person);
		assertThat(referenceId.get()).isNotEqualTo(presetReference.getId());
	}
}
