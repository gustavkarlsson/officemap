package se.gustavkarlsson.officemap.resources;

import static java.lang.System.currentTimeMillis;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.dropwizard.jersey.params.IntParam;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import se.gustavkarlsson.officemap.api.changesets.person.PersonChangeSet;
import se.gustavkarlsson.officemap.api.items.Person;
import se.gustavkarlsson.officemap.core.ItemNotFoundException;
import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.events.Event;
import se.gustavkarlsson.officemap.events.person.CreatePersonEvent;
import se.gustavkarlsson.officemap.events.person.DeletePersonEvent;
import se.gustavkarlsson.officemap.events.person.update.MapRefNotFoundException;

import com.sun.jersey.api.ConflictException;
import com.sun.jersey.api.NotFoundException;

@Path("/persons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class PersonsResource extends ItemsResource<Person> {

	public PersonsResource(final State state, final EventDao dao) {
		super(state, dao, state.getPersons());
	}

	@GET
	@Path("/{ref}")
	public synchronized Person read(@PathParam("ref") final IntParam ref) {
		try {
			return items.get(ref.get());
		} catch (final ItemNotFoundException e) {
			throw new NotFoundException();
		}
	}
	
	@GET
	public synchronized Map<Integer, Person> readAll() {
		return items.getAll();
	}
	
	@POST
	@UnitOfWork
	public synchronized Response create(@Valid final Person person, @Context final UriInfo uriInfo) {
		final int ref = items.getNextRef();
		final Event event = new CreatePersonEvent(currentTimeMillis(), ref, person);
		try {
			processEvent(event);
		} catch (final MapRefNotFoundException e) {
			throw new ConflictException(e.getMessage());
		}
		final URI uri = getCreatedResourceUri(uriInfo, String.valueOf(ref));
		return Response.created(uri).build();
	}
	
	@PATCH
	@Path("/{ref}")
	@UnitOfWork
	public synchronized Response update(@PathParam("ref") final IntParam ref, @Valid final PersonChangeSet changes) {
		final List<Event> events = changes.generateEvents(currentTimeMillis(), ref.get());
		try {
			processEvents(events);
		} catch (final ItemNotFoundException e) {
			throw new NotFoundException();
		} catch (final MapRefNotFoundException e) {
			throw new ConflictException(e.getMessage());
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("/{ref}")
	@UnitOfWork
	public synchronized Response delete(@PathParam("ref") final IntParam ref) {
		final Event event = new DeletePersonEvent(currentTimeMillis(), ref.get());
		try {
			processEvent(event);
		} catch (final ItemNotFoundException e) {
			throw new NotFoundException();
		}
		return Response.ok().build();
	}
}
