package se.gustavkarlsson.officemap.dao;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static se.gustavkarlsson.officemap.dao.UpdateResponse.NOT_FOUND;
import static se.gustavkarlsson.officemap.dao.UpdateResponse.SAME;
import static se.gustavkarlsson.officemap.dao.UpdateResponse.UPDATED;
import io.dropwizard.hibernate.AbstractDAO;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import se.gustavkarlsson.officemap.api.Item;

import com.google.common.base.Optional;

public abstract class AbstractItemDao<E extends Item> extends AbstractDAO<E> implements ItemDao<E> {

	public AbstractItemDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
	public Optional<Long> insert(final E item) {
		checkNotNull(item);
		final E preparedItem = prepareForInsertion(item, getFreeReference(), System.currentTimeMillis());
		checkItemIdNull(preparedItem);
		checkItemTimestampNonNegative(preparedItem);
		checkItemReferenceNonNegative(preparedItem);
		if (equalToHead(preparedItem)) {
			return Optional.absent();
		}
		currentSession().save(preparedItem);
		currentSession().flush();
		return Optional.of(preparedItem.getReference());
	}
	
	@Override
	public UpdateResponse update(final long reference, final E item) {
		checkReferenceNonNegative(reference);
		checkNotNull(item);
		final Optional<E> head = findHeadByRef(reference);
		if (!head.isPresent()) {
			return NOT_FOUND;
		}
		final E preparedItem = prepareForUpdate(item, System.currentTimeMillis());
		checkItemIdNull(preparedItem);
		checkItemTimestampNonNegative(preparedItem);
		checkItemReferenceNonNegative(preparedItem);
		if (equalToHead(preparedItem)) {
			return SAME;
		}
		currentSession().save(preparedItem);
		currentSession().flush();
		return UPDATED;
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
		final List<E> list = currentSession().createQuery(
				"select e from Item e where e.id = "
						+ "(select max(e_sub.id) from Item e_sub where e_sub.reference = e.reference) "
						+ "order by e.id asc").list();
		return list;
	}
	
	@Override
	public List<E> findAll() {
		@SuppressWarnings("unchecked")
		final List<E> list = criteria().addOrder(Order.asc("id")).list();
		return list;
	}

	private long getFreeReference() {
		final BigInteger result = (BigInteger) currentSession().createSQLQuery("select max(reference) from Item")
				.uniqueResult();
		final BigInteger highestReference = Optional.fromNullable(result).or(BigInteger.valueOf(0l));
		final long nextReference = highestReference.longValue() + 1;
		return nextReference;
	}

	private void checkItemIdNull(final E item) {
		checkState(item.getId() == null, "item id is not null");
	}

	private void checkItemReferenceNonNegative(final E item) {
		// checkState(item.getReference() >= 0, "item reference is negative");
	}

	private void checkItemTimestampNonNegative(final E item) {
		// checkState(item.getTimestamp() >= 0, "item timestamp is negative");
	}

	private void checkReferenceNonNegative(final long reference) {
		checkArgument(reference >= 0, "reference is negative");
	}

	private boolean equalToHead(final E item) {
		final List<E> heads = findAllHeads();
		final boolean contains = heads.contains(item);
		return contains;
	}
	
	protected abstract E prepareForInsertion(E item, long reference, long timestamp);
	
	protected abstract E prepareForUpdate(E item, long timestamp);

}
