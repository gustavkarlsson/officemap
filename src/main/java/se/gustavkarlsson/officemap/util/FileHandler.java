package se.gustavkarlsson.officemap.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.StreamingOutput;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.gustavkarlsson.officemap.api.items.Sha1;

import com.google.common.net.MediaType;

public class FileHandler {
	private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);

	private static final int BUFFER_SIZE = 4096;
	private static final int NO_BYTES_READ = -1;

	private final Tika tika = new Tika();

	private final MessageDigest sha1Digest;
	private final Path dataPath;

	public FileHandler(final Path dataPath) {
		try {
			this.sha1Digest = MessageDigest.getInstance("SHA-1");
		} catch (final NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		this.dataPath = checkNotNull(dataPath);
		logger.debug("dataPath: " + dataPath);
	}

	public Sha1 writeFile(final InputStream input) {
		checkNotNull(input);
		logger.debug("Saving file");
		try {
			final Path tempFile = Files.createTempFile(null, null);
			logger.debug("Temp file: " + tempFile);

			logger.debug("Transferring...");
			final Sha1 fileSha1 = transferData(input, Files.newOutputStream(tempFile), true);
			final Path targetFile = dataPath.resolve(fileSha1.getHex());
			logger.debug("Target file: " + targetFile);
			try {
				logger.debug("Attempting to move temp file to target file");
				Files.move(tempFile, targetFile);
			} catch (final FileAlreadyExistsException e) {
				logger.debug("Target file already exists");
			}
			return fileSha1;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Sha1 transferData(final InputStream input, final OutputStream output, final boolean calculateSha1)
			throws IOException {
		try {
			int read = 0;
			final byte[] buffer = new byte[BUFFER_SIZE];
			while ((read = input.read(buffer)) != NO_BYTES_READ) {
				output.write(buffer, 0, read);
				if (calculateSha1) {
					sha1Digest.update(buffer, 0, read);
				}
			}
			output.flush();
			if (calculateSha1) {
				final byte[] sha1Bytes = sha1Digest.digest();
				return Sha1.builder().withBytes(sha1Bytes).build();
			}
			return null;
		} finally {
			input.close();
			output.close();
		}
	}

	public boolean exists(final Sha1 fileSha1) {
		checkNotNull(fileSha1);
		logger.debug("Getting file: " + fileSha1.getHex());
		final Path path = dataPath.resolve(fileSha1.getHex());

		if (!Files.exists(path)) {
			return false;
		}
		if (!Files.isRegularFile(path)) {
			logger.error(path + " is not a file");
			return false;
		}
		return true;
	}

	public StreamingOutput readFile(final Sha1 fileSha1) {
		logger.debug("Getting stream of file: " + fileSha1.getHex());
		final Path path = dataPath.resolve(fileSha1.getHex());
		return new FileStreamingOutput(path);
	}

	public String getMimeType(final Sha1 fileSha1) {
		checkNotNull(fileSha1);
		final Path path = dataPath.resolve(fileSha1.getHex());
		logger.debug("Determining MIME type of " + path);
		String mimeType;
		try {
			mimeType = tika.detect(Files.newInputStream(path));
		} catch (final IOException e) {
			logger.warn("Could not determine MIME type of file: " + path);
			mimeType = MediaType.OCTET_STREAM.toString();
		}
		logger.debug("MIME type is " + mimeType);
		return mimeType;
	}

	private class FileStreamingOutput implements StreamingOutput {

		private final Path path;

		public FileStreamingOutput(final Path path) {
			this.path = checkNotNull(path);
		}

		// TODO use NIO? (http://stackoverflow.com/questions/16050827/filechannel-bytebuffer-and-hashing-files)
		@Override
		public void write(final OutputStream output) throws IOException {
			transferData(Files.newInputStream(path), output, false);
		}
	}
}
