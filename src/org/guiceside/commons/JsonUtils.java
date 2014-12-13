package org.guiceside.commons;


import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.StringUtils;

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

    private static String  aliasField(String field){
        if(StringUtils.isNotBlank(field)){
            if(field.indexOf("\\.")==-1){
                return field;
            }
            String[] fields=field.split("\\.");
            if(fields!=null&&fields.length>0){
                return fields[fields.length-1];
            }
            return null;
        }
        return null;
    }
    public static JSONObject formObject(Object obj) {
        JSONObject jsonObject=null;
        try{
            jsonObject=JSONObject.fromObject(obj);
        }catch (Exception e){
        }
        return jsonObject;
    }


    public static JSONObject formObjectIgnore(Object obj,String ...ignoreField) {
        JSONObject jsonObject=null;
        try{
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            jsonConfig.setExcludes(ignoreField);
            jsonObject=JSONObject.fromObject(obj,jsonConfig);
        }catch (Exception e){
        }
        return jsonObject;
    }

    public static JSONObject formObjectInclude(Object obj,String ...includeField) {
        return formObjectInclude(obj,null,null,includeField);
    }

    public static JSONObject formObjectInclude(Object obj,Map<String,String> keyMap,
                                               String ...includeField) {
        return formObjectInclude(obj,null,keyMap,includeField);
    }

    public static JSONObject formObjectInclude(Object obj,JsonDateProcessor jsonDateProcessor,Map<String,String> keyMap,
                                               String ...includeField) {
        JSONObject jsonObject=null;
        try{
            if(includeField!=null&&includeField.length>0){
                if(keyMap!=null&&!keyMap.isEmpty()){
                    jsonObject = new JSONObject();
                    for (String field : includeField) {
                        String key=null;
                        if(keyMap.containsKey(field)){
                            key=keyMap.get(field);
                        }else{
                            key=aliasField(field);
                        }
                        if(StringUtils.isNotBlank(key)){
                            if(jsonDateProcessor==null){
                                jsonObject.put(key, BeanUtils.getValue(obj,field));
                            }else{
                                jsonObject.put(key, jsonDateProcessor.process(field,obj));
                            }
                        }
                    }
                }else{
                    jsonObject = new JSONObject();
                    for (String field : includeField) {
                        String key=aliasField(field);
                        if(StringUtils.isNotBlank(key)){
                            if(jsonDateProcessor==null){
                                jsonObject.put(key, BeanUtils.getValue(obj,field));
                            }else{
                                jsonObject.put(key, jsonDateProcessor.process(field,obj));
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
        }
        return jsonObject;
    }
}
