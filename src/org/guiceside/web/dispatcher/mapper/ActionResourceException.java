package org.guiceside.web.dispatcher.mapper;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808

 *
 */
public class ActionResourceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActionResourceException() {
		/* empty */
	}

	public ActionResourceException(String message) {
		super(message);
	}

	public ActionResourceException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	public ActionResourceException(Throwable rootCause) {
		super(rootCause);
	}

	public Throwable getRootCause() {
		return getCause();
	}
}
