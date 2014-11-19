package org.guiceside.persistence.hibernate.dao;

import com.google.inject.Inject;
import org.guiceside.commons.Assert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;

/**
 * <p>
 * 常用普通dao基类 <br/>
 * 繁衍Dao需继承此类获取session实例<br/>
 * 支持子类可定义为Singleton<br/>
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808 
 */

public class HibernateDaoSupport {

	@Inject
	SessionFactory sessionFactory;

	/**
	 * 
	 * @return 返回当前session
	 */
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 
	 * @param clazz
	 * @return 返回PO的ID属性名称
	 */
	protected String getIdName(Class clazz) {
		Assert.notNull(clazz);
		ClassMetadata meta = sessionFactory.getClassMetadata(clazz);
		return meta.getIdentifierPropertyName();
	}
}
