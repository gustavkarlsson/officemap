package se.gustavkarlsson.officemap.resources;

import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.gustavkarlsson.officemap.api.items.Sha1;
import se.gustavkarlsson.officemap.util.FileHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.InputStream;
import java.net.URI;

@Path("/files")
public final class FilesResource extends Resource {
	private static final Logger logger = LoggerFactory.getLogger(FilesResource.class);
	
	private final FileHandler fileHandler;

	public FilesResource(final FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
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
	public Response send(@PathParam("sha1") final String hex, @QueryParam("size") IntParam sizeParam) {
		final Sha1 sha1;
		try {
			sha1 = Sha1.builder().withHex(hex).build();
		} catch (final IllegalArgumentException e) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		Integer size = sizeParam == null ? null : sizeParam.get();
		final Optional<? extends StreamingOutput> stream = fileHandler.readFile(sha1, Optional.fromNullable(size));
		if (!stream.isPresent()) {
			throw new NotFoundException();
		}
		final String mimeType = fileHandler.getMimeType(sha1);
		return Response.ok(stream.get()).type(mimeType).build();
	}
}
