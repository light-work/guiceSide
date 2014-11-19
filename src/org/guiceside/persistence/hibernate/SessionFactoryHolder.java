package org.guiceside.persistence.hibernate;

import org.hibernate.SessionFactory;

/**
 * <p>
 * SessionFactory持有者,单例模式<br/>
 * 负责实例化SessionFactory对像<br/>
 * 由SessionFactoryProvider提供返回
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class SessionFactoryHolder {

	private SessionFactory sessionFactory;

	private static SessionFactoryHolder sessionFactoryHolder;

	public SessionFactoryHolder() {
		sessionFactoryHolder = this;
	}

	SessionFactory getSessionFactory() {

		return sessionFactory;
	}

	synchronized void setSessionFactory(SessionFactory sessionFactory) {
		if (null != this.sessionFactory)
			throw new RuntimeException(
					"Duplicate session factory creation! Only one session factory is allowed per injector");

		this.sessionFactory = sessionFactory;
	}

	public static SessionFactory getCurrentSessionFactory() {
		return sessionFactoryHolder.getSessionFactory();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof SessionFactoryHolder))
			return false;

		SessionFactoryHolder that = (SessionFactoryHolder) o;

		return (sessionFactory == null ? that.sessionFactory == null
				: sessionFactory.equals(that.sessionFactory));

	}

	@Override
	public int hashCode() {
		return (sessionFactory != null ? sessionFactory.hashCode() : 0);
	}
}
