package org.guiceside.web.action;

import java.util.Map;

/**
 * <p>
 * Action上下文接口 提供一个Map<String,Object><br/>
 * 存放ActionMapping,HttpServletRequest,HttpServletResponse,HttpSession,ServletContext<br/>
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>

 * @since JDK1.5
 * @version 1.0 $Date:200808
 * @see DefaultActionContext
 * 
 */
public interface ActionContext {
	public static final String ACTIONMAPPING = "org.guiceside.web.dispatcher.mapper.ActionMapping";

	public static final String HTTPSERVLETREQUEST = "javax.servlet.http.HttpServletRequest";

	public static final String HTTPSERVLETRESPONSE = "javax.servlet.http.HttpServletResponse";

	public static final String SERVLETCONTEXT = "javax.servlet.ServletContext";

    public static final String REQUESTDATA = "org.guiceside.commons.collection.RequestData";
	
	public Map<String, Object> getActionContext();

	public void clear();
}
