package org.guiceside.commons.lang;


import org.guiceside.commons.collection.RequestData;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 将带有同一参数名称的参数值封装为数组进行返回
 * </p>
 *
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-9-7
 * @since JDK1.5
 */
public class ArrayBuilder {
    @SuppressWarnings("unchecked")
    public static <T> T[] buildModel(RequestData<String, Object> requestData, HttpServletRequest httpServletRequest, Class<?> T, String shortName)
            throws ArrayBuilderException {
        List<String> values = null;
        String[] vs = httpServletRequest.getParameterValues(shortName);
        if (vs != null && vs.length > 0) {
            values = new ArrayList<String>();
            for (String v : vs) {
                values.add(v);
            }
        } else {
            List<String> vList = requestData.getList(shortName + "_list");
            if (vList != null && !vList.isEmpty()) {
                values = new ArrayList<String>();
                for (String v : vList) {
                    values.add(v);
                }
            } else {
                String value = requestData.getString(shortName);
                if (StringUtils.isNotBlank(value)) {
                    values = new ArrayList<String>();
                    values.add(value);
                }
            }
        }

        if (values.size() == 0) {
            return null;
        }

        T[] results = (T[]) Array.newInstance(T, values.size());
        int index = 0;
        for (String value : values) {
            if (StringUtils.isBlank(value)) {
                index++;
                continue;
            }
            results[index] = (T) BeanUtils.convertValue(value, T);
            index++;
        }
        return results;
    }

    public static Class<? extends Object> getArrayClass(Class<?> T) {
        String tName = T.getName();
        int beginIndex = tName.indexOf("[L");
        int endIndex = tName.lastIndexOf(";");
        if (beginIndex > -1 && endIndex > -1) {
            tName = tName.substring(beginIndex + 2, endIndex);
        }
        try {
            Class<? extends Object> finalT = Class.forName(tName);
            return finalT;
        } catch (Exception e) {
            throw new ArrayBuilderException("not found class:" + tName, e);
        }
    }
}
