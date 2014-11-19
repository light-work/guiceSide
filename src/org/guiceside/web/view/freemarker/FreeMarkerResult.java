package org.guiceside.web.view.freemarker;

import freemarker.template.*;
import org.apache.log4j.Logger;
import org.guiceside.GuiceSideConstants;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.listener.DefaultGuiceSideListener;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-28
 * Time: 12:07:57
 * To change this template use File | Settings | File Templates.
 */
public class FreeMarkerResult {
    private static final Logger log = Logger.getLogger(FreeMarkerResult.class);
    private static final String SETTING_NO_CACHE = "true";

    private static final String SETTING_CONTENT_TYPE = "text/html";
    private ActionContext actionContext;
    private Map<String, Object> actionContextMap;
    private Configuration configuration;
    private ObjectWrapper wrapper;

    private boolean bufferOutput = false;
    private String location;

    public void doExecute(String location, ServletContext servletContext, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, StringBuilder sb) throws IOException, Exception {
        this.location = location;
        org.guiceside.config.Configuration guiceSideCfg = (org.guiceside.config.Configuration) servletContext.getAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);
        if (guiceSideCfg == null) {
            log.error("Configuration not found", new UnavailableException(
                    "Configuration not found (did you forget to register a "
                            + DefaultGuiceSideListener.class.getSimpleName()
                            + "?)"));
            throw new UnavailableException(
                    "Configuration not found (did you forget to register a "
                            + DefaultGuiceSideListener.class.getSimpleName()
                            + "?)");
        }

        this.configuration = (Configuration) servletContext.getAttribute(GuiceSideConstants.FREEMARKER_CONFIG);
        if (configuration == null) {
            configuration = getConfiguration(servletContext, guiceSideCfg);
        }
        this.wrapper = getObjectWrapper();
        Template template = this.configuration.getTemplate(this.location, this.configuration.getLocale());
        TemplateModel model = createModel(servletContext, httpServletRequest, httpServletResponse);
        if (preTemplateProcess(template, httpServletResponse, guiceSideCfg)) {
            try {
                // Process the template
                // First, get the writer
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(baos);
                    template.process(model, osw);
                    osw.flush();
                    osw.close();
                    sb.append(baos.toString());
                }
                catch (IllegalStateException ise) {
                    // Getting the writer failed, try using getOutputStream()
                    // This can happen on some application servers such as WebLogic 8.1
                }
            } finally {
                // Give subclasses a chance to hook into postprocessing
                postTemplateProcess(template, model);
            }
        }
    }

    public void doExecute(String location, ActionContext actionContext) throws IOException, Exception {
        this.location = location;
        this.actionContext = actionContext;
        actionContextMap = this.actionContext.getActionContext();
        ServletContext servletContext = (ServletContext) this.actionContextMap.get(ActionContext.SERVLETCONTEXT);
        org.guiceside.config.Configuration guiceSideCfg = (org.guiceside.config.Configuration) servletContext.getAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);
        if (guiceSideCfg == null) {
            log.error("Configuration not found", new UnavailableException(
                    "Configuration not found (did you forget to register a "
                            + DefaultGuiceSideListener.class.getSimpleName()
                            + "?)"));
            throw new UnavailableException(
                    "Configuration not found (did you forget to register a "
                            + DefaultGuiceSideListener.class.getSimpleName()
                            + "?)");
        }

        this.configuration = (Configuration) servletContext.getAttribute(GuiceSideConstants.FREEMARKER_CONFIG);
        if (configuration == null) {
            configuration = getConfiguration(servletContext, guiceSideCfg);
        }
        this.wrapper = getObjectWrapper();
        Template template = this.configuration.getTemplate(this.location, this.configuration.getLocale());
        TemplateModel model = createModel();
        if (preTemplateProcess(template, guiceSideCfg)) {
            try {
                // Process the template
                // First, get the writer
                Writer writer = null;
                boolean useOutputStream = false;
                try {
                    writer = getWriter();
                }
                catch (IllegalStateException ise) {
                    // Getting the writer failed, try using getOutputStream()
                    // This can happen on some application servers such as WebLogic 8.1
                    useOutputStream = true;
                }
                if (useOutputStream) {
                    // If we are here, we don't have the issue of WW-1458, since
                    // we are already writing through a temporary buffer.

                    // Use a StringWriter as a buffer to write the template output to
                    writer = new StringWriter();
                    template.process(model, writer);
                    writer.flush();

                    // Then write the contents of the writer to the OutputStream
                    OutputStream os = getOutputStream();
                    os.write(writer.toString().getBytes());
                } else {
                    // Process the template with the normal writer since it was available

                    // WW-1458
                    // Allow customization of either (when true) to write result to response stream/writer
                    // only when everything is ok (without exception) or otherwise. This is usefull
                    // when using Freemarker's "rethrow" exception handler, where we don't want
                    // partial of the page to be writen and then exception occurred and we have
                    // freemarker's "rethrow" exception handler to take over but its too late since
                    // part of the response has already been 'commited' to the stream/writer.
                    if (configuration.getTemplateExceptionHandler() == TemplateExceptionHandler.RETHROW_HANDLER ||
                            getBufferOutput()) {
                        CharArrayWriter tempBuffer = new CharArrayWriter();
                        template.process(model, tempBuffer);
                        tempBuffer.flush();

                        tempBuffer.writeTo(writer);
                    } else {
                        template.process(model, writer);
                    }
                }
            } finally {
                // Give subclasses a chance to hook into postprocessing
                postTemplateProcess(template, model);
            }
        }
    }

    private Configuration getConfiguration(ServletContext servletContext, org.guiceside.config.Configuration guiceConfig) throws Exception {
        return FreeMarkerManager.getInstance().getConfiguration(servletContext, guiceConfig);
    }

    private ObjectWrapper getObjectWrapper() {
        return this.configuration.getObjectWrapper();
    }

    private TemplateModel createModel() throws TemplateModelException {
        ServletContext servletContext = (ServletContext) this.actionContextMap.get(ActionContext.SERVLETCONTEXT);
        HttpServletRequest request = (HttpServletRequest) this.actionContextMap.get(ActionContext.HTTPSERVLETREQUEST);
        HttpServletResponse response = (HttpServletResponse) this.actionContextMap.get(ActionContext.HTTPSERVLETRESPONSE);
        return FreeMarkerManager.getInstance().buildTemplateModel(servletContext, request, response, wrapper);
    }

    private TemplateModel createModel(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws TemplateModelException {
        return FreeMarkerManager.getInstance().buildTemplateModel(servletContext, request, response, wrapper);
    }

    private boolean preTemplateProcess(Template template, org.guiceside.config.Configuration guiceConfig) throws IOException, Exception {
        String contentType = guiceConfig.getFreemarkerContentType();
        HttpServletResponse response = (HttpServletResponse) this.actionContextMap.get(ActionContext.HTTPSERVLETRESPONSE);
        if (StringUtils.isBlank(contentType)) {
            contentType = SETTING_CONTENT_TYPE;
        }
        String encoding = template.getEncoding();
        if (encoding != null) {
            contentType += "; charset=" + encoding;
        }

        response.setContentType(contentType);

        String nocache = guiceConfig.getFreemarkerNoCache();
        if (StringUtils.isBlank(nocache)) {
            nocache = SETTING_NO_CACHE;
        }
        if (nocache.equals("true")) {
            // HTTP/1.1 + IE extensions
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, "
                    + "post-check=0, pre-check=0");
            // HTTP/1.0
            response.setHeader("Pragma", "no-cache");
            // Last resort for those that ignore all of the above
            //response.setHeader("Expires", EXPIRATION_DATE);
        }
        return true;
    }

    private Writer getWriter() throws IOException {
        HttpServletResponse response = (HttpServletResponse) this.actionContextMap.get(ActionContext.HTTPSERVLETRESPONSE);
        return response.getWriter();
    }

    private OutputStream getOutputStream() throws IOException {
        HttpServletResponse response = (HttpServletResponse) this.actionContextMap.get(ActionContext.HTTPSERVLETRESPONSE);
        return response.getOutputStream();
    }

    private boolean getBufferOutput() {
        return bufferOutput;
    }

    private void postTemplateProcess(Template template, TemplateModel data) throws IOException {
    }

    private boolean preTemplateProcess(Template template, HttpServletResponse response, org.guiceside.config.Configuration guiceConfig) throws IOException, Exception {
        String contentType = guiceConfig.getFreemarkerContentType();
        if (StringUtils.isBlank(contentType)) {
            contentType = SETTING_CONTENT_TYPE;
        }
        String encoding = template.getEncoding();
        if (encoding != null) {
            contentType += "; charset=" + encoding;
        }

        response.setContentType(contentType);

        String nocache = guiceConfig.getFreemarkerNoCache();
        if (StringUtils.isBlank(nocache)) {
            nocache = SETTING_NO_CACHE;
        }
        if (nocache.equals("true")) {
            // HTTP/1.1 + IE extensions
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, "
                    + "post-check=0, pre-check=0");
            // HTTP/1.0
            response.setHeader("Pragma", "no-cache");
            // Last resort for those that ignore all of the above
            //response.setHeader("Expires", EXPIRATION_DATE);
        }
        return true;
    }
}
