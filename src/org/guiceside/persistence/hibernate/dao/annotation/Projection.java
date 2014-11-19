package org.guiceside.persistence.hibernate.dao.annotation;

import org.guiceside.persistence.hibernate.dao.enums.ProjectionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 在使用@FinderByCriteria查询时候在FinderByCriteria使用<br/>
 * 创建基本的投影<br/>
 * column:配置entity里需要进行投影的字段名称<br/>
 * value():column字段的投影类型<br/>
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-3
 *
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Projection {
	String column();
	
	ProjectionType type();
}
