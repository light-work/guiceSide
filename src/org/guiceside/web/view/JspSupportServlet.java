package org.guiceside.web.view;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-28
 * Time: 0:04:14
 * To change this template use File | Settings | File Templates.
 */
public class JspSupportServlet extends HttpServlet{

    private static final long serialVersionUID = -8268185076433481017L;

	public static JspSupportServlet jspSupportServlet;

	
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        jspSupportServlet = this;
    }
}
