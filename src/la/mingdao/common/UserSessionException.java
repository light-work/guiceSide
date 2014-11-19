package la.mingdao.common;

import org.guiceside.web.exception.SessionException;

public class UserSessionException extends SessionException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UserSessionException() {
        super();
    }

    public UserSessionException(String message) {
        super(message);
    }
}