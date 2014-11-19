package org.guiceside.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 描述Action组件注解<br/>
 * 以name和namespace两个属性描述Action访问路径
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
	/**
	 * 指定当前Action访问name
	 * @return 返回name属性值
	 */
	String name() ;

	/**
	 * 指定当前Action访问namesapce
	 * @return 返回namespace属性值
	 */
	String namespace() ;
}
