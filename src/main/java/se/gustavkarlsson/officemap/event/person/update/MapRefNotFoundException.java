package se.gustavkarlsson.officemap.event.person.update;

@SuppressWarnings("serial")
public class MapRefNotFoundException extends RuntimeException {

	public MapRefNotFoundException(final int mapRef) {
		super("A Map with ref " + mapRef + " could not be found");
	}
}
