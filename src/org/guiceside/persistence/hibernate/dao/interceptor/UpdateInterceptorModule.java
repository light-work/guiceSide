package org.guiceside.persistence.hibernate.dao.interceptor;

import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.guiceside.guice.strategy.AbstractInterceptorStrategy;
import org.guiceside.persistence.hibernate.dao.annotation.Update;

import java.lang.reflect.Method;



/**
 * <p>
 * 定义对@Update的拦截策略
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-8-29
 *
 **/
public class UpdateInterceptorModule extends AbstractInterceptorStrategy{

	public Matcher<? super Class<?>> getClassMatcher() {
		
		return Matchers.any();
	}

	public MethodInterceptor getMethodInterceptor() {
		return new UpdateInterceptor();
	}

	public Matcher<? super Method> getMethodMatcher() {
		
		return Matchers.annotatedWith(Update.class);
	}

	
	
}
