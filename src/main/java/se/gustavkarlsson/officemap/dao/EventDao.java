package se.gustavkarlsson.officemap.dao;

import java.util.List;

import se.gustavkarlsson.officemap.api.VersionedEntity;

import com.google.common.base.Optional;

public interface EventDao<E extends VersionedEntity> {

	Optional<Long> insert(E entity);
	
	void update(long ref, E entity);

	Optional<E> findHeadByRef(long ref);

	List<E> findAllByRef(long ref);

	List<E> findAllHeads();

	List<E> findAll();
}
