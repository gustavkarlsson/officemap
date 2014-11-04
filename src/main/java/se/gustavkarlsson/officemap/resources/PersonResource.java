package se.gustavkarlsson.officemap.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import se.gustavkarlsson.officemap.api.Person;
import se.gustavkarlsson.officemap.dao.PersonDao;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {
	
	private final PersonDao dao;
	
	public PersonResource(final PersonDao dao) {
		this.dao = dao;
	}
	
	@POST
	@UnitOfWork
	@Timed
	public Response create(@Valid final Person person) {
		if (person == null) {
			throw new WebApplicationException(Status.NO_CONTENT);
		}
		final Optional<Long> ref = dao.insert(person);
		if (!ref.isPresent()) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		final URI uri = UriBuilder.fromPath(ref.toString()).build();
		return Response.created(uri).build();
	}
	
	@Path("/{ref}")
	@GET
	@UnitOfWork
	@Timed
	public Person read(@PathParam("ref") final LongParam ref) {
		final Optional<Person> person = dao.findHeadByRef(ref.get());
		if (!person.isPresent()) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return person.get();
	}
	
	@GET
	@UnitOfWork
	@Timed
	public Person[] list() {
		// TODO redefine method
		final List<Person> persons = dao.findAllHeads();
		return persons.toArray(new Person[persons.size()]);
	}
	
	@Path("/{id}")
	@PUT
	@UnitOfWork
	@Timed
	public Response update(@PathParam("id") final LongParam id, @Valid final Person person) {
		if (person.getId() != null && person.getId() != id.get()) {
			throw new WebApplicationException(Status.CONFLICT);
		}
		// TODO perform update
		return Response.ok().build();
	}
	
	@Path("/{id}")
	@DELETE
	@UnitOfWork
	@Timed
	public Response delete(@PathParam("id") final LongParam id) {
		final Optional<Person> person = dao.findHeadByRef(id.get());
		if (!person.isPresent()) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		// TODO perform deletion
		return Response.ok().build();
	}
}
