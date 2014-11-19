package org.guiceside.persistence.hibernate;

import com.google.inject.Binder;
import com.google.inject.Singleton;
import org.guiceside.persistence.PersistenceService;
import org.guiceside.persistence.WorkManager;
import org.hibernate.SessionFactory;

/**
 * <p>
 * 
 * 显示声明SessionFactory、Session<br/>
 * PersistenceService、WorkManager构造策略<br/>
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class HibernateBindingSupport {
	/**
	 * Binding SessionFactory,Session,PersistenceService,WorkManager
	 * 
	 * @param binder
	 */
	public static void addBindings(Binder binder) {
		binder.bind(SessionFactoryHolder.class).in(Singleton.class);
		binder.bind(SessionFactory.class).toProvider(
				SessionFactoryProvider.class);
		//binder.bind(Session.class).toProvider(SessionProvider.class);
		binder.bind(PersistenceService.class).to(
				HibernatePersistenceService.class).in(Singleton.class);
		binder.bind(WorkManager.class).to(HibernateWorkManager.class);
       // binder.bind(SessionUtils.class).toProvider(SessionUtilsProvider.class).in(Singleton.class);
	}
}
