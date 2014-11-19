package org.guiceside.commons.lang;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;
import org.guiceside.commons.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * <p>
 * 扩展Apache Commons BeanUtils, 提供一些反射方面缺失功能的封装. 声明 此辅助类部分方法来源于(SpringSide)
 * 版权归作者所有 借此一用 谢谢!
 * </p>
 */
@SuppressWarnings("unchecked")
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	

	protected static final Logger log = Logger.getLogger(BeanUtils.class);
	private BeanUtils() {

	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * 
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 */
	public static Field getDeclaredField(Object object, String propertyName)
			throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.isNotBlank(propertyName);
		return getDeclaredField(object.getClass(), propertyName);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * 
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 */

	public static Field getDeclaredField(Class clazz, String propertyName)
			throws NoSuchFieldException {
		Assert.notNull(clazz);
		Assert.isNotBlank(propertyName);
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName()
				+ '.' + propertyName);
	}

	/**
	 * 暴力获取对象变量值,忽略private,protected修饰符的限制.
	 * 
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 */
	public static Object forceGetProperty(Object object, String propertyName)
			throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.isNotBlank(propertyName);

		Field field = getDeclaredField(object, propertyName);

		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			if(log.isDebugEnabled()){
				log.debug("error wont' happen");
			}
		}
		field.setAccessible(accessible);
		return result;
	}

	/**
	 * 暴力设置对象变量值,忽略private,protected修饰符的限制.
	 * 
	 * @throws NoSuchFieldException
	 *             如果没有该Field时抛出.
	 */
	public static void forceSetProperty(Object object, String propertyName,
			Object newValue) throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.isNotBlank(propertyName);

		Field field = getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, newValue);
		} catch (IllegalAccessException e) {
			if(log.isDebugEnabled()){
				log.debug("error wont' happen");
			}
		}
		field.setAccessible(accessible);
	}

	/**
	 * 暴力调用对象函数,忽略private,protected修饰符的限制.
	 * 
	 * @throws NoSuchMethodException
	 *             如果没有该Method时抛出.
	 */
	public static Object invokePrivateMethod(Object object, String methodName,
			Object... params) throws NoSuchMethodException {
		Assert.notNull(object);
		Assert.isNotBlank(methodName);
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class clazz = object.getClass();
		Method method = null;
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				// 方法不在当前类定义,继续向上转型
			}
		}

		if (method == null)
			throw new NoSuchMethodException("No Such Method:"
					+ clazz.getSimpleName() + methodName);

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(object, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		method.setAccessible(accessible);
		return result;
	}

	/**
	 * 内部调用ognl 实现BeanUtils.setProperty(arg0, arg1, arg2)
	 * 
	 * @param object
	 * @param property
	 * @param value
	 * @throws OgnlException
	 */
	public static void setValue(Object object, String property, Object value)
			throws OgnlException {
		Ognl.setValue(property, object, value);
	}

	/**
	 * 内部调用ognl 实现BeanUtils.getProperty(arg0, arg1)
	 * 
	 * @param object
	 * @param property
	 * @return Object
	 * @throws OgnlException
	 */
	public static Object getValue(Object object, String property)
			throws OgnlException {
		return Ognl.getValue(property, object);
	}

	/**
	 * 内部调用ognl 实现BeanUtils.getProperty(arg0, arg1) 添加泛型返回
	 * @param <T>
	 * @param object
	 * @param property
	 * @param classType
	 * @return T
	 * @throws OgnlException
	 */
	public static <T> T getValue(Object object, String property,
			Class<T> classType) throws OgnlException {
		Object value= Ognl.getValue(property, object);
		return convertValue(value, classType);
	}

	/**
	 * 将value转换为type类型
	 * 
	 * @param value
	 * @param type
	 * @return Object
	 */
	public static <T> T convertValue(Object value, Class<T> type) {
		if (value != null) {
			value = BeanUtilsBean.getInstance().getConvertUtils().convert(
					value.toString(), type);
		}
		return (T) value;
	}

	/**
	 * 判断object是否是数组
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isArray(Object value) {
		Class<? extends Object> c = value.getClass();
		return c.isArray();
	}
}
