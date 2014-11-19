package org.guiceside.web.view.freemarker;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.*;
import org.apache.log4j.Logger;
import org.guiceside.GuiceSideConstants;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.config.Configuration;
import org.guiceside.web.view.JspSupportServlet;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-27
 * Time: 22:29:41
 * To change this template use File | Settings | File Templates.
 */
public class FreeMarkerManager {
    private static final Logger log = Logger.getLogger(FreeMarkerManager.class);
    private static final String WRAPPER_SIMPLE = "simple";
    private static final String WRAPPER_BEANS = "beans";
    
    private static final String SETTING_LOCALTE = "zh_CN";
    private static final String SETTING_ENCODING = "UTF-8";
    private static final String SETTING_UPDATE_DELAY = "15";
    // coppied from freemarker servlet - since they are private
    private static final String ATTR_APPLICATION_MODEL = ".freemarker.Application";
    private static final String ATTR_JSP_TAGLIBS_MODEL = ".freemarker.JspTaglibs";
    private static final String ATTR_SESSION_MODEL = ".freemarker.Session";
    private static final String ATTR_REQUEST_MODEL = ".freemarker.Request";
    private static final String ATTR_REQUEST_PARAMETERS_MODEL = ".freemarker.RequestParameters";

    // coppied from freemarker servlet - so that there is no dependency on it
    public static final String KEY_APPLICATION = "Application";
    public static final String KEY_REQUEST_MODEL = "Request";
    public static final String KEY_SESSION_MODEL = "Session";
    public static final String KEY_JSP_TAGLIBS = "JspTaglibs";
    public static final String KEY_REQUEST_PARAMETER_MODEL = "Parameters";
    private static FreeMarkerManager freeMarkerManager = null;

    public final static synchronized FreeMarkerManager getInstance() {
        if (freeMarkerManager == null) {
            freeMarkerManager = new FreeMarkerManager();
        }
        return freeMarkerManager;
    }

    public final synchronized freemarker.template.Configuration getConfiguration(ServletContext servletContext,Configuration guiceConfig) throws Exception {
        freemarker.template.Configuration configuration = (freemarker.template.Configuration) servletContext.getAttribute(GuiceSideConstants.FREEMARKER_CONFIG);
        if (configuration == null) {
            configuration = createConfiguration(servletContext,guiceConfig);
        }
        return configuration;
    }

    protected ScopesHashModel buildScopesHashModel(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, ObjectWrapper wrapper) {
        ScopesHashModel scopesHashModel = new ScopesHashModel(wrapper, servletContext, request);
        synchronized (servletContext) {
            ServletContextHashModel servletContextModel = (ServletContextHashModel) servletContext.getAttribute(ATTR_APPLICATION_MODEL);

            if (servletContextModel == null) {

                GenericServlet servlet = JspSupportServlet.jspSupportServlet;
                // TODO if the jsp support  servlet isn't load-on-startup then it won't exist
                // if it hasn't been accessed, and a JSP page is accessed
                if (servlet != null) {
                    servletContextModel = new ServletContextHashModel(servlet, wrapper);
                    servletContext.setAttribute(ATTR_APPLICATION_MODEL, servletContextModel);
                    TaglibFactory taglibs = new TaglibFactory(servletContext);
                    servletContext.setAttribute(ATTR_JSP_TAGLIBS_MODEL, taglibs);
                }

            }

            scopesHashModel.put(KEY_APPLICATION, servletContextModel);
            scopesHashModel.put(KEY_JSP_TAGLIBS, (TemplateModel) servletContext.getAttribute(ATTR_JSP_TAGLIBS_MODEL));
        }
        TemplateHashModel sessionModel;

        HttpSession session = request.getSession(false);
        if (session != null) {
            scopesHashModel.put(KEY_SESSION_MODEL, new HttpSessionHashModel(session, wrapper));
        } else {
            // no session means no attributes ???
            //            model.put(KEY_SESSION_MODEL, new SimpleHash());
        }

        HttpRequestHashModel requestModel = (HttpRequestHashModel) request.getAttribute(ATTR_REQUEST_MODEL);

        if ((requestModel == null) || (requestModel.getRequest() != request)) {
            requestModel = new HttpRequestHashModel(request, response, wrapper);
            request.setAttribute(ATTR_REQUEST_MODEL, requestModel);
        }

        scopesHashModel.put(KEY_REQUEST_MODEL, requestModel);

        HttpRequestParametersHashModel reqParametersModel = (HttpRequestParametersHashModel) request.getAttribute(ATTR_REQUEST_PARAMETERS_MODEL);
        if (reqParametersModel == null || requestModel.getRequest() != request) {
            reqParametersModel = new HttpRequestParametersHashModel(request);
            request.setAttribute(ATTR_REQUEST_PARAMETERS_MODEL, reqParametersModel);
        }
        scopesHashModel.put(KEY_REQUEST_PARAMETER_MODEL, reqParametersModel);

        return scopesHashModel;
    }

    public SimpleHash buildTemplateModel(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, ObjectWrapper wrapper) {
        ScopesHashModel scopesHashModel = buildScopesHashModel(servletContext,request,response,wrapper);
        return scopesHashModel;
    }

    protected TemplateLoader createTemplateLoader(Configuration guiceSideCfg, ServletContext servletContext) throws IOException {
        String templeteLoadPath = guiceSideCfg.getFreemarkerLoadPath();
        if (StringUtils.isBlank(templeteLoadPath)) {
            templeteLoadPath = "/";
        }
        if (templeteLoadPath.toLowerCase().startsWith("file://")) {
            templeteLoadPath = templeteLoadPath.substring(7);
            return new FileTemplateLoader(new File(templeteLoadPath));
        } else if (templeteLoadPath.toLowerCase().startsWith("class://")) {
            templeteLoadPath = templeteLoadPath.substring(7);
            return new ClassTemplateLoader(getClass(), templeteLoadPath);
        } else {
            return new WebappTemplateLoader(servletContext, templeteLoadPath);
        }
    }

    private  ObjectWrapper createObjectWrapper(Configuration guiceSideCfg) {
        String wrapper = guiceSideCfg.getFreemarkerWrapper();
        if (StringUtils.isNotBlank(wrapper)) {
            if (wrapper.toLowerCase().equals(WRAPPER_SIMPLE)) {
                return ObjectWrapper.SIMPLE_WRAPPER;
            } else if (wrapper.toLowerCase().equals(WRAPPER_BEANS)) {
                return ObjectWrapper.BEANS_WRAPPER;
            } else {
                return ObjectWrapper.DEFAULT_WRAPPER;
            }
        } else {
            return ObjectWrapper.DEFAULT_WRAPPER;
        }
    }

    private freemarker.template.Configuration createConfiguration(ServletContext servletContext,Configuration guiceSideCfg) throws Exception {
        

        freemarker.template.Configuration configuration = new freemarker.template.Configuration();
        String exceptionHandler=guiceSideCfg.getFreemarkerExceptionHandler();
        if(StringUtils.isBlank(exceptionHandler)){
            exceptionHandler=TemplateExceptionHandler.HTML_DEBUG_HANDLER.toString();
        }
        if(exceptionHandler.equals(TemplateExceptionHandler.HTML_DEBUG_HANDLER.toString())){
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        }else if(configuration.equals(TemplateExceptionHandler.IGNORE_HANDLER.toString())){
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        }else if(configuration.equals(TemplateExceptionHandler.DEBUG_HANDLER.toString())){
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        }else if(configuration.equals(TemplateExceptionHandler.RETHROW_HANDLER.toString())){
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        }

        ObjectWrapper objectWrapper = createObjectWrapper(guiceSideCfg);
        configuration.setObjectWrapper(objectWrapper);
        TemplateLoader templateLoader = createTemplateLoader(guiceSideCfg, servletContext);
        configuration.setTemplateLoader(templateLoader);
        String encoding = guiceSideCfg.getFreemarkerEncoding();
        if (StringUtils.isBlank(encoding)) {
            encoding = SETTING_ENCODING;
        }
        configuration.setDefaultEncoding(encoding);

       

        String updatedDelay = guiceSideCfg.getFreemarkerUpdateDelay();
        if (StringUtils.isBlank(updatedDelay)) {
            updatedDelay = SETTING_UPDATE_DELAY;
        }
        configuration.setSetting("template_update_delay", updatedDelay);

        String locale = guiceSideCfg.getFreemarkerLocale();
        if (StringUtils.isBlank(locale)) {
            locale = SETTING_LOCALTE;
        }
        configuration.setSetting("locale", locale);

        

        servletContext.setAttribute(GuiceSideConstants.FREEMARKER_CONFIG, configuration);
        return configuration;
    }
}
