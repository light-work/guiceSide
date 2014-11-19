package org.guiceside.web.action;

import javax.servlet.ServletException;

/**
 * User:zhenjia(zhenjiaWang@gmail.com) Date:2008-8-6 Time:上午11:18:00
 * Email:(zhenjiaWang@gmail.com) QQ:(119582291)
 */
public class ActionExcetion extends ServletException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1550995613778558386L;

	public ActionExcetion() {
		/* empty */
	}

	public ActionExcetion(String message) {
		super(message);
	}

	public ActionExcetion(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	public ActionExcetion(Throwable rootCause) {
		super(rootCause);
	}

	public Throwable getRootCause() {
		return getCause();
	}
}
