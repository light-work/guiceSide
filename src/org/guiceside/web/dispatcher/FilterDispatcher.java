package org.guiceside.web.dispatcher;

import com.google.inject.Injector;
import com.google.inject.OutOfScopeException;
import org.apache.log4j.Logger;
import org.guiceside.GuiceSideConstants;
import org.guiceside.commons.GlobalExceptionMapping;
import org.guiceside.commons.GlobalResult;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.config.Configuration;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.action.ActionExcetion;
import org.guiceside.web.action.DefaultActionContext;
import org.guiceside.web.annotation.Dispatcher;
import org.guiceside.web.dispatcher.mapper.ActionMapper;
import org.guiceside.web.dispatcher.mapper.ActionMapperFactory;
import org.guiceside.web.dispatcher.mapper.ActionMapping;
import org.guiceside.web.listener.DefaultGuiceSideListener;
import org.guiceside.web.view.freemarker.FreeMarkerResult;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * GuiceSide Web Mvc 核心Filter
 * 统一接收用户请求并根据规则指派Action类处理用户请求
 * </p>
 *
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 $Date:200808
 * @since JDK1.5
 */
public class FilterDispatcher implements Filter {

    private static final Logger log = Logger.getLogger(FilterDispatcher.class);

    protected FilterConfig filterConfig;

    static final ThreadLocal<Context> localContext = new ThreadLocal<Context>();


    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void destroy() {

    }

    /**
     * guiceSide 核心Filter<br/>
     * 统一接收并处理用户请求
     *
     * @see org.guiceside.web.dispatcher.DispatcherUtils
     * @see com.google.inject.Inject
     * @see org.guiceside.web.dispatcher.mapper.ActionMapper
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        ServletContext servletContext = filterConfig.getServletContext();
        Injector injector = (Injector) servletContext
                .getAttribute(Injector.class.getName());
        if (injector == null) {
            log.error("Guice Injector not found", new UnavailableException(
                    "Guice Injector not found (did you forget to register a "
                            + DefaultGuiceSideListener.class.getSimpleName()
                            + "?)"));
            throw new UnavailableException(
                    "Guice Injector not found (did you forget to register a "
                            + DefaultGuiceSideListener.class.getSimpleName()
                            + "?)");
        }
        Configuration configuration = (Configuration) servletContext
                .getAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);
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

        ActionMapper actionMapper = ActionMapperFactory.getActionMapper();
        ActionMapping actionMapping = actionMapper.getMapping(
                httpServletRequest, configuration);
        if (actionMapping != null) {
            Context previous = localContext.get();
            HttpSession httpSession = httpServletRequest.getSession(false);
            DispatcherUtils du = DispatcherUtils.getInstance();
            try {
                localContext.set(new Context(actionMapping.getParams(), httpSession));
                actionMapper.createAction(actionMapping, injector);
                du.execute(httpServletRequest, httpServletResponse,
                        servletContext, actionMapping, getRequestData(), injector);
                return;
            } catch (Exception e) {
                boolean exceptionLog = true;
                if (e instanceof SocketException) {
                    exceptionLog = false;
                }
                if (e instanceof IllegalStateException) {
                    exceptionLog = false;
                }
                if (exceptionLog) {
                    log.error("execute failed", new ActionExcetion("[execute failed] In Action {"
                            + actionMapping.getActionClass().getName()
                            + "} Method#" + actionMapping.getMethod()
                            + "# On an Error", e));
                    throw new ActionExcetion("[execute failed] In Action {"
                            + actionMapping.getActionClass().getName()
                            + "} Method#" + actionMapping.getMethod()
                            + "# On an Error", e);
                }
            } finally {
                localContext.set(previous);
            }
        }
        chain.doFilter(request, response);

    }

    public Map<String, Object> createdContext(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            ServletContext servletContext, ActionMapping actionMapping, RequestData requestData) {
        Map<String, Object> actionContext = new HashMap<String, Object>();
        actionContext.put(ActionContext.HTTPSERVLETREQUEST, httpServletRequest);
        actionContext.put(ActionContext.HTTPSERVLETRESPONSE,
                httpServletResponse);
        actionContext.put(ActionContext.SERVLETCONTEXT, servletContext);
        actionContext.put(ActionContext.ACTIONMAPPING, actionMapping);
        actionContext.put(ActionContext.REQUESTDATA, requestData);
        return actionContext;
    }

    private boolean exceptionKill(ActionContext actionContext, HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse, String path,
                            Dispatcher dispatcher) throws Exception {
        if (dispatcher == null) {
            dispatcher = Dispatcher.Redirect;
        }
        switch (dispatcher) {
            case Redirect:
                httpServletResponse.sendRedirect(path);
                return true;
            case Forward:
                httpServletRequest.getRequestDispatcher(path).forward(
                        httpServletRequest, httpServletResponse);
                return true;
            case FreeMarker:
                FreeMarkerResult freeMarkerResult = new FreeMarkerResult();
                freeMarkerResult.doExecute(path, actionContext);
                return true;
        }
        return false;
    }

    /**
     * 初始化DispatcherUtils
     *
     * @param filterConfig
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        DispatcherUtils.initialize();
    }

    public static RequestData getRequestData() {
        return getContext().getRequestData();
    }


    static Context getContext() {
        Context context = localContext.get();
        if (context == null) {
            throw new OutOfScopeException(
                    "Cannot access scoped object. Either we"
                            + " are not currently inside an HTTP Servlet request, or you may"
                            + " have forgotten to apply "
                            + GuiceSideFilter.class.getName()
                            + " as a servlet filter for this request.");
        }
        return context;
    }

    static class Context {

        final RequestData requestData;


        Context(RequestData requestData, HttpSession httpSession) {
            this.requestData = requestData;
            if (httpSession != null) {
                Object chainData = httpSession.getAttribute("contentData");
                if (chainData != null) {
                    if (chainData instanceof List) {
                        List<RequestData<String, Object>> dataList = (List<RequestData<String, Object>>) chainData;
                        if (dataList != null && !dataList.isEmpty()) {
                            RequestData<String, Object> requestDataOLD = dataList.get(0);
                            if (requestDataOLD != null) {
                                this.requestData.putAll(requestDataOLD);
                            }
                            dataList.remove(requestDataOLD);
                            if (dataList.isEmpty()) {
                                httpSession.removeAttribute("contentData");
                            }
                        }
                    } else if (chainData instanceof RequestData) {
                        RequestData<String, Object> requestDataOLD = (RequestData<String, Object>) chainData;
                        if (requestDataOLD != null) {
                            this.requestData.putAll(requestDataOLD);
                            httpSession.removeAttribute("contentData");
                        }
                    }
                }
            }

        }

        RequestData getRequestData() {
            return requestData;
        }
    }


}
