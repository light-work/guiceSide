package org.guiceside.web.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.GuiceSideConstants;
import org.guiceside.commons.GlobalExceptionMapping;
import org.guiceside.commons.GlobalResult;
import org.guiceside.config.Configuration;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.annotation.Dispatcher;
import org.guiceside.web.view.freemarker.FreeMarkerResult;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 负责拦截Action抛出的异常进行转发
 * </p>
 *
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 $Date:200808
 * @since JDK1.5
 */
public class ExceptionInterceptor implements MethodInterceptor {

    /**
     * 在Action执行过程中抛出异常将捕获并跳转至guiceSide.xml<br/>
     * 定义的global-exception-mappings
     *
     * @param invocation
     * @return 返回action执行结果
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object result = null;
        try {
            result = invocation.proceed();
        } catch (Exception e) {
            ActionContext actionContext = (ActionContext) invocation.getArguments()[0];
            HttpServletRequest httpServletRequest = (HttpServletRequest) actionContext
                    .getActionContext().get(ActionContext.HTTPSERVLETREQUEST);
            HttpServletResponse httpServletResponse = (HttpServletResponse) actionContext
                    .getActionContext().get(ActionContext.HTTPSERVLETRESPONSE);

            ServletContext servletContext = (ServletContext) actionContext
                    .getActionContext().get(ActionContext.SERVLETCONTEXT);
            Configuration configuration = (Configuration) servletContext
                    .getAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);
            List<GlobalExceptionMapping> globalExceptionMappings = configuration
                    .getGlobalExceptionMappings();
            if (globalExceptionMappings == null
                    || globalExceptionMappings.size() <= 0) {
                throw new Exception(e);
            }
            for (GlobalExceptionMapping exceptionMapping : globalExceptionMappings) {

                Class superClass = e.getCause() == null ? e.getClass() : e.getCause().getClass();
                Class currentClass;
                while (true) {
                    currentClass = superClass;
                    if (currentClass == exceptionMapping.getException()) {
                        List<GlobalResult> globalResults = configuration
                                .getGlobalResults();
                        if (globalResults != null) {
                            boolean isExecute = false;
                            for (GlobalResult globalResult : globalResults) {
                                if (globalResult.getName().equals(
                                        exceptionMapping.getResult())) {
                                    httpServletRequest.setAttribute("errorStack", e.getCause() == null ? e.getStackTrace() : e.getCause().getStackTrace());
                                    httpServletRequest.setAttribute("errorType", e.getCause() == null ? e.toString() : e.getCause().toString());
                                    String path = globalResult.getPath();
                                    Dispatcher dispatcher = globalResult
                                            .getType();
                                    isExecute = execute(actionContext, httpServletRequest,
                                            httpServletResponse, path,
                                            dispatcher);
                                    if (isExecute) {
                                        return null;
                                    }
                                }
                            }
                        }
                        break;
                    }
                    superClass = currentClass.getSuperclass();
                    if (superClass == null || superClass == Object.class) {
                        break;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 根据guiceSide.xml  global-exception-mappings<br/>
     * 执行相应跳转
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param path
     * @param dispatcher
     * @throws java.io.IOException
     * @throws ServletException
     */
    private boolean execute(ActionContext actionContext, HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse, String path,
                            Dispatcher dispatcher) throws IOException, ServletException, Exception {
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

}
