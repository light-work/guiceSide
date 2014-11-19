package org.guiceside.web.context.module;

import com.google.inject.AbstractModule;

import javax.servlet.ServletContext;

/**
 * <p>
 * ServletContextModule 继承google guice AbstractModule<br/>
 * 显示定义ServletContext获取策略
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class ServletContextModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(ServletContext.class).toProvider(ServletContextProvider.class);
	}

}
