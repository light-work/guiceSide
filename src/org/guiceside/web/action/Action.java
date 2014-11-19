package org.guiceside.web.action;

/**
 * <p>Action接口</p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * 
 */
public interface Action {
	/**
	 * Action接口执行方法
	 * @param actionContext
	 * @return Action执行结果
	 * @throws Exception
	 */
	public Object execute(ActionContext actionContext) throws Exception;
}
