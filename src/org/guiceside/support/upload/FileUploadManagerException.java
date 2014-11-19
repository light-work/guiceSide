package org.guiceside.support.upload;

/**
 * 
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-11
 *
 */
public class FileUploadManagerException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileUploadManagerException() {
		super();
	}
	
	public FileUploadManagerException(String arg0) {
		super(arg0);
		
	}

	public FileUploadManagerException(Throwable arg0) {
		super(arg0);
		
	}

	public FileUploadManagerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}
	
	public void printStackTrace() {
		printStackTrace(System.err);
	}
}
