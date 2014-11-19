package org.guiceside.persistence.hibernate.dao.interceptor;

import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.guiceside.guice.strategy.AbstractInterceptorStrategy;
import org.guiceside.persistence.hibernate.dao.annotation.ThreadSafe;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-29
 * Time: 23:04:14
 * To change this template use File | Settings | File Templates.
 */
public class ThreadSafeInterceptorModule extends AbstractInterceptorStrategy {

    public Matcher<? super Class<?>> getClassMatcher() {

		return Matchers.any();
	}

	public MethodInterceptor getMethodInterceptor() {
		return new ThreadSafeInteceptor();
	}

	public Matcher<? super Method> getMethodMatcher() {
		return Matchers.annotatedWith(ThreadSafe.class);
	}
}
