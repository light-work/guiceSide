package org.guiceside.commons.lang;

import ognl.OgnlException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.guiceside.commons.collection.RequestData;

import java.util.*;

/**
 * *
 * <p>
 * 通过运用OGNL的特性,将请求参数转换为一个模型对象返回 支持泛型 无限层次构造模型对象 *
 * </p> *
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a> * @since
 * JDK1.5 * @version 1.0 $Date:200808
 */
public class ModelBuilder {
	public static Object buildModel(RequestData<String, Object> requestData, Class<?> T,
			String shortName) throws ModelBuilderException {
		if (StringUtils.isBlank(shortName)) {
			shortName = ClassUtils.getShortClassName(T);
			shortName = StringUtils.uncapitalize(shortName);
		}
		List<String> propertyOrderList = parseKeySet(requestData, shortName);
		String[] propertyArr = null;
		Object object = createObj(T);
		Object tempObj=null;
		Object propertyObj = null;
		for (String propertys : propertyOrderList) {

			propertyArr = propertys.split("\\.");
			int _i=0;
			for (String property : propertyArr) {
				if(_i==0){
					tempObj=object;
				}else if(_i>0){
					tempObj=propertyObj;
				}
				if (containsProperty(tempObj, property)) {
					propertyObj = buildProperty(tempObj, property);
				}
				_i++;
			}
		}
		Set<String> keySet = requestData.keySet();
		Class<?> propertyType = null;
		Object value = null;
		boolean sign = false;
		for (String key : keySet) {
			try {
				key = key.substring(key.indexOf(shortName) + shortName.length()
						+ 1);
				propertyType = getPropertyType(object, key);
				value = BeanUtils.convertValue(
						requestData.getString(shortName + "." + key),
						propertyType);
				setValue(object,key,  value);
				sign = true;
			} catch (Exception e) {
				continue;
			}
		}
		if (!sign) {
			object = null;
		}
		return object;
	}

	/**
	 * * 构造关联属性对像 为NULL值是创建，否则返回当前对象 * * @param object * @param property * @return
	 * 返回当前对象
	 */
	private static Object buildProperty(Object object, String property)
			throws ModelBuilderException {
		Object propertyObj = getValue(object,property);
		if (propertyObj == null) {
			setValue(object,property,  createObj(getPropertyType(object,
					property)));
			propertyObj = getValue(object,property);
		}
		return propertyObj;
	}

	/** * * @param classz * @return 返回实例 */
	private static Object createObj(Class<?> classz)
			throws ModelBuilderException {
		Object obj = null;
		try {
			obj = classz.newInstance();
		} catch (Exception e) {
			throw new ModelBuilderException("instance object ["
					+ classz.getName() + "] failed", e);
		}
		return obj;
	}

	private static Object getValue(Object obj,String property)
			throws ModelBuilderException {
		Object value = null;
		try {
			value = BeanUtils.getValue(obj,property);
		} catch (OgnlException e) {
			throw new ModelBuilderException("get object ["
					+ obj.getClass().getName() + "] Property+{" + property
					+ "} Value failed", e);
		}
		return value;
	}

	private static void setValue(Object obj,String property,  Object value)
			throws ModelBuilderException {
		try {
			BeanUtils.setValue(obj,property, value);
		} catch (OgnlException e) {
			throw new ModelBuilderException("set object ["
					+ obj.getClass().getName() + "] Property+{" + property
					+ "} Value failed", e);
		}
	}

	/**
	 * * 获取对象属性类型 * @param obj * @param property * @return Class<?> * @throws
	 * ModelBuilderException
	 */
	private static Class<?> getPropertyType(Object obj, String property)
			throws ModelBuilderException {
		Class<?> propertyType = null;
		try {
			propertyType = PropertyUtils.getPropertyType(obj, property);
		} catch (Exception e) {
			throw new ModelBuilderException("get object ["
					+ obj.getClass().getName() + "] Property+{" + property
					+ "} Type failed", e);
		}
		return propertyType;
	}

	/** * 查看当前对象里是否包含属性 * * @param obj * @param property * @return 是否包含属性 */
	private static boolean containsProperty(Object obj, String property) {
		boolean contains = false;
		try {
			PropertyUtils.getProperty(obj, property);
			contains = true;
		} catch (Exception e) {
		}
		return contains;
	}

	/**
	 * * 解析请求参数的名称,将请求参数中存在关联模型对象的属性按照实例化顺序以List形式返回 * * @param params * @param
	 * shortName * @return 返回属性KEY SET
	 */
	public static List<String> parseKeySet(Map<String, Object> params,
			String shortName) {
		List<String> propertyOrderList = new ArrayList<String>();
		Map<String, Integer> propertyOrderMap = new HashMap<String, Integer>();
		Set<String> keySet = params.keySet();
		int beginIndex = -1;
		int endInex = -1;
		String property;
		int max = -1;
		int currentOrder = -1;
		for (String key : keySet) {
			if (!key.startsWith(shortName)) {
				continue;
			}
			beginIndex = key.indexOf(".");
			endInex = key.lastIndexOf(".");
			if (beginIndex == -1 || endInex == -1) {
				continue;
			}
			if (beginIndex == endInex) {
				continue;
			}
			property = key.substring(beginIndex + 1, endInex);
			currentOrder = property.split("\\.").length;
			propertyOrderMap.put(property, currentOrder);
			if (max < currentOrder) {
				max = currentOrder;
			}
		}
		int currentMax = -1;
		for (int i = 0; i <= max; i++) {
			keySet = propertyOrderMap.keySet();
			for (String key : keySet) {
				currentMax = propertyOrderMap.get(key);
				if (currentMax == i) {
					propertyOrderList.add(key);
				}
			}
		}
		return propertyOrderList;
	}
}