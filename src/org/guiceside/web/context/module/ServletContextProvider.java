package org.guiceside.web.context.module;

import com.google.inject.Provider;
import org.guiceside.web.dispatcher.GuiceSideFilter;

import javax.servlet.ServletContext;

/**
 * <p>
 * ServletContext提供者
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class ServletContextProvider implements Provider<ServletContext> {

	/**
	 * 返回ServletContext
	 * @return ServletContext
	 */
	public ServletContext get() {
		return GuiceSideFilter.getServletContext();
	}

}
