package org.guiceside.persistence.hibernate.dao;

/**
 * User:zhenjia Date:2008-8-18 Time:下午10:05:16 Email:(zhenjiaWang@gmail.com)
 * QQ:(119582291)
 */
public class HibernateDataAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HibernateDataAccessException() {
		/* empty */
	}

	public HibernateDataAccessException(String message) {
		super(message);
	}

	public HibernateDataAccessException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

	public HibernateDataAccessException(Throwable rootCause) {
		super(rootCause);
	}

	public Throwable getRootCause() {
		return getCause();
	}
}
