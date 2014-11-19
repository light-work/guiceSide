package org.guiceside.persistence.hibernate;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.hibernate.SessionFactory;

/**
 * <p>
 * SessionFactory提供者<br.>
 * 由sessionFactoryHolder创建<br/>
 * 负责提供SessionFactory实例
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * 
 */
public class SessionFactoryProvider implements Provider<SessionFactory> {

	private final SessionFactoryHolder sessionFactoryHolder;

	@Inject
	public SessionFactoryProvider(SessionFactoryHolder sessionFactoryHolder) {
		this.sessionFactoryHolder = sessionFactoryHolder;

	}

	public SessionFactory get() {
		return this.sessionFactoryHolder.getSessionFactory();
	}

}
