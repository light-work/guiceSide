package org.guiceside.persistence.hibernate.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 在使用@FinderByCriteria查询时候在FinderByCriteria使用<br/>
 * 定义创建别名策略<br/>
 * column:为entity里需要进行创建别名字段<br/>
 * alias:为别名名称<br/>
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Alias {
	/**
	 * 
	 * @return 返回需要创建别名字段
	 */
	String column();
	
	/**
	 * 
	 * @return 返回需要创建别名名称 
	 */
	String alias();
}
