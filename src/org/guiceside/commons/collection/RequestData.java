package org.guiceside.commons.collection;

import org.apache.log4j.Logger;
import org.guiceside.commons.lang.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * request参数存储类
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class RequestData<K,V> extends AbstractData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(RequestData.class);

	public RequestData() {

	}

	public RequestData(String name) {
		this.name = name;
	}

	public RequestData(int arg) {
		super(arg);
	}

	public RequestData(int arg, float arg1) {
		super(arg, arg1);
	}

	public RequestData(int arg, float arg1, boolean arg2) {
		super(arg, arg1, arg2);
	}

	public RequestData(Map<String, Object> arg) {
		super(arg);
	}

	public Object get(Object key) {
		Object o = super.get(key);
		return o;
	}

    public List<String> getList(Object key){
        Object o = get(key);
        if(o==null){
            return null;
        }
        List<String> values=new ArrayList<String>();
        if(o instanceof  String){
            values.add(o.toString());
        }else if(o instanceof List){
            values.addAll((List)o);
        }
        return values;
    }

    public <T> List<T> getList(Object key,Class<T> type){
        List<String> values=getList(key);
        if(values==null){
            return null;
        }
        List<Object> valuesByType=new ArrayList<Object>();
        for(String value:values){
            valuesByType.add(BeanUtils.convertValue(value,type));
        }
        return (List<T>) valuesByType;
    }

	private String listToString(Object key){
        Object o = get(key);
        if(o==null){
            return null;
        }
        if(o instanceof  String){
            return o.toString();
        }else if(o instanceof List){
            List<String> values= (List<String>) o;
            return values.get(values.size()-1);
        }
        return null;
    }

	public <T> T getValueByType(Object key,Class<T> type) {
		String o = getString(key);
		if (o == null) {
			return null;
		}
		return BeanUtils.convertValue(o,type);
	}

    public String getString(Object key) {
		Object o = listToString(key);
		if (o == null) {
			return null;
		}
		return o.toString();
	}

	public String getStringTrim(Object key) {
		Object o = listToString(key);
		if (o == null) {
			return null;
		}
        return o.toString().trim();
	}
}
