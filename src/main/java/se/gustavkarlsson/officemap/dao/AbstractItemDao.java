package se.gustavkarlsson.officemap.dao;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static se.gustavkarlsson.officemap.dao.ItemDao.UpdateResponse.NOT_FOUND;
import static se.gustavkarlsson.officemap.dao.ItemDao.UpdateResponse.SAME;
import static se.gustavkarlsson.officemap.dao.ItemDao.UpdateResponse.UPDATED;
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
		final E preparedItem = prepareForInsertion(item);
		doPrePersistCheck(preparedItem);
		checkItemNotDeleted(preparedItem);
		final Optional<E> possibleEqual = findEqualHead(preparedItem);
		if (possibleEqual.isPresent() && !possibleEqual.get().isDeleted()) {
			return Optional.absent();
		}
		
		currentSession().save(preparedItem);
		currentSession().flush();
		return Optional.of(preparedItem.getReference());
	}
	
	// TODO fix with similar treatment as insert
	@Override
	public UpdateResponse update(final long reference, final E item) {
		checkReferenceNonNegative(reference);
		checkNotNull(item);
		final Optional<E> possiblyFound = findHeadByRef(reference);
		if (!possiblyFound.isPresent()) {
			return NOT_FOUND;
		}
		final E preparedItem = prepareForUpdate(item);
		doPrePersistCheck(preparedItem);
		checkItemNotDeleted(preparedItem);
		if (equalToHead(preparedItem)) {
			return SAME;
		}
		currentSession().save(preparedItem);
		currentSession().flush();
		return UPDATED;
	}

	// TODO Synchronize
	@Override
	public DeleteResponse delete(final long ref) {
		final Optional<E> possiblyFound = findHeadByRef(ref);
		if (!possiblyFound.isPresent()) {
			return DeleteResponse.NOT_FOUND;
		}
		if (possiblyFound.get().isDeleted()) {
			return DeleteResponse.ALREADY_DELETED;
		}
		final E preparedItem = prepareForDelete(possiblyFound.get());
		doPrePersistCheck(preparedItem);
		checkItemDeleted(preparedItem);
		currentSession().save(preparedItem);
		currentSession().flush();
		return DeleteResponse.DELETED;
	}

	// TODO Synchronize
	@Override
	public UndeleteResponse undelete(final long ref) {
		final Optional<E> possiblyFound = findHeadByRef(ref);
		if (!possiblyFound.isPresent()) {
			return UndeleteResponse.NOT_FOUND;
		}
		if (!possiblyFound.get().isDeleted()) {
			return UndeleteResponse.ALREADY_UNDELETED;
		}
		final E preparedItem = prepareForUndelete(possiblyFound.get());
		doPrePersistCheck(preparedItem);
		checkItemNotDeleted(preparedItem);
		currentSession().save(preparedItem);
		currentSession().flush();
		return UndeleteResponse.UNDELETED;
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

	private Optional<E> findEqualHead(final E item) {
		final List<E> heads = findAllHeads();
		final int index = heads.indexOf(item);
		if (index == -1) {
			return Optional.absent();
		}
		final E equal = heads.get(index);
		return Optional.of(equal);
	}

	private void doPrePersistCheck(final E preparedItem) {
		checkNotNull(preparedItem);
		checkItemIdNull(preparedItem);
		checkNotNull(preparedItem.getTimestamp());
		checkNotNull(preparedItem.getReference());
	}
	
	private boolean equalToHead(final E item) {
		final List<E> heads = findAllHeads();
		final boolean contains = heads.contains(item);
		return contains;
	}

	private void checkItemIdNull(final E item) {
		checkState(item.getId() == null, "item id is not null");
	}
	
	private void checkReferenceNonNegative(final long reference) {
		checkArgument(reference >= 0, "reference is negative");
	}

	private void checkItemNotDeleted(final E item) {
		checkState(!item.isDeleted(), "item is deleted");
	}
	
	private void checkItemDeleted(final E preparedItem) {
		checkState(preparedItem.isDeleted(), "item is not deleted");
	}

	protected long getFreeReference() {
		final BigInteger result = (BigInteger) currentSession().createSQLQuery("select max(reference) from Item")
				.uniqueResult();
		final BigInteger highestReference = Optional.fromNullable(result).or(BigInteger.valueOf(0l));
		final long nextReference = highestReference.longValue() + 1;
		return nextReference;
	}

	protected abstract E prepareForInsertion(E item);

	protected abstract E prepareForUpdate(E item);

	protected abstract E prepareForDelete(E item);

	protected abstract E prepareForUndelete(E item);
	
}
