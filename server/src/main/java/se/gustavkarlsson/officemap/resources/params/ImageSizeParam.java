package se.gustavkarlsson.officemap.resources.params;

import io.dropwizard.jersey.params.AbstractParam;
import se.gustavkarlsson.officemap.resources.FilesResource;

public class ImageSizeParam extends AbstractParam<FilesResource.ImageSize> {

	protected ImageSizeParam(final String input) {
		super(input);
	}
	
	@Override
	protected String errorMessage(final String input, final Exception e) {
		return '"' + input + "\" is not a valid image size. Expected: " + FilesResource.ImageSize.values();
	}
	
	@Override
	protected FilesResource.ImageSize parse(final String input) throws Exception {
		switch (input.toLowerCase()) {
			case "small":
				return FilesResource.ImageSize.SMALL;
			case "medium":
				return FilesResource.ImageSize.MEDIUM;
			case "large":
				return FilesResource.ImageSize.LARGE;
			case "full":
				return FilesResource.ImageSize.FULL;
			default: throw new Exception("Invalid image size: " + input);
		}
	}

}
