package org.guiceside.guice.strategy;



import com.google.inject.AbstractModule;

/**
 * <p>
 * 构造匹配条件拦截机<br/>
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * 
 */
public abstract class AbstractInterceptorStrategy extends AbstractModule
		implements InterceptorStrategy {
	@Override
	protected void configure() {
		bindInterceptor(getClassMatcher(), getMethodMatcher(),
				getMethodInterceptor());
	}
}
