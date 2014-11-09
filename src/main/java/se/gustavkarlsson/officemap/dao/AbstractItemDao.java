package se.gustavkarlsson.officemap.dao;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
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
	
	// TODO Synchronize
	@Override
	public Optional<Long> insert(final E item) {
		checkNotNull(item);
		
		E preparedItem = item;
		preparedItem = setId(preparedItem, null);
		preparedItem = setTimestamp(preparedItem, System.currentTimeMillis());
		preparedItem = setReference(preparedItem, getFreeReference());
		
		persistCheck(preparedItem);

		currentSession().save(preparedItem);
		currentSession().flush();
		return Optional.of(preparedItem.getReference());
	}

	// TODO Synchronize
	@Override
	public UpdateResponse update(final long reference, final E item) {
		checkArgument(reference >= 0, "reference is negative");
		checkNotNull(item);
		final Optional<E> possiblyEqualHead = findHeadByRef(reference);
		if (!possiblyEqualHead.isPresent()) {
			return UpdateResponse.NOT_FOUND;
		}

		E preparedItem = item;
		preparedItem = setId(preparedItem, null);
		preparedItem = setTimestamp(preparedItem, System.currentTimeMillis());

		persistCheck(preparedItem);

		currentSession().save(preparedItem);
		currentSession().flush();

		return UpdateResponse.UPDATED;
	}
	
	@Override
	public Optional<E> findHeadByRef(final long ref) {
		@SuppressWarnings("unchecked")
		final E result = (E) criteria().add(Restrictions.eq("reference", ref)).addOrder(Order.desc("id"))
				.setMaxResults(1).uniqueResult();
		return Optional.fromNullable(result);
	}
	
	@Override
	public List<E> findAllByRef(final long ref) {
		@SuppressWarnings("unchecked")
		final List<E> result = criteria().add(Restrictions.eq("reference", ref)).addOrder(Order.asc("id")).list();
		return result;
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
	
	private void persistCheck(final E item) {
		checkNotNull(item);
		checkState(item.getId() == null, "item id is not null");
		checkNotNull(item.getTimestamp());
		checkNotNull(item.getReference());
	}
	
	protected long getFreeReference() {
		final BigInteger result = (BigInteger) currentSession().createSQLQuery("select max(reference) from Item")
				.uniqueResult();
		final BigInteger highestReference = Optional.fromNullable(result).or(BigInteger.valueOf(0l));
		final long nextReference = highestReference.longValue() + 1;
		return nextReference;
	}

	protected abstract E setId(E item, Long id);

	protected abstract E setReference(E item, Long reference);

	protected abstract E setTimestamp(E item, Long timestamp);

}
