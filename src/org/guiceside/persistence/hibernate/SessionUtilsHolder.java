package org.guiceside.persistence.hibernate;

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
public class SessionUtilsHolder {

	private SessionUtils sessionUtils;

	private static SessionUtilsHolder sessionProviderHolder;

	public SessionUtilsHolder() {
		sessionProviderHolder = this;
	}

    SessionUtils getSessionUtils() {

		return sessionUtils;
	}

	synchronized void setSessionUtils(SessionUtils sessionUtils) {
		if (null != this.sessionUtils)
			throw new RuntimeException(
					"Duplicate session factory creation! Only one session factory is allowed per injector");
		this.sessionUtils = sessionUtils;
	}

	public static SessionUtils getCurrentSessionUtils() {
		return sessionProviderHolder.getSessionUtils();
	}

	
}