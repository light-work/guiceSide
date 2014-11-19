package org.guiceside.persistence.hibernate.dao.hquery;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-4-1
 * Time: 11:01:30
 * To change this template use File | Settings | File Templates.
 */
public class HQueryException extends RuntimeException{
    public HQueryException() {
		/* empty */
	}

	public HQueryException(String message) {
		super(message);
	}

	public HQueryException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	public HQueryException(Throwable rootCause) {
		super(rootCause);
	}

	public Throwable getRootCause() {
		return getCause();
	}
}
