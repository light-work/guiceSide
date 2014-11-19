package org.guiceside.web.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.guiceside.GuiceSideConstants;
import org.guiceside.commons.TokenUtils;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.annotation.Token;
import org.guiceside.web.dispatcher.mapper.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * <p>
 * 校验Token如重复提交则抛出异常TokenValidatorException
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 **/
public class TokenValidatorInterceptor implements MethodInterceptor{
	public static final Logger log=Logger.getLogger(TokenValidatorInterceptor.class);
	
	/**
	 * 
	 * 进行@Token声明方法的重复提交校验
	 * 
	 * @param invocation
	 * @return 返回当前action执行结果
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result=null;
		Object[] args=invocation.getArguments();
		ActionContext actionContext=(ActionContext) args[0];
		ActionMapping actionMapping=(ActionMapping) actionContext.getActionContext().get(ActionContext.ACTIONMAPPING);
		Method method=actionMapping.getMethod();
		if(method.isAnnotationPresent(Token.class)){
            HttpServletRequest request = (HttpServletRequest) actionContext.getActionContext().get(ActionContext.HTTPSERVLETREQUEST);
			HttpSession httpSession=request.getSession(false);
            if(httpSession==null){
                throw new TokenValidatorException();
            }
            RequestData<String,Object> requestData = (RequestData) actionContext.getActionContext().get(ActionContext.REQUESTDATA);
			if(httpSession!=null&&requestData!=null){
				Object tokenObject=httpSession.getAttribute(GuiceSideConstants.GUICE_SIDE_TOKEN);
				if(tokenObject==null){
					throw new TokenValidatorException();
				}
				String sessionToken=tokenObject.toString();
				if(sessionToken.equals(requestData.getString(GuiceSideConstants.GUICE_SIDE_TOKEN))){
					httpSession.removeAttribute(GuiceSideConstants.GUICE_SIDE_TOKEN);
					httpSession.setAttribute(GuiceSideConstants.GUICE_SIDE_TOKEN, TokenUtils.getToken(httpSession.getId()));
					result=invocation.proceed();
//                    HttpSession session= (HttpSession) actionContext.getActionContext().get(ActionContext.HTTPSESSION);
//                    session.setAttribute("tokenBeforeResult",result);
				}else{
//                    HttpSession session= (HttpSession) actionContext.getActionContext().get(ActionContext.HTTPSESSION);
//                    result= session.getAttribute("tokenBeforeResult");
//                    session.removeAttribute("tokenBeforeResult");
                    throw new TokenValidatorException("系统拦截重复提交，请不要刷新，点击返回按钮回到主页");
				}
			}
		}else{
			result=invocation.proceed();
		}
		return result;
	}

}
