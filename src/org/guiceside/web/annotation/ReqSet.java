package org.guiceside.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * request.setAttribute快截注解<br/>
 * value()属性默认为""时以当前显示声明@ReqSet的Filed的名称作为setAttribute key<br/>
 * value()属性不为""时,以value值作为setAttribute key
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * @see org.guiceside.web.interceptor.ReqSetAttrInterceptor
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReqSet {
	/**
	 * 指定setAttribute key
	 * @return 返回指定key
	 */
	String value() default "";
}
