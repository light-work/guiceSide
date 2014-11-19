package org.guiceside.web.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.guiceside.commons.TimeUtils;

/**<p>
 * 统计Action执行时间
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-18
 *
 **/
public class TimeDifferenceInterceptor implements MethodInterceptor{

	private static final Logger log=Logger.getLogger(TimeDifferenceInterceptor.class);
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long tStart = System.currentTimeMillis();
		Object result=invocation.proceed();
		long tEnd = System.currentTimeMillis();
		if(log.isDebugEnabled()){
			log.debug("Finished! time "+TimeUtils.getTimeDiff(tStart, tEnd));
		}
		return result;
	}

}
