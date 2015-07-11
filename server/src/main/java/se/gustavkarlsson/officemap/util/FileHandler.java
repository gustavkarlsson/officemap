package se.gustavkarlsson.officemap.util;

import com.google.common.base.Optional;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.gustavkarlsson.officemap.api.items.Sha1;
import se.gustavkarlsson.officemap.resources.FilesResource;

import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.google.common.base.Preconditions.checkNotNull;

public class FileHandler {
	private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);

	private static final int BUFFER_SIZE = 4096;
	private static final int NO_BYTES_READ = -1;

	private final Tika tika = new Tika();
	private final Path dataPath;
	private final ThumbnailHandler thumbnailHandler;

	public FileHandler(final Path dataPath, ThumbnailHandler thumbnailHandler) throws IOException {
		checkNotNull(dataPath);
		checkNotNull(thumbnailHandler);
		logger.debug("Creating data directory in " + dataPath);
		this.dataPath = Files.createDirectories(dataPath);
		this.thumbnailHandler = thumbnailHandler;
	}

	public Sha1 saveFile(final InputStream input) {
		logger.debug("Saving file");
		checkNotNull(input);
		Path tempFile = null;
		try {
			logger.debug("Creating temp file");
			tempFile = Files.createTempFile(null, null);
			logger.debug("Created temp file: " + tempFile);
			logger.debug("Writing data to temp file...");
			final Sha1 fileSha1 = transferData(input, Files.newOutputStream(tempFile), true);
			logger.debug("SHA-1 of file: " + fileSha1.getHex());
			final Path targetFile = dataPath.resolve(fileSha1.getHex());
			logger.debug("Target file: " + targetFile);
			try {
				logger.debug("Attempting to move temp file to target file");
				Files.move(tempFile, targetFile);
				logger.debug("File moved");
			} catch (final FileAlreadyExistsException e) {
				logger.debug("Target file already exists");
			}
			return fileSha1;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (tryTodeleteFile(tempFile)) {
				logger.debug("Deleted temp file");
			}
		}
	}

	private static boolean tryTodeleteFile(final Path file) {
		try {
			if (file != null) {
				return Files.deleteIfExists(file);
			}
		} catch (final IOException e) {
			logger.warn("Failed to delete file", e);
		}
		return false;
	}

	private static Sha1 transferData(final InputStream input, final OutputStream output, final boolean calculateSha1)
			throws IOException {
		checkNotNull(input);
		checkNotNull(output);
		try (final ReadableByteChannel source = Channels.newChannel(input);
				final WritableByteChannel target = Channels.newChannel(output)) {
			MessageDigest sha1Digest = null;
			if (calculateSha1) {
				sha1Digest = MessageDigest.getInstance("SHA-1");
			}
			final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
			while (source.read(buffer) != NO_BYTES_READ) {
				buffer.flip();
				while (buffer.hasRemaining()) {
					if (calculateSha1) {
						sha1Digest.update(buffer);
						buffer.rewind();
					}
					target.write(buffer);
				}
				buffer.clear();
			}
			if (calculateSha1) {
				final byte[] sha1Bytes = sha1Digest.digest();
				return Sha1.builder().withBytes(sha1Bytes).build();
			}
			return null;
		} catch (final NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
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

	public Optional<? extends StreamingOutput> readFile(final Sha1 fileSha1, FilesResource.ImageSize size) {
		checkNotNull(fileSha1);
		checkNotNull(size);
		logger.debug("Getting stream of file: " + fileSha1.getHex());
		final Path path = dataPath.resolve(fileSha1.getHex());
		if (!Files.exists(path)) {
			return Optional.absent();
		}
		if (size != FilesResource.ImageSize.FULL) {
			logger.debug("Requesting thumbnail of size: " + size);
			Path thumbnail = thumbnailHandler.getThumbnail(path, size);
			return Optional.of(new FileStreamingOutput(thumbnail));
		}
		return Optional.of(new FileStreamingOutput(path));
	}

	public String getMimeType(final Sha1 fileSha1) {
		checkNotNull(fileSha1);
		final Path path = dataPath.resolve(fileSha1.getHex());
		logger.debug("Determining MIME type of " + path);
		try {
			return tika.detect(Files.newInputStream(path));
		} catch (final IOException e) {
			logger.warn("Could not determine MIME type of file: " + path);
			throw new RuntimeException(e);
		}
	}

	private class FileStreamingOutput implements StreamingOutput {
		private final Path path;

		public FileStreamingOutput(final Path path) {
			this.path = checkNotNull(path);
		}

		@Override
		public void write(final OutputStream output) throws IOException {
			transferData(Files.newInputStream(path), output, false);
		}
	}
}
