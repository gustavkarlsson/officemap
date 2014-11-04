package se.gustavkarlsson.officemap.dao;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import io.dropwizard.hibernate.AbstractDAO;

import java.math.BigInteger;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import se.gustavkarlsson.officemap.api.VersionedEntity;

import com.google.common.base.Optional;

public abstract class AbstractEventDao<E extends VersionedEntity> extends AbstractDAO<E> implements EventDao<E> {

	public AbstractEventDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
	public Optional<Long> insert(@Valid final E entity) {
		checkNotNull(entity);
		checkEntityReferenceNull(entity);
		final E preparedEntity = assignReference(getFreeReference(), entity);
		checkEntityIdNull(preparedEntity);
		checkEntityReferencePositive(preparedEntity);
		if (equalToHead(preparedEntity)) {
			return Optional.absent();
		}
		currentSession().save(preparedEntity);
		currentSession().flush();
		return Optional.of(preparedEntity.getReference());
	}
	
	@Override
	public void update(final long reference, @Valid final E entity) {
		checkReferencePositive(reference);
		checkNotNull(entity);
		checkEntityReferencePositive(entity);
		currentSession().save(entity);
		currentSession().flush();
	}

	@Override
	public Optional<E> findHeadByRef(final long ref) {
		@SuppressWarnings("unchecked")
		final E head = (E) criteria().add(Restrictions.eq("reference", ref)).addOrder(Order.desc("id"))
				.setMaxResults(1).uniqueResult();
		return Optional.fromNullable(head);
	}
	
	@Override
	public List<E> findAllByRef(final long ref) {
		@SuppressWarnings("unchecked")
		final List<E> list = criteria().add(Restrictions.eq("reference", ref)).addOrder(Order.asc("id")).list();
		return list;
	}
	
	@Override
	public List<E> findAllHeads() {
		@SuppressWarnings("unchecked")
		final List<E> list = currentSession()
				.createQuery(
						"select e from VersionedEntity e where e.id = (select max(e_sub.id) from VersionedEntity e_sub where e_sub.reference = e.reference) order by e.id asc")
				.list();
		return list;
	}
	
	@Override
	public List<E> findAll() {
		@SuppressWarnings("unchecked")
		final List<E> list = criteria().addOrder(Order.asc("id")).list();
		return list;
	}

	private long getFreeReference() {
		final BigInteger result = (BigInteger) currentSession().createSQLQuery(
				"select max(reference) from VersionedEntity").uniqueResult();
		final BigInteger highestReference = Optional.fromNullable(result).or(BigInteger.valueOf(0l));
		final long nextReference = highestReference.longValue() + 1;
		return nextReference;
	}

	private void checkEntityIdNull(final E entity) {
		checkState(entity.getId() == null, "entity id is not null");
	}
	
	private void checkEntityReferenceNull(final E entity) {
		checkState(entity.getReference() == null, "entity reference is not null");
	}

	private void checkEntityReferencePositive(final E entity) {
		checkState(entity.getReference() >= 0, "entity reference is negative");
	}

	private void checkReferencePositive(final long reference) {
		checkArgument(reference >= 0, "reference is negative");
	}

	private boolean equalToHead(final E entity) {
		final List<E> heads = findAllHeads();
		final boolean contains = heads.contains(entity);
		return contains;
	}
	
	protected abstract E assignReference(long reference, E entity);

}
