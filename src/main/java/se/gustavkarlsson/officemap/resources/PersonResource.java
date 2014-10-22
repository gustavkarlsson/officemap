package se.gustavkarlsson.officemap.resources;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import se.gustavkarlsson.officemap.representations.Person;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/person")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

	private final AtomicLong counter;

	public PersonResource() {
		this.counter = new AtomicLong();
	}

	@GET
	@Timed
	public Person getPerson(@QueryParam("username") final Optional<String> username,
			@QueryParam("firstName") final Optional<String> firstName,
			@QueryParam("lastName") final Optional<String> lastName, @QueryParam("email") final Optional<String> email) {
		return new Person(counter.incrementAndGet(), username.or("johndoe"), firstName.or("John"), lastName.or("Doe"),
				email.or("john.doe@mycompany.com"));
	}
}
