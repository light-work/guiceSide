package org.guiceside.persistence.hibernate.dao.interceptor;

import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.guiceside.guice.strategy.AbstractInterceptorStrategy;
import org.guiceside.persistence.hibernate.dao.annotation.FinderById;

import java.lang.reflect.Method;

/**
 User:zhenjia
 Date:2008-8-21
 Time:上午11:54:13
 Email:(zhenjiaWang@gmail.com)
 QQ:(119582291)
 **/
public class FinderByIdInterceptorModule extends AbstractInterceptorStrategy{

	public Matcher<? super Class<?>> getClassMatcher() {
	
		return Matchers.any();
	}

	public MethodInterceptor getMethodInterceptor() {
		return new FinderByIdInterceptor();
	}

	public Matcher<? super Method> getMethodMatcher() {
		return Matchers.annotatedWith(FinderById.class);
	}

}
