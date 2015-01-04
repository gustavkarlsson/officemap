package se.gustavkarlsson.officemap.resources.api;

import static java.lang.System.currentTimeMillis;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;

import java.net.URI;
import java.util.List;

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
import se.gustavkarlsson.officemap.State;
import se.gustavkarlsson.officemap.api.changeset.map.MapChangeSet;
import se.gustavkarlsson.officemap.api.item.Map;
import se.gustavkarlsson.officemap.dao.EventDao;
import se.gustavkarlsson.officemap.event.Event;
import se.gustavkarlsson.officemap.event.map.CreateMapEvent;
import se.gustavkarlsson.officemap.event.map.DeleteMapEvent;
import se.gustavkarlsson.officemap.resources.PATCH;

import com.sun.jersey.api.NotFoundException;

@Path("/maps")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class MapsResource extends ItemsResource<Map> {

	public MapsResource(final State state, final EventDao dao) {
		super(state, dao, state.getMaps());
	}
	
	@GET
	@Path("/{ref}")
	public synchronized Map read(@PathParam("ref") final IntParam ref) {
		try {
			return items.get(ref.get());
		} catch (final ItemNotFoundException e) {
			throw new NotFoundException();
		}
	}

	@GET
	public synchronized java.util.Map<Integer, Map> readAll() {
		return items.getAll();
	}

	@POST
	@UnitOfWork
	public synchronized Response create(@Valid final Map map, @Context final UriInfo uriInfo) {
		final int ref = items.getNextRef();
		final Event event = new CreateMapEvent(currentTimeMillis(), ref, map);
		processEvent(event);
		final URI uri = getCreatedResourceUri(uriInfo, String.valueOf(ref));
		return Response.created(uri).build();
	}

	@PATCH
	@Path("/{ref}")
	@UnitOfWork
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
