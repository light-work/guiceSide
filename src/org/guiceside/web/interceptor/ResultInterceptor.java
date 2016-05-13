package org.guiceside.web.interceptor;

import ognl.Ognl;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.guiceside.GuiceSideConstants;
import org.guiceside.commons.GlobalResult;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.config.Configuration;
import org.guiceside.support.properties.PropertiesConfig;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.annotation.Dispatcher;
import org.guiceside.web.annotation.PageFlow;
import org.guiceside.web.annotation.Result;
import org.guiceside.web.dispatcher.mapper.ActionMapping;
import org.guiceside.web.view.freemarker.FreeMarkerResult;
import org.guiceside.web.view.velocity.VelocityResult;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;


/**
 * <p>
 * 通过Action  Method 检查@PageFlow注释
 * 根据注释进行相应的请求结果发送
 * </p>
 *
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 $Date:200808
 * @since JDK1.5
 */
public class ResultInterceptor implements MethodInterceptor {

    private static final Logger log = Logger.getLogger(ResultInterceptor.class);

    /**
     * 根据@PageFlow定义进行相应跳转
     *
     * @param invocation
     * @return 返回当前action执行结果
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();
        ActionContext actionContext = (ActionContext) invocation.getArguments()[0];
        ActionMapping actionMapping = (ActionMapping) actionContext.getActionContext().get(ActionContext.ACTIONMAPPING);
        Method method = actionMapping.getMethod();
        HttpServletRequest httpServletRequest = (HttpServletRequest) actionContext.getActionContext().get(ActionContext.HTTPSERVLETREQUEST);
        HttpServletResponse httpServletResponse = (HttpServletResponse) actionContext.getActionContext().get(ActionContext.HTTPSERVLETRESPONSE);
        ServletContext servletContext = (ServletContext) actionContext.getActionContext().get(ActionContext.SERVLETCONTEXT);
        String path = null;
        Dispatcher dispatcher = null;
        if (method.isAnnotationPresent(PageFlow.class)) {
            PageFlow pageFlow = method.getAnnotation(PageFlow.class);
            Result[] results = pageFlow.result();
            boolean isExecute = false;
            for (Result anResult : results) {
                if (anResult.name().equals(result)) {
                    path = anResult.path();
                    dispatcher = anResult.type();
                    isExecute = execute(actionContext, httpServletRequest, httpServletResponse, path, dispatcher, servletContext);
                    if (isExecute) {
                        return null;
                    }
                }
            }
            Configuration configuration = (Configuration) servletContext.getAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);
            List<GlobalResult> globalResults = configuration.getGlobalResults();
            if (globalResults != null) {
                for (GlobalResult globalResult : globalResults) {
                    if (globalResult.getName().equals(result)) {
                        path = globalResult.getPath();
                        dispatcher = globalResult.getType();
                        isExecute = execute(actionContext, httpServletRequest, httpServletResponse, path, dispatcher, servletContext);
                        if (isExecute) {
                            return null;
                        }
                    }
                }
            }
        } else {
            Configuration configuration = (Configuration) servletContext.getAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);
            boolean isExecute = false;
            List<GlobalResult> globalResults = configuration.getGlobalResults();
            if (globalResults != null) {
                for (GlobalResult globalResult : globalResults) {
                    if (globalResult.getName().equals(result)) {
                        path = globalResult.getPath();
                        dispatcher = globalResult.getType();
                        isExecute = execute(actionContext, httpServletRequest, httpServletResponse, path, dispatcher, servletContext);
                        if (isExecute) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 根据Annotation进行跳转
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param path
     * @param dispatcher
     * @throws java.io.IOException
     * @throws ServletException
     */
    private boolean execute(ActionContext actionContext, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String path, Dispatcher dispatcher, ServletContext servletContext) throws IOException, ServletException, Exception {
        if (dispatcher == null) {
            dispatcher = Dispatcher.Redirect;
        }
        switch (dispatcher) {
            case Redirect:
                path = analysisPath(httpServletRequest, path);
                PropertiesConfig webConfig= (PropertiesConfig)servletContext.getAttribute("webConfig");
                if(webConfig!=null){
                    String secure=webConfig.getString("secure");
                    if(StringUtils.isBlank(secure)){
                        secure="http";
                    }
                    if(secure.equals("https")){
                        if (!httpServletRequest.isSecure()) { // it is HTTP
                            String getProtocol = httpServletRequest.getScheme();
                            String getDomain = httpServletRequest.getServerName();
                            String getPort = Integer.toString(httpServletRequest.getServerPort());
                            if (getProtocol.toLowerCase().equals("http")) {
                                String httpsPath = "https" + "://" + getDomain;
                                if(!getPort.equals("80")){
                                    httpsPath+=":" + getPort;
                                }
                                httpsPath+=path;
                                String queryString = httpServletRequest.getQueryString();
                                if (StringUtils.isNotBlank(queryString)){
                                    httpsPath += '?' + queryString;
                                }
                                path=httpsPath;
                            }
                        }
                    }
                }
                log(Dispatcher.Redirect.toString(), path);
                httpServletResponse.sendRedirect(path);
                return true;
            case Forward:
                log(Dispatcher.Forward.toString(), path);
                httpServletRequest.getRequestDispatcher(path).forward(httpServletRequest,
                        httpServletResponse);
                return true;
            case FreeMarker:
                FreeMarkerResult freeMarkerResult = new FreeMarkerResult();
                freeMarkerResult.doExecute(path, actionContext);
                return true;
            case Velocity:
                VelocityResult velocityResult = new VelocityResult();
                velocityResult.doExecute(path, actionContext);
                return true;
        }
        return false;
    }

    private void log(String dispatcher, String path) {
        if (log.isDebugEnabled()) {
            log.debug("DispatcherType:" + dispatcher + " path:" + path);
        }
    }

    /**
     * 解析path
     *
     * @param httpServletRequest
     * @param path
     * @return path
     */
    private String analysisPath(HttpServletRequest httpServletRequest, String path) {
        int _sigh = path.indexOf("?");
        if (_sigh == -1) {
            if(path.startsWith("${")&&path.endsWith("}")){
                path=analysisParameter(httpServletRequest, path);
                try{
                    path=URLDecoder.decode(path, "UTF-8");
                }catch (Exception e){
                    path=null;
                }
            }
            return path;
        }
        String parameterStr = path.substring(_sigh + 1);
        if (StringUtils.isBlank(parameterStr)) {
            return path;
        }
        String[] parameterStrs = parameterStr.split("\\&");
        if (parameterStrs == null || parameterStrs.length <= 0) {
            return path;
        }
        path = path.substring(0, _sigh + 1);
        int _index = 0;
        for (String parameter : parameterStrs) {
            parameter = analysisParameter(httpServletRequest, parameter);
            if (StringUtils.isBlank(parameter)) {
                continue;
            }
            if (_index == 0) {
                path += parameter;
            } else {
                path += "&" + parameter;
            }
            _index++;
        }
        return path;
    }

    /**
     * 解析parameter
     *
     * @param httpServletRequest
     * @param parameter
     * @return 返回对request求值结果
     */
    private String analysisParameter(HttpServletRequest httpServletRequest, String parameter) {
        int beginIndex = parameter.indexOf("${");
        int endIndex = parameter.lastIndexOf("}");
        if (beginIndex == -1 || endIndex == -1) {
            return parameter;
        }
        if (beginIndex > endIndex) {
            return null;
        }
        String key = parameter.substring(beginIndex + 2, endIndex);
        if (StringUtils.isBlank(key)) {
            return null;
        }
        parameter = parameter.substring(0, beginIndex);
        Object value = null;
        int _sigh = key.indexOf(".");
        if (_sigh != -1) {
            String property = key.substring(_sigh + 1);
            key = key.substring(0, _sigh);
            Object object = httpServletRequest.getAttribute(key);
            if (object == null) {
                value = "";
            } else {
                try {
                    value = Ognl.getValue(property, object);
                } catch (Exception e) {
                    value = "";
                }
            }
        } else {
            value = httpServletRequest.getAttribute(key);
        }
        value = value == null ? "" : value;
        try {
            value = URLEncoder.encode(value.toString(), "UTF-8");
        } catch (Exception e) {

        }
        parameter = parameter + value;
        return parameter;
    }
}
