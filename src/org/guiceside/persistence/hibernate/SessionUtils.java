package org.guiceside.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-4-29
 * Time: 11:30:28
 * To change this template use File | Settings | File Templates.
 */

public class SessionUtils {
    private final SessionFactory sessionFactory;

    private final ThreadLocal<Session> localSession;

    
	public SessionUtils(SessionFactory sessionFactory) {
        System.out.println(this.toString()+" init");
		this.sessionFactory = sessionFactory;
        this.localSession = new ThreadLocal<Session>();
	}

	public Session get() {
		System.out.println("************* init");
        Session s = localSession.get();
		if ((s == null) || !s.isOpen()) {
			synchronized (this) {
				if ((s == null) || !s.isOpen()){
					s = sessionFactory.openSession();
					localSession.set(s);
				}
			}
		}
		return s;
	}
    public void releaseSession() {
		final Session s = localSession.get();
		localSession.remove();
		if (s != null) {
			if (s.isOpen()) {
				s.flush();
				s.close();
			}
		}
	}
}
