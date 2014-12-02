package org.guiceside.persistence.hibernate;

import org.guiceside.persistence.WorkManager;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.context.internal.ManagedSessionContext;

/**
 * <p>
 * 实现了WorkManager<br/>
 * 管理HibernateSession
 * 当Hibernate配置属性current_session_context_class为managed有效
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class HibernateWorkManager implements WorkManager {



	public void beginWork() {
		if (ManagedSessionContext.hasBind(SessionFactoryHolder
				.getCurrentSessionFactory()))
			return;
		Session session = SessionFactoryHolder.getCurrentSessionFactory()
				.openSession();

		ManagedSessionContext.bind(session);
	}

	public void endWork() {
		SessionFactory sessionFactory = SessionFactoryHolder
				.getCurrentSessionFactory();
		if (!ManagedSessionContext.hasBind(sessionFactory))
			return;
		try {
			Session session = sessionFactory.getCurrentSession();

			session.close();
		} finally {
			ManagedSessionContext.unbind(sessionFactory);
		}
	}

}
