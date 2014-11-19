package org.guiceside.persistence.hibernate;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.hibernate.SessionFactory;

/**
 * <p>
 * Session提供者<br.>
 * 由sessionFactory获得当前Session<br/>
 * 负责提供Session实例
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */

public class SessionUtilsProvider implements Provider<SessionUtils> {

    private final SessionFactory sessionFactory;
    private  static SessionUtils sessionUtils;
	@Inject
	public SessionUtilsProvider(SessionFactory sessionFactory) {
        System.out.println(this.toString()+" init");
        this.sessionFactory=sessionFactory;
	}

    public synchronized  SessionUtils getSessionUtils(){
        if(sessionUtils!=null){
            return sessionUtils;
        }
        sessionUtils=new SessionUtils(sessionFactory);
        return sessionUtils;
    }
    
	public SessionUtils get() {
        return getSessionUtils();
	}
}