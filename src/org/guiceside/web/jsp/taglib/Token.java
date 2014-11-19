package org.guiceside.web.jsp.taglib;


import org.guiceside.GuiceSideConstants;
import org.guiceside.commons.TokenUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;


/**
 User:zhenjia(zhenjiaWang@gmail.com)
 Date:2008-8-6
 Time:下午03:11:58
 **/
public class Token extends BaseTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4665042074616342555L;
	@Override
	public int doStartTag() throws JspException {
		String tokenId=TokenUtils.getToken(getHttpSession().getId());
		getHttpSession().setAttribute(GuiceSideConstants.GUICE_SIDE_TOKEN, tokenId);
		String tokenInput="<input type=\"hidden\" name=\""+GuiceSideConstants.GUICE_SIDE_TOKEN+"\" id=\""+GuiceSideConstants.GUICE_SIDE_TOKEN+"\" value=\""+tokenId+"\" />";
		outprint(tokenInput);
		return Tag.SKIP_BODY;
	}
	
}
