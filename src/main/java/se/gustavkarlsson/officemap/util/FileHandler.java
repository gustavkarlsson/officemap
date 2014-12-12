package se.gustavkarlsson.officemap.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.StreamingOutput;

import se.gustavkarlsson.officemap.api.fileentry.FileEntry;

public class FileHandler {
	
	private static final int BUFFER_SIZE = 1024;
	private static final int NO_BYTES_READ = -1;
	private static final int FILENAME_ATTEMPTS = 1000;
	private static final String UPLOADED_FILE_PREFIX = "uploaded_file_";
	
	private final String dataDirectory;
	private final String tempDirectory;
	
	public FileHandler(final String dataDirectory, final String tempDirectory) {
		checkNotNull(dataDirectory);
		checkNotNull(tempDirectory);
		this.dataDirectory = dataDirectory;
		this.tempDirectory = tempDirectory;
	}
	
	public FileEntry receive(final InputStream inputStream, final String mimeType) throws IOException {
		for (int i = 0; i < FILENAME_ATTEMPTS; i++) {
			final String tempFileName = UPLOADED_FILE_PREFIX + i;
			final String tempFileLocation = tempDirectory + "/" + tempFileName;
			final File tempFile = new File(tempFileLocation);
			final byte[] sha1 = transferData(inputStream, tempFile);
			final FileEntry fileEntry = FileEntry.builder().with(null, mimeType, sha1).build();
			
			if (sha1 != null) {
				final File targetFile = new File(dataDirectory + "/" + fileEntry.getSha1Hex());
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

	public StreamingOutput send(final FileEntry sha1) {
		return new StreamingOutput() {
			@Override
			public void write(final OutputStream output) throws IOException {
				final String fileLocation = dataDirectory + "/" + sha1.getSha1Hex();
				final File file = new File(fileLocation);
				try (final FileInputStream inputStream = new FileInputStream(file)) {
					int read = 0;
					final byte[] bytes = new byte[BUFFER_SIZE];
					while ((read = inputStream.read(bytes)) != NO_BYTES_READ) {
						output.write(bytes, 0, read);
					}
				}
			}
		};
	}
}
