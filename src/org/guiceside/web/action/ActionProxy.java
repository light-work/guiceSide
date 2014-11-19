package org.guiceside.web.action;

import org.guiceside.web.annotation.ActionInterceptor;
import org.guiceside.web.annotation.BindingGuice;
import org.guiceside.web.dispatcher.mapper.ActionMapping;

import java.lang.reflect.Method;

/**
 * <p>
 * Action接口默认实现,作为Action动态代理类<br/>
 * 所有Action级别Interceptor的横切点<br/>
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * 
 */
@BindingGuice
public class ActionProxy implements Action {
	
	private static final Object[] COMMAND_METHOD_PARAM_VALUE = new Object[] {};

	public ActionProxy() {

	}

	/**
	 * 实现了Action接口的execute方法<br/>
	 * 通过actionContext统一接收用户请求<br/>
	 * 并分发到各Action组件进行处理<br/>
	 * 
	 * @param actionContext
	 * @return Action执行结果
	 * @see org.guiceside.web.dispatcher.mapper.ActionMapping
	 * @see org.guiceside.web.dispatcher.DispatcherUtils#execute(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ServletContext servletContext, ActionMapping actionMapping,
			Injector injector)
	 */
	@ActionInterceptor
	public Object execute(ActionContext actionContext) throws Exception {
		ActionMapping actionMapping = (ActionMapping) actionContext
				.getActionContext().get(ActionContext.ACTIONMAPPING);
		Object proxy = actionMapping.getActionObject();
		Method method = actionMapping.getMethod();
		Object result = method.invoke(proxy, COMMAND_METHOD_PARAM_VALUE);
		return result;
	}

}
