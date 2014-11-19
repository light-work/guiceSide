/**
 * Copyright (C) 2006 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.guiceside.web.dispatcher;

import org.apache.log4j.Logger;
import org.guiceside.GuiceSideConstants;
import org.guiceside.commons.collection.DataUtility;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.config.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * GuiceSideFilter将ServletContext压入ThreadLocal提供给Guice调用
 * 根据初始化参数设置encoding
 * </p>
 *
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 $Date:200808
 * @since JDK1.5
 */
public class UrlRewriteFilter implements Filter {

    protected FilterConfig filterConfig;

    private String encoding;

    private Configuration configuration;

    public static final Logger log = Logger.getLogger(UrlRewriteFilter.class);

    /**
     * 设置servletContext<br/>
     * 根据Filter参数设置characterEncoding<br/>
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     */
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        configuration = (Configuration) servletContext.getAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            String uri = getUri(httpServletRequest);
            String query = getQueryString(httpServletRequest);
            String rewriteUrl = urlRewrite(uri, query);
            RequestData<String, Object> requestData = DataUtility.getRequestData(httpServletRequest);
            HttpSession httpSession = httpServletRequest.getSession(false);
            if (httpSession != null) {
                Object oldData = httpSession.getAttribute("contentData");
                if (oldData != null) {
                    List<RequestData<String, Object>> dataList = null;
                    if (oldData instanceof List) {
                        dataList = (List<RequestData<String, Object>>) oldData;
                        if (dataList != null && !dataList.isEmpty()) {
                            dataList.add(requestData);
                        }
                    } else if (oldData instanceof RequestData) {
                        RequestData<String, Object> requestDataOLD = (RequestData<String, Object>) oldData;
                        if (requestDataOLD != null) {
                            dataList = new ArrayList<RequestData<String, Object>>();
                            dataList.add(requestDataOLD);
                            dataList.add(requestData);
                        }
                    }
                    httpSession.setAttribute("contentData", dataList);
                } else {
                    httpSession.setAttribute("contentData", requestData);
                }
            }
            httpServletResponse.sendRedirect(rewriteUrl);
            return;
        } finally {
        }
    }


    private String getUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = uri.substring(request.getContextPath().length());
        return uri;
    }

    private String getQueryString(HttpServletRequest request) {
        String query = request.getQueryString();
        return query;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    private boolean ignoreParamsCheck(String k, Set<String> ignoreParams) {
        if (ignoreParams != null && !ignoreParams.isEmpty()) {
            return ignoreParams.contains(k);
        } else {
            return false;
        }
    }

    private boolean ignoreParamsKeyCheck(String k, Set<String> ignoreParamKeys) {
        if (ignoreParamKeys != null && !ignoreParamKeys.isEmpty()) {
            return ignoreParamKeys.contains(k);
        } else {
            return false;
        }
    }

    private String buildQuery(String query) {
        String buildQuery = null;
        Set<String> ignoreParams = configuration.getIgnoreParams();
        Set<String> ignoreParamKeys = configuration.getIgnoreParamsKey();
        if (StringUtils.isNotBlank(query)) {
            String[] queryList = query.split("&");
            if (queryList != null && queryList.length > 0) {
                buildQuery = "";
                for (String pattern : queryList) {
                    String[] kvs = pattern.split("=");
                    if (kvs != null && kvs.length > 1) {
                        String k = kvs[0].toString();
                        String v = kvs[1].toString();
                        if (StringUtils.isNotBlank(v)) {
                            if (StringUtils.isNotBlank(k)) {
                                if (ignoreParamsKeyCheck(k, ignoreParamKeys)) {
                                    buildQuery += "/" + v;
                                } else {
                                    if (!ignoreParamsCheck(k, ignoreParams)) {
                                        buildQuery += "/" + k + "/" + v;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return buildQuery;
    }

    private String urlRewrite(String path, String query) {
        int lastSlash = path.lastIndexOf(".");
        if (lastSlash == -1) {
            return null;
        }
        String rewriteUrl = path;
        path = path.substring(0, lastSlash);
        String namespace = null;
        String name = null;
        String methodName = null;
        lastSlash = path.lastIndexOf("/");
        if (lastSlash == -1) {
            namespace = "";
            name = path;
        } else if (lastSlash == 0) {
            namespace = "/";
            name = path.substring(lastSlash + 1);
        } else {
            namespace = path.substring(0, lastSlash);
            name = path.substring(lastSlash + 1);
        }

        int exclamation = -1;
        exclamation = name.lastIndexOf("!");
        if (exclamation != -1) {
            methodName = name.substring(exclamation + 1);
            name = name.substring(0, exclamation);
        } else {
            methodName = "";
        }
        if (StringUtils.isNotBlank(namespace) && StringUtils.isNotBlank(name)) {
            rewriteUrl = namespace + "/" + name;
            if (StringUtils.isNotBlank(methodName)) {
                rewriteUrl += "/" + methodName;
            }
            query = buildQuery(query);
            if (StringUtils.isNotBlank(query)) {
                rewriteUrl += query;
                query = null;
            }
        }
        return rewriteUrl;
    }

    public void destroy() {
    }


}
