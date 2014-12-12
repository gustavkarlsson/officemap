package se.gustavkarlsson.officemap.resources.api;

import io.dropwizard.hibernate.UnitOfWork;

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
import javax.ws.rs.core.StreamingOutput;

import org.apache.tika.Tika;

import se.gustavkarlsson.officemap.api.fileentry.FileEntry;
import se.gustavkarlsson.officemap.dao.FileEntryDao;
import se.gustavkarlsson.officemap.util.FileHandler;

import com.google.common.base.Optional;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/file")
@Consumes(MediaType.TEXT_PLAIN)
public final class FileResource {
	
	private final Tika tika = new Tika();

	private final FileEntryDao dao;
	private final FileHandler fileHandler;

	public FileResource(final FileEntryDao dao, final FileHandler fileHandler) {
		this.dao = dao;
		this.fileHandler = fileHandler;
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	@UnitOfWork
	public FileEntry receive(@FormDataParam("file") final InputStream fileInputStream,
			@FormDataParam("file") final FormDataContentDisposition contentDispositionHeader) {
		final String mimeType = getMimeType(contentDispositionHeader);
		try {
			final FileEntry sha1 = fileHandler.receive(fileInputStream, mimeType);
			dao.insert(sha1);
			return sha1;
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@Path("/{sha1}")
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@UnitOfWork
	public Response downloadFile(@PathParam("sha1") final String sha1Hex) throws Exception {
		final Optional<FileEntry> possibleFileEntry = dao.find(sha1Hex);
		if (!possibleFileEntry.isPresent()) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		final FileEntry fileEntry = possibleFileEntry.get();
		final StreamingOutput output = fileHandler.send(fileEntry);
		return Response.ok(output).type(fileEntry.getMimeType()).build();
	}

	private String getMimeType(final FormDataContentDisposition contentDispositionHeader) {
		final String fileName = contentDispositionHeader.getFileName();
		final String mimeType = tika.detect(fileName);
		return mimeType;
	}
}
