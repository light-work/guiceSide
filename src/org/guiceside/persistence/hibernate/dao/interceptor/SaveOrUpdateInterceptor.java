package org.guiceside.persistence.hibernate.dao.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.persistence.entity.IdEntity;
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
 * 对注释为@SaveOrUpdate的方法进行拦截<br/>
 * 将参数@PO进行持久化保存操作
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-8-29
 *
 **/
public class SaveOrUpdateInterceptor implements MethodInterceptor{

	private final Map<Method, SaveOrUpdateDescriptor> saveOrUpdateCache=new ConcurrentHashMap<Method, SaveOrUpdateDescriptor>();
	public Object invoke(MethodInvocation invocation) throws Throwable {
		SaveOrUpdateDescriptor saveOrUpdateDescriptor=getSaveOrUpdateDescriptor(invocation);
		if(saveOrUpdateDescriptor==null){
			throw new HibernateDataAccessException(
			"在使用@SaveOrUpdate查询对象发生错误,当前方法的saveOrUpdateDescriptor必须存在");
		}
		if(!saveOrUpdateDescriptor.isPO){
			throw new HibernateDataAccessException(
			"在使用@SaveOrUpdate持久化对象发生错误,参数:entity必须声明为@PO");
		}
		Object argument = invocation.getArguments()[0];
		if (argument == null) {
			throw new HibernateDataAccessException(
					"在使用@SaveOrUpdate持久化对象发生错误,参数:entity不能为null");
		}
		if(argument instanceof IdEntity){
			Session session = SessionFactoryHolder.getCurrentSessionFactory()
			.getCurrentSession();
			session.saveOrUpdate(argument);
		}else{
			throw new HibernateDataAccessException(
			"在使用@SaveOrUpdate持久化对象发生错误,参数:entity必须 extends IdEntity");
		}
		return null;
	}
	
	private SaveOrUpdateDescriptor getSaveOrUpdateDescriptor(MethodInvocation invocation){
		Method method=invocation.getMethod();
		SaveOrUpdateDescriptor saveOrUpdateDescriptor=saveOrUpdateCache.get(method);
		if(saveOrUpdateDescriptor!=null){
			return saveOrUpdateDescriptor;
		}
		saveOrUpdateDescriptor=new SaveOrUpdateDescriptor();
		Annotation[][] annotationArray = invocation.getMethod().getParameterAnnotations();
		if (annotationArray != null && annotationArray.length == 1) {
			for (Annotation[] annotations : annotationArray) {
				for (Annotation annotation : annotations) {
					if(annotation instanceof PO){
						saveOrUpdateDescriptor.isPO=true;
						break;
					}
				}
			}
			saveOrUpdateCache.put(method, saveOrUpdateDescriptor);
		}
		return saveOrUpdateDescriptor;
	}
	
	private static class SaveOrUpdateDescriptor {
		boolean isPO=false;
	}
}
