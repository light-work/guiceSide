package org.guiceside.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 数组驱动注解,与@ReqGet配合使用,可将参数自动封装为相应数组<br/>
 * 如果参数里没有任何一个属性匹配该数组对象则返回null<br>
 * 如果参数里存在匹配属性将自动装备到该数组对象,并且自动进行类型转换<br/> 
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * @see org.guiceside.web.interceptor.ParamsInterceptor
 */
@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayDriver {

}
