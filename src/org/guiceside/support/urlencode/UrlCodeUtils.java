package org.guiceside.support.urlencode;

import org.guiceside.commons.lang.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2009-3-1
 *
 **/
public class UrlCodeUtils {

	public static String getEncodeUrl(String url,String pattern,UrlCode urlCode){
		String encodeUrl=null;
		if(urlCode==null){
			return url;
		}
		if(StringUtils.isBlank(pattern)){
			return url;
		}
		try {
			URI uri=new URI(url);
			String path=uri.getPath();
			if(StringUtils.isNotBlank(path)){
				String[] patterns=pattern.split(",");
				for(String pt:patterns){
					if(pt.equals("/*")){
						encodeUrl= urlCode.encode(url);
						break;
					}else{
						int i=path.lastIndexOf(pt);
						if(i==-1){
							continue;
						}
						String ec=path.substring(0,i);
						encodeUrl=urlCode.encode(ec);
						encodeUrl+=pt;
						String query=uri.getQuery();
						if(StringUtils.isNotBlank(query)){
							encodeUrl+="?"+query;
							//encodeUrl+="?"+urlCode.encode(query);
						}
						break;
					}
				}
			}
		} catch (URISyntaxException e) {
			return null;
		}
		return encodeUrl;
	}
	
	public static String getDecodeUrl(String url,String pattern,UrlCode urlCode){
		String decodeUrl=null;
		if(urlCode==null){
			return url;
		}
		if(StringUtils.isBlank(pattern)){
			return url;
		}
		try {
			URI uri=new URI(url);
			String path=uri.getPath();
			if(StringUtils.isNotBlank(path)){
				String[] patterns=pattern.split(",");
				for(String pt:patterns){
					if(pt.equals("/*")){
						decodeUrl= urlCode.decode(url);
						break;
					}else{
						int i=path.lastIndexOf(pt);
						if(i==-1){
							continue;
						}
						String ec=path.substring(0,i);
						decodeUrl=urlCode.decode(ec);
						decodeUrl+=pt;
						String query=uri.getQuery();
						if(StringUtils.isNotBlank(query)){
							decodeUrl+="?"+query;
							//decodeUrl+="?"+urlCode.decode(query);
						}
						break;
					}
				}
			}
		} catch (URISyntaxException e) {
			return null;
		}
		return decodeUrl;
	}
	
}
