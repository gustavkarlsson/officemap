package se.gustavkarlsson.officemap.resources;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.gustavkarlsson.officemap.api.Person;

import com.codahale.metrics.annotation.Timed;

@Path("/person/{username: .*}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {
	
	@POST
	@Timed
	public Response create(@Valid final Person person) {
		// TODO Create person
		return null;
	}

	@GET
	@Timed
	public Person read(@PathParam("username") final String username) {
		// TODO Read person
		return null;
	}

	@PUT
	@Timed
	public Person update(@PathParam("username") final String username, @Valid final Person person) {
		// TODO Update person
		return null;
	}

	@DELETE
	@Timed
	public Person delete(@PathParam("username") final String username) {
		// TODO Update person
		return null;
	}
}
