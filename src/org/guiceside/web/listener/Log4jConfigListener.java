package org.guiceside.web.listener;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.guiceside.support.properties.PropertiesConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-11
 * 
 */
public class Log4jConfigListener implements ServletContextListener {
	private static final Logger log=Logger.getLogger(Log4jConfigListener.class);
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			PropertiesConfig propertiesConfig= new PropertiesConfig("log4j.properties");	
			
			PropertyConfigurator.configure(propertiesConfig.getProperties());// 装入log4j配置信息
			if (log.isDebugEnabled()) {
				log.debug("Initialization Log4j done");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
