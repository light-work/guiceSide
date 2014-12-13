package org.guiceside.web.action;

import com.google.inject.Inject;
import net.sf.json.JSONObject;
import ognl.OgnlException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.log4j.Logger;
import org.guiceside.GuiceSideConstants;
import org.guiceside.commons.TokenUtils;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.support.converter.DateConverter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Action辅助基类<br/>
 * 提供常用便捷的方法 推荐所有Action继承此类<br/>
 * </p>
 *
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 $Date:200808
 * @since JDK1.5
 */

public abstract class BaseAction {

    protected final Logger log = Logger.getLogger(getClass());

    protected String charset = "UTF-8";

    static {
        registConverter();
    }

    @Inject
    private HttpServletRequest httpServletRequest;


    @Inject
    private HttpServletResponse httpServletResponse;

    @Inject
    private HttpSession httpSession;

    @Inject
    private ServletContext servletContext;


    @Inject
    private RequestData requestData;

    /**
     * 静态方法,注册ApacheConvert策略
     */
    public static void registConverter() {
        ConvertUtils.register(new StringConverter(), String.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new FloatConverter(null), Float.class);
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        ConvertUtils.register(new DateConverter(), Date.class);
    }


    /**
     * 获得 Servlet httpServletRequest 对象
     *
     * @return 返回当前httpServletRequest对象
     */
    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    /**
     * 获得 Servlet httpServletResponse 对象
     *
     * @return 返回当前httpServletResponse
     */
    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    /**
     * 获得 Servlet httpSession 对象
     *
     * @return 返回当前HttpSession对象
     */
    public HttpSession getHttpSession() {
        return httpSession;
    }

    /**
     * 获得ServletContext对象
     *
     * @return 返回Application ServletContext
     */
    public ServletContext getServletContext() {

        return servletContext;
    }


    /**
     * 获取httpServletRequest参数快截方法<br/>
     * 如果得到空字符传默认转换为null
     *
     * @param parameterName
     * @return 返回参数值
     */
    public String getParameter(String parameterName) {
        String value = requestData.getString(parameterName);
        if (StringUtils.isBlank(value))
            return null;
        return value;
    }


    /**
     * 获取httpServletRequest参数快截方法<br/>
     * 如果得到空字符传默认转换为null
     *
     * @param parameterName
     * @return 返回参数值
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String parameterName, Class<T> type) {
        return (T) requestData.getValueByType(parameterName, type);
    }

    public List<String> getParameters(String parameterName) {
        return requestData.getList(parameterName);
    }

    public <T> List<T> getParameters(String parameterName, Class<T> type) {
        return requestData.getList(parameterName, type);
    }

    public RequestData getRequestData() {
        return requestData;
    }

    /**
     * 返回Project根目录
     *
     * @return 返回projectRootPath
     */
    public String getRootPath() {
        return getServletContext().getRealPath(File.separator);
    }

    /**
     * 输出.
     *
     * @param text
     * @param contentType
     */
    private void writeByAction(String text, String contentType) {
        this.httpServletResponse.setContentType(contentType);
        this.httpServletResponse.setHeader("Pragma", "no-chache");
        this.httpServletResponse.setHeader("Chache-Control", "no-chache");
        this.httpServletResponse.setDateHeader("Expires", 0);

        PrintWriter pw = null;
        try {
            pw = this.httpServletResponse.getWriter();
            pw.write(text);
            pw.close();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * 输出text/plain
     *
     * @param text
     */
    protected void writeTextByAction(String text) {
        writeByAction(text, "text/plain;charset=" + charset);
    }

    /**
     * 输出text/html
     *
     * @param text
     */
    protected void writeHtmlByAction(String text) {
        writeByAction(text, "text/html;charset=" + charset);
    }

    /**
     * 输出text/xml
     *
     * @param text
     */
    protected void writeXmlByAction(String text) {
        writeByAction(text, "text/xml;charset=" + charset);
    }

    /**
     * 输出text/json
     *
     * @param text
     */
    protected void writeJsonByAction(String text) {

        writeByAction(text, "text/json;charset=" + charset);
    }

    protected void writeJsonMapByAction(HashMap<String, Object> results) {
        if (null != results && results.isEmpty()) {
            writeByAction(JSONObject.fromObject(new HashMap<String, Object>()).toString(), "application/x-javascript;charset=" + charset);
        } else if (null != results && !results.isEmpty()) {
            writeByAction(JSONObject.fromObject(results).toString(), "application/x-javascript;charset=" + charset);
        }
    }


    public abstract String execute() throws Exception;

    protected boolean isPostBack() {
        if (httpSession != null && requestData != null) {
            Object tokenObject = httpSession.getAttribute(GuiceSideConstants.GUICE_SIDE_TOKEN);
            if (tokenObject == null) {
                return true;
            }
            String sessionToken = tokenObject.toString();
            if (sessionToken.equals(requestData.getString(GuiceSideConstants.GUICE_SIDE_TOKEN))) {
                httpSession.removeAttribute(GuiceSideConstants.GUICE_SIDE_TOKEN);
                httpSession.setAttribute(GuiceSideConstants.GUICE_SIDE_TOKEN, TokenUtils.getToken(httpSession.getId()));
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public String download(File file, String fileName) throws Exception {
        download(file, fileName, false);
        return null;
    }

    public String download(File file, String fileName, boolean deleteFile) throws Exception {
        try {
// 取得文件名。
// 取得文件的后缀名。
// 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
// 清空response
            HttpServletResponse response = this.getHttpServletResponse();
            response.reset();
// 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (deleteFile) {
                file.delete();
            }
        }
        return null;
    }

    public String download(InputStream in, String fileName,long fileLength) throws Exception {
        try {
// 取得文件名。
// 取得文件的后缀名。
// 以流的形式下载文件。
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
// 清空response
            HttpServletResponse response = this.getHttpServletResponse();
            response.reset();
// 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
            response.addHeader("Content-Length", "" + fileLength);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {

        }
        return null;
    }

    public String getIpAddr() {
        HttpServletRequest request = this.getHttpServletRequest();
        String ip = null;
        if (request != null) {
            ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }
        if (StringUtils.isNotBlank(ip)) {
            int sign = ip.indexOf(",");
            if (sign != -1) {
                ip = ip.substring(0, sign);
            }
        }
        return ip;
    }

    protected String get(Object entity, String property) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        return StringUtils.defaultIfEmpty(result);
    }

    protected String getDate(Object entity, String property, String f) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        return StringUtils.defaultIfEmptyByDate((Date) result, f);
    }

    protected <T> T get(Object entity, String property, Class<T> type) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        result = StringUtils.defaultIfEmpty(result);
        result = BeanUtils.convertValue(result, type);
        return (T) result;
    }
}
