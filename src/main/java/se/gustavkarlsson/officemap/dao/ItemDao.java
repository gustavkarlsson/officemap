package se.gustavkarlsson.officemap.dao;

import java.util.List;

import se.gustavkarlsson.officemap.api.Item;
import se.gustavkarlsson.officemap.api.Reference;

import com.google.common.base.Optional;

public interface ItemDao<E extends Item<E>> {

	Optional<Long> insert(E item);
	
	UpdateResponse update(long referenceId, E item);

	Optional<E> findHeadByReference(long referenceId);

	List<E> findAllByReference(long referenceId);
	
	List<E> findAllHeads();

	Optional<Reference<E>> findReference(long referenceId);

	public static enum UpdateResponse {
		UPDATED, SAME, NOT_FOUND;
	}
}
