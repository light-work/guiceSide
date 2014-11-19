package org.guiceside.support.file;
/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-17
 *
 **/
public class FileManagerException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileManagerException() {
		super();
	}
	
	public FileManagerException(String arg0) {
		super(arg0);
		
	}

	public FileManagerException(Throwable arg0) {
		super(arg0);
		
	}

	public FileManagerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}
	
	public void printStackTrace() {
		printStackTrace(System.err);
	}
}
