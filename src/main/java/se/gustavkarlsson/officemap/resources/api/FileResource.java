package se.gustavkarlsson.officemap.resources.api;

import io.dropwizard.hibernate.UnitOfWork;

import java.io.File;
import java.io.IOException;
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
import javax.ws.rs.core.UriInfo;

import se.gustavkarlsson.officemap.api.item.Sha1;
import se.gustavkarlsson.officemap.util.FileHandler;

import com.google.common.base.Optional;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/files")
public final class FileResource extends Resource {
	
	private final FileHandler fileHandler;
	
	public FileResource(final FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@UnitOfWork
	public Response receive(@FormDataParam("file") final InputStream fileInputStream,
			@FormDataParam("file") final FormDataContentDisposition contentDispositionHeader,
			@Context final UriInfo uriInfo) {
		try {
			final Sha1 file = fileHandler.saveFile(fileInputStream);
			final URI uri = getCreatedResourceUri(uriInfo, file.getHex());
			return Response.created(uri).build();
		} catch (final IOException e) {
			// TODO log error (unexpected IO)
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

	}

	@Path("/{sha1}")
	@GET
	@UnitOfWork
	public Response send(@PathParam("sha1") final String hex) throws Exception {
		final Sha1 sha1;
		try {
			sha1 = Sha1.builder().withHex(hex).build();
		} catch (final IllegalArgumentException e) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

		final Optional<File> possibleFile = fileHandler.getFile(sha1);
		if (!possibleFile.isPresent()) {
			throw new NotFoundException();
		}
		final File file = possibleFile.get();
		
		final String mimeType = fileHandler.getMimeType(file);
		return Response.ok(file).type(mimeType).build();
	}
}
