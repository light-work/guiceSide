package org.guiceside.persistence.hibernate.dao.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.entity.Permanent;
import org.guiceside.persistence.hibernate.SessionFactoryHolder;
import org.guiceside.persistence.hibernate.dao.HibernateDataAccessException;
import org.guiceside.persistence.hibernate.dao.annotation.PO;
import org.hibernate.Session;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 对注释为@Delete的方法进行拦截<br/>
 * 将参数@PO进行持久化保存操作
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-8-29
 *
 **/
public class DeleteInterceptor implements MethodInterceptor{
	private final Map<Method, DeleteDescriptor> deleteCache=new ConcurrentHashMap<Method, DeleteDescriptor>();
	public Object invoke(MethodInvocation invocation) throws Throwable {
		DeleteDescriptor deleteDescriptor=getDeleteDescriptor(invocation);
		if(deleteDescriptor==null){
			throw new HibernateDataAccessException(
			"在使用@Delete查询对象发生错误,当前方法的deleteDescriptor必须存在");
		}
		if(!deleteDescriptor.isPO){
			throw new HibernateDataAccessException(
			"在使用@Delete持久化对象发生错误,参数:entity必须声明为@PO");
		}
		Object argument = invocation.getArguments()[0];
		if (argument == null) {
			throw new HibernateDataAccessException(
					"在使用@Delete持久化对象发生错误,参数:entity不能为null");
		}
		
		if(argument instanceof IdEntity){
			Session session = SessionFactoryHolder.getCurrentSessionFactory()
			.getCurrentSession();
			if (argument.getClass().isAnnotationPresent(Permanent.class)) {
				Permanent permanent = argument.getClass().getAnnotation(
						Permanent.class);
				String column = permanent.column();
				String defaultValue = permanent.defaultValue();
				BeanUtils.setValue(argument, column, defaultValue);
				session.update(argument);
			} else {
				session.delete(argument);
			}
		}else{
			throw new HibernateDataAccessException(
			"在使用@Delete持久化对象发生错误,参数:entity必须 extends IdEntity");
		}
		return null;
	}
	
	private DeleteDescriptor getDeleteDescriptor(MethodInvocation invocation){
		Method method=invocation.getMethod();
		DeleteDescriptor deleteDescriptor=deleteCache.get(method);
		if(deleteDescriptor!=null){
			return deleteDescriptor;
		}
		deleteDescriptor=new DeleteDescriptor();
		Annotation[][] annotationArray = invocation.getMethod().getParameterAnnotations();
		if (annotationArray != null && annotationArray.length == 1) {
			for (Annotation[] annotations : annotationArray) {
				for (Annotation annotation : annotations) {
					if(annotation instanceof PO){
						deleteDescriptor.isPO=true;
						break;
					}
				}
			}
			deleteCache.put(method, deleteDescriptor);
		}
		return deleteDescriptor;
	}
	private static class DeleteDescriptor {
		boolean isPO=false;
	}
}
