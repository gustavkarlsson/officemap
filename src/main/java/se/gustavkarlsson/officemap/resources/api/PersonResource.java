package se.gustavkarlsson.officemap.resources.api;

import static java.lang.System.currentTimeMillis;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;

import java.net.URI;
import java.util.ArrayList;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.changeset.person.PersonChangeSet;
import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.event.Event;
import se.gustavkarlsson.officemap.event.ProcessEventException;
import se.gustavkarlsson.officemap.event.person.CreatePersonEvent;
import se.gustavkarlsson.officemap.event.person.DeletePersonEvent;
import se.gustavkarlsson.officemap.resources.PATCH;

import com.google.common.base.Optional;

// TODO better error handling (give good feedback)
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
	
	@Path("/{ref}")
	@GET
	@UnitOfWork
	public synchronized Person read(@PathParam("ref") final IntParam ref) {
		final Optional<Person> person = state.getPersons().get(ref.get());
		if (!person.isPresent()) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return person.get();
	}
	
	@GET
	@UnitOfWork
	public synchronized Map<Integer, Person> readAll() {
		return state.getPersons().getAll();
	}
	
	@POST
	@UnitOfWork
	public synchronized Response create(@Valid final PersonChangeSet changes, @Context final UriInfo uriInfo) {
		final long timestamp = currentTimeMillis();
		final int ref = state.getPersons().getNextRef();
		
		final List<Event> events = new ArrayList<>();
		events.add(new CreatePersonEvent(timestamp, ref));
		events.addAll(changes.generateEvents(timestamp, ref));

		try {
			storeAndProcessEvents(events);
		} catch (final ProcessEventException e) {
			throw new WebApplicationException(e, Status.CONFLICT);
		}
		
		final URI uri = getCreatedResourceUri(uriInfo, String.valueOf(ref));
		final Response response = Response.created(uri).build();
		return response;
	}
	
	private URI getCreatedResourceUri(final UriInfo uriInfo, final String path) {
		return uriInfo.getAbsolutePathBuilder().path(path).build();
	}

	@Path("/{ref}")
	@DELETE
	@UnitOfWork
	public synchronized Response delete(@PathParam("ref") final IntParam ref) {
		final Event event = new DeletePersonEvent(currentTimeMillis(), ref.get());
		try {
			storeAndProcessEvent(event);
		} catch (final ProcessEventException e) {
			throw new WebApplicationException(e, Status.CONFLICT);
		}
		return Response.ok().build();
	}
	
	@Path("/{ref}")
	@PATCH
	@UnitOfWork
	public synchronized Response update(@PathParam("ref") final IntParam ref, @Valid final PersonChangeSet changes) {
		final List<Event> events = changes.generateEvents(currentTimeMillis(), ref.get());
		try {
			storeAndProcessEvents(events);
		} catch (final ProcessEventException e) {
			throw new WebApplicationException(e, Status.CONFLICT);
		}
		return Response.ok().build();
	}
	
	private void storeAndProcessEvents(final List<? extends Event> events) throws ProcessEventException {
		for (final Event event : events) {
			storeAndProcessEvent(event);
		}
	}
	
	private void storeAndProcessEvent(final Event event) throws ProcessEventException {
		event.process(state);
		dao.store(event);
	}
}
