package se.gustavkarlsson.officemap.resources.api;

import static java.lang.System.currentTimeMillis;
import io.dropwizard.hibernate.UnitOfWork;
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

import se.gustavkarlsson.officemap.ItemNotFoundException;
import se.gustavkarlsson.officemap.MapRefNotFoundException;
import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.changeset.person.PersonChangeSet;
import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.event.Event;
import se.gustavkarlsson.officemap.event.person.CreatePersonEvent;
import se.gustavkarlsson.officemap.event.person.DeletePersonEvent;
import se.gustavkarlsson.officemap.resources.PATCH;

import com.sun.jersey.api.ConflictException;
import com.sun.jersey.api.NotFoundException;

@Path("/person")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class PersonResource {

	private final State state;
	private final EventDao dao;

	public PersonResource(final State state, final EventDao dao) {
		this.state = state;
		this.dao = dao;
	}
	
	@GET
	@Path("/{ref}")
	public synchronized Person read(@PathParam("ref") final IntParam ref) {
		try {
			return state.getPersons().get(ref.get());
		} catch (final ItemNotFoundException e) {
			throw new NotFoundException();
		}
	}

	@GET
	public synchronized Map<Integer, Person> readAll() {
		return state.getPersons().getAll();
	}

	@POST
	@UnitOfWork
	public synchronized Response create(@Valid final Person person, @Context final UriInfo uriInfo) {
		final int ref = state.getPersons().getNextRef();
		final Event event = new CreatePersonEvent(currentTimeMillis(), ref, person);
		processEvent(event);
		final URI uri = getCreatedResourceUri(uriInfo, String.valueOf(ref));
		final Response response = Response.created(uri).build();
		return response;
	}

	private URI getCreatedResourceUri(final UriInfo uriInfo, final String path) {
		return uriInfo.getAbsolutePathBuilder().path(path).build();
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

	private void processEvents(final List<? extends Event> events) {
		state.getPersons().backup();
		try {
			for (final Event event : events) {
				event.process(state);
				dao.store(event);
			}
			dao.flush();
		} catch (final Exception e) {
			state.getPersons().restore();
			throw e;
		}
	}

	private void processEvent(final Event event) {
		state.getPersons().backup();
		try {
			event.process(state);
			dao.store(event);
			dao.flush();
		} catch (final Exception e) {
			state.getPersons().restore();
			throw e;
		}
	}
}
