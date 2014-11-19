package org.guiceside.persistence.hibernate.dao.annotation;

import org.guiceside.commons.UnknownType;
import org.guiceside.persistence.hibernate.dao.enums.Relation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 在使用@FinderByCriteria查询时候在方法中使用<br/>
 * 标识需要进行等于条件查询<br/>
 * column:配置entity里需要进行约束的字段名称<br/>
 * ignoreCase:配置是否忽略大小写 默认为false<br/> 
 * not:配置条件是否为反向默认为false<br/>
 * ignoreBank:配置是否忽略空对象或者空字符串 默认为true<br/>
 * reloation:配置当前约束是否存在和其他约束的连接约束关系 默认为Relation.NULL<br/>
 * or:如果当前约束reloation=Relation.OR,则配置or策略配置,须和reloation一起使用<br/>
 * and:如果当前约束reloation=Relation.AND,则配置and策略配置,须和reloation一起使用<br/>
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 *          
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface EQ {
	/**
	 * 
	 * @return 返回需要EQ约束的字段
	 */
	String column();

	/**
	 * 强制转换类型
	 * @return 强制转换类型
	 */
	Class<? extends Object> forciblyType() default UnknownType.class ;
	/**
	 * 
	 * @return 返回是否忽略大小写
	 */
	boolean ignoreCase() default false;

	/**
	 * 
	 * @return 返回是否为反向条件
	 */
	boolean not() default false;

	/**
	 * 
	 * @return 返回是否忽略空白字符串
	 */
	boolean ignoreBank() default true;

	/**
	 * @see Relation
	 * @return 返回连接关系
	 */
	Relation relation() default Relation.NULL;

	/**
	 * @see OR
	 * @return 返回OR策略
	 */
	OR or() default @OR(group = "");

	/**
	 * @see org.guiceside.persistence.hibernate.dao.annotation.AND
	 * @return 返回AND策略
	 */
	AND and() default @AND(group = "");
}
