package org.guiceside.commons;

import org.apache.commons.lang.StringUtils;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-12-3
 *
 **/
public class FileIdUtils {

	/**
	 * 
	 * 随即获得4位数字
	 * 
	 * @return 返回随即生成小于10000的随即数
	 */
	private static String getRandom(){
		double dblTmp;
		for (dblTmp = Math.random() * 100000.0; dblTmp < 10000.0; dblTmp = Math
				.random() * 100000.0) {
			/* empty */
		}
		String strRnd = String.valueOf(dblTmp).substring(0, 4);
		return strRnd;
	}
	
	/**
	 * 随即获得n位数字
	 * @return 返回随即生成小于1000000的随即数
	 */
	private static String getTempRandom(int n){
		double dblTmp;
		for (dblTmp = Math.random() * 10000000.0; dblTmp < 1000000.0; dblTmp = Math
				.random() * 10000000.0) {
			/* empty */
		}
		String strRnd = String.valueOf(dblTmp).substring(0, n);
		return strRnd;
	}
	
	/**
	 * 避免异常方法 补足不足6为数字
	 * @param currentTimeMillis
	 * @return
	 */
	private static String replenish(String currentTimeMillis){
		if(StringUtils.isNotBlank(currentTimeMillis)){
			int clength=currentTimeMillis.length();
			if(clength<6){
				int dim=6-clength;
				String tempRandom=getTempRandom(dim);
				currentTimeMillis+=tempRandom;
			}
		}
		return currentTimeMillis;
	}
	
	/**
	 * 
	 * 获取当前时间戳后6位数字
	 * 
	 * @return 返回当前时间戳后6位数字
	 */
	private static String getCurrentTimeMillis(){
		String currentTimeMillis=String.valueOf(System.currentTimeMillis()).substring(7);
		return currentTimeMillis;
	}
	
	/**
	 * 
	 * 将2位数字转换为A-Z字母或者数字
	 * 
	 * @param str
	 * @param benginIndex
	 * @param endIndex
	 * @return A-Z字母或者数字
	 */
	private static String getCHAR(String str,int benginIndex,int endIndex){
		String temp=str.substring(benginIndex,endIndex);
		int charNumber=Integer.valueOf(temp).intValue();
		if(charNumber>=65&&charNumber<=90){
			return String.valueOf((char)charNumber);
		}
		return temp;
	}
	
	/**
	 * 
	 * 通过SessionId 混淆生成token唯一标识
	 * 
	 * @param sessionId
	 * @return 返回混淆生成token唯一标识
	 */
	public static String getFileUnId(){
		String currentTimeMillis=getCurrentTimeMillis();
		currentTimeMillis=replenish(currentTimeMillis);
		String strRnd=getRandom();
		StringBuilder sb=new StringBuilder();
		sb.append(getCHAR(currentTimeMillis, 0, 2));
		sb.append(getCHAR(strRnd, 0, 2));
		sb.append(getCHAR(currentTimeMillis, 2,4));
		sb.append(getCHAR(strRnd, 2,4));
		sb.append(getCHAR(currentTimeMillis, 4,6));
		currentTimeMillis=getCurrentTimeMillis();
		currentTimeMillis=replenish(currentTimeMillis);
		strRnd=getRandom();
		sb.append(getCHAR(currentTimeMillis, 4,6));
		sb.append(getCHAR(currentTimeMillis, 2,4));
		sb.append(getCHAR(strRnd, 2,4));
		sb.append(getCHAR(currentTimeMillis, 0, 2));
		sb.append(getCHAR(strRnd, 0, 2));
		currentTimeMillis=getCurrentTimeMillis();
		currentTimeMillis=replenish(currentTimeMillis);
		strRnd=getRandom();
		sb.append(getCHAR(currentTimeMillis, 0, 2));
		sb.append(getCHAR(strRnd, 2,4));
		sb.append(getCHAR(currentTimeMillis, 4,6));
		sb.append(getCHAR(currentTimeMillis, 2,4));
		sb.append(getCHAR(strRnd, 0, 2));
		return sb.toString();
	}
}
