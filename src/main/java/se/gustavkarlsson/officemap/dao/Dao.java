package se.gustavkarlsson.officemap.dao;

import java.util.List;

public interface Dao<T> {
	
	Long save(T entity);
	
	T findById(long id);
	
	List<T> list();
	
	T update(T entity);
	
	void delete(T entity);
}
