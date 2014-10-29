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
		final Long savedId = dao.save(person);
		if (savedId == null) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		final URI uri = UriBuilder.fromPath("" + savedId).build();
		return Response.created(uri).build();
	}
	
	@Path("/{id}")
	@GET
	@UnitOfWork
	@Timed
	public Person read(@PathParam("id") final LongParam id) {
		final Person person = dao.findById(id.get());
		if (person == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return person;
	}
	
	@GET
	@UnitOfWork
	@Timed
	public Person[] list() {
		final List<Person> persons = dao.list();
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
		dao.update(person);
		return Response.ok().build();
	}
	
	@Path("/{id}")
	@DELETE
	@UnitOfWork
	@Timed
	public Response delete(@PathParam("id") final LongParam id) {
		final Person person = dao.findById(id.get());
		if (person == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		dao.delete(person);
		return Response.ok().build();
	}
}
