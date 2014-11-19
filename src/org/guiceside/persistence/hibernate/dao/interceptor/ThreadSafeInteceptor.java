package org.guiceside.persistence.hibernate.dao.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2009-3-29
 * Time: 23:05:51
 * To change this template use File | Settings | File Templates.
 */
public class ThreadSafeInteceptor implements MethodInterceptor {
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object result=methodInvocation.proceed();
        Object obj=methodInvocation.getThis();
        Class objClass=obj.getClass();
        Method method=objClass.getMethod("threadSafe");
        if(method!=null){
            method.invoke(obj);
        }
        return result;
    }
}
