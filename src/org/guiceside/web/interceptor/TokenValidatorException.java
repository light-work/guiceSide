package org.guiceside.web.interceptor;



/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class TokenValidatorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenValidatorException() {
		/* empty */
	}

	public TokenValidatorException(String message) {
		super(message);
	}

	public TokenValidatorException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	public TokenValidatorException(Throwable rootCause) {
		super(rootCause);
	}

	public Throwable getRootCause() {
		return getCause();
	}
}
