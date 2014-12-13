package se.gustavkarlsson.officemap.dao;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.api.item.Reference;
import se.gustavkarlsson.officemap.api.item.area.Area;
import se.gustavkarlsson.officemap.api.item.area.AreaReference;
import se.gustavkarlsson.officemap.api.item.person.Person;
import se.gustavkarlsson.officemap.api.item.person.PersonReference;
import se.gustavkarlsson.officemap.dao.item.AreaDao;
import se.gustavkarlsson.officemap.dao.item.ItemDao;
import se.gustavkarlsson.officemap.dao.item.ItemDao.UpdateResponse;
import se.gustavkarlsson.officemap.dao.item.PersonDao;

import com.google.common.base.Optional;

public class AreaDaoTest extends AbstractDaoTest {

	private static final Area validArea1 = Area
			.builder()
			.with(null, null, new AreaReference(1l, Collections.<Area> emptyList()), false, "Floor 1",
					Sha1.builder().withSha1("cf23df2207d99a74fbe169e3eba045e633b65d94").build(), null).build();
	private static final Area validArea2 = Area
			.builder()
			.with(1l, null, null, true, "Floor 2",
					Sha1.builder().withSha1("cf23df2207d99a74abe169e3eba035e633b65d94").build(), null).build();
	private static final Area validArea3 = Area
			.builder()
			.with(null, -400l, null, false, "Floor 3",
					Sha1.builder().withSha1("cf23df2207d93a74fbe169e3eba035e633b65d94").build(), null).build();

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

	private static ItemDao<Area> dao;
	private static ItemDao<Person> personDao;

	@BeforeClass
	public static void initDao() {
		dao = new AreaDao(getSessionFactory());
		personDao = new PersonDao(getSessionFactory());
	}

	@Test
	public void insertValidAreasSucceeds() throws Exception {
		final Area area1 = validArea1;
		final Area area2 = validArea2;
		dao.insert(area1);
		dao.insert(area2);
	}

	@Test
	public void personEqualsAfterPersistence() throws Exception {
		final Optional<Long> ref = dao.insert(validArea1);
		assertThat(ref.isPresent()).isTrue();
		final Optional<Area> fetchedArea = dao.findHeadByReference(ref.get());
		assertThat(fetchedArea.isPresent()).isTrue();
		assertThat(fetchedArea.get()).isEqualTo(validArea1);
	}

	@Test
	public void refIncrementsAfterInsert() throws Exception {
		final Optional<Long> ref1 = dao.insert(validArea1);
		final Optional<Long> ref2 = dao.insert(validArea2);
		final Optional<Long> ref3 = dao.insert(validArea3);
		final Long ref1Id = ref1.get();
		assertThat(ref2.get()).isEqualTo(ref1Id + 1);
		assertThat(ref3.get()).isEqualTo(ref1Id + 2);
	}

	@Test
	public void updateSetsHead() throws Exception {
		final Optional<Long> ref = dao.insert(validArea1);
		final Area fetchedArea = dao.findHeadByReference(ref.get()).get();
		final Area modifiedArea = fetchedArea.toBuilder().withName("Floor 11").build();
		final UpdateResponse updateResponse = dao.update(modifiedArea.getReference().getId(), modifiedArea);
		assertThat(updateResponse).isEqualTo(UpdateResponse.UPDATED);
		final Area fetchedModifiedArea = dao.findHeadByReference(ref.get()).get();
		assertThat(fetchedModifiedArea).isEqualTo(modifiedArea);
		assertThat(fetchedModifiedArea.getId()).isEqualTo(fetchedArea.getId() + 1);
	}

	@Test
	public void findAllHeads() throws Exception {
		dao.insert(validArea1);
		dao.insert(validArea2);

		final List<Area> heads = dao.findAllHeads();
		assertThat(heads).containsExactly(validArea1, validArea2);
	}

	@Test
	public void findAllByReference() throws Exception {
		dao.insert(validArea1);
		final Long area2Ref = dao.insert(validArea2).get();
		final Area area2Fetched = dao.findHeadByReference(area2Ref).get();
		final Area area2Updated = area2Fetched.toBuilder().withName("Floor 1 modified").build();
		dao.update(area2Updated.getReference().getId(), area2Updated);

		final List<Area> allWithArea2Ref = dao.findAllByReference(area2Ref);
		assertThat(allWithArea2Ref).containsExactly(validArea2, area2Updated);
	}

	@Test(expected = NullPointerException.class)
	public void insertNullFails() throws Exception {
		dao.insert(null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void insertWithInvalidNameFails() throws Exception {
		final Area area = validArea1.toBuilder().withName("").build();
		dao.insert(area);
	}

	@Test
	public void timestampUpdatesOnInsert() throws Exception {
		final long presetTimestamp = -1l;
		final Area area = validArea1.toBuilder().withTimestamp(presetTimestamp).build();
		final Optional<Long> reference = dao.insert(area);
		final Area fetchedArea = dao.findHeadByReference(reference.get()).get();
		assertThat(fetchedArea.getTimestamp()).isNotEqualTo(presetTimestamp);
	}

	@Test
	public void referenceUpdatesOnInsert() throws Exception {
		final Reference<Area> presetReference = new AreaReference();
		final Area area = validArea1.toBuilder().withReference(presetReference).build();
		final Optional<Long> referenceId = dao.insert(area);
		assertThat(referenceId.get()).isNotEqualTo(presetReference.getId());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void validPersonsPersists() throws Exception {
		final Long person1RefId = personDao.insert(validPerson1).get();
		final Reference<Person> person1Ref = personDao.findHeadByReference(person1RefId).get().getReference();
		final Long person2RefId = personDao.insert(validPerson1).get();
		final Reference<Person> person2Ref = personDao.findHeadByReference(person2RefId).get().getReference();

		final Area area = validArea1.toBuilder().withPersonsVararg(person1Ref, person2Ref).build();
		final Optional<Long> possibleArea1RefId = dao.insert(area);
		final Optional<Area> fetchedArea = dao.findHeadByReference(possibleArea1RefId.get());

		assertThat(fetchedArea.get().getPersons()).containsOnly(person1Ref, person2Ref);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void modifyingPersonsPersists() throws Exception {
		final Long person1RefId = personDao.insert(validPerson1).get();
		final Reference<Person> person1Ref = personDao.findReference(person1RefId).get();
		final Long person2RefId = personDao.insert(validPerson2).get();
		final Reference<Person> person2Ref = personDao.findReference(person2RefId).get();

		final Area area = validArea1.toBuilder().withPersonsVararg(person1Ref, person2Ref).build();
		final Optional<Long> possibleArea1RefId = dao.insert(area);
		final Optional<Area> fetchedArea = dao.findHeadByReference(possibleArea1RefId.get());

		final Area updatedArea = fetchedArea.get().toBuilder().withPersonsVararg(person1Ref).build();
		final UpdateResponse updateResponse = dao.update(possibleArea1RefId.get(), updatedArea);
		assertThat(updateResponse).isSameAs(UpdateResponse.UPDATED);
		final Optional<Area> updatedFetchedArea = dao.findHeadByReference(possibleArea1RefId.get());

		assertThat(updatedFetchedArea.get().getPersons()).containsOnly(person1Ref);
	}

	@Test(expected = IllegalStateException.class)
	public void insertOtherAreaWithPersonFails() throws Exception {
		final Long person1RefId = personDao.insert(validPerson1).get();
		final Reference<Person> personRef = personDao.findReference(person1RefId).get();

		final Area area = validArea1.toBuilder().withPersonsVararg(personRef).build();
		dao.insert(area);

		final Area area2 = validArea1.toBuilder().withPersonsVararg(personRef).withName("Other Area").build();
		dao.insert(area2);
	}

	@Test(expected = IllegalStateException.class)
	public void updateOtherAreaWithPersonFails() throws Exception {
		final Long person1RefId = personDao.insert(validPerson1).get();
		final Reference<Person> personRef = personDao.findReference(person1RefId).get();

		final Area area = validArea1.toBuilder().withPersonsVararg(personRef).build();
		dao.insert(area);

		final Area area2 = validArea1.toBuilder().withName("Other Area").build();
		dao.insert(area2);
		final Area area2Updated = area2.toBuilder().withPersonsVararg(personRef).build();
		dao.update(area2Updated.getReference().getId(), area2Updated);
	}
}
