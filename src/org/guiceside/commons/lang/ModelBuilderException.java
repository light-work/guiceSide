package org.guiceside.commons.lang;
/**
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 **/
public class ModelBuilderException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModelBuilderException() {
		/* empty */
	}

	public ModelBuilderException(String message) {
		super(message);
	}

	public ModelBuilderException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	public ModelBuilderException(Throwable rootCause) {
		super(rootCause);
	}

	public Throwable getRootCause() {
		return getCause();
	}
}
