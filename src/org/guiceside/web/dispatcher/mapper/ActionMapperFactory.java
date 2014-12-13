package org.guiceside.web.dispatcher.mapper;

import org.guiceside.GuiceSideConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * ActionMapper工厂类<br/>
 * 默认以DefaultActionMapper为实现
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 *
 */
public class ActionMapperFactory {
	 private static final Map<String, ActionMapper> mapperMap=new ConcurrentHashMap<String, ActionMapper>();
	 
	 /**
	  * 
	  * 返回ActionMapper实现类
	  * 
	  * @return 返回DefaultActionMapper
	  */
	 public static ActionMapper getActionMapper(){
         synchronized (mapperMap){
             ActionMapper actionMapper=mapperMap.get(GuiceSideConstants.GUICE_SIDE_ACTION_MAPPER);
             if (actionMapper != null) {
                 return actionMapper;
             }
             actionMapper = new DefaultActionMapper();
             mapperMap.put(GuiceSideConstants.GUICE_SIDE_ACTION_MAPPER, actionMapper);
             return actionMapper;
         }
	 }
}
