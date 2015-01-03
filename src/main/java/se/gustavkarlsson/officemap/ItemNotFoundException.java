package se.gustavkarlsson.officemap;

@SuppressWarnings("serial")
public class ItemNotFoundException extends RuntimeException {
	
	public ItemNotFoundException(final int ref) {
		super("An item with ref " + ref + " could not be found");
	}
}
