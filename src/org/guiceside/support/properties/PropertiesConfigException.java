package org.guiceside.support.properties;
/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-11
 *
 **/
public class PropertiesConfigException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PropertiesConfigException() {
		/* empty */
	}

	public PropertiesConfigException(String message) {
		super(message);
	}

	public PropertiesConfigException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	public PropertiesConfigException(Throwable rootCause) {
		super(rootCause);
	}

	public Throwable getRootCause() {
		return getCause();
	}
}
