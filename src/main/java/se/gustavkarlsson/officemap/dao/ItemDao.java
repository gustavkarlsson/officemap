package se.gustavkarlsson.officemap.dao;

import java.util.List;

import se.gustavkarlsson.officemap.api.Item;

import com.google.common.base.Optional;

public interface ItemDao<E extends Item> {
	
	Optional<Long> insert(E item);

	UpdateResponse update(long ref, E item);

	DeleteResponse delete(long ref);

	UndeleteResponse undelete(long ref);
	
	Optional<E> findHeadByRef(long ref);
	
	List<E> findAllByRef(long ref);
	
	List<E> findAllHeads();
	
	List<E> findAll();
	
	public static enum UpdateResponse {
		UPDATED, SAME, NOT_FOUND;
	}
	
	public static enum DeleteResponse {
		DELETED, ALREADY_DELETED, NOT_FOUND;
	}
	
	public static enum UndeleteResponse {
		UNDELETED, ALREADY_UNDELETED, NOT_FOUND;
	}
}
