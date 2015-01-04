package se.gustavkarlsson.officemap.resources;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

public class Resource {

	public Resource() {
		super();
	}
	
	protected URI getCreatedResourceUri(final UriInfo uriInfo, final String path) {
		return uriInfo.getAbsolutePathBuilder().path(path).build();
	}

}