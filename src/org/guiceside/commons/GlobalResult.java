package org.guiceside.commons;

import org.guiceside.web.annotation.Dispatcher;

import java.io.Serializable;

/**
 * <p>
 * guiceSide.xml global-results节点JavaBean
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 **/
public class GlobalResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3516800966450920244L;

	private String name;
	
	private String path;
	
	private Dispatcher type;

	public Dispatcher getType() {
		return type;
	}

	public void setType(Dispatcher type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}	
