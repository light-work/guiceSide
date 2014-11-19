package org.guiceside.web.dispatcher.mapper;

import org.guiceside.commons.collection.RequestData;

import java.lang.reflect.Method;



/**
 * <p>
 * ActionMapping JavaBean
 * </p>
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808

 */
public class ActionMapping {
	private Class<?> actionClass;
	private Object actionObject;
	private String name;
    private String namespace;
    private String methodName;
    private Method method;
    private RequestData<String,Object> params;
    private String uri;
    private String extension;
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName =methodName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public RequestData<String,Object> getParams() {
		return params;
	}
	public void setParams(RequestData<String,Object> params) {
		this.params = params;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public Class<?> getActionClass() {
		return actionClass;
	}
	public void setActionClass(Class<?> actionClass) {
		this.actionClass = actionClass;
	}
	public Object getActionObject() {
		return actionObject;
	}
	public void setActionObject(Object actionObject) {
		this.actionObject = actionObject;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
}
