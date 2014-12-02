package fund.mingdao.common;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-10-30
 * @since JDK1.5
 */
public class UserSession implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public final static String SESSION_NAME = "userSession";


    public static UserInfo create(HttpServletRequest req) throws UserSessionException {

        HttpSession session = req.getSession(false);
        UserInfo userInfo = null;
        if (session == null) {
            userInfo = new UserInfo();
            session = req.getSession();
            userInfo.setSessionId(session.getId());
            session.setAttribute(UserSession.SESSION_NAME, userInfo);
            session.setMaxInactiveInterval(60 * 30);
        } else {
            userInfo = new UserInfo();
            userInfo.setSessionId(session.getId());
            session.setAttribute(UserSession.SESSION_NAME, userInfo);
            session.setMaxInactiveInterval(60 * 30);
        }
        return userInfo;
    }

    public static UserInfo getUserInfo(HttpServletRequest req) throws UserSessionException {

        HttpSession session = req.getSession(false);
        UserInfo userInfo = (UserInfo) session.getAttribute(SESSION_NAME);
        if (userInfo == null) {
            throw new UserSessionException();
        }
        return userInfo;
    }




    public static boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null
                && session.getAttribute(SESSION_NAME) != null
                && ((UserInfo) session.getAttribute(SESSION_NAME)).isLoggedIn())
            return true;
        else
            return false;
    }

    public static boolean isAuthorize(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null
                && session.getAttribute(SESSION_NAME) != null
                && ((UserInfo) session.getAttribute(SESSION_NAME)).isAuthorize())
            return true;
        else
            return false;
    }



    public static void removeAttribute(HttpServletRequest req, String key) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    public static Object getAttribute(HttpServletRequest req, String key) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            return session.getAttribute(key);
        }
        return null;
    }

    public static void setAttribute(HttpServletRequest req, String key, Object obj) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.setAttribute(key, obj);
        }
    }



    public static void invalidate(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
            session = null;
        }
    }
}