package org.guiceside.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 模型驱动注解,与@ReqGet配合使用,可将参数自动封装为JavaBean<br/>
 * 如果参数里没有任何一个属性匹配该模型对象则返回null<br>
 * 如果参数里存在匹配属性将自动装备到该模型对象,并且自动进行类型转换<br/> 
 * value()参数默认为""时,表示参数key为 显示声明@ReqGet @ModelDriver 的Filed的名称<br/>
 * value()参数不为""时,使用value值作为参数key
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * @see org.guiceside.web.interceptor.ParamsInterceptor
 */
@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelDriver {
	/**
	 * 指定参数key
	 * @return 返回指定key
	 */
	String value() default "";
}
