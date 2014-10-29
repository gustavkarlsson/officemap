package se.gustavkarlsson.officemap.dao;

import static com.google.common.base.Preconditions.checkNotNull;
import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.Identifiable;

public abstract class GenericDao<E extends Identifiable> extends AbstractDAO<E> implements Dao<E> {

	public GenericDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
	public Long save(final E entity) {
		checkNotNull(entity);
		final Long id = (Long) currentSession().save(entity);
		return id;
	}
	
	@Override
	public E findById(final long id) {
		return get(id);
	}
	
	@Override
	public List<E> list() {
		final Criteria crit = currentSession().createCriteria(getEntityClass());
		@SuppressWarnings("unchecked")
		final List<E> list = crit.list();
		return list;
	}
	
	@Override
	public E update(final E entity) {
		checkNotNull(entity);
		checkNotNull(entity.getId());
		currentSession().update(entity);
		return entity;
	}
	
	@Override
	public void delete(final E entity) {
		checkNotNull(entity);
		currentSession().delete(entity);
	}
	
}
