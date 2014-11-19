package org.guiceside.persistence.hibernate.dao.interceptor;

import com.google.inject.name.Named;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.hibernate.SessionFactoryHolder;
import org.guiceside.persistence.hibernate.dao.HibernateDataAccessException;
import org.guiceside.persistence.hibernate.dao.annotation.FinderBySql;
import org.guiceside.persistence.hibernate.dao.annotation.FirstResult;
import org.guiceside.persistence.hibernate.dao.annotation.MaxResults;
import org.guiceside.persistence.hibernate.dao.enums.ReturnType;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;




/**
 * <p>
 * 应用于显示声明@FinderBySql方法的拦截机<br/>
 * 进行Sql查询操作
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 **/
public class FinderBySqlInterceptor implements MethodInterceptor{

	private final Map<Method, FinderBySqlDescriptor> finderBySqlCache=new ConcurrentHashMap<Method, FinderBySqlDescriptor>();
	
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		FinderBySqlDescriptor finderBySqDescriptor=getFinderBySqlDescriptior(invocation);
		if (finderBySqDescriptor == null) {
		
			throw new HibernateDataAccessException(
					"在使用@FinderBySql查询对象发生错误,当前方法的finderByHqDescriptor必须存在");
		}

		Object result = null;
		Object arguments[] = invocation.getArguments();

		Session session = SessionFactoryHolder.getCurrentSessionFactory()
				.getCurrentSession();
		SQLQuery sqlQuery =session.createSQLQuery(finderBySqDescriptor.query);
		if(finderBySqDescriptor.isBindAsRawParameters){
			bindQueryRawParameters(sqlQuery, finderBySqDescriptor, arguments);
		}else{
			bindQueryNamedParameters(sqlQuery, finderBySqDescriptor, arguments);
		}
		sqlQuery.addEntity(finderBySqDescriptor.entity);
		if (ReturnType.LIST
				.equals(finderBySqDescriptor.returnType)) {
			result = getAsCollection(finderBySqDescriptor, sqlQuery
					.list());
		} else if (ReturnType.PLAIN
				.equals(finderBySqDescriptor.returnType)) {
			result = sqlQuery.uniqueResult();
		} else if (ReturnType.ARRAY
				.equals(finderBySqDescriptor.returnType)) {
			List tempList = sqlQuery.list();
			if (tempList != null && !tempList.isEmpty()) {
				result = tempList.toArray();
			}
			result = null;
		}
		
		return result;
	}
	
	/**
	 * 
	 * 通过@Named进行参数绑定
	 * 
	 * @param sqlQuery
	 * @param finderBySqDescriptor
	 * @param arguments
	 */
	private void bindQueryNamedParameters(SQLQuery sqlQuery, FinderBySqlDescriptor finderBySqDescriptor, Object[] arguments) {
		if(arguments==null&&arguments.length<=0){
			return;
		}
		int index=0;
		for(Object argument:arguments){
			Object annotation = finderBySqDescriptor.parameterAnnotations[index];
			Class<?> type=finderBySqDescriptor.parameterTypes[index];
			if (annotation==null) {
				index++;
				continue;
			}else if (annotation instanceof Named) {
				Named named = (Named)annotation;
				argument=BeanUtils.convertValue(argument, type);
				sqlQuery.setParameter(named.value(), argument);
			}else if (annotation instanceof FirstResult){
				argument=BeanUtils.convertValue(argument, type);
				sqlQuery.setFirstResult((Integer)argument);
			}else if (annotation instanceof MaxResults){
				argument=BeanUtils.convertValue(argument, type);
				sqlQuery.setMaxResults((Integer)argument);
			}
			index++;
		}
    }
	
	/**
	 * 
	 * 通过索引顺序进行参数绑定
	 * 
	 * @param sqlQuery
	 * @param finderBySqDescriptor
	 * @param arguments
	 */
	private void bindQueryRawParameters(SQLQuery sqlQuery, FinderBySqlDescriptor finderBySqDescriptor, Object[] arguments) {
		if(arguments==null&&arguments.length<=0){
			return;
		}
		int index=0;
		int parameIndex=0;
		for(Object argument:arguments){
			Object annotation = finderBySqDescriptor.parameterAnnotations[index];
			Class<?> type=finderBySqDescriptor.parameterTypes[index];
			if (annotation==null) {
				argument=BeanUtils.convertValue(argument, type);
				sqlQuery.setParameter(parameIndex, argument);
				parameIndex++;
			}else if (annotation instanceof Named) {
				Named named = (Named)annotation;
				argument=BeanUtils.convertValue(argument, type);
				sqlQuery.setParameter(named.value(), argument);
			}else if (annotation instanceof FirstResult){
				argument=BeanUtils.convertValue(argument, type);
				sqlQuery.setFirstResult((Integer)argument);
			}else if (annotation instanceof MaxResults){
				argument=BeanUtils.convertValue(argument, type);
				sqlQuery.setMaxResults((Integer)argument);
			}
			index++;
		}
    }
	
	/**
	 * 
	 * 返回当前@FinderBySql方法描述
	 * 
	 * @param invocation
	 * @return FinderBySqlDescriptor
	 */
	private FinderBySqlDescriptor getFinderBySqlDescriptior(
			MethodInvocation invocation) {
		Method method = invocation.getMethod();
		
		FinderBySqlDescriptor finderBySqlDescriptior = finderBySqlCache
				.get(method);
		if (finderBySqlDescriptior != null) {
			return finderBySqlDescriptior;
		}
		finderBySqlDescriptior = new FinderBySqlDescriptor();
		finderBySqlDescriptior.returnClass = invocation.getMethod()
				.getReturnType();
		finderBySqlDescriptior.returnType = determineReturnType(finderBySqlDescriptior.returnClass);
		finderBySqlDescriptior.parameterTypes = method.getParameterTypes();
		FinderBySql finderBySql = method
				.getAnnotation(FinderBySql.class);
		finderBySqlDescriptior.entity=finderBySql.entity();
		finderBySqlDescriptior.query= finderBySql.query();
		Annotation[][] annotationArray = method.getParameterAnnotations();
		Object[] parameterAnnotations = new Object[annotationArray.length];
		if (ReturnType.LIST
				.equals(finderBySqlDescriptior.returnType)) {
			finderBySqlDescriptior.returnCollectionType = finderBySql
					.returnAs();
			try {
				finderBySqlDescriptior.returnCollectionTypeConstructor = finderBySqlDescriptior.returnCollectionType
						.getConstructor();
				finderBySqlDescriptior.returnCollectionTypeConstructor
						.setAccessible(true); // UGH!
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(
						"Finder's collection return type specified has no default constructor! returnAs: "
								+ finderBySqlDescriptior.returnCollectionType,
						e);
			}
		}
		int index = 0;
		if (annotationArray != null && annotationArray.length > 0) {
			for (Annotation[] annotations : annotationArray) {
				for (Annotation annotation : annotations) {
					Class<? extends Annotation> annotationType = annotation.annotationType();
					if (annotationType.equals(Named.class)) {
						parameterAnnotations[index] = annotation;
						finderBySqlDescriptior.isBindAsRawParameters = false;
	                    break;
					}else if(annotationType.equals(FirstResult.class)){
						parameterAnnotations[index] = annotation;
						break;
					}else if(annotationType.equals(MaxResults.class)){
						parameterAnnotations[index] = annotation;
						break;
					}
				}
				index++;
			}
			finderBySqlDescriptior.parameterAnnotations = parameterAnnotations;
			finderBySqlCache.put(method, finderBySqlDescriptior);
		}
		return finderBySqlDescriptior;
	}
	/**
	 * <p>
	 *    一个@FinderBySql描述类
	 * </p>
	 * @author zhenjia
	 *
	 */
	private static class FinderBySqlDescriptor {
		ReturnType returnType;

		Class<?> returnClass;

		Object[] parameterAnnotations;

		Class<?>[] parameterTypes;

		Class<? extends Collection> returnCollectionType;

		Constructor returnCollectionTypeConstructor;
		
		String query;
		
		boolean isBindAsRawParameters = true;
		
		Class<? extends IdEntity> entity;

	}
	
	


	/**
	 * 通过返回类型得到返回类型枚举
	 * @param returnClass
	 * @return ReturnType
	 */
	private ReturnType determineReturnType(Class<?> returnClass) {
		if (Collection.class.isAssignableFrom(returnClass)) {
			return ReturnType.LIST;
		} else if (returnClass.isArray()) {
			return ReturnType.ARRAY;
		}
		return ReturnType.PLAIN;
	}
	
	/**
	 * 将结果集构造成方法返回类型的Collection
	 * 
	 * @param finderBySqlDescriptor
	 * @param results
	 * @return 返回Collection对象
	 */
	@SuppressWarnings("unchecked")
	private Object getAsCollection(
			FinderBySqlDescriptor finderBySqlDescriptor,
			List results) {
		Collection<?> collection;
		try {
			collection = (Collection) finderBySqlDescriptor.returnCollectionTypeConstructor
					.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(
					"Specified collection class of Finder's returnAs could not be instantated: "
							+ finderBySqlDescriptor.returnCollectionType,
					e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
					"Specified collection class of Finder's returnAs could not be instantated (do not have access privileges): "
							+ finderBySqlDescriptor.returnCollectionType,
					e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(
					"Specified collection class of Finder's returnAs could not be instantated (it threw an exception): "
							+ finderBySqlDescriptor.returnCollectionType,
					e);
		}

		collection.addAll(results);
		return collection;
	}

}
