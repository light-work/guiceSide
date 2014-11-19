package org.guiceside.persistence.hibernate.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;


/**
 * <p>
 * 标识使用ByHql的方式进行查询
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FinderByHql {
	/**
	 * 
	 * @return 返回当前执行的hql
	 */
	String query();

	/**
	 * 
	 * @return 返回当前方法的返回值类型
	 */
	Class<? extends Collection> returnAs() default ArrayList.class;
}
