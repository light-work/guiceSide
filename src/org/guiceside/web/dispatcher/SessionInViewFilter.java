package org.guiceside.web.dispatcher;

import com.google.inject.Injector;
import org.apache.log4j.Logger;
import org.guiceside.persistence.WorkManager;
import org.guiceside.web.listener.DefaultGuiceSideListener;

import javax.servlet.*;
import java.io.IOException;

/**
 * <p>
 * 实现Hibernate延迟加载Filter
 * Hibernate Session对象统一由这里进行管理
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * @see org.guiceside.persistence.WorkManager
 */
public class SessionInViewFilter implements Filter {
	
	private static final  Logger log=Logger.getLogger(SessionInViewFilter.class);
	
	protected FilterConfig filterConfig;

	public void destroy() {

	}

	/**
	 * 统一管理session
	 * @param request
	 * @param response
	 * @param chain
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		ServletContext servletContext = filterConfig.getServletContext();
		Injector injector = (Injector) servletContext
				.getAttribute(Injector.class.getName());
		if (injector == null) {
			log.error("Guice Injector not found",new UnavailableException(
					"Guice Injector not found (did you forget to register a "
							+ DefaultGuiceSideListener.class.getSimpleName()
							+ "?)"));
			
			throw new UnavailableException(
					"Guice Injector not found (did you forget to register a "
							+ DefaultGuiceSideListener.class.getSimpleName()
							+ "?)");
		}
		injector.getInstance(WorkManager.class).beginWork();
		if(log.isDebugEnabled()){
			log.debug("current session open!");
		}
		try {
			chain.doFilter(request, response);
		} finally {
            
			if(log.isDebugEnabled()){
				log.debug("current session close!");
			}
            injector.getInstance(WorkManager.class).endWork();
            //SessionUtilsHolder.getCurrentSessionUtils().releaseSession();
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = arg0;
	}

}
