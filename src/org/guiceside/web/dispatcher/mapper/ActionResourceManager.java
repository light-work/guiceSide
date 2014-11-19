package org.guiceside.web.dispatcher.mapper;

import org.apache.log4j.Logger;
import org.guiceside.commons.Assert;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Action资源管理类
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 *
 */
public class ActionResourceManager {
	private static final  Map<String, Class<?>> actionMaps = new HashMap<String, Class<?>>();

	private static final  Set<String> actionUrls = new HashSet<String>();

	private static final  Set<Class<?>> actions = new HashSet<Class<?>>();

	private static final Logger log=Logger.getLogger(ActionResourceManager.class);
	
	private ActionResourceManager() {

	}

	/**
	 * 
	 * 载入Action资源
	 * 
	 * @param name
	 * @param actionClas
	 * @throws org.guiceside.web.dispatcher.mapper.ActionResourceException
	 */
	public static void putActionMap(String name, Class<?> actionClas)
			throws ActionResourceException {
		actionMaps.put(name, actionClas);
	}

	/**
	 * 
	 * 获取Action
	 * 

	 * @param name
	 * @return 返回ActionClass
	 */
	public static Class<?> getAction(String name) {
		return actionMaps.get(name);
	}

	/**
	 * 
	 * 通过namespace,name查找Action<br/>
	 * 将验证通过的action载入
	 * 
	 * @param nameSpace
	 * @param actionName
	 * @param actionClas
	 * @throws org.guiceside.web.dispatcher.mapper.ActionResourceException
	 */
	public static void put(String nameSpace, String actionName,
			Class<?> actionClas) throws ActionResourceException {
		Assert.isNotBlank(nameSpace);
		Assert.isNotBlank(actionName);
		Assert.notNull(actionClas);
		if (actionUrls.contains(nameSpace + "/" + actionName)) {
			log.error("Repeat the definition",new ActionResourceException("url:" + nameSpace + "/" + actionName
					+ "Repeat the definition"));
			throw new ActionResourceException("url:" + nameSpace + "/" + actionName
					+ "Repeat the definition");
		}
		if (actions.contains(actionClas)) {
			log.error("Repeat the definition",new ActionResourceException("Action:" + actionClas.getName()
					+ "Repeat the definition"));
			throw new ActionResourceException("Action:" + actionClas.getName()
					+ "Repeat the definition");
		}
		putActionMap(nameSpace + "/" + actionName, actionClas);
		actionUrls.add(nameSpace + "/" + actionName);
		actions.add(actionClas);

	}

	/**
	 * 
	 * 获取Action资源Map
	 * @return 返回Map结构Action资源
	 */
	public static Map<String, Class<?>> getActionMaps() {
		return actionMaps;
	}

	/**
	 * 
	 * 清空Action资源
	 * 
	 *
	 */
	public static void clear() {
		actionUrls.clear();
		actions.clear();
		actionMaps.clear();
	}
}
