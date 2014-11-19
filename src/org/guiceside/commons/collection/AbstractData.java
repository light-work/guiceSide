package org.guiceside.commons.collection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 抽象的参数存储类<br/>
 * 继承至LinkedHashMap
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 * @see java.util.LinkedHashMap
 **/
public class AbstractData extends LinkedHashMap<String, Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;

	public AbstractData() {
		super();

	}

	public AbstractData(int arg) {
		super(arg);
		name = null;

	}

	public AbstractData(int arg, float arg1) {
		super(arg, arg1);
		name = null;

	}

	public AbstractData(int arg, float arg1, boolean arg2) {
		super(arg, arg1, arg2);
		name = null;

	}

	
	public AbstractData(Map<String,Object> arg) {
		super(arg);
		name = null;

	}
}
