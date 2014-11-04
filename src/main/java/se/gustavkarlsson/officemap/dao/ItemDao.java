package se.gustavkarlsson.officemap.dao;

import java.util.List;

import se.gustavkarlsson.officemap.api.Item;

import com.google.common.base.Optional;

public interface ItemDao<E extends Item> {

	Optional<Long> insert(E item);
	
	void update(long ref, E item);

	Optional<E> findHeadByRef(long ref);

	List<E> findAllByRef(long ref);

	List<E> findAllHeads();

	List<E> findAll();
}
