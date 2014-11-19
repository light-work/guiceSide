package org.guiceside.web.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.guiceside.GuiceSideConstants;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.commons.lang.ArrayBuilder;
import org.guiceside.commons.lang.ModelBuilder;
import org.guiceside.config.Configuration;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.annotation.ArrayDriver;
import org.guiceside.web.annotation.ModelDriver;
import org.guiceside.web.annotation.ReqGet;
import org.guiceside.web.dispatcher.mapper.ActionMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * <p>
 * 负责动态构造请求参数
 * </p>
 *
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 $Date:200808
 * @since JDK1.5
 */
public class ParamsInterceptor implements MethodInterceptor {

    public static final Logger log = Logger.getLogger(ParamsInterceptor.class);

    /**
     * 在Action前执行参数封装<br>
     * 实现@ReqGet @ModelDriver
     *
     * @param invocation
     * @return 返回action执行结果
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object[] args = invocation.getArguments();
        ActionContext actionContext = (ActionContext) args[0];
        ActionMapping actionMapping = (ActionMapping) actionContext
                .getActionContext().get(ActionContext.ACTIONMAPPING);
        ServletContext servletContext= (ServletContext) actionContext.getActionContext().get(ActionContext.SERVLETCONTEXT);
        Configuration configuration = (Configuration) servletContext
				.getAttribute(GuiceSideConstants.GUICE_SIDE_CONFIG);
        HttpServletRequest httpServletRequest= (HttpServletRequest) actionContext.getActionContext().get(ActionContext.HTTPSERVLETREQUEST);
        RequestData<String,Object> requestData = (RequestData) actionContext.getActionContext().get(ActionContext.REQUESTDATA);
        constructAction(actionMapping.getActionObject(), requestData,httpServletRequest);
        Object result = invocation.proceed();

        return result;

    }

    /**
     * 根据@Annotation获取参数值
     *
     * @param field
     * @param fieldName
     * @param currentClass
     * @return 返回参数值
     */
    private Object getValue(Field field, 
                            String fieldName, Class<?> currentClass, RequestData<String,Object> requestData,HttpServletRequest httpServletRequest) {

        Object value = null;
        if (field.isAnnotationPresent(ModelDriver.class)) {
            // 如果Field存在@ModelDriver 则进行模型驱动Inject
            ModelDriver modelDriver = field.getAnnotation(ModelDriver.class);
            if (modelDriver != null) {
                value = ModelBuilder.buildModel(requestData, field.getType(),
                        modelDriver.value());
            } else {
                value = null;
            }

        } else if (field.isAnnotationPresent(ArrayDriver.class)) {
            //如果Field存在@ArrayDriver 则进行数组驱动Inject
            ArrayDriver arrayDriver = field.getAnnotation(ArrayDriver.class);
            if (arrayDriver != null) {
                Class<? extends Object> arrayClass = ArrayBuilder.getArrayClass(field.getType());
                value = ArrayBuilder.buildModel(requestData, httpServletRequest, arrayClass,
                        fieldName);
            } else {
                value = null;
            }
        } else {
            // 如果为普通类型Field 则进行类型转换
            try {
                value = BeanUtilsBean.getInstance().getConvertUtils().convert(
                        requestData.get(fieldName).toString(), field.getType());

            } catch (Exception e) {
                value = null;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("[" + currentClass.getName() + "] set variable '"
                    + fieldName + "' by value '" + value + "'");
        }
        return value;
    }

    /**
     * 构造当前Action对象上的所有Field
     *
     * @param object
     */
    private void constructAction(Object object, RequestData<String,Object> requestData,HttpServletRequest httpServletRequest) {
        Class<? extends Object> superClass = object.getClass();
        Class<? extends Object> currentClass;
        while (true) {
            currentClass = superClass;
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                Object value = null;
                try {
                    if (field.isAnnotationPresent(ReqGet.class)) {
                        // 如果Field有@ReqGet注释
                        ReqGet reqGet = field.getAnnotation(ReqGet.class);
                        if (reqGet != null) {
                            // 从@ReqGet注释获取fieldName 没有则为默认
                            fieldName = StringUtils.isNotBlank(reqGet.value()) ? reqGet
                                    .value()
                                    : fieldName;
                        }
                        if (log.isDebugEnabled()) {
                            log
                                    .debug("["
                                            + currentClass.getName()
                                            + "] variable '"
                                            + fieldName
                                            + "' has a [ReqGet] annotation , so inject it directly!");
                        }

                        value = getValue(field, fieldName,
                                currentClass, requestData,httpServletRequest);
                        field.setAccessible(true);// 将Field访问权限打开
                        field.set(object, value);// Field赋值
                    } else {
                        // 如果Field没有@ReqGet注释 则进行检查setter方法
                        /*
                              if (log.isDebugEnabled()) {
                                  log
                                          .debug("["
                                                  + currentClass.getName()
                                                  + "] variable '"
                                                  + fieldName
                                                  + "' didn't have [ReqGet] annotation , so inject it by it's setter method!");
                              }
                              value = getValue(field, params, fieldName,
                                      currentClass,httpServletRequest);
                              BeanUtils.setProperty(object, fieldName, value);// 如果存在setter方法则能完成赋值*/
                    }
                } catch (Exception e) {
                    if (log.isDebugEnabled()) {
                        log
                                .debug("if ["
                                        + currentClass.getName()
                                        + "] variable '"
                                        + fieldName
                                        + "  have [Inject] annotation , so inject it directly! else ignore;");
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
