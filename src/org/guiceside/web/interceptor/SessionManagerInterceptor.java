package org.guiceside.web.interceptor;

import com.google.inject.Injector;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.guiceside.persistence.WorkManager;
import org.guiceside.web.action.ActionContext;

import javax.servlet.ServletContext;

/**<p>
 * 统计Action执行时间
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-18
 *
 **/
public class SessionManagerInterceptor implements MethodInterceptor{

	private static final Logger log=Logger.getLogger(SessionManagerInterceptor.class);

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
        ActionContext actionContext = (ActionContext) args[0];

		ServletContext servletContext = (ServletContext) actionContext
					.getActionContext().get(ActionContext.SERVLETCONTEXT);
        Injector injector= (Injector) servletContext.getAttribute(Injector.class.getName());
        injector.getInstance(WorkManager.class).beginWork();
        Object result=invocation.proceed();
        injector.getInstance(WorkManager.class).endWork();
		return result;
	}

}