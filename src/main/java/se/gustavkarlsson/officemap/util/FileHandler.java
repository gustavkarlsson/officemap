package se.gustavkarlsson.officemap.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.tika.Tika;

import se.gustavkarlsson.officemap.api.item.Sha1;

import com.google.common.base.Optional;

public class FileHandler {
	
	private static final int BUFFER_SIZE = 1024;
	private static final int NO_BYTES_READ = -1;
	private static final int FILENAME_ATTEMPTS = 1000;
	private static final String UPLOADED_FILE_PREFIX = "uploaded_file_";
	
	private final Tika tika = new Tika();
	
	private final String dataDirectory;
	private final String tempDirectory;
	
	public FileHandler(final String dataDirectory, final String tempDirectory) {
		checkNotNull(dataDirectory);
		checkNotNull(tempDirectory);
		this.dataDirectory = dataDirectory;
		this.tempDirectory = tempDirectory;
	}
	
	public Sha1 saveFile(final InputStream inputStream) throws IOException {
		for (int i = 0; i < FILENAME_ATTEMPTS; i++) {
			final String tempFileName = UPLOADED_FILE_PREFIX + i;
			final String tempFileLocation = tempDirectory + "/" + tempFileName;
			final File tempFile = new File(tempFileLocation);
			final byte[] sha1 = transferData(inputStream, tempFile);
			final Sha1 fileEntry = Sha1.builder().withBytes(sha1).build();
			
			if (sha1 != null) {
				final File targetFile = new File(dataDirectory + "/" + fileEntry.getHex());
				if (!targetFile.exists()) {
					Files.move(tempFile.toPath(), targetFile.toPath());
					// TODO handle errors
				}
				return fileEntry;
			}
		}
		throw new IOException("Could not create temporary file in " + tempDirectory);
	}

	private byte[] transferData(final InputStream inputStream, final File outputFile) throws IOException {
		try (OutputStream outputStream = new FileOutputStream(outputFile)) {
			int read = 0;
			final byte[] bytes = new byte[BUFFER_SIZE];
			final MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
			while ((read = inputStream.read(bytes)) != NO_BYTES_READ) {
				outputStream.write(bytes, 0, read);
				sha1Digest.update(bytes, 0, read);
			}
			outputStream.flush();
			final byte[] sha1 = sha1Digest.digest();
			return sha1;
		} catch (final FileNotFoundException e) {
			// TODO log warning
			return null;
		} catch (final NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Optional<File> getFile(final Sha1 sha1) {
		try {
			final String fileLocation = dataDirectory + "/" + sha1.getHex();
			final File file = new File(fileLocation);
			if (!file.isFile()) {
				throw new FileNotFoundException(file.getCanonicalPath());
			}
			return Optional.of(file);
		} catch (final IOException e) {
			// TODO log exception
			return Optional.absent();
		}
	}
	
	public String getMimeType(final File file) {
		String mimeType;
		try {
			mimeType = tika.detect(file);
			return mimeType;
		} catch (final IOException e) {
			// TODO log warning
			e.printStackTrace();
			return null;
		}
	}
}
