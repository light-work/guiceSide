package org.guiceside.web.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.annotation.ReqSet;
import org.guiceside.web.dispatcher.mapper.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;


/**
 *  <p>
 *  负责将Action属性压入httpServletRequest对象作为属性
 *  </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 **/
public class ReqSetAttrInterceptor implements MethodInterceptor{
	public static final Logger log=Logger.getLogger(ReqSetAttrInterceptor.class);
	
	/**
	 * 
	 * 在action执行完毕将@ReqSet的属性压至request对象
	 * 
	 * @param invocation
	 * @return 返回当前action执行结果
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result=invocation.proceed();
		Object[] args=invocation.getArguments();
		ActionContext actionContext=(ActionContext) args[0];
		ActionMapping actionMapping=(ActionMapping) actionContext.getActionContext().get(ActionContext.ACTIONMAPPING);
		HttpServletRequest httpServletRequest=(HttpServletRequest) actionContext.getActionContext().get(ActionContext.HTTPSERVLETREQUEST);
		setAttribute(actionMapping.getActionObject(),httpServletRequest);
		return result;
	}
	/**
	 * 
	 * 根据Annotation进行属性设置
	 * 
	 * @param object
	 * @param httpServletRequest
	 */
	private void setAttribute(Object object,HttpServletRequest httpServletRequest) {
		Class superClass = object.getClass();
		Class currentClass;
		while (true) {
			currentClass = superClass;
			Field[] fields = currentClass.getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				Object value = null;
				try{
					if (field.isAnnotationPresent(ReqSet.class)) {
						//如果Field有@ReqSet注释
						ReqSet reqSet=field.getAnnotation(ReqSet.class);
						if(reqSet!=null){
							//从@ReqSet注释获取fieldName 没有则为默认
							fieldName=StringUtils.isNotBlank(reqSet.value())?reqSet.value():fieldName;
						}
						if(log.isDebugEnabled()){
							log.debug("[" + currentClass.getSimpleName() + "] variable '" + fieldName
									+ "' has a [ReqSet] annotation , so httpServletRequest setAttribute!");
						}
						try{
						value=BeanUtils.forceGetProperty(object, fieldName);
						}catch (Exception e) {
							value=null;
						}
						httpServletRequest.setAttribute(fieldName, value);
					}
				}catch (Exception e) {
					if(log.isDebugEnabled()){
						log.debug("[" + currentClass.getName() + "] variable '" + fieldName
								+ "   ignore;");
					}
					continue;
				}
				
			}
			superClass = currentClass.getSuperclass();
			if (superClass == null || superClass == Object.class) {
				break;
			}
		}
	}

}
