package fund.mingdao.web.startup;


import com.google.inject.Injector;
import fund.mingdao.common.TradeYesterdayTask;
import org.apache.log4j.Logger;
import org.guiceside.commons.TimeUtils;
import org.guiceside.support.properties.PropertiesConfig;
import org.guiceside.web.listener.DefaultGuiceSideListener;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-12-1
 * @since JDK1.5
 */
public class PlatformLoader {
    private static final Logger log = Logger.getLogger(PlatformLoader.class);

    private ScheduledExecutorService scheduExec;

    public void init(ServletContext servletContext) throws Exception {
        long tStart = System.currentTimeMillis();
        long tEnd = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("PlatformLoader done! time:" + TimeUtils.getTimeDiff(tStart, tEnd));
        }
        PropertiesConfig webConfig = new PropertiesConfig("webconfig.properties");
        servletContext.setAttribute("webConfig", webConfig);
        Injector injector = (Injector) servletContext
                .getAttribute(Injector.class.getName());
        if (injector == null) {
            log.error("Guice Injector not found", new UnavailableException(
                    "Guice Injector not found (did you forget to register a "
                            + DefaultGuiceSideListener.class.getSimpleName()
                            + "?)"));
            throw new Exception(
                    "Guice Injector not found (did you forget to register a "
                            + DefaultGuiceSideListener.class.getSimpleName()
                            + "?)");
        }
        injector = (Injector) servletContext.getAttribute(Injector.class
                .getName());
        if (injector == null) {
            log.error("Guice Injector not found", new UnavailableException(
                    "Guice Injector not found (did you forget to register a "
                            + DefaultGuiceSideListener.class.getSimpleName()
                            + "?)"));
        }

        scheduExec = Executors
                .newScheduledThreadPool(1);
        TradeYesterdayTask tradeYesterdayTask=new TradeYesterdayTask(scheduExec,injector);
        tradeYesterdayTask.start();

    }


    public void destroyed(ServletContext servletContext) throws Exception {
        if(scheduExec!=null){
            System.out.println("shutdown");
            scheduExec.shutdown();
        }
    }
}