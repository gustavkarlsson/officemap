package se.gustavkarlsson.officemap.resources.params;

import io.dropwizard.jersey.params.AbstractParam;

public class DoubleParam extends AbstractParam<Double> {
	
	protected DoubleParam(final String input) {
		super(input);
	}

	@Override
	protected String errorMessage(final String input, final Exception e) {
		return '"' + input + "\" is not a floating point number.";
	}

	@Override
	protected Double parse(final String input) throws Exception {
		return Double.valueOf(input);
	}
	
}
