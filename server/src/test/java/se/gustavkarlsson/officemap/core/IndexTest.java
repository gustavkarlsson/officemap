package se.gustavkarlsson.officemap.core;

import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import se.gustavkarlsson.officemap.api.items.Searchable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexTest {

	private Index index;

	@Before
	public void setUp() {
		index = new Index();
	}


	@Test
	public void matchesSame() {
		String value = "foo";
		storeAndQuerySingleShouldSucceed(value, value);
	}

	@Test
	 public void storeAndSearchInvalidFindsNothing() {
		index.add(0, new StringSearchable("foo"));
		assertThat(index.search("bar", 10)).isEmpty();
	}

	@Test
	public void searchEmptyFindsNothing() {
		assertThat(index.search("bar", 10)).isEmpty();
	}

	@Test
	public void doesNotFindAfterRemove() {
		int ref = 8;
		String value = "foo";
		index.add(ref, new StringSearchable(value));
		index.remove(ref);
		assertThat(index.search(value, 10)).isEmpty();
	}

	@Test
	public void updateChangesValue() {
		int ref = 0;
		String initialValue = "foo";
		String updatedValue = "bar";
		index.add(ref, new StringSearchable(initialValue));
		index.update(ref, new StringSearchable(updatedValue));
		assertThat(index.search(initialValue, 10)).isEmpty();
		List<Map.Entry<String, Integer>> results = index.search(updatedValue, 10);
		assertThat(results).hasSize(1);
		Map.Entry<String, Integer> singleResult = results.get(0);
		assertThat(singleResult.getKey()).isEqualTo(StringSearchable.TYPE);
		assertThat(singleResult.getValue()).isEqualTo(ref);
	}

	@Test
	public void deleteSingleDigitRefDoesNotDeleteDoubleDigitRef() {
		int refSingle = 1;
		int refDouble = 11;
		String value = "foo";
		index.add(refSingle, new StringSearchable(value));
		index.add(refDouble, new StringSearchable(value));
		index.remove(refSingle);
		assertThat(index.search(value, 10)).hasSize(1);
	}

	@Test
	public void allTermsMustMatch() {
		int ref1 = 3;
		int ref2 = 7;
		String value1 = "foo bar";
		String value2 = "bar burr";
		index.add(ref1, new StringSearchable(value1));
		index.add(ref2, new StringSearchable(value2));
		List<Map.Entry<String, Integer>> results = index.search(value1, 10);
		assertThat(results).hasSize(1);
		Map.Entry<String, Integer> singleResult = results.get(0);
		assertThat(singleResult.getKey()).isEqualTo(StringSearchable.TYPE);
		assertThat(singleResult.getValue()).isEqualTo(ref1);
	}

	@Test
	public void matchesSubstring() {
		storeAndQuerySingleShouldSucceed("foobar", "oob");
	}

	@Test
	public void matchesDifferentCase() {
		storeAndQuerySingleShouldSucceed("fOObar", "FooBAR");
	}

	@Test
	public void matchesFirstAndLastTerm() {
		storeAndQuerySingleShouldSucceed("one too three", "one three");
	}

	@Test
	public void matchesEmail() {
		String email = "foo.bar@email.com";
		storeAndQuerySingleShouldSucceed(email, email);
	}

	@Test
	public void matchesEmailSubstring() {
		storeAndQuerySingleShouldSucceed("foo.bar@email.com", "ar@em");
	}

	@Test
	public void leadingStarDoesNotThrowException() {
		index.search("*foo", 100);
	}

	@Test
	public void reservedCharectersAreEscaped() {
		storeAndQuerySingleShouldSucceed("fo+o*bar", "+o*b");
	}

	@Test
	public void emptyStringDoesNotThrowException() {
		index.search("", 100);
	}

	@Test
	public void tooLongSearchString() {
		storeAndQuerySingleShouldFail("foo", "foob");
	}

	private void storeAndQuerySingleShouldSucceed(String value, String query) {
		int ref = 5;
		index.add(ref, new StringSearchable(value));
		List<Map.Entry<String, Integer>> results = index.search(query, 100);
		assertThat(results).hasSize(1);
		Map.Entry<String, Integer> singleResult = results.get(0);
		assertThat(singleResult.getKey()).isEqualTo(StringSearchable.TYPE);
		assertThat(singleResult.getValue()).isEqualTo(ref);
	}

	private void storeAndQuerySingleShouldFail(String value, String query) {
		int ref = 5;
		index.add(ref, new StringSearchable(value));
		List<Map.Entry<String, Integer>> results = index.search(query, 100);
		assertThat(results).isEmpty();
	}

	private static class StringSearchable implements Searchable {
		public static final String TYPE = "string_searchable";

		private final String value;

		public StringSearchable(String value) {
			this.value = value;
		}

		@Override
		public Map<String, String> getFields() {
			Map<String, String> fields = new HashMap<>();
			fields.put("value", value);
			return fields;
		}

		@Override
		public String getType() {
			return TYPE;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj.getClass() != getClass()) {
				return false;
			}
			final StringSearchable rhs = (StringSearchable) obj;
			return new EqualsBuilder().append(value, rhs.value).isEquals();
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(value);
		}
	}
	
}
