package org.guiceside.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-4-29
 * Time: 11:30:28
 * To change this template use File | Settings | File Templates.
 */

public class FullTextSessionFactory {

	private final Session session;

    private final ThreadLocal<FullTextSession> localSession;


	public FullTextSessionFactory(Session session) {
		this.session=session;
        this.localSession = new ThreadLocal<FullTextSession>();
	}

	public FullTextSession get() {
		FullTextSession s = localSession.get();
		if ((s == null) || !s.isOpen()) {
			synchronized (this) {
				if ((s == null) || !s.isOpen()){
					s = Search.getFullTextSession(session);
					localSession.set(s);
				}
			}
		}
		return s;
	}
    public void releaseSession() {
		localSession.remove();
	}
}
