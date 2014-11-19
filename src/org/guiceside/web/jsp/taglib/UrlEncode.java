package org.guiceside.web.jsp.taglib;

import org.guiceside.GuiceSideConstants;
import org.guiceside.config.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;


/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-2
 * 
 */
public class UrlEncode extends BaseTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String url;
	
	
	
	@Override
	public int doStartTag() throws JspException {
		ServletContext servletContext=pageContext.getServletContext();
		Configuration configuration=  (Configuration) servletContext.getAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);
		outprint(url);
		return Tag.SKIP_BODY;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
}
