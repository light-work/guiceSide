package org.guiceside.web.exception;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 12-3-6
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class SessionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SessionException() {
        super();
    }

    public SessionException(String message) {
        super(message);
    }
}
