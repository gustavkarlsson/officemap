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

import se.gustavkarlsson.officemap.api.Item;

import com.google.common.base.Optional;

public abstract class AbstractItemDao<E extends Item> extends AbstractDAO<E> implements ItemDao<E> {
	
	public AbstractItemDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Optional<Long> insert(@Valid final E item) {
		checkNotNull(item);
		checkItemReferenceNull(item);
		final E preparedItem = assignReference(getFreeReference(), item);
		checkItemIdNull(preparedItem);
		checkItemReferencePositive(preparedItem);
		if (equalToHead(preparedItem)) {
			return Optional.absent();
		}
		currentSession().save(preparedItem);
		currentSession().flush();
		return Optional.of(preparedItem.getReference());
	}

	@Override
	public void update(final long reference, @Valid final E item) {
		checkReferencePositive(reference);
		checkNotNull(item);
		checkItemReferencePositive(item);
		currentSession().save(item);
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

	private void checkItemReferenceNull(final E item) {
		checkState(item.getReference() == null, "item reference is not null");
	}
	
	private void checkItemReferencePositive(final E item) {
		checkState(item.getReference() >= 0, "item reference is negative");
	}
	
	private void checkReferencePositive(final long reference) {
		checkArgument(reference >= 0, "reference is negative");
	}
	
	private boolean equalToHead(final E item) {
		final List<E> heads = findAllHeads();
		final boolean contains = heads.contains(item);
		return contains;
	}

	protected abstract E assignReference(long reference, E item);
	
}
