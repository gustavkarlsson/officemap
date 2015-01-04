package se.gustavkarlsson.officemap.api.changeset;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("serial")
public class ValueMappingException extends JsonMappingException {

	public ValueMappingException(final String nodePath, final JsonNode node, final String parameterClass,
			final String cause) {
		super(createMessageString(nodePath, node, parameterClass) + createCauseString(cause));
	}

	public ValueMappingException(final String nodePath, final JsonNode node, final String parameterClass) {
		this(nodePath, node, parameterClass, null);
	}

	private static String createMessageString(final String nodePath, final JsonNode node, final String parameterClass) {
		return nodePath + "\" with value " + node + " could not be mapped to Value<" + parameterClass + ">";
	}

	private static String createCauseString(final String cause) {
		if (cause == null) {
			return "";
		}
		return ": " + cause;
	}
}
