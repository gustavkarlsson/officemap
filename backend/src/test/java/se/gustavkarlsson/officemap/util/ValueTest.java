package se.gustavkarlsson.officemap.util;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValueTest {
	
	@Test
	public void nullValue() throws Exception {
		assertValue(Value.ofNull(), null);
		assertValue(Value.of(null), null);
	}
	
	@Test
	public void stringValue() throws Exception {
		final String value = "apa";
		assertValue(Value.of(value), value);
	}
	
	@Test
	public void intValue() throws Exception {
		final int value = 5;
		assertValue(Value.of(value), value);
	}
	
	@Test()
	public void absence() throws Exception {
		assertThat(Value.absent().isAbsent()).isTrue();
		assertThat(Value.absent().isPresent()).isFalse();
	}
	
	@Test(expected = IllegalStateException.class)
	public void absentGetFails() throws Exception {
		Value.absent().get();
	}
	
	@Test(expected = IllegalStateException.class)
	public void absentIsNullFails() throws Exception {
		Value.absent().isNull();
	}
	
	@Test(expected = IllegalStateException.class)
	public void absentIsNotNullFails() throws Exception {
		Value.absent().isNotNull();
	}
	
	@Test
	public void equalsContract() throws Exception {
		EqualsVerifier.forClass(Value.class).usingGetClass().allFieldsShouldBeUsed().verify();
		assertThat(Value.ofNull()).isEqualTo(Value.of(null));
		assertThat(Value.of("apa")).isEqualTo(Value.of("apa"));
		assertThat(Value.of("apa")).isNotEqualTo(Value.of("kaka"));
	}

	private <T> void assertValue(final Value<T> v, final T embedded) {
		final boolean nulll = embedded == null;
		assertThat(v.isNull()).isEqualTo(nulll);
		assertThat(v.isNotNull()).isNotEqualTo(nulll);
		assertThat(v.get()).isEqualTo(embedded);
		assertThat(v.get()).isSameAs(embedded);
		assertThat(v.isAbsent()).isFalse();
		assertThat(v.isPresent()).isTrue();
	}
}
