package se.gustavkarlsson.officemap.api.event;

@SuppressWarnings("serial")
public class ProcessEventException extends Exception {

	public ProcessEventException() {
		super();
	}

	public ProcessEventException(final String message) {
		super(message);
	}

	public ProcessEventException(final Throwable cause) {
		super(cause);
	}

	public ProcessEventException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
