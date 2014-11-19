package org.guiceside.persistence.hibernate.dao.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.hibernate.SessionFactoryHolder;
import org.guiceside.persistence.hibernate.dao.HibernateDataAccessException;
import org.guiceside.persistence.hibernate.dao.annotation.FinderById;
import org.guiceside.persistence.hibernate.dao.annotation.PK;
import org.guiceside.persistence.hibernate.dao.enums.FinderType;
import org.hibernate.Session;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;





/**
 * <p>
 * 应用于显示声明@FinderById方法的拦截机<br/>
 * 进行byID查询操作
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class FinderByIdInterceptor implements MethodInterceptor {

	private final Map<Method, FinderByIdDescriptor> finderByIdCache = new ConcurrentHashMap<Method, FinderByIdDescriptor>();

	public Object invoke(MethodInvocation invocation) throws Throwable {
		FinderByIdDescriptor finderByIdDescriptor=getFinderByIdDescriptor(invocation);
		if(finderByIdDescriptor==null){
			throw new HibernateDataAccessException("在使用@FinderById查询发生错误,当前方法的finderByIdDescriptor必须存在");
		}
		Object result=null;
		Object argObj=invocation.getArguments()[0];
		if(argObj==null){
			throw new HibernateDataAccessException("在使用@FinderById查询发生错误,参数:ID不能为null");
		}
		Serializable id=null;
		try{
			id=(Serializable) argObj;
		}catch (Exception e) {
			throw new HibernateDataAccessException("在使用@FinderById查询发生错误,参数:ID 必须为Serializable",e);
		}
		Session session = SessionFactoryHolder.getCurrentSessionFactory().getCurrentSession();
		if(finderByIdDescriptor.finderType.equals(FinderType.GET)){
			result=session.get(finderByIdDescriptor.entityClass, id);
		}else if(finderByIdDescriptor.finderType.equals(FinderType.LOAD)){
			result=session.load(finderByIdDescriptor.entityClass, id);
		}
		return result;
	}

	private FinderByIdDescriptor getFinderByIdDescriptor(
			MethodInvocation invocation){
		Method method = invocation.getMethod();
		FinderByIdDescriptor finderByIdDescriptor=finderByIdCache.get(method);
		if(finderByIdDescriptor!=null){
			return finderByIdDescriptor;
		}
		finderByIdDescriptor=new FinderByIdDescriptor();
		FinderById finderById = method.getAnnotation(FinderById.class);
		finderByIdDescriptor.entityClass=finderById.entity();

		finderByIdDescriptor.finderType=finderById.type();
		Annotation[][] annotationArray=method.getParameterAnnotations();
		if(annotationArray!=null&&annotationArray.length==1){
			Annotation annotation=annotationArray[0][0];
			Class<? extends Annotation> annotationType= annotation.annotationType();
			if(annotationType.equals(PK.class)){
				finderByIdDescriptor.parameterAnnotation=annotationArray[0][0];
			}else{
				finderByIdDescriptor=null;
				throw new HibernateDataAccessException("在使用@FinderById查询发生错误,声明参数时Annotation必须为@PK");
			}
			finderByIdCache.put(method,finderByIdDescriptor);
			return finderByIdDescriptor;
		}else{
			finderByIdDescriptor=null;
			throw new HibernateDataAccessException("在使用@FinderById查询发生错误,声明参数时须显示声明Annotation为@PK");
		}
		
		
	}

	private static class FinderByIdDescriptor {
		Class<? extends IdEntity> entityClass;
		FinderType finderType;
		Object parameterAnnotation;
	}

}
