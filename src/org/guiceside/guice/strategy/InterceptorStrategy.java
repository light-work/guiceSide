package org.guiceside.guice.strategy;

import com.google.inject.matcher.Matcher;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;

/**
 * <p>
 *  拦截机匹配策略接口<br/>
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public interface InterceptorStrategy {
	Matcher<? super Class<?>> getClassMatcher();

	Matcher<? super Method> getMethodMatcher();

	MethodInterceptor getMethodInterceptor();
}
