package org.guiceside.commons;

import java.io.Serializable;

/**
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 **/
public class GlobalExceptionMapping implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4912230583234637505L;

	private Class<? super Exception> exception;
	
	private String result;

	public Class<? super Exception> getException() {
		return exception;
	}

	public void setException(Class<? super Exception> exception) {
		this.exception = exception;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
