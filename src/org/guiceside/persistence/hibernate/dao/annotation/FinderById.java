package org.guiceside.persistence.hibernate.dao.annotation;

import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.hibernate.dao.enums.FinderType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 标识使用ById的方式进行查询
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FinderById {
	/**
	 * 
	 * @return 返回当前查询class
	 */
	Class<? extends IdEntity> entity();

	/**
	 * @see FinderType
	 * @return 返回当前查询方式 getOrLoad
	 */
	FinderType type() default FinderType.GET;
}
