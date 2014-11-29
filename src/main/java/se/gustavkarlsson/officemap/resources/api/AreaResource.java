package se.gustavkarlsson.officemap.resources.api;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import java.util.HashSet;
import java.util.Set;

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

import se.gustavkarlsson.officemap.api.Reference;
import se.gustavkarlsson.officemap.api.area.Area;
import se.gustavkarlsson.officemap.api.person.Person;
import se.gustavkarlsson.officemap.dao.ItemDao;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/areas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class AreaResource extends AbstractItemResource<Area> {

	private final ItemDao<Person> personDao;

	public AreaResource(final ItemDao<Area> dao, final ItemDao<Person> personDao) {
		super(dao);
		this.personDao = personDao;
	}

	@Override
	@Path("/{reference}")
	@GET
	@Consumes(MediaType.WILDCARD)
	@UnitOfWork
	@Timed
	public Area find(@PathParam("reference") final LongParam reference) {
		return super.find(reference);
	}

	@Override
	@GET
	@Consumes(MediaType.WILDCARD)
	@UnitOfWork
	@Timed
	public Area[] list() {
		return super.list();
	}

	@Override
	@POST
	@UnitOfWork
	@Timed
	public Response insert(@Valid final Area area) {
		final Area updatedArea = updateWithRealReferences(area);
		return super.insert(updatedArea);
	}

	@Override
	@Path("/{reference}")
	@PUT
	@UnitOfWork
	@Timed
	public Response update(@PathParam("reference") final LongParam reference, @Valid final Area area) {
		final Area updatedArea = updateWithRealReferences(area);
		return super.update(reference, updatedArea);
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
	protected Area getDeletedInstance(final Area area) {
		final Area deletedArea = Area.Builder.fromArea(area).withDeleted(true).build();
		return deletedArea;
	}

	private Area updateWithRealReferences(final Area area) {
		final Set<Reference<Person>> personReferences = new HashSet<>();
		for (final Reference<Person> placeholderReference : area.getPersons()) {
			final Optional<Reference<Person>> actualReference = personDao.findReference(placeholderReference.getId());
			if (!actualReference.isPresent()) {
				// TODO Handle if reference does not exist
			}
			personReferences.add(actualReference.get());
		}
		final Area updatedArea = Area.Builder.fromArea(area).withPersons(personReferences).build();
		return updatedArea;
	}
}
