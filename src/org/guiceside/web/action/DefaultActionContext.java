package org.guiceside.web.action;

import java.util.Map;

/**
 * <p>
 * ActionContext默认实现<br/>
 * 提供ActionContext的获取方法
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class DefaultActionContext implements ActionContext {
	private static final ThreadLocal<Map<String, Object>> actionContext = new ThreadLocal<Map<String, Object>>();

	/**
	 * 构造ActionContext
	 * @param context
	 */
	public DefaultActionContext(Map<String, Object> context) {
		actionContext.set(context);
	}

	/**
	 * 
	 * 返回ActionContext
	 * 
	 * @return 以Map形式的ActionContext
	 */
	public Map<String, Object> getActionContext() {
		return actionContext.get();
	}

	/**
	 * 清除ActionContext
	 * 
	 */
	public void clear() {
		actionContext.remove();
	}
}
