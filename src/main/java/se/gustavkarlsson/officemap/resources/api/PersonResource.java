package se.gustavkarlsson.officemap.resources.api;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

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

import se.gustavkarlsson.officemap.api.person.Person;
import se.gustavkarlsson.officemap.dao.ItemDao;

import com.codahale.metrics.annotation.Timed;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class PersonResource extends AbstractItemResource<Person> {

	public PersonResource(final ItemDao<Person> dao) {
		super(dao);
	}

	@Override
	@Path("/{reference}")
	@GET
	@Consumes(MediaType.WILDCARD)
	@UnitOfWork
	@Timed
	public Person find(@PathParam("reference") final LongParam reference) {
		return super.find(reference);
	}

	@Override
	@GET
	@Consumes(MediaType.WILDCARD)
	@UnitOfWork
	@Timed
	public Person[] list() {
		return super.list();
	}

	@Override
	@POST
	@UnitOfWork
	@Timed
	public Response insert(@Valid final Person person) {
		return super.insert(person);
	}
	
	@Override
	@Path("/{reference}")
	@PUT
	@UnitOfWork
	@Timed
	public Response update(@PathParam("reference") final LongParam reference, @Valid final Person person) {
		return super.update(reference, person);
	}

	@Override
	@Path("/{reference}")
	@DELETE
	@UnitOfWork
	@Timed
	public Response delete(@PathParam("reference") final LongParam reference) {
		return super.delete(reference);
	}
	
	@Override
	protected Person getDeletedInstance(final Person person) {
		final Person deletedPerson = Person.Builder.fromPerson(person).withDeleted(true).build();
		return deletedPerson;
	}
}
