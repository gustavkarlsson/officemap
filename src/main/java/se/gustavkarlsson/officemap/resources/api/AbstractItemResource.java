package se.gustavkarlsson.officemap.resources.api;

import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.util.Generics;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import se.gustavkarlsson.officemap.api.item.Item;
import se.gustavkarlsson.officemap.dao.item.ItemDao;
import se.gustavkarlsson.officemap.dao.item.ItemDao.UpdateResponse;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

abstract class AbstractItemResource<T extends Item<T>> {
	
	@SuppressWarnings("unchecked")
	private final Class<T> type = (Class<T>) Generics.getTypeParameter(getClass());
	
	private final ItemDao<T> dao;
	
	protected AbstractItemResource(final ItemDao<T> dao) {
		this.dao = dao;
	}
	
	protected T find(@PathParam("reference") final LongParam reference) {
		final Optional<T> item = dao.findHeadByReference(reference.get());
		if (!item.isPresent()) {
			throw new WebApplicationException(NOT_FOUND);
		}
		return item.get();
	}
	
	protected T[] list() {
		final List<T> items = dao.findAllHeads();
		final T[] array = Iterables.toArray(items, type);
		return array;
	}
	
	protected Response insert(@Valid final T item) {
		final Optional<Long> reference = dao.insert(item);
		if (!reference.isPresent()) {
			throw new WebApplicationException(CONFLICT);
		}
		final URI uri = UriBuilder.fromPath(Long.toString(reference.get())).build();
		return Response.created(uri).build();
	}

	protected Response update(@PathParam("reference") final LongParam reference, @Valid final T item) {
		final UpdateResponse response = dao.update(reference.get(), item);
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
	
	protected Response delete(@PathParam("reference") final LongParam reference) {
		final Optional<T> possiblyFoundItem = dao.findHeadByReference(reference.get());
		if (!possiblyFoundItem.isPresent()) {
			throw new WebApplicationException(NOT_FOUND);
		}
		final T item = possiblyFoundItem.get();
		if (item.isDeleted()) {
			throw new WebApplicationException(CONFLICT);
		}
		final T deletedItem = getDeletedInstance(item);
		final UpdateResponse response = dao.update(reference.get(), deletedItem);
		if (response != UpdateResponse.UPDATED) {
			// TODO log error (should not happen)
			throw new WebApplicationException(INTERNAL_SERVER_ERROR);
		}
		return Response.ok().build();
	}
	
	protected final ItemDao<T> getDao() {
		return dao;
	}
	
	protected abstract T getDeletedInstance(T item);
}
