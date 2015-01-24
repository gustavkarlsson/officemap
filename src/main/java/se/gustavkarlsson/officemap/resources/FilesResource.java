package se.gustavkarlsson.officemap.resources;

import io.dropwizard.hibernate.UnitOfWork;

import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.gustavkarlsson.officemap.api.items.Sha1;
import se.gustavkarlsson.officemap.util.FileHandler;

import com.google.common.base.Optional;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/files")
public final class FilesResource extends Resource {
	private static final Logger logger = LoggerFactory.getLogger(FilesResource.class);

	private final FileHandler fileHandler;
	
	public FilesResource(final FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@UnitOfWork
	public Response receive(@FormDataParam("file") final InputStream fileInputStream,
			@FormDataParam("file") final FormDataContentDisposition contentDispositionHeader,
			@Context final UriInfo uriInfo) {
		final Sha1 file = fileHandler.saveFile(fileInputStream);
		final URI uri = getCreatedResourceUri(uriInfo, file.getHex());
		logger.info("Saved file at " + uri);
		return Response.created(uri).entity(file.getHex()).build();
	}

	@Path("/{sha1}")
	@GET
	@UnitOfWork
	public Response send(@PathParam("sha1") final String hex) {
		final Sha1 sha1;
		try {
			sha1 = Sha1.builder().withHex(hex).build();
		} catch (final IllegalArgumentException e) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		final Optional<? extends StreamingOutput> stream = fileHandler.readFile(sha1);
		if (!stream.isPresent()) {
			throw new NotFoundException();
		}
		final String mimeType = fileHandler.getMimeType(sha1);
		return Response.ok(stream.get()).type(mimeType).build();
	}
}
