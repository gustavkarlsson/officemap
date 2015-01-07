package se.gustavkarlsson.officemap.util;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import javax.ws.rs.core.StreamingOutput;

import org.junit.Before;
import org.junit.Test;

import se.gustavkarlsson.officemap.api.items.Sha1;

import com.google.common.io.Resources;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

public class FileHandlerTest {
	
	private static final String DATA_PATH = "data";
	private static final String FILE_SHA1 = "ddf7c2558cd58a06075d6506b886e7c6ebc858cc";
	
	private static final File file = new File(Resources.getResource("file.png").getPath());
	
	private FileHandler unixFileHandler;
	private FileHandler windowsFileHandler;
	private FileHandler osxFileHandler;
	
	@Before
	public void setUp() throws Exception {
		unixFileHandler = getFileHandler(Configuration.unix());
		windowsFileHandler = getFileHandler(Configuration.windows());
		osxFileHandler = getFileHandler(Configuration.osX());
	}
	
	private FileHandler getFileHandler(final Configuration configuration) throws IOException {
		return new FileHandler(Files.createDirectory(Jimfs.newFileSystem(configuration).getRootDirectories().iterator()
				.next().resolve(DATA_PATH)));
	}

	@Test
	public void testWriteOnUnix() throws Exception {
		assertWriteFile(unixFileHandler);
	}

	@Test
	public void testWriteOnWindows() throws Exception {
		assertWriteFile(windowsFileHandler);
	}

	@Test
	public void testWriteOnOsx() throws Exception {
		assertWriteFile(osxFileHandler);
	}

	// TODO verify stream?
	@Test
	public void testGetOnUnix() throws Exception {
		final FileHandler fh = unixFileHandler;
		final Sha1 fileHash = writeFile(fh);
		final StreamingOutput stream = fh.readFile(fileHash);
		assertThat(stream).isNotNull();
	}
	
	private void assertWriteFile(final FileHandler fh) throws FileNotFoundException {
		final Sha1 fileHash = writeFile(fh);
		assertThat(fileHash.getHex()).isEqualTo(FILE_SHA1);
	}
	
	private Sha1 writeFile(final FileHandler fh) throws FileNotFoundException {
		final Sha1 savedFileHash = fh.writeFile(new FileInputStream(file));
		return savedFileHash;
	}
}
