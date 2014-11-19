package org.guiceside.web.dispatcher.mapper;


import com.google.inject.Injector;
import org.guiceside.config.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 定义ActionMapper接口
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 *
 */
public interface ActionMapper {
	/**
	 * 定义返回ActionMapping JavaBean方法
	 * @param httpServletRequest
	 * @param configuration
	 * @return 返回ActionMapping JavaBean
	 * @throws RuntimeException
	 */
	ActionMapping getMapping(HttpServletRequest httpServletRequest, Configuration configuration) throws RuntimeException;

    void createAction(ActionMapping actionMapping, Injector injector);
}
