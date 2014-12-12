package se.gustavkarlsson.officemap.dao;

import static com.google.common.base.Preconditions.checkNotNull;
import io.dropwizard.hibernate.AbstractDAO;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import se.gustavkarlsson.officemap.api.fileentry.FileEntry;

import com.google.common.base.Optional;

public final class FileEntryDao extends AbstractDAO<FileEntry> {

	public FileEntryDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public void insert(final FileEntry item) {
		checkNotNull(item);

		final FileEntry preparedItem = item.toBuilder().withId(null).build();

		currentSession().save(preparedItem);
		currentSession().flush();
	}

	public Optional<FileEntry> find(final byte[] value) {
		final FileEntry result = (FileEntry) criteria().add(Restrictions.eq("sha1", value)).uniqueResult();
		return Optional.fromNullable(result);
	}

	public Optional<FileEntry> find(final String hexValue) {
		try {
			final byte[] value = Hex.decodeHex(hexValue.toCharArray());
			return find(value);
		} catch (final DecoderException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

}
