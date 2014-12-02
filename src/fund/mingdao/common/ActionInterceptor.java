package fund.mingdao.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.dispatcher.mapper.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-11-6
 * @since JDK1.5
 */
public class ActionInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;

        Object[] args = invocation.getArguments();

        ActionContext actionContext = (ActionContext) args[0];
        ActionMapping actionMapping = (ActionMapping) actionContext
                .getActionContext().get(ActionContext.ACTIONMAPPING);
        HttpServletRequest httpServletRequest = (HttpServletRequest) actionContext.getActionContext().get(ActionContext.HTTPSERVLETREQUEST);
        HttpServletResponse httpServletResponse = (HttpServletResponse) actionContext.getActionContext().get(ActionContext.HTTPSERVLETRESPONSE);
        RequestData requestData = (RequestData) actionContext.getActionContext().get(ActionContext.REQUESTDATA);
        if (actionMapping.getNamespace().equals("/common")) {
            if (actionMapping.getName().equals("authorize")) {
                if (actionMapping.getMethodName().equals("execute") || actionMapping.getMethodName().equals("h5")) {
                    result = invocation.proceed();
                    return result;
                }
            }
            if (actionMapping.getName().equals("event")) {
                result = invocation.proceed();
                return result;
            }
        }
        return result;
    }
}