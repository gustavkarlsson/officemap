package se.gustavkarlsson.officemap.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.dropwizard.jersey.params.IntParam;
import se.gustavkarlsson.officemap.api.changesets.map.MapChangeSet;
import se.gustavkarlsson.officemap.api.items.Map;
import se.gustavkarlsson.officemap.core.ItemNotFoundException;
import se.gustavkarlsson.officemap.core.State;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.events.Event;
import se.gustavkarlsson.officemap.events.map.CreateMapEvent;
import se.gustavkarlsson.officemap.events.map.DeleteMapEvent;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static java.lang.System.currentTimeMillis;


@Path("/maps")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class MapsResource extends ItemsResource<Map> {

	public MapsResource(final State state, final EventDao dao) {
		super(state, dao);
	}
	
	@GET
	@Path("/{ref}")
	@Timed
	public synchronized Map read(@PathParam("ref") final IntParam ref) {
		try {
			return state.getMaps().get(ref.get());
		} catch (final ItemNotFoundException e) {
			throw new NotFoundException();
		}
	}

	@GET
	public synchronized java.util.Map<Integer, Map> readAll() {
		return state.getMaps().getAll();
	}

	@POST
	@UnitOfWork
	@Timed
	public synchronized Response create(@Valid final Map map, @Context final UriInfo uriInfo) {
		final int ref = state.getUniqueRef();
		final Event event = new CreateMapEvent(currentTimeMillis(), ref, map);
		processEvent(event);
		final URI uri = getCreatedResourceUri(uriInfo, String.valueOf(ref));
		return Response.created(uri).entity(ref).build();
	}

	@PATCH
	@Path("/{ref}")
	@UnitOfWork
	@Timed
	public synchronized Response update(@PathParam("ref") final IntParam ref, @Valid final MapChangeSet changes) {
		final List<Event> events = changes.generateEvents(currentTimeMillis(), ref.get());
		try {
			processEvents(events);
		} catch (final ItemNotFoundException e) {
			throw new NotFoundException();
		}
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{ref}")
	@UnitOfWork
	@Timed
	public synchronized Response delete(@PathParam("ref") final IntParam ref) {
		final Event event = new DeleteMapEvent(currentTimeMillis(), ref.get());
		try {
			processEvent(event);
		} catch (final ItemNotFoundException e) {
			throw new NotFoundException();
		}
		return Response.ok().build();
	}
}
