package org.guiceside.web.view.velocity;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.guiceside.GuiceSideConstants;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.config.Configuration;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.listener.DefaultGuiceSideListener;
import org.guiceside.web.view.JspSupportServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-4-4
 * Time: 22:22:41
 * To change this template use File | Settings | File Templates.
 */
public class VelocityResult {
    
    private static final Logger log = Logger.getLogger(VelocityResult.class);
    private ActionContext actionContext;
    private Map<String, Object> actionContextMap;
    private boolean bufferOutput = false;
    private String location;

    public void doExecute(String location, ActionContext actionContext) throws IOException, Exception {


        this.location = location;
        this.actionContext = actionContext;
        actionContextMap = this.actionContext.getActionContext();
        JspFactory jspFactory = null;
        Servlet servlet = JspSupportServlet.jspSupportServlet;
        ServletContext servletContext = (ServletContext) this.actionContextMap.get(ActionContext.SERVLETCONTEXT);
        HttpServletRequest request = (HttpServletRequest) this.actionContextMap.get(ActionContext.HTTPSERVLETREQUEST);
        HttpServletResponse response = (HttpServletResponse) this.actionContextMap.get(ActionContext.HTTPSERVLETRESPONSE);
        HttpSession httpSession = request.getSession(false);
        Configuration configuration = (Configuration) servletContext.getAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);


        if (configuration == null) {
            log.error("Configuration not found", new UnavailableException(
                    "Configuration not found (did you forget to register a "
                            + DefaultGuiceSideListener.class.getSimpleName()
                            + "?)"));
            throw new UnavailableException(
                    "Configuration not found (did you forget to register a "
                            + DefaultGuiceSideListener.class.getSimpleName()
                            + "?)");
        }

        VelocityManager.getInstance().init(servletContext, configuration);
        PageContext pageContext = null;
        boolean usedJspFactory = false;
        if (pageContext == null && servlet != null) {
            jspFactory = JspFactory.getDefaultFactory();
            pageContext = jspFactory.getPageContext(servlet, request, response, null, true, 8192, true);
            usedJspFactory = true;
        }

        String encoding = configuration.getVelocityEncoding();
        String contentType = configuration.getVelocityContentType();
        if (StringUtils.isBlank(encoding)) {
            encoding = VelocityManager.DEFAULT_ENCODING;
        }
        if (StringUtils.isBlank(contentType)) {
            contentType = VelocityManager.DEFAULT_CONTENT_TYPE;
        }
        if (encoding != null) {
            contentType = contentType + ";charset=" + encoding;
        }
        VelocityManager velocityManager = VelocityManager.getInstance();
        Template template = getTemplate(velocityManager.getVelocityEngine(), location, configuration.getVelocityEncoding());
        Context context = createContext(velocityManager, request, response, httpSession);
        Writer writer = new OutputStreamWriter(response.getOutputStream(), encoding);
        response.setContentType(contentType);
        template.merge(context, writer);
        writer.flush();
        if (usedJspFactory) {
            jspFactory.releasePageContext(pageContext);
        }
    }

    protected Template getTemplate(VelocityEngine velocity, String location, String encoding) throws Exception {
        Template template = velocity.getTemplate(location, encoding);
        return template;
    }

    protected Context createContext(VelocityManager velocityManager, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        return velocityManager.createContext(request, response, httpSession);
    }
}
