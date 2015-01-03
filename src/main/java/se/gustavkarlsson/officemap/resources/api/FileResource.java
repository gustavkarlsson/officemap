package se.gustavkarlsson.officemap.resources.api;

import io.dropwizard.hibernate.UnitOfWork;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import se.gustavkarlsson.officemap.api.item.Sha1;
import se.gustavkarlsson.officemap.util.FileHandler;

import com.google.common.base.Optional;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/file")
public final class FileResource {

	private final FileHandler fileHandler;

	public FileResource(final FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	@UnitOfWork
	public String receive(@FormDataParam("file") final InputStream fileInputStream,
			@FormDataParam("file") final FormDataContentDisposition contentDispositionHeader) {
		try {
			final Sha1 file = fileHandler.saveFile(fileInputStream);
			return file.getHex();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@Path("/{sha1}")
	@GET
	@UnitOfWork
	public Response send(@PathParam("sha1") final String sha1Hex) throws Exception {
		final Sha1 sha1;
		try {
			sha1 = Sha1.builder().withSha1(sha1Hex).build();
		} catch (final IllegalArgumentException e) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

		final Optional<File> possibleFile = fileHandler.getFile(sha1);
		if (!possibleFile.isPresent()) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

		final String mimeType = fileHandler.getMimeType(possibleFile.get());
		return Response.ok(possibleFile).type(mimeType).build();
	}
}
