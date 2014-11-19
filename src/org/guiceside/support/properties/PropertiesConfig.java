package org.guiceside.support.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-11
 *
 **/
public class PropertiesConfig {
	private Properties properties = null;

	
	/**
	 * 
	 */
	public PropertiesConfig(String fileName) {
		loadProperties(fileName);
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public Set<Object> getKeySet() {
		if(properties==null)
			return null;
		return properties.keySet();
	}
	
	/**
	 * 加载配置文件
	 * @param fileName
	 */
	public void loadProperties(String fileName) {
		InputStream is = this.getClass().getResourceAsStream("/" + fileName);
		try {
			properties=new Properties();
			properties.load(is);
		} catch (IOException e) {
			throw new PropertiesConfigException("properties file [["
					+ fileName + "]] not exist");
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getString(String key) {
		if (properties == null)
			return null;
		Object o = properties.get(key);
		if (o == null)
			throw new PropertiesConfigException("properties file key [[" + key
					+ "]] not exist");
		return o.toString();
	}
	
	public void setString(String key,String value) {
		if (properties == null)
			return;
		properties.setProperty(key, value);
	}

	public int getInt(String key) {
		if (properties == null)
			return 0;
		Object o = properties.get(key);
		if (o == null)
			throw new PropertiesConfigException("properties file key [[" + key
					+ "]] not exist");
		int i = Integer.parseInt(o.toString());
		return i;
	}
	
	

	public long getLong(String key) {
		if (properties == null)
			return 0;
		Object o = properties.get(key);
		if (o == null)
			throw new PropertiesConfigException("properties file key [[" + key
					+ "]] not exist");

		long l = Long.parseLong(o.toString());
		return l;
	}

	public float getFloat(String key) {
		if (properties == null)
			return 0;
		Object o = properties.get(key);
		if (o == null)
			throw new PropertiesConfigException("properties file key [[" + key
					+ "]] not exist");
		float f = Float.parseFloat(o.toString());
		return f;
	}

	public double getDouble(String key) {
		if (properties == null)
			return 0;
		Object o = properties.get(key);
		if (o == null)
			throw new PropertiesConfigException("properties file key [[" + key
					+ "]] not exist");
		double d = Double.parseDouble(o.toString());
		return d;
	}

}
