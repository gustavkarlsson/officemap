package se.gustavkarlsson.officemap.resources;

import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
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
import javax.ws.rs.core.UriBuilder;

import se.gustavkarlsson.officemap.api.Person;
import se.gustavkarlsson.officemap.dao.ItemDao.UpdateResponse;
import se.gustavkarlsson.officemap.dao.PersonDao;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/api/persons")
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
	public Response insert(@Valid final Person person) {
		final Optional<Long> reference = dao.insert(person);
		if (!reference.isPresent()) {
			throw new WebApplicationException(CONFLICT);
		}
		final URI uri = UriBuilder.fromPath(Long.toString(reference.get())).build();
		return Response.created(uri).build();
	}
	
	@Path("/{reference}")
	@GET
	@UnitOfWork
	@Timed
	public Person find(@PathParam("reference") final LongParam reference) {
		final Optional<Person> person = dao.findHeadByRef(reference.get());
		if (!person.isPresent()) {
			throw new WebApplicationException(NOT_FOUND);
		}
		return person.get();
	}
	
	@GET
	@UnitOfWork
	@Timed
	public Person[] list() {
		final List<Person> persons = dao.findAllHeads();
		return persons.toArray(new Person[persons.size()]);
	}
	
	@Path("/{reference}")
	@PUT
	@UnitOfWork
	@Timed
	public Response update(@PathParam("reference") final LongParam reference, @Valid final Person person) {
		final UpdateResponse response = dao.update(reference.get(), person);
		switch (response) {
			case SAME:
				throw new WebApplicationException(CONFLICT);
			case NOT_FOUND:
				throw new WebApplicationException(NOT_FOUND);
			case UPDATED:
				return Response.ok().build();
			default:
				// TODO log error (should not happen)
				throw new WebApplicationException(INTERNAL_SERVER_ERROR);
		}
	}
	
	@Path("/{reference}")
	@DELETE
	@UnitOfWork
	@Timed
	public Response delete(@PathParam("reference") final LongParam reference) {
		final Optional<Person> possiblyFoundPerson = dao.findHeadByRef(reference.get());
		if (!possiblyFoundPerson.isPresent()) {
			throw new WebApplicationException(NOT_FOUND);
		}
		final Person person = possiblyFoundPerson.get();
		if (person.isDeleted()) {
			throw new WebApplicationException(CONFLICT);
		}
		final Person deletedPerson = Person.Builder.fromPerson(person).withDeleted(true).build();
		final UpdateResponse response = dao.update(reference.get(), deletedPerson);
		if (response != UpdateResponse.UPDATED) {
			// TODO log error (should not happen)
			throw new WebApplicationException(INTERNAL_SERVER_ERROR);
		}
		return Response.ok().build();
	}
}
