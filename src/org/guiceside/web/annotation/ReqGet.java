package org.guiceside.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 参数自动获取注解<br/>
 * 将参数获取自动类型转换<br/>
 * value()参数默认为""时,表示参数key为 显示声明@ReqGet的Filed的名称
 * value()参数不为""时,使用value值作为参数key
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * @see org.guiceside.web.interceptor.ParamsInterceptor

 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReqGet {
	/**
	 * 指定参数key
	 * @return 返回指定key
	 */
	String value() default "";
}
