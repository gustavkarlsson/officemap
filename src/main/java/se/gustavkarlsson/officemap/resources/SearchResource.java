package se.gustavkarlsson.officemap.resources;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import se.gustavkarlsson.officemap.api.SearchResult;
import se.gustavkarlsson.officemap.api.items.Person;
import se.gustavkarlsson.officemap.core.State;

import com.google.common.collect.Lists;

@Path("/search")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class SearchResource {
	
	private final State state;
	
	public SearchResource(final State state) {
		this.state = state;
	}
	
	@POST
	public List<SearchResult<Person>> search(final String query, @Context final UriInfo uriInfo) {
		final List<String> terms = Arrays.asList(query.split(" "));
		final List<SearchResult<Person>> results = Lists.newArrayList();
		for (final Entry<Integer, Person> entry : state.getPersons().getAll().entrySet()) {
			final Person person = entry.getValue();
			final double score = calculateSearchScore(terms, person.getKeywords());
			if (score > 0) {
				final String url = "/persons/" + entry.getKey();
				results.add(new SearchResult<Person>(person, url, score));
			}
		}
		Collections.sort(results, Collections.reverseOrder());
		return results;
	}
	
	private double calculateSearchScore(final Collection<String> terms, final Collection<String> keywords) {
		double score = 0;
		for (final String keyword : keywords) {
			for (final String term : terms) {
				score += calculateWordScore(term, keyword);
			}
		}
		return score;
	}
	
	// TODO More sophisticated search
	private double calculateWordScore(final String term, final String keyword) {
		final String preparedKeyword = keyword.trim().toLowerCase();
		final String preparedTerm = term.trim().toLowerCase();
		if (preparedKeyword.equals(preparedTerm)) {
			return 1.0;
		}
		if (preparedKeyword.contains(preparedTerm)) {
			if (preparedKeyword.length() == 0) {
				return 0;
			}
			return ((double) preparedTerm.length()) / ((double) preparedKeyword.length());
		}
		return 0;
	}
}
