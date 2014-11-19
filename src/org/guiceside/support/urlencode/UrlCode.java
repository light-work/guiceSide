package org.guiceside.support.urlencode;
/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2009-2-27
 *
 **/
public interface UrlCode {
	public String encode(String url);
	
	public String encode(String url, String characterSet);
	
	public String decode(String url);
	
	public String decode(String url, String characterSet);
}
