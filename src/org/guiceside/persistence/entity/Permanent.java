package org.guiceside.persistence.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 对显示使用@Permanent的PO将永久保存，不会被真正删除<br/>
 * 在删除时候会根据column定义的字段自动更新为defaultValue的值<br/>
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permanent {
	/**
	 * 
	 * @return 返回当前需要更新字段
	 */
	String column() default "useYn";

	/**
	 * 
	 * @return 返回当前需要更新字段值
	 */
	String defaultValue() default "N";
}
