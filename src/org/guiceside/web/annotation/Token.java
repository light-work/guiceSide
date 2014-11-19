package org.guiceside.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Token防重复提交注解<br/>
 * 显示声明在Action方法之上<br/>
 * 可验证当前方法是否重复提交<br/>
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * @see org.guiceside.web.interceptor.TokenValidatorInterceptor
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {

}
