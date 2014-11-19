package org.guiceside.commons.collection;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>
 * request 参数封装工具类<br/>
 * 将request参数封装至RequestData
 * </p>
 *
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 $Date:200808
 * @see RequestData
 * @since JDK1.5
 */
public class DataUtility {
    private static final Logger log = Logger.getLogger(DataUtility.class);

    @SuppressWarnings("unchecked")
    public static RequestData<String, Object> getRequestData(HttpServletRequest req) {
        RequestData<String, Object> requestData = new RequestData<String, Object>("REQUEST_DATA");
        Enumeration<String> e = req.getParameterNames();
        String key = null;
        while (e.hasMoreElements()) {
            key = e.nextElement();
            //处理values
            if (requestData.containsKey(key)) {
                List<String> values = new ArrayList<String>();
                Object v = requestData.get(key);
                if (v != null) {
                    if (v instanceof String) {
                        values.add(v.toString());
                    } else if (v instanceof List) {
                        values.addAll((List)v);
                    }
                    values.add(req.getParameter(key));
                    requestData.remove(key);
                    requestData.put(key,values);
                }
            } else {
                requestData.put(key, req.getParameter(key));
            }
        }
        return requestData;
    }
}
