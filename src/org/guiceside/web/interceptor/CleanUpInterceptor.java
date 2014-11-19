package org.guiceside.web.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.guiceside.web.action.ActionContext;

/**
 * <p>
 * 负责清理当前上下文
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class CleanUpInterceptor implements MethodInterceptor {
	public static final Logger log = Logger.getLogger(CleanUpInterceptor.class);

	/**
	 * 
	 * 在Action执行结束以后清理当前Action上下文
	 * 
	 * @param invocation
	 * @return 返回action执行结果
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = null;
		try {
			result = invocation.proceed();
		} finally {
			ActionContext actionContext = (ActionContext) invocation.getArguments()[0];
			actionContext.clear();
			if (log.isDebugEnabled()) {
				log.debug("Current ThreadLocal ActionContext CleanUp Successful");
			}
		}
		return result;
	}

}
