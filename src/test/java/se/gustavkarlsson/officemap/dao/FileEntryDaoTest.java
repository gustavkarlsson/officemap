package se.gustavkarlsson.officemap.dao;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.fileentry.FileEntry;

import com.google.common.base.Optional;

public class FileEntryDaoTest extends AbstractDaoTest {
	
	private static final FileEntry validSha1 = FileEntry.builder()
			.with(null, "application/text", "cf23df2207d99a74fbe169e3eba035e633b65d94").build();
	private static final FileEntry validSha2 = FileEntry.builder()
			.with(4l, "application/text", "cf23df2207d99a74fbe169e3eba035e633b65d9a").build();

	private static FileEntryDao dao;

	@BeforeClass
	public static void initDao() {
		dao = new FileEntryDao(getSessionFactory());
	}

	@Test
	public void insertValidPersonsSucceeds() throws Exception {
		dao.insert(validSha1);
		dao.insert(validSha2);
	}

	@Test
	public void sha1EqualsAfterPersistence() throws Exception {
		dao.insert(validSha1);
		final Optional<FileEntry> possiblyFetchedSha1 = dao.find(validSha1.getSha1());
		assertThat(possiblyFetchedSha1.isPresent()).isTrue();
		assertThat(possiblyFetchedSha1.get()).isEqualTo(validSha1);
	}
}
