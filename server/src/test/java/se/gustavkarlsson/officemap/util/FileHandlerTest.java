package se.gustavkarlsson.officemap.util;

import com.google.common.base.Optional;
import com.google.common.io.Resources;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.Test;
import se.gustavkarlsson.officemap.api.items.Sha1;

import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class FileHandlerTest {

	private static final String DATA_PATH = "data/";
	private static final String THUMBS_CACHE_PATH = "thumbs_cache/";
	private static final String FILE_SHA1 = "ddf7c2558cd58a06075d6506b886e7c6ebc858cc";

	private static final File file = new File(Resources.getResource("file.png").getPath());

	@Test
	public void testSaveOnUnix() throws Exception {
		assertSaveFile(getUnixFileHandler());
	}

	@Test
	public void testSaveOnWindows() throws Exception {
		assertSaveFile(getWindowsFileHandler());
	}

	@Test
	public void testSaveOnOsx() throws Exception {
		assertSaveFile(getOsxFileHandler());
	}

	@Test
	public void testReadOnUnix() throws Exception {
		assertReadFile(getUnixFileHandler());
	}

	@Test
	public void testReadOnWindows() throws Exception {
		assertReadFile(getWindowsFileHandler());
	}

	@Test
	public void testReadOnOsx() throws Exception {
		assertReadFile(getOsxFileHandler());
	}

	// TODO verify stream content
	private void assertReadFile(final FileHandler fh) throws FileNotFoundException {
		final Sha1 fileHash = saveFile(fh);
		final Optional<? extends StreamingOutput> stream = fh.readFile(fileHash, Optional.of(40));
		assertThat(stream.isPresent()).isTrue();
	}

	private static FileHandler getUnixFileHandler() throws IOException {
		return getFileHandlerForOs(Configuration.unix());
	}

	private static FileHandler getWindowsFileHandler() throws IOException {
		return getFileHandlerForOs(Configuration.windows());
	}

	private static FileHandler getOsxFileHandler() throws IOException {
		return getFileHandlerForOs(Configuration.osX());
	}

	private static FileHandler getFileHandlerForOs(final Configuration configuration) throws IOException {
		Path root = createInMemoryFileSystemRoot(configuration);
		return new FileHandler(Files.createDirectory(root.resolve(DATA_PATH)), new ThumbnailHandler(root.resolve(THUMBS_CACHE_PATH)));
	}

	private static Path createInMemoryFileSystemRoot(Configuration configuration) {
		return Jimfs.newFileSystem(configuration).getRootDirectories().iterator()
				.next();
	}

	private static void assertSaveFile(final FileHandler fh) throws FileNotFoundException {
		final Sha1 fileHash = saveFile(fh);
		assertThat(fileHash.getHex()).isEqualTo(FILE_SHA1);
	}

	private static Sha1 saveFile(final FileHandler fh) throws FileNotFoundException {
		final Sha1 savedFileHash = fh.saveFile(new FileInputStream(file));
		return savedFileHash;
	}
}
