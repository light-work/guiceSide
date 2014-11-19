package org.guiceside.web.jsp.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-2
 * 
 */
public class Time extends BaseTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
		outprint(String.valueOf(System.nanoTime()));
		return Tag.SKIP_BODY;
	}
}
