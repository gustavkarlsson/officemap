package se.gustavkarlsson.officemap.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
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
import com.google.common.net.MediaType;

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
			final File tempFile = getTempFile(i);
			if (tempFile.exists()) {
				continue;
			}
			
			final byte[] fileSha1Bytes = transferData(inputStream, tempFile);
			final Sha1 fileSha1 = Sha1.builder().withBytes(fileSha1Bytes).build();
			final File targetFile = new File(dataDirectory + "/" + fileSha1.getHex());
			if (!targetFile.exists()) {
				Files.move(tempFile.toPath(), targetFile.toPath());
			}
			return fileSha1;
		}
		throw new IOException("Could not create temporary file in " + tempDirectory);
	}

	private File getTempFile(final int i) {
		final String tempFileName = UPLOADED_FILE_PREFIX + i;
		final String tempFileLocation = tempDirectory + "/" + tempFileName;
		final File tempFile = new File(tempFileLocation);
		return tempFile;
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
		} catch (final NoSuchAlgorithmException e) {
			// TODO log fatal
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Optional<File> getFile(final Sha1 sha1) {
		final String fileLocation = dataDirectory + "/" + sha1.getHex();
		final File file = new File(fileLocation);
		if (!file.exists()) {
			return Optional.absent();
		}
		if (!file.isFile()) {
			// TODO log warning (not a file)
			return Optional.absent();
		}
		return Optional.of(file);
	}

	public String getMimeType(final File file) {
		String mimeType;
		try {
			mimeType = tika.detect(file);
			return mimeType;
		} catch (final IOException e) {
			// TODO log warning (could not determine mimetype)
			e.printStackTrace();
			return MediaType.OCTET_STREAM.toString();
		}
	}
}
