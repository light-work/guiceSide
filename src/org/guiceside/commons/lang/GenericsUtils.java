package org.guiceside.commons.lang;

import org.apache.log4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 * <p>
 * 泛型工具类 根据泛型实现的子类得出泛型的真实原形
 * 通过调用getSuperClassGenricType()方法,传入继承泛型超类的类,返回真实泛型原形
 * 来源网上资源
 * </p>
 *
 */
public class GenericsUtils {
	private final static Logger log=Logger.getLogger(GenericsUtils.class);
	
	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如public MyDao extends GenericHibernateDAO<My>
	 * 返回My
	 * @param clazz 继承泛型的对象
	 * @return 返回继承泛型时声明的父类的泛型参数的类型, 如果没有声明则返回 <code>Object.class</code>
	 */
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public MyDao extends GenericHibernateDAO<My>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or <code>Object.class</code> if cannot be determined
	 */
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(Class clazz, int index) {

		Type genType = clazz.getGenericSuperclass();
		
		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}
}
