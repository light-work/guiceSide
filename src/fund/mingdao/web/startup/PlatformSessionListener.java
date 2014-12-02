package fund.mingdao.web.startup;


import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2009-1-18
 * @since JDK1.5
 */
public class PlatformSessionListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent arg0) {
    }

    public void sessionDestroyed(HttpSessionEvent arg0) {
        synchronized (this) {

        }

    }

}