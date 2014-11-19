package org.guiceside.commons;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-28
 * Time: 12:30:59
 * To change this template use File | Settings | File Templates.
 */
public class RequestUtils {

    public static String getServletPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();

        if (null != servletPath && !"".equals(servletPath)) {
            return servletPath;
        }

        String requestUri = request.getRequestURI();
        int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
        int endIndex = request.getPathInfo() == null ? requestUri.length() : requestUri.lastIndexOf(request.getPathInfo());
        
        if (startIndex > endIndex) { // this should not happen
            endIndex = startIndex;
        }

        return requestUri.substring(startIndex, endIndex);
    }
}
