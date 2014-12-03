package se.gustavkarlsson.officemap.dao;

import io.dropwizard.hibernate.AbstractDAO;

import org.hibernate.SessionFactory;

import se.gustavkarlsson.officemap.api.BlobHash;

public class BlobHashDao extends AbstractDAO<BlobHash> {
	
	public BlobHashDao(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	// TODO implement
}
