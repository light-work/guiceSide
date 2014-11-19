package org.guiceside.web.jsp.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;




/**
 User:zhenjia(zhenjiaWang@gmail.com)
 Date:2008-8-6
 Time:下午03:14:06
 **/
public class BaseTag extends TagSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -211036794988629860L;

	protected HttpServletRequest getHttpServletRequest() {
		return (HttpServletRequest)pageContext.getRequest();
	}
	
	protected HttpSession getHttpSession(){
		return getHttpServletRequest().getSession();
	}
	
	protected void outprint(String content){
		JspWriter out = pageContext.getOut();
		try {
			out.print(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void outprintln(String content){
		JspWriter out = pageContext.getOut();
		try {
			out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
