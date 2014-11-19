package org.guiceside.persistence.hibernate.dao.annotation;

import org.guiceside.persistence.hibernate.dao.enums.Relation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 在使用@FinderByCriteria查询时候在方法中使用<br/>
 * 与EQ LT LE GE GT LIKE BETWEEN联合使用<br/>
 * 定义需要进行[并且]关联的约束策略<br/>
 * group:设置约束所在组<br/> joinGroup:设置约束连接组 默认为""<br/>
 * joinRelation:设置连接组关系 默认为Relation.NULL<br/>
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AND {
	/**
	 * 
	 * @return 返回当前组名
	 */
	String group();

	/**
	 * 
	 * @return 返回需要连接至组名
	 */
	String joinGroup() default "";

	/**
	 * @see Relation
	 * @return 返回需要连接至组的连接关系
	 */
	Relation joinRelation() default Relation.NULL;
}
