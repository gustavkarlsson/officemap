package se.gustavkarlsson.officemap.api;

import static se.gustavkarlsson.officemap.test.AssertValidation.assertThat;

import java.util.ArrayList;
import java.util.List;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import se.gustavkarlsson.officemap.api.person.Person;
import se.gustavkarlsson.officemap.api.person.PersonReference;
import se.gustavkarlsson.officemap.test.TestPersonBuilder;

import com.google.common.collect.Lists;

public class PersonReferenceTest {
	
	private static final Person person = TestPersonBuilder.withTestParameters().build();
	private static final PersonReference personReference = new PersonReference(1l, Lists.newArrayList(person));
	
	@Test
	public void validPersonReferenceValidates() throws Exception {
		assertThat(personReference).isValid();
	}
	
	@Test
	public void invalidId() throws Exception {
		assertInvalidId(-1l);
		assertInvalidId(Long.MIN_VALUE);
	}
	
	@Test
	public void invalidItems() throws Exception {
		assertInvalidItems(null);
		assertInvalidItems(new ArrayList<Person>());
	}
	
	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(PersonReference.class).usingGetClass().allFieldsShouldBeUsedExcept("items").verify();
	}
	
	private void assertInvalidId(final Long id) {
		final PersonReference invalidPersonReference = new PersonReference(id, Lists.newArrayList(person));
		assertThatPersonHasInvalid(invalidPersonReference, "id");
	}
	
	private void assertInvalidItems(final List<Person> items) {
		final PersonReference invalidPersonReference = new PersonReference(1l, items);
		assertThatPersonHasInvalid(invalidPersonReference, "items");
	}
	
	private void assertThatPersonHasInvalid(final PersonReference personReference, final String property) {
		assertThat(personReference).hasInvalid(property);
	}
	
}
