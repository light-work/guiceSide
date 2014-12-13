package org.guiceside.persistence.hibernate;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * <p>
 * Session提供者<br.>
 * 由sessionFactory获得当前Session<br/>
 * 负责提供Session实例
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
@Singleton
public class SessionProvider implements Provider<Session> {

	private final SessionFactory sessionFactory;

    private final ThreadLocal<Session> localSession;
	@Inject
	public SessionProvider(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
        this.localSession = new ThreadLocal<Session>();
	}

	public Session get() {
		System.out.println("************* 1222");
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
		//return this.sessionFactory.getCurrentSession();
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
