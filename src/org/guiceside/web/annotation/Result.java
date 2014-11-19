package org.guiceside.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * 定义跳转契约注解<br/>
 * 显示声明在@PageFlow内部<br/>
 * type 为跳转机制:Dispatcher.Forward、Dispatcher.Redirect<br/>
 * name 为Action组件方法返回的结果<br/>
 * path 为Action组件方法返回以后转至的路径地址<br/>
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * @see org.guiceside.web.interceptor.ResultInterceptor
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Result {
	/**
	 * 
	 * @return 返回跳转机制
	 */
	Dispatcher type() default Dispatcher.Forward;

	/**
	 * 
	 * @return 返回Action result结果
	 */
	String name() ;

	/**
	 * 
	 * @return 返回跳转path
	 */
	String path() ;
}
