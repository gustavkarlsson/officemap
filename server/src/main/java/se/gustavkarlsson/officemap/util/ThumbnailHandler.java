package se.gustavkarlsson.officemap.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.gustavkarlsson.officemap.resources.FilesResource;

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

	public Path getThumbnail(Path source, FilesResource.ImageSize size) {
		checkNotNull(source);
		checkNotNull(size);
		checkArgument(size != FilesResource.ImageSize.FULL, "Cannot get a full sized thumbnail");
		Path target = getTarget(source, size);
		if (!Files.exists(target)) {
			try {
				createThumbnail(source, target, getDimension(size));
			} catch (IOException e) {
				throw new RuntimeException("Failed to create thumbnail of " + source, e);
			}
		}
		return target;
	}

	private Path getTarget(Path source, FilesResource.ImageSize size) {
		String targetPath = source.toFile().getName() + DELIMITER + getSuffix(size);
		return thumbsCachePath.resolve(targetPath);
	}

	private static String getSuffix(FilesResource.ImageSize size) {
		switch (size) {
			case SMALL:
				return "small";
			case MEDIUM:
				return "medium";
			case LARGE:
				return "large";
			default:
				throw new IllegalArgumentException("No suffix for thumbnail size: " + size);
		}
	}

	private static int getDimension(FilesResource.ImageSize size) {
		switch (size) {
			case SMALL:
				return 40;
			case MEDIUM:
				return 160;
			case LARGE:
				return 640;
			default:
				throw new IllegalArgumentException("No suffix for thumbnail size: " + size);
		}
	}

	private void createThumbnail(Path source, Path target, int dimension) throws IOException {
		checkNotNull(source);
		checkNotNull(target);
		checkArgument(dimension > 0, "dimension must be positive");
		try (OutputStream out = new FileOutputStream(target.toFile())) {
			Thumbnails.of(source.toFile()).size(dimension, dimension).crop(Positions.CENTER).toOutputStream(out);
		}
	}
}
