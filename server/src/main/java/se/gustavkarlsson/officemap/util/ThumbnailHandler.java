package se.gustavkarlsson.officemap.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ThumbnailHandler {
	private static final Logger logger = LoggerFactory.getLogger(ThumbnailHandler.class);

	private static final String DELIMITER = "_";

	private final Path thumbsCachePath;

	public ThumbnailHandler(final Path thumbsCachePath) throws IOException {
		checkNotNull(thumbsCachePath);
		logger.debug("Creating thumbnail cache directory in " + thumbsCachePath);
		this.thumbsCachePath = Files.createDirectories(thumbsCachePath);
	}

	public Path getThumbnail(Path source, int size) {
		checkNotNull(source);
		checkNotNull(size);
		checkArgument(size > 0, "size must be positive");
		Path target = getTarget(source, size);
		if (!Files.exists(target)) {
			try {
				createThumbnail(source, target, size);
			} catch (IOException e) {
				throw new RuntimeException("Failed to create thumbnail of " + source, e);
			}
		}
		return target;
	}

	private Path getTarget(Path source, int size) {
		String targetPath = source.toFile().getName() + DELIMITER + size + 'x' + size;
		return thumbsCachePath.resolve(targetPath);
	}

	private void createThumbnail(Path source, Path target, int size) throws IOException {
		try (OutputStream out = new FileOutputStream(target.toFile())) {
			Thumbnails.of(source.toFile()).size(size, size).crop(Positions.CENTER).toOutputStream(out);
		}
	}
}
