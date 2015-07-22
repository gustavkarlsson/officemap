package se.gustavkarlsson.officemap.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.dropwizard.jersey.params.IntParam;
import se.gustavkarlsson.officemap.api.changesets.person.PersonChangeSet;
import se.gustavkarlsson.officemap.api.items.Location;
import se.gustavkarlsson.officemap.api.items.Person;
import se.gustavkarlsson.officemap.core.ItemNotFoundException;
import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.events.Event;
import se.gustavkarlsson.officemap.events.person.CreatePersonEvent;
import se.gustavkarlsson.officemap.events.person.DeletePersonEvent;
import se.gustavkarlsson.officemap.events.person.update.MapRefNotFoundException;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.System.currentTimeMillis;


@Path("/persons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class PersonsResource extends ItemsResource<Person> {

	public PersonsResource(final State state, final EventDao dao) {
		super(state, dao);
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
	public synchronized Map<Integer, Person> readAll(
			@QueryParam("mapRef") IntParam mapRef) {
		Map<Integer, Person> persons = state.getPersons().getAll();
		if (mapRef != null) {
			return onlyWithMapRef(persons, mapRef.get());
		}
		return persons;
	}

	private Map<Integer, Person> onlyWithMapRef(Map<Integer, Person> persons,
			final int ref) {
		Map<Integer, Person> filteredPersons = new HashMap<>(
				persons);
		for (Iterator<Entry<Integer, Person>> it = filteredPersons.entrySet()
				.iterator(); it.hasNext();) {
			Person person = it.next().getValue();
			Location location = person.getLocation();
			if (location == null || location.getMapRef() != ref) {
				it.remove();
			}
		}
		return filteredPersons;
	}

	@POST
	@UnitOfWork
	public synchronized Response create(@Valid final Person person,
			@Context final UriInfo uriInfo) {
		final int ref = state.getUniqueRef();
		final Event event = new CreatePersonEvent(currentTimeMillis(), ref,
				person);
		try {
			processEvent(event);
		} catch (final MapRefNotFoundException e) {
			throw new ClientErrorException(e.getMessage(), Response.Status.CONFLICT);
		}
		final URI uri = getCreatedResourceUri(uriInfo, String.valueOf(ref));
		return Response.created(uri).entity(ref).build();
	}

	@PATCH
	@Path("/{ref}")
	@UnitOfWork
	public synchronized Response update(@PathParam("ref") final IntParam ref,
			@Valid final PersonChangeSet changes) {
		final List<Event> events = changes.generateEvents(currentTimeMillis(),
				ref.get());
		try {
			processEvents(events);
		} catch (final ItemNotFoundException e) {
			throw new NotFoundException();
		} catch (final MapRefNotFoundException e) {
			throw new ClientErrorException(e.getMessage(), Response.Status.CONFLICT);
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("/{ref}")
	@UnitOfWork
	public synchronized Response delete(@PathParam("ref") final IntParam ref) {
		final Event event = new DeletePersonEvent(currentTimeMillis(),
				ref.get());
		try {
			processEvent(event);
		} catch (final ItemNotFoundException e) {
			throw new NotFoundException();
		}
		return Response.ok().build();
	}
}
