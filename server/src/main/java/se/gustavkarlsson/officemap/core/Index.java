package se.gustavkarlsson.officemap.core;

import com.google.common.base.Joiner;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import se.gustavkarlsson.officemap.api.items.Searchable;

import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Index { // TODO Synchronize

	private static final String REF = "ref";
	private static final String TYPE = "type";
	private static final String CONTENT = "content";

	private final Directory directory = new RAMDirectory();
	private final Analyzer analyzer =new Analyzer() {
		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			Tokenizer source = new NGramTokenizer(2, 10);
			TokenStream filter = new LowerCaseFilter(source);
			return new TokenStreamComponents(source, filter);
		}
	};

	public Index() {
		try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(analyzer))) {
			writer.commit();
		} catch (IOException e) {
			// TODO handle Index constructor errors
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	void add(int ref, Searchable item) {
		checkNotNull(item);
		checkReservedKeys(item);

		Document document = createDocument(ref, item);
		try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(analyzer))) {
			writer.addDocument(document);
			writer.commit();
		} catch (IOException e) {
			// TODO handle Index add errors
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	void update(int ref, Searchable item) {
		checkNotNull(item);
		checkReservedKeys(item);

		Term term = new Term(REF, String.valueOf(ref));
		Document document = createDocument(ref, item);
		try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(analyzer))) {
			writer.updateDocument(term, document);
			writer.commit();
		} catch (IOException e) {
			// TODO handle Index update errors
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	void remove(int ref) {
		Term term = new Term(REF, String.valueOf(ref));
		try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(analyzer))) {
			writer.deleteDocuments(term);
			writer.commit();
		} catch (IOException e) {
			// TODO handle Index remove errors
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Map.Entry<String, Integer>> search(String text, int maxResults) {
		checkNotNull(text);
		checkArgument(maxResults > 0, "maxResults must be positive");
		if (text.isEmpty()) {
			return Collections.emptyList();
		}

		List<Map.Entry<String, Integer>> results = new ArrayList<>();
		try (DirectoryReader reader = DirectoryReader.open(directory)) {
			IndexSearcher searcher = new IndexSearcher(reader);
			Query query = createQuery(text);
			TopDocs topDocs = searcher.search(query, maxResults);
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document document = searcher.doc(scoreDoc.doc);
				results.add(new AbstractMap.SimpleEntry<>(document.get(TYPE), Integer.valueOf(document.get(REF))));
			}
			return results;
		} catch (IOException | ParseException e) {
			// TODO handle Index search errors
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private Query createQuery(String text) throws ParseException {
		QueryParser parser = new QueryParser(CONTENT, analyzer);
		parser.setDefaultOperator(QueryParser.Operator.AND);
		String escaped = QueryParser.escape(text);
		return parser.parse(escaped);
	}

	private Document createDocument(int ref, Searchable item) {
		Document document = new Document();
		document.add(new StringField(REF, String.valueOf(ref), Field.Store.YES));
		document.add(new StringField(TYPE, item.getType(), Field.Store.YES));
		document.add(new TextField(CONTENT, createContentString(item), Field.Store.NO));
		return document;
	}

	private String createContentString(Searchable item) {
		return Joiner.on(' ').join(item.getFields().values());
	}

	private static void checkReservedKeys(Searchable item) {
		List<String> reservedKeys = Arrays.asList(REF, TYPE, CONTENT);
		for (String key : reservedKeys) {
			checkArgument(!item.getFields().keySet().contains(key), "Field must not contain reserved key: " + key);
		}
	}


}
