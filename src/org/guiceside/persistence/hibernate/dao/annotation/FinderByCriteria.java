package org.guiceside.persistence.hibernate.dao.annotation;

import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.hibernate.dao.enums.Condition;
import org.guiceside.persistence.hibernate.dao.enums.ProjectionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * 标识使用ByCriteria的方式进行查询
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FinderByCriteria {
	/**
	 * 
	 * @return 返回当前查询class
	 */
	Class<? extends IdEntity> entity();

	/**
	 *  
	 * @return 返回当前指定的约束条件数组
	 */
	Restriction[] restriction() default @Restriction(column = "", value = "", condition = Condition.NULL);

	/**
	 * 
	 * @return 返回当前指定的投影数组
	 */
	Projection[] projection() default @Projection(column="",type=ProjectionType.NULL);
	
	/**
	 * 
	 * @return 返回当前指定的排序数组
	 */
	OrderBy[] orderBy() default @OrderBy(column = "");

	/**
	 * 
	 * @return 返回当前需要创建别名数组
	 */
	Alias[] alias() default @Alias(alias = "", column = "");

	/**
	 * 
	 * @return 返回当前方法的返回值类型
	 */
	Class<? extends Collection> returnAs() default ArrayList.class;
}
