package org.guiceside.web.interceptor;

import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.guiceside.guice.strategy.AbstractInterceptorStrategy;
import org.guiceside.web.annotation.ActionInterceptor;
import org.guiceside.web.annotation.BindingGuice;

import java.lang.reflect.Method;

/**
 * <p>
 * ReqSetAttrInterceptor 拦截策略定义
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808

 *
 */
public class ReqSetAttrInterceptorModule extends AbstractInterceptorStrategy{

	public Matcher<? super Class<?>> getClassMatcher() {
		
		return Matchers.annotatedWith(BindingGuice.class);
	}

	public MethodInterceptor getMethodInterceptor() {
		
		return new ReqSetAttrInterceptor();
	}

	public Matcher<? super Method> getMethodMatcher() {

		return Matchers.annotatedWith(ActionInterceptor.class);
	}

}
