package org.guiceside.persistence.hibernate.dao.interceptor;


import com.google.inject.name.Named;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.persistence.hibernate.SessionFactoryHolder;
import org.guiceside.persistence.hibernate.dao.HibernateDataAccessException;
import org.guiceside.persistence.hibernate.dao.annotation.QueryByHql;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
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
public class QueryBySqlInterceptor implements MethodInterceptor{

	private final Map<Method, QueryBySqlDescriptor> queryBySqlCache=new ConcurrentHashMap<Method, QueryBySqlDescriptor>();
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		QueryBySqlDescriptor queryBySqDescriptor=getQueryBySqlDescriptior(invocation);
		if (queryBySqDescriptor == null) {
			
			throw new HibernateDataAccessException(
					"在使用@QueryBySql查询对象发生错误,当前方法的queryBySqDescriptor必须存在");
		}

		Object arguments[] = invocation.getArguments();
		Session session = SessionFactoryHolder.getCurrentSessionFactory()
		.getCurrentSession();
		SQLQuery sqlQuery=session.createSQLQuery(queryBySqDescriptor.query);
		if(queryBySqDescriptor.isBindAsRawParameters){
			bindQueryRawParameters(sqlQuery, queryBySqDescriptor, arguments);
		}else{
			bindQueryNamedParameters(sqlQuery, queryBySqDescriptor, arguments);
		}
		sqlQuery.executeUpdate();
		return null;
	}
	
	/**
	 * 
	 * 通过@Named进行参数绑定
	 * 
	 * @param sqlQuery
	 * @param queryBySqDescriptor
	 * @param arguments
	 */
	private void bindQueryNamedParameters(Query sqlQuery, QueryBySqlDescriptor queryBySqDescriptor, Object[] arguments) {
		if(arguments==null&&arguments.length<=0){
			return;
		}
		int index=0;
		for(Object argument:arguments){
			Object annotation = queryBySqDescriptor.parameterAnnotations[index];
			Class<?> type=queryBySqDescriptor.parameterTypes[index];
			if (annotation==null) {
				index++;
				continue;
			}else if (annotation instanceof Named) {
				Named named = (Named)annotation;
				argument=BeanUtils.convertValue(argument, type);
				sqlQuery.setParameter(named.value(), argument);
			}
			index++;
		}
    }
	
	/**
	 * 
	 * 通过索引顺序进行参数绑定
	 * 
	 * @param sqlQuery
	 * @param queryBySqDescriptor
	 * @param arguments
	 */
	private void bindQueryRawParameters(Query sqlQuery, QueryBySqlDescriptor queryBySqDescriptor, Object[] arguments) {
		if(arguments==null&&arguments.length<=0){
			return;
		}
		int index=0;
		int parameIndex=0;
		for(Object argument:arguments){
			Object annotation = queryBySqDescriptor.parameterAnnotations[index];
			Class<?> type=queryBySqDescriptor.parameterTypes[index];
			if (annotation==null) {
				argument=BeanUtils.convertValue(argument, type);
				sqlQuery.setParameter(parameIndex, argument);
				parameIndex++;
			}else if (annotation instanceof Named) {
				Named named = (Named)annotation;
				argument=BeanUtils.convertValue(argument, type);
				sqlQuery.setParameter(named.value(), argument);
			}
			index++;
		}
    }
	/**
	 * 
	 * 返回当前@FinderByHql方法描述
	 * 
	 * @param invocation
	 * @return QueryByHqlDescriptor
	 */
	private QueryBySqlDescriptor getQueryBySqlDescriptior(
			MethodInvocation invocation) {
		Method method = invocation.getMethod();
		
		QueryBySqlDescriptor queryBySqlDescriptior = queryBySqlCache
				.get(method);
		if (queryBySqlDescriptior != null) {
			return queryBySqlDescriptior;
		}
		queryBySqlDescriptior = new QueryBySqlDescriptor();
		
		
		queryBySqlDescriptior.parameterTypes = method.getParameterTypes();
		QueryByHql queryByHql = method
				.getAnnotation(QueryByHql.class);
		queryBySqlDescriptior.query= queryByHql.query();
		Annotation[][] annotationArray = method.getParameterAnnotations();
		Object[] parameterAnnotations = new Object[annotationArray.length];
		
		int index = 0;
		if (annotationArray != null && annotationArray.length > 0) {
			for (Annotation[] annotations : annotationArray) {
				for (Annotation annotation : annotations) {
					Class<? extends Annotation> annotationType = annotation.annotationType();
					if (annotationType.equals(Named.class)) {
						parameterAnnotations[index] = annotation;
						queryBySqlDescriptior.isBindAsRawParameters = false;
	                    break;
					}
				}
				index++;
			}
			queryBySqlDescriptior.parameterAnnotations = parameterAnnotations;
			queryBySqlCache.put(method, queryBySqlDescriptior);
		}
		return queryBySqlDescriptior;
	}
	/**
	 * <p>
	 *    一个@QueryByHql描述类
	 * </p>
	 * @author zhenjia
	 *
	 */
	private static class QueryBySqlDescriptor {
		Object[] parameterAnnotations;

		Class<?>[] parameterTypes;

		String query;
		
		boolean isBindAsRawParameters = true;

	}

}
