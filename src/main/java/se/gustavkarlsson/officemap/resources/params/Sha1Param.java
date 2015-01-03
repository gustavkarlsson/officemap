package se.gustavkarlsson.officemap.resources.params;

import io.dropwizard.jersey.params.AbstractParam;
import se.gustavkarlsson.officemap.api.Sha1;

public class Sha1Param extends AbstractParam<Sha1> {

	protected Sha1Param(final String input) {
		super(input);
	}
	
	@Override
	protected String errorMessage(final String input, final Exception e) {
		return '"' + input + "\" is not a valid SHA-1.";
	}
	
	@Override
	protected Sha1 parse(final String input) throws Exception {
		return Sha1.builder().withSha1(input).build();
	}

}
