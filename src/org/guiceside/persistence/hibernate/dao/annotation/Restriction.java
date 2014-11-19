package org.guiceside.persistence.hibernate.dao.annotation;

import org.guiceside.persistence.hibernate.dao.enums.Condition;
import org.guiceside.persistence.hibernate.dao.enums.Match;
import org.guiceside.persistence.hibernate.dao.enums.Relation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 在使用@FinderByCriteria查询时候在FinderByCriteria使用<br/>
 * 创建基本的约束条件,加至条件的最末尾<br/>
 * column:配置entity里需要进行约束的字段名称<br/>
 * value():column字段的约束值<br/>
 * valueType():value()值的类型，默认为String.class<br/>
 * value1():column字段的第二约束值,应用于Between条件<br/>
 * value1Type():value1()值的类型，默认为String.class<br/>
 * condition();约束条件<br/>
 * ignoreCase:配置是否忽略大小写 默认为false<br/> 
 * not:配置条件是否为反向默认为false<br/>
 * ignoreBank:配置是否忽略空对象或者空字符串 默认为true<br/>
 * reloation:配置当前约束是否存在和其他约束的连接约束关系 默认为Relation.NULL<br/>
 * or:如果当前约束reloation=Relation.OR,则配置or策略配置,须和reloation一起使用<br/>
 * and:如果当前约束reloation=Relation.AND,则配置and策略配置,须和reloation一起使用<br/>
 * matchMode:配置模糊匹配策略 默认为Match.ANYWHERE 应用于LIKE条件<br/>
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Restriction {
	/**
	 * 
	 * @return 返回需要进行约束条件的字段
	 */
	String column();

	/**
	 * 
	 * @return 返回约束条件值
	 */
	String value();

	
	/**
	 * 
	 * @return 返回约束条件值类型
	 */
	Class<? extends Object> valueType() default String.class;

	/**
	 * 
	 * @return 返回第二约束条件值 应用与Between
	 */
	String value1() default "";

	
	/**
	 * 
	 * @return 返回第二约束条件值类型 应用与Between
	 */
	Class<? extends Object> value1Type() default String.class;

	/**
	 * 
	 * @see Condition
	 * @return 返回约束条件类型
	 */
	Condition condition();

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
	 * @see org.guiceside.persistence.hibernate.dao.annotation.OR
	 * @return 返回OR策略
	 */
	OR or() default @OR(group = "");

	/**
	 * @see org.guiceside.persistence.hibernate.dao.annotation.AND
	 * @return 返回AND策略
	 */
	AND and() default @AND(group = "");
	
	/**
	 * @see Match
	 * @return 返回模糊匹配方式
	 */
	Match matchMode() default Match.ANYWHERE;
}
