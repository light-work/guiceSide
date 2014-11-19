package org.guiceside.web.view;

import org.guiceside.commons.RequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-28
 * Time: 12:31:52
 * To change this template use File | Settings | File Templates.
 */
public class ResourceUtils {
    public static String getResourceBase(HttpServletRequest req) {
        String path = RequestUtils.getServletPath(req);
        if (path == null || "".equals(path)) {
            return "";
        }
        return path.substring(0, path.lastIndexOf('/'));
    }
}
