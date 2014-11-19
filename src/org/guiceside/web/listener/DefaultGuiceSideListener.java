package org.guiceside.web.listener;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.apache.log4j.Logger;
import org.guiceside.GuiceSideConstants;
import org.guiceside.config.Configuration;
import org.guiceside.config.ConfigurationManager;
import org.guiceside.guice.module.ApplicationModule;
import org.guiceside.web.dispatcher.mapper.ActionResourceManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * <p>
 * 将此Listener(或子类)配置在web.xml中 加载guiceSide.xml,进行Guice的初始化
 * 才能在Application中通过Guice Inject 你的任何对像
 * </p>
 *
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 $Date:200808
 * @since JDK1.5
 */
public class DefaultGuiceSideListener extends GuiceServletContextListener {

    private static final Logger log = Logger.getLogger(DefaultGuiceSideListener.class);


    private ServletContext servletContext;

    /**
     * 加载guiceSide.xml
     *
     * @param servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.servletContext = servletContextEvent.getServletContext();
        ConfigurationManager.initialize(getConfigFileName());
        try {
            Configuration configuration = ConfigurationManager.getInstance().loadConfig();
            servletContext.setAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG, configuration);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Unable to load guiceSide.xml, file path:[" + getConfigFileName() + "] is wrong");
            }
            e.printStackTrace();
        }
        if (log.isDebugEnabled()) {
            log.debug("load guiceSide.xml done");
        }
        super.contextInitialized(servletContextEvent);

    }

    /**
     * 释放资源
     *
     * @param servletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ActionResourceManager.clear();
        servletContext.removeAttribute(Injector.class.getName());
        servletContext.removeAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);
        if (log.isDebugEnabled()) {
            log.debug("CleanUp ActionResourceManager Successful");
            log.debug("CleanUp Guice Injector Successful");
            log.debug("CleanUp Configuration Successful");
        }
        super.contextDestroyed(servletContextEvent);
    }

    protected String getConfigFileName() {
        return "guiceSide.xml";
    }

    /**
     * 初始化Google Injector
     *
     * @return injector
     */
    @Override
    protected Injector getInjector() {
        Injector injector = null;
        try {
            Configuration configuration = (Configuration) getServletContext().getAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);
            injector = Guice.createInjector(new ApplicationModule(configuration));
            if (log.isDebugEnabled()) {
                log.debug("Initialization  Guice Injector done");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return injector;
    }


    public ServletContext getServletContext() {
        return servletContext;
    }

}
