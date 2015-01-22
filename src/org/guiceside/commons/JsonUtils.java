package org.guiceside.commons;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * JSON  JAVABEAN
 * <p/>
 * <p/>
 * </p>
 *
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 $Date:200808
 * @since JDK1.5
 */
public class JsonUtils {

    private static String aliasField(String field) {
        if (StringUtils.isNotBlank(field)) {
            if (field.indexOf(".") == -1) {
                return field;
            }
            String[] fields = field.split("\\.");
            if (fields != null && fields.length > 0) {
                return fields[fields.length - 1];
            }
            return null;
        }
        return null;
    }

    public static JSONObject formObject(Object obj) {
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.fromObject(obj);
        } catch (Exception e) {
        }
        return jsonObject;
    }

    public static JSONArray formList(List<JSONObject> objectList) {
        JSONArray jsonArray = null;
        if (objectList != null && !objectList.isEmpty()) {
            jsonArray = new JSONArray();
            for (JSONObject jsonObject : objectList) {
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }


    public static JSONObject formObjectIgnore(Object obj, String... ignoreField) {
        JSONObject jsonObject = null;
        try {
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            jsonConfig.setExcludes(ignoreField);
            jsonObject = JSONObject.fromObject(obj, jsonConfig);
        } catch (Exception e) {
        }
        return jsonObject;
    }

    public static JSONObject formObjectInclude(Object obj, String... includeField) {
        return formObjectInclude(obj, null, null, includeField);
    }

    public static JSONObject formObjectInclude(Object obj, Map<String, String> keyMap,
                                               String... includeField) {
        return formObjectInclude(obj, null, keyMap, includeField);
    }

    public static List<JSONObject> formListInclude(List<Object> objList, JsonDataProcessor jsonDataProcessor, Map<String, String> keyMap,
                                               String... includeField) {
        List<JSONObject> objectList=null;
        if(objList!=null&&!objList.isEmpty()){
            objectList=new ArrayList<JSONObject>();
            for(Object obj:objList){
                JSONObject o=formObjectInclude(obj,jsonDataProcessor,keyMap,includeField);
                if(o!=null){
                    objectList.add(o);
                }
            }
        }
        return objectList;
    }
    public static JSONObject formObjectInclude(Object obj, JsonDataProcessor jsonDataProcessor, Map<String, String> keyMap,
                                               String... includeField) {
        JSONObject jsonObject = null;
        try {
            if (includeField != null && includeField.length > 0) {
                if (keyMap != null && !keyMap.isEmpty()) {
                    jsonObject = new JSONObject();
                    for (String field : includeField) {
                        String key = null;
                        if (keyMap.containsKey(field)) {
                            key = keyMap.get(field);
                        } else {
                            key = aliasField(field);
                        }
                        if (StringUtils.isNotBlank(key)) {
                            if (jsonDataProcessor == null) {
                                jsonObject.put(key, BeanUtils.getValue(obj, field));
                            } else {
                                jsonObject.put(key, jsonDataProcessor.process(field, obj));
                            }
                        }
                    }
                } else {
                    jsonObject = new JSONObject();
                    for (String field : includeField) {
                        String key = aliasField(field);
                        if (StringUtils.isNotBlank(key)) {
                            if (jsonDataProcessor == null) {
                                jsonObject.put(key, BeanUtils.getValue(obj, field));
                            } else {
                                jsonObject.put(key, jsonDataProcessor.process(field, obj));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return jsonObject;
    }

    public static <T> T toBean(JSONObject obj, Class<T> type, JsonValueProcessor jsonValueProcessor, JsonDataProcessor jsonDataProcessor, String... includeField) {
        T result = null;
        try {
            if (includeField != null && includeField.length > 0) {
                result = type.newInstance();
                for (String field : includeField) {
                    Object valueObj = obj.get(field);
                    if (jsonDataProcessor != null) {
                        valueObj = jsonDataProcessor.process(field, valueObj);
                    }
                    Field declaredField = null;
                    try {
                        declaredField = BeanUtils.getDeclaredField(result, field);
                        if (declaredField != null) {
                            BeanUtils.setValue(result, field, valueObj);
                        }
                    } catch (Exception e) {
                        result = jsonValueProcessor.process(field, valueObj, result);
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }
}
