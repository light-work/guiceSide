package org.guiceside.persistence.hibernate.dao.interceptor;

import com.google.inject.name.Named;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.persistence.hibernate.SessionFactoryHolder;
import org.guiceside.persistence.hibernate.dao.HibernateDataAccessException;
import org.guiceside.persistence.hibernate.dao.annotation.FinderByHql;
import org.guiceside.persistence.hibernate.dao.annotation.FirstResult;
import org.guiceside.persistence.hibernate.dao.annotation.MaxResults;
import org.guiceside.persistence.hibernate.dao.enums.ReturnType;
import org.hibernate.Query;
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
 * 应用于显示声明@FinderByHql方法的拦截机<br/>
 * 进行Hql查询操作
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 **/
public class FinderByHqlInterceptor implements MethodInterceptor{

	private final Map<Method, FinderByHqlDescriptor> finderByHqlCache=new ConcurrentHashMap<Method, FinderByHqlDescriptor>();
	
	

	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		FinderByHqlDescriptor finderByHqDescriptor=getFinderByHqlDescriptior(invocation);
		if (finderByHqDescriptor == null) {
			
			throw new HibernateDataAccessException(
					"在使用@FinderByHql查询对象发生错误,当前方法的finderByHqDescriptor必须存在");
		}

		Object result = null;
		Object arguments[] = invocation.getArguments();

		Session session = SessionFactoryHolder.getCurrentSessionFactory()
				.getCurrentSession();
		Query hibernateQuery=session.createQuery(finderByHqDescriptor.query);
		if(finderByHqDescriptor.isBindAsRawParameters){
			bindQueryRawParameters(hibernateQuery, finderByHqDescriptor, arguments);
		}else{
			bindQueryNamedParameters(hibernateQuery, finderByHqDescriptor, arguments);
		}
		if (ReturnType.LIST
				.equals(finderByHqDescriptor.returnType)) {
			result = getAsCollection(finderByHqDescriptor, hibernateQuery
					.list());
		} else if (ReturnType.PLAIN
				.equals(finderByHqDescriptor.returnType)) {
			result = hibernateQuery.uniqueResult();
		} else if (ReturnType.ARRAY
				.equals(finderByHqDescriptor.returnType)) {
			List tempList = hibernateQuery.list();
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
	 * @param hibernateQuery
	 * @param finderByHqDescriptor
	 * @param arguments
	 */
	private void bindQueryNamedParameters(Query hibernateQuery, FinderByHqlDescriptor finderByHqDescriptor, Object[] arguments) {
		if(arguments==null&&arguments.length<=0){
			return;
		}
		int index=0;
		for(Object argument:arguments){
			Object annotation = finderByHqDescriptor.parameterAnnotations[index];
			Class<?> type=finderByHqDescriptor.parameterTypes[index];
			if (annotation==null) {
				index++;
				continue;
			}else if (annotation instanceof Named) {
				Named named = (Named)annotation;
				argument=BeanUtils.convertValue(argument, type);
                hibernateQuery.setParameter(named.value(), argument);
			}else if (annotation instanceof FirstResult){
				argument=BeanUtils.convertValue(argument, type);
                hibernateQuery.setFirstResult((Integer)argument);
			}else if (annotation instanceof MaxResults){
				argument=BeanUtils.convertValue(argument, type);
                hibernateQuery.setMaxResults((Integer)argument);
			}
			index++;
		}
    }
	
	/**
	 * 
	 * 通过索引顺序进行参数绑定
	 * 
	 * @param hibernateQuery
	 * @param finderByHqDescriptor
	 * @param arguments
	 */
	private void bindQueryRawParameters(Query hibernateQuery, FinderByHqlDescriptor finderByHqDescriptor, Object[] arguments) {
		if(arguments==null&&arguments.length<=0){
			return;
		}
		int index=0;
		int parameIndex=0;
		for(Object argument:arguments){
			Object annotation = finderByHqDescriptor.parameterAnnotations[index];
			Class<?> type=finderByHqDescriptor.parameterTypes[index];
			if (annotation==null) {
				argument=BeanUtils.convertValue(argument, type);
				hibernateQuery.setParameter(parameIndex, argument);
				parameIndex++;
			}else if (annotation instanceof Named) {
				Named named = (Named)annotation;
				argument=BeanUtils.convertValue(argument, type);
                hibernateQuery.setParameter(named.value(), argument);
			}else if (annotation instanceof FirstResult){
				argument=BeanUtils.convertValue(argument, type);
                hibernateQuery.setFirstResult((Integer)argument);
			}else if (annotation instanceof MaxResults){
				argument=BeanUtils.convertValue(argument, type);
                hibernateQuery.setMaxResults((Integer)argument);
			}
			index++;
		}
    }
	
	/**
	 * 
	 * 返回当前@FinderByHql方法描述
	 * 
	 * @param invocation
	 * @return FinderByHqlDescriptor
	 */
	private FinderByHqlDescriptor getFinderByHqlDescriptior(
			MethodInvocation invocation) {
		Method method = invocation.getMethod();
		
		FinderByHqlDescriptor finderByHqlDescriptior = finderByHqlCache
				.get(method);
		if (finderByHqlDescriptior != null) {
			return finderByHqlDescriptior;
		}
		finderByHqlDescriptior = new FinderByHqlDescriptor();
		finderByHqlDescriptior.returnClass = invocation.getMethod()
				.getReturnType();
		finderByHqlDescriptior.returnType = determineReturnType(finderByHqlDescriptior.returnClass);
		finderByHqlDescriptior.parameterTypes = method.getParameterTypes();
		FinderByHql finderByHql = method
				.getAnnotation(FinderByHql.class);
		finderByHqlDescriptior.query= finderByHql.query();
		Annotation[][] annotationArray = method.getParameterAnnotations();
		Object[] parameterAnnotations = new Object[annotationArray.length];
		if (ReturnType.LIST
				.equals(finderByHqlDescriptior.returnType)) {
			finderByHqlDescriptior.returnCollectionType = finderByHql
					.returnAs();
			try {
				finderByHqlDescriptior.returnCollectionTypeConstructor = finderByHqlDescriptior.returnCollectionType
						.getConstructor();
				finderByHqlDescriptior.returnCollectionTypeConstructor
						.setAccessible(true); // UGH!
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(
						"Finder's collection return type specified has no default constructor! returnAs: "
								+ finderByHqlDescriptior.returnCollectionType,
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
						finderByHqlDescriptior.isBindAsRawParameters = false;
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
			finderByHqlDescriptior.parameterAnnotations = parameterAnnotations;
			finderByHqlCache.put(method, finderByHqlDescriptior);
		}
		return finderByHqlDescriptior;
	}
	/**
	 * <p>
	 *    一个@FinderByHql描述类
	 * </p>
	 * @author zhenjia
	 *
	 */
	private static class FinderByHqlDescriptor {
		ReturnType returnType;

		Class<?> returnClass;

		Object[] parameterAnnotations;

		Class<?>[] parameterTypes;

		Class<? extends Collection> returnCollectionType;

		Constructor returnCollectionTypeConstructor;
		
		String query;
		
		boolean isBindAsRawParameters = true;

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
	 * @param finderByHqlDescriptor
	 * @param results
	 * @return 返回Collection对象
	 */
	@SuppressWarnings("unchecked")
	private Object getAsCollection(
			FinderByHqlDescriptor finderByHqlDescriptor,
			List results) {
		Collection<?> collection;
		try {
			collection = (Collection) finderByHqlDescriptor.returnCollectionTypeConstructor
					.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(
					"Specified collection class of Finder's returnAs could not be instantated: "
							+ finderByHqlDescriptor.returnCollectionType,
					e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
					"Specified collection class of Finder's returnAs could not be instantated (do not have access privileges): "
							+ finderByHqlDescriptor.returnCollectionType,
					e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(
					"Specified collection class of Finder's returnAs could not be instantated (it threw an exception): "
							+ finderByHqlDescriptor.returnCollectionType,
					e);
		}

		collection.addAll(results);
		return collection;
	}

}
