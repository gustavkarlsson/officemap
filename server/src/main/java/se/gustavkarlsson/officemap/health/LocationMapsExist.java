package se.gustavkarlsson.officemap.health;

import com.codahale.metrics.health.HealthCheck;
import se.gustavkarlsson.officemap.api.items.Person;
import se.gustavkarlsson.officemap.core.ItemNotFoundException;
import se.gustavkarlsson.officemap.core.State;

import java.util.HashMap;
import java.util.Map;

public class LocationMapsExist extends HealthCheck {
	
	private final State state;

	public LocationMapsExist(State state) {
		this.state = state;
	}

	@Override
	protected Result check() throws Exception {
		Map<Integer, Integer> personMapRefs = getPersonMapRefs();
		Map<Integer, Integer> invalidPersonMapRefs = new HashMap<>();
		for (Map.Entry<Integer, Integer> personMapRef : personMapRefs.entrySet()) {
			Integer personRef = personMapRef.getKey();
			Integer mapRef = personMapRef.getValue();
			try {
				state.getMaps().get(mapRef);
			} catch (ItemNotFoundException e) {
				invalidPersonMapRefs.put(personRef, mapRef);
			}
		}
		if (!invalidPersonMapRefs.isEmpty()) {
			return Result.unhealthy("No map was found for the following Person <-> Map references: " + invalidPersonMapRefs);
		}
		return Result.healthy();
	}

	private Map<Integer, Integer> getPersonMapRefs() {
		Map<Integer, Integer> personMapRefs = new HashMap<>();
		for (Map.Entry<Integer, Person> entry : state.getPersons().getAll().entrySet()) {
			Integer personRef = entry.getKey();
			Person person = entry.getValue();
			if (person.getLocation() != null) {
				personMapRefs.put(personRef, person.getLocation().getMapRef());
			}
		}
		return personMapRefs;
	}
	
}
