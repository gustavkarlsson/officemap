package se.gustavkarlsson.officemap.resources;

import io.dropwizard.jersey.params.IntParam;
import se.gustavkarlsson.officemap.api.SearchResult;
import se.gustavkarlsson.officemap.api.items.Map;
import se.gustavkarlsson.officemap.api.items.Person;
import se.gustavkarlsson.officemap.core.State;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

@Path("/search")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class SearchResource {
	
	private final State state;
	
	public SearchResource(final State state) {
		this.state = state;
	}
	
	@POST
	public List<SearchResult< ? >> search(@QueryParam("results") @DefaultValue("10")final IntParam maxResults, final String query, @Context final UriInfo uriInfo) {
		List<SearchResult< ? >> results = new ArrayList<>();
		List<Entry<String, Integer>> searchResults = state.getIndex().search(query, maxResults.get());
		for (Entry<String, Integer> searchResult : searchResults) {
			String type = searchResult.getKey();
			Integer ref = searchResult.getValue();
			switch (type) {
				case Person.TYPE:
					Person person = state.getPersons().get(ref);
					results.add(new SearchResult<>(ref, type, person));
					break;
				case Map.TYPE:
					Map map = state.getMaps().get(ref);
					results.add(new SearchResult<>(ref, type, map));
					break;
				default:
					throw new RuntimeException("Unknown type: " + type);
			}
		}
		return results;
	}
}
