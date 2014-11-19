package org.guiceside.persistence.hibernate.dao.interceptor;


import com.google.inject.name.Named;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.persistence.hibernate.SessionFactoryHolder;
import org.guiceside.persistence.hibernate.dao.HibernateDataAccessException;
import org.guiceside.persistence.hibernate.dao.annotation.QueryByHql;
import org.hibernate.Query;
import org.hibernate.Session;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-23
 *
 **/
public class QueryByHqlInterceptor implements MethodInterceptor{

	private final Map<Method, QueryByHqlDescriptor> queryByHqlCache=new ConcurrentHashMap<Method, QueryByHqlDescriptor>();
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		QueryByHqlDescriptor queryByHqDescriptor=getQueryByHqlDescriptior(invocation);
		if (queryByHqDescriptor == null) {
			
			throw new HibernateDataAccessException(
					"在使用@QueryByHql查询对象发生错误,当前方法的queryByHqDescriptor必须存在");
		}

		Object arguments[] = invocation.getArguments();
		Session session = SessionFactoryHolder.getCurrentSessionFactory()
		.getCurrentSession();
		Query hibernateQuery=session.createQuery(queryByHqDescriptor.query);
		if(queryByHqDescriptor.isBindAsRawParameters){
			bindQueryRawParameters(hibernateQuery, queryByHqDescriptor, arguments);
		}else{
			bindQueryNamedParameters(hibernateQuery, queryByHqDescriptor, arguments);
		}
		hibernateQuery.executeUpdate();
		return null;
	}
	
	/**
	 * 
	 * 通过@Named进行参数绑定
	 * 
	 * @param hibernateQuery
	 * @param queryByHqDescriptor
	 * @param arguments
	 */
	private void bindQueryNamedParameters(Query hibernateQuery, QueryByHqlDescriptor queryByHqDescriptor, Object[] arguments) {
		if(arguments==null&&arguments.length<=0){
			return;
		}
		int index=0;
		for(Object argument:arguments){
			Object annotation = queryByHqDescriptor.parameterAnnotations[index];
			Class<?> type=queryByHqDescriptor.parameterTypes[index];
			if (annotation==null) {
				index++;
				continue;
			}else if (annotation instanceof Named) {
				Named named = (Named)annotation;
				argument=BeanUtils.convertValue(argument, type);
                hibernateQuery.setParameter(named.value(), argument);
			}
			index++;
		}
    }
	
	/**
	 * 
	 * 通过索引顺序进行参数绑定
	 * 
	 * @param hibernateQuery
	 * @param queryByHqDescriptor
	 * @param arguments
	 */
	private void bindQueryRawParameters(Query hibernateQuery, QueryByHqlDescriptor queryByHqDescriptor, Object[] arguments) {
		if(arguments==null&&arguments.length<=0){
			return;
		}
		int index=0;
		int parameIndex=0;
		for(Object argument:arguments){
			Object annotation = queryByHqDescriptor.parameterAnnotations[index];
			Class<?> type=queryByHqDescriptor.parameterTypes[index];
			if (annotation==null) {
				argument=BeanUtils.convertValue(argument, type);
				hibernateQuery.setParameter(parameIndex, argument);
				parameIndex++;
			}else if (annotation instanceof Named) {
				Named named = (Named)annotation;
				argument=BeanUtils.convertValue(argument, type);
                hibernateQuery.setParameter(named.value(), argument);
			}
			index++;
		}
    }
	/**
	 * 
	 * 返回当前@QueryByHql方法描述
	 * 
	 * @param invocation
	 * @return QueryByHqlDescriptor
	 */
	private QueryByHqlDescriptor getQueryByHqlDescriptior(
			MethodInvocation invocation) {
		Method method = invocation.getMethod();
		
		QueryByHqlDescriptor queryByHqlDescriptior = queryByHqlCache
				.get(method);
		if (queryByHqlDescriptior != null) {
			return queryByHqlDescriptior;
		}
		queryByHqlDescriptior = new QueryByHqlDescriptor();
		
		
		queryByHqlDescriptior.parameterTypes = method.getParameterTypes();
		QueryByHql queryByHql = method
				.getAnnotation(QueryByHql.class);
		queryByHqlDescriptior.query= queryByHql.query();
		Annotation[][] annotationArray = method.getParameterAnnotations();
		Object[] parameterAnnotations = new Object[annotationArray.length];
		
		int index = 0;
		if (annotationArray != null && annotationArray.length > 0) {
			for (Annotation[] annotations : annotationArray) {
				for (Annotation annotation : annotations) {
					Class<? extends Annotation> annotationType = annotation.annotationType();
					if (annotationType.equals(Named.class)) {
						parameterAnnotations[index] = annotation;
						queryByHqlDescriptior.isBindAsRawParameters = false;
	                    break;
					}
				}
				index++;
			}
			queryByHqlDescriptior.parameterAnnotations = parameterAnnotations;
			queryByHqlCache.put(method, queryByHqlDescriptior);
		}
		return queryByHqlDescriptior;
	}
	/**
	 * <p>
	 *    一个@QueryByHql描述类
	 * </p>
	 * @author zhenjia
	 *
	 */
	private static class QueryByHqlDescriptor {
		Object[] parameterAnnotations;

		Class<?>[] parameterTypes;

		String query;
		
		boolean isBindAsRawParameters = true;

	}

}
