package se.gustavkarlsson.officemap.api.items;

public interface Searchable {

	java.util.Map<String, String> getFields();

	String getType();
}
