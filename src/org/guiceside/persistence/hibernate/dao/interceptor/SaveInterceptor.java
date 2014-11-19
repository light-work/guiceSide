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
 * 对注释为@Save的方法进行拦截<br/>
 * 将参数@PO进行持久化保存操作
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-8-29
 *
 **/
public class SaveInterceptor implements MethodInterceptor{
	private final Map<Method, SaveDescriptor> saveCache=new ConcurrentHashMap<Method, SaveDescriptor>();
	public Object invoke(MethodInvocation invocation) throws Throwable {
        
		SaveDescriptor saveDescriptor=getSaveDescriptor(invocation);
		if(saveDescriptor==null){
			throw new HibernateDataAccessException(
			"在使用@Save查询对象发生错误,当前方法的saveDescriptor必须存在");
		}
		if(!saveDescriptor.isPO){
			throw new HibernateDataAccessException(
			"在使用@Save持久化对象发生错误,参数:entity必须声明为@PO");
		}
		
		Object argument = invocation.getArguments()[0];
		if (argument == null) {
			throw new HibernateDataAccessException(
					"在使用@Save持久化对象发生错误,参数:entity不能为null");
		}
		if(!argument.getClass().isAnnotationPresent(PO.class)){
			throw new HibernateDataAccessException(
			"在使用@Save持久化对象发生错误,参数:entity必须声明为@PO");
		}
		if(argument instanceof IdEntity){
			Session session = SessionFactoryHolder.getCurrentSessionFactory()
			.getCurrentSession();
			session.save(argument);
			
		}else{
			throw new HibernateDataAccessException(
			"在使用@Save持久化对象发生错误,参数:entity必须 extends IdEntity");
		}
		return null;
	}
	
	private SaveDescriptor getSaveDescriptor(MethodInvocation invocation){
		Method method=invocation.getMethod();
		SaveDescriptor saveDescriptor=saveCache.get(method);
		if(saveDescriptor!=null){
			return saveDescriptor;
		}
		saveDescriptor=new SaveDescriptor();
		Annotation[][] annotationArray = invocation.getMethod().getParameterAnnotations();
		if (annotationArray != null && annotationArray.length == 1) {
			for (Annotation[] annotations : annotationArray) {
				for (Annotation annotation : annotations) {
					if(annotation instanceof PO){
						saveDescriptor.isPO=true;
						break;
					}
				}
			}
			saveCache.put(method, saveDescriptor);
		}
		return saveDescriptor;
	}
	private static class SaveDescriptor {
		boolean isPO=false;
	}
}
