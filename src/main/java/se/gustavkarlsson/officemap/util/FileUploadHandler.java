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

import se.gustavkarlsson.officemap.api.Sha1;

public class FileUploadHandler {

	private static final int NO_BYTES_READ = -1;
	private static final int FILENAME_ATTEMPTS = 1000;
	private static final String UPLOADED_FILE_PREFIX = "uploaded_file_";
	private final String dataDirectory;
	private final String tempDirectory;

	public FileUploadHandler(final String dataDirectory, final String tempDirectory) {
		checkNotNull(dataDirectory);
		checkNotNull(tempDirectory);
		this.dataDirectory = dataDirectory;
		this.tempDirectory = tempDirectory;
	}

	public Sha1 uploadFile(final InputStream inputStream) throws IOException {
		for (int i = 0; i < FILENAME_ATTEMPTS; i++) {
			final String tempFileName = UPLOADED_FILE_PREFIX + i;
			final File tempFile = new File(tempDirectory + "/" + tempFileName);
			final Sha1 sha1 = transferData(inputStream, tempFile);

			if (sha1 != null) {
				final File targetFile = new File(dataDirectory + "/" + sha1.getHex());
				if (!targetFile.exists()) {
					Files.move(tempFile.toPath(), targetFile.toPath());
					// TODO handle errors
				}
				return sha1;
			}
		}
		throw new IOException("Could not create temporary file in " + tempDirectory);
	}
	
	private Sha1 transferData(final InputStream inputStream, final File outputFile) throws IOException {
		try (OutputStream outputStream = new FileOutputStream(outputFile)) {
			int read = 0;
			final byte[] bytes = new byte[1024];
			final MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
			while ((read = inputStream.read(bytes)) != NO_BYTES_READ) {
				outputStream.write(bytes, 0, read);
				sha1Digest.update(bytes, 0, read);
			}
			outputStream.flush();
			final byte[] sha1Bytes = sha1Digest.digest();
			return new Sha1(sha1Bytes);
		} catch (final FileNotFoundException e) {
			// TODO log warning
			return null;
		} catch (final NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
