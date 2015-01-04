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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.gustavkarlsson.officemap.api.items.Sha1;

import com.google.common.base.Optional;
import com.google.common.net.MediaType;

public class FileHandler {
	private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);
	
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
		logger.debug("dataDirectory: " + dataDirectory);
		logger.debug("tempDirectory: " + tempDirectory);
	}

	public Sha1 saveFile(final InputStream inputStream) {
		logger.debug("Saving file");
		try {
			for (int i = 0; i < FILENAME_ATTEMPTS; i++) {
				logger.debug("Attempt " + (i + 1));
				final File tempFile = getTempFile(i);
				logger.debug("Temp file: " + tempFile.getAbsolutePath());
				if (tempFile.exists()) {
					logger.debug("Temp file already exists");
					continue;
				}

				logger.debug("Transferring...");
				final byte[] fileSha1Bytes = transferData(inputStream, tempFile);
				final Sha1 fileSha1 = Sha1.builder().withBytes(fileSha1Bytes).build();
				final File targetFile = new File(dataDirectory + "/" + fileSha1.getHex());
				logger.debug("Target file: " + targetFile.getAbsolutePath());
				if (!targetFile.exists()) {
					logger.debug("Target file does not exists");
					logger.debug("Moving...");
					Files.move(tempFile.toPath(), targetFile.toPath());
				} else {
					logger.debug("Target file already exists");
				}
				logger.debug("Saved file as " + targetFile.getName());
				return fileSha1;
			}
			throw new IOException("Gave up trying to create temporary file in " + tempDirectory + " after "
					+ FILENAME_ATTEMPTS + " attempts");
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
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
			logger.error("Could not digest file data", e);
			throw new RuntimeException(e);
		}
	}
	
	public Optional<File> getFile(final Sha1 fileSha1) {
		final String fileLocation = dataDirectory + "/" + fileSha1.getHex();
		logger.debug("Getting file: " + fileSha1.getHex());
		final File file = new File(fileLocation);
		if (!file.exists()) {
			logger.debug("File does not exist");
			return Optional.absent();
		}
		if (!file.isFile()) {
			logger.error("Could not get " + file.getAbsolutePath() + ": Not a file");
			return Optional.absent();
		}
		return Optional.of(file);
	}

	public String getMimeType(final File file) {
		logger.debug("Determining MIME type of " + file.getAbsolutePath());
		String mimeType;
		try {
			mimeType = tika.detect(file);
		} catch (final IOException e) {
			logger.warn("Could not determine MIME type of file: " + file.getAbsolutePath());
			mimeType = MediaType.OCTET_STREAM.toString();
		}
		logger.debug("MIME type is " + mimeType);
		return mimeType;
	}
}
