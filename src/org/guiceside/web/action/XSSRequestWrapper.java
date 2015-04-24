package org.guiceside.web.action;

import org.apache.commons.lang.StringEscapeUtils;
import org.guiceside.commons.lang.StringUtils;
import org.owasp.validator.html.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhenjiaWang on 15/1/22.
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper {
    private static Policy policy = null;

    static{
        String path =XSSRequestWrapper.class.getClassLoader().getResource("antisamy-rule-oa.xml").getFile();
        if(path.startsWith("file")){
            path = path.substring(6);
        }
        try {
            policy = Policy.getInstance(path);
        } catch (PolicyException e) {
            e.printStackTrace();
        }
    }

    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @SuppressWarnings("rawtypes")
    public Map<String,String[]> getParameterMap(){
        Map<String,String[]> request_map = super.getParameterMap();
        Iterator iterator = request_map.entrySet().iterator();
        System.out.println("request_map"+request_map.size());
        while(iterator.hasNext()){
            Map.Entry me = (Map.Entry)iterator.next();
            //System.out.println(me.getKey()+":");
            String[] values = (String[])me.getValue();
            for(int i = 0 ; i < values.length ; i++){
                System.out.println(values[i]);
                values[i] = xssClean(values[i]);
            }
        }
        return request_map;
    }
    public String[] getParameterValues(String paramString)
    {
        String[] arrayOfString1 = super.getParameterValues(paramString);
        if (arrayOfString1 == null)
            return null;
        int i = arrayOfString1.length;
        String[] arrayOfString2 = new String[i];
        for (int j = 0; j < i; j++)
            arrayOfString2[j] = xssClean(arrayOfString1[j]);
        return arrayOfString2;
    }

    public String getParameter(String paramString)
    {
        String str = super.getParameter(paramString);
        if (str == null)
            return null;
        return xssClean(str);
    }

    public String getHeader(String paramString)
    {
        String str = super.getHeader(paramString);
        if (str == null)
            return null;
        return xssClean(str);
    }


    private String xssClean(String value) {
        AntiSamy antiSamy = new AntiSamy();
        try {
            //CleanResults cr = antiSamy.scan(dirtyInput, policyFilePath);
            final CleanResults cr = antiSamy.scan(value, policy);
            String str= StringEscapeUtils.unescapeHtml(cr.getCleanHTML());
            if(StringUtils.isNotBlank(str)){
                str=str.replaceAll((antiSamy.scan("&nbsp;",policy)).getCleanHTML(),"");
            }else{
                str=cr.getCleanHTML();
            }
            //安全的HTML输出

            return str;
        } catch (ScanException e) {
            e.printStackTrace();
        } catch (PolicyException e) {
            e.printStackTrace();
        }
        return value;
    }
}
