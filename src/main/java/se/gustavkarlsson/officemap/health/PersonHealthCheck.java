package se.gustavkarlsson.officemap.health;

import se.gustavkarlsson.officemap.representations.Person;
import se.gustavkarlsson.officemap.resources.PersonResource;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Optional;

public class PersonHealthCheck extends HealthCheck {

	private final PersonResource resource = new PersonResource();
	
	@Override
	protected Result check() throws Exception {
		final Person person1 = getDefaultPerson();
		final Person person2 = getDefaultPerson();
		if (person1.getId() >= person2.getId()) {
			return Result.unhealthy("ID:s don't increment. person1 ID: " + person1.getId() + ", person2 ID: "
					+ person2.getId());
		}
		return Result.healthy();
	}
	
	private Person getDefaultPerson() {
		return resource.getPerson(Optional.<String> absent(), Optional.<String> absent(), Optional.<String> absent(),
				Optional.<String> absent());
	}
	
}
