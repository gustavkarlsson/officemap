package se.gustavkarlsson.officemap.dao.item;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import io.dropwizard.hibernate.AbstractDAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.item.Item;
import se.gustavkarlsson.officemap.api.item.Reference;

import com.google.common.base.Optional;

abstract class AbstractItemDao<E extends Item<E>> extends AbstractDAO<E> implements ItemDao<E> {
	
	public AbstractItemDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
	public Optional<Long> insert(final E item) {
		checkNotNull(item);

		final Reference<E> reference = createNewReference();
		
		E preparedItem = item;
		preparedItem = setId(preparedItem, null);
		preparedItem = setTimestamp(preparedItem, System.currentTimeMillis());
		preparedItem = setReference(preparedItem, reference);
		reference.getItems().add(preparedItem);
		
		persistCheck(preparedItem);
		
		currentSession().save(reference);
		currentSession().flush();
		return Optional.of(reference.getId());
	}
	
	@Override
	public UpdateResponse update(final long referenceId, final E item) {
		checkArgument(referenceId >= 0, "reference is negative");
		checkNotNull(item);
		
		final Optional<Reference<E>> possibleReference = findReference(referenceId);
		if (!possibleReference.isPresent()) {
			return UpdateResponse.NOT_FOUND;
		}
		final Reference<E> reference = possibleReference.get();
		
		final E head = possibleReference.get().getHead();
		if (item.equals(head)) {
			return UpdateResponse.SAME;
		}
		
		E preparedItem = item;
		preparedItem = setId(preparedItem, null);
		preparedItem = setTimestamp(preparedItem, System.currentTimeMillis());
		preparedItem = setReference(preparedItem, reference);
		reference.getItems().add(preparedItem);
		
		persistCheck(preparedItem);
		
		currentSession().save(reference);
		currentSession().flush();
		
		return UpdateResponse.UPDATED;
	}
	
	@Override
	public Optional<E> findHeadByReference(final long referenceId) {
		checkArgument(referenceId >= 0, "reference is negative");
		
		final Optional<Reference<E>> possibleReference = findReference(referenceId);
		if (!possibleReference.isPresent()) {
			return Optional.absent();
		}
		
		final E head = possibleReference.get().getHead();
		return Optional.of(head);
	}
	
	@Override
	public List<E> findAllByReference(final long referenceId) {
		checkArgument(referenceId >= 0, "reference is negative");
		
		final Optional<Reference<E>> possibleReference = findReference(referenceId);
		if (!possibleReference.isPresent()) {
			return new ArrayList<E>();
		}
		
		final List<E> items = possibleReference.get().getItems();
		return items;
	}
	
	@Override
	public List<E> findAllHeads() {
		final List<E> heads = new ArrayList<E>();
		
		final List<Reference<E>> references = getReferences();
		for (final Reference<E> reference : references) {
			final E head = reference.getHead();
			heads.add(head);
		}
		
		return heads;
	}
	
	@Override
	public Optional<Reference<E>> findReference(final long referenceId) {
		final Class<? extends Reference<E>> referenceClass = getReferenceClass();
		@SuppressWarnings("unchecked")
		final Reference<E> reference = (Reference<E>) currentSession().get(referenceClass, referenceId);
		return Optional.fromNullable(reference);
	}
	
	private void persistCheck(final E item) {
		checkNotNull(item);
		checkState(item.getId() == null, "item id is not null");
		checkNotNull(item.getTimestamp());
		checkNotNull(item.getReference());
	}
	
	private List<Reference<E>> getReferences() {
		final Class<? extends Reference<E>> referenceClass = getReferenceClass();
		@SuppressWarnings("unchecked")
		final List<Reference<E>> list = currentSession().createCriteria(referenceClass).list();
		return list;
	}
	
	protected abstract Reference<E> createNewReference();
	
	protected abstract Class<? extends Reference<E>> getReferenceClass();

	protected abstract E setId(E item, Long id);
	
	protected abstract E setReference(E item, Reference<E> reference);
	
	protected abstract E setTimestamp(E item, Long timestamp);
	
}
