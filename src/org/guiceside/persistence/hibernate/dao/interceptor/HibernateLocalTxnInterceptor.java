package org.guiceside.persistence.hibernate.dao.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.guiceside.persistence.TransactionType;
import org.guiceside.persistence.Transactional;
import org.guiceside.persistence.hibernate.SessionFactoryHolder;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.lang.reflect.Method;

/**
 * User:zhenjiawang(zhenjiaWang@gmail.com) Date:2008-7-22 Time:下午05:19:22
 * Email:(zhenjiaWang@gmail.com) QQ:(119582291)
 * <p>
 * 针对@Transactional 处理简单事务
 * </p>
 */
public class HibernateLocalTxnInterceptor implements MethodInterceptor {

	private static final  Logger log=Logger.getLogger(HibernateLocalTxnInterceptor.class);
	
	@Transactional
	private static class Internal {
	}

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
//        try{
//        SessionFactoryHolder.getCurrentSessionFactory()
//				.getCurrentSession();
//        }catch (HibernateException e){
//            if (!ManagedSessionContext.hasBind(SessionFactoryHolder
//				.getCurrentSessionFactory())){
//                    ManagedSessionContext.bind(SessionFactoryHolder.getCurrentSessionFactory()
//				.openSession());
//            }
//        }
        //Session session = SessionUtilsHolder.getCurrentSessionUtils().get();
		Session session = SessionFactoryHolder.getCurrentSessionFactory()
				.getCurrentSession();

		if (session.getTransaction().isActive()) {
			return methodInvocation.proceed();
		}

		Transactional transactional = readTransactionMetadata(methodInvocation);

		FlushMode savedFlushMode = FlushMode.AUTO;
		if (TransactionType.READ_ONLY.equals(transactional.type())) {
			session.setFlushMode(FlushMode.MANUAL);
		}
		try {
			
			Transaction txn = session.beginTransaction();
			if(log.isDebugEnabled()){
				log.debug("begin Transaction");
			}
			Object result;
			try {
				result = methodInvocation.proceed();
			} catch (Exception e) {

				// 发生异常判断@Transactional声明
				if (rollbackIfNecessary(transactional, e, txn)){
					txn.commit();
					if(log.isDebugEnabled()){
						log.debug("end Transaction");
					}
				}
					

				// propagate whatever exception is thrown anyway
				throw e;
			}
			Exception commitException = null;
			try {

				txn.commit();
				if(log.isDebugEnabled()){
					log.debug("end Transaction");
				}
			} catch (RuntimeException re) {
				txn.rollback();
				if(log.isDebugEnabled()){
					log.debug("end Transaction");
				}
				commitException = re;
			}
			if (null != commitException)
				throw commitException;
			return result;
		} finally {
			if (TransactionType.READ_ONLY.equals(transactional.type()))
				session.setFlushMode(savedFlushMode);
		}

	}

	private Transactional readTransactionMetadata(
			MethodInvocation methodInvocation) {
		Transactional transactional;
		Method method = methodInvocation.getMethod();
		Class<?> targetClass = methodInvocation.getThis().getClass()
				.getSuperclass();
		if (method.isAnnotationPresent(Transactional.class)) {
			transactional = method.getAnnotation(Transactional.class);
		} else if (targetClass.isAnnotationPresent(Transactional.class)) {
			transactional = targetClass.getAnnotation(Transactional.class);
		} else {
			transactional = Internal.class.getAnnotation(Transactional.class);
		}
		return transactional;
	}

	private boolean rollbackIfNecessary(Transactional transactional,
			Exception e, Transaction txn) {
		boolean commit = true;
		for (Class<? extends Exception> rollBackOn : transactional.rollbackOn()) {
			if (rollBackOn.isInstance(e)) {
				commit = false;
				for (Class<? extends Exception> exceptOn : transactional
						.exceptOn()) {
					if (exceptOn.isInstance(e)) {
						commit = true;
						break;
					}
				}
				if (!commit) {
					txn.rollback();
				}
				break;
			}
		}
		return commit;
	}

}
