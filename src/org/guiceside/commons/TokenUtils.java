package org.guiceside.commons;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * <p>
 * 生成Token工具类
 * </p>
 * @author zhenjia  <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 **/
public class TokenUtils {

	private final static String str = "1234567890abcdefghijklmnopqrstuvwxyz";
	private final static int pixLen = str.length();
	private static volatile int pixOne = 0;
	private static volatile int pixTwo = 0;
	private static volatile int pixThree = 0;
	private static volatile int pixFour = 0;


	private static volatile int base64One = 0;
	private static volatile int base64Two = 0;
	private static volatile int base64Three = 0;
	private static volatile int base64Four = 0;
	private static volatile int base64Five = 0;
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
	 * 获取SESSIONID前14位字符
	 * 
	 * @param sessionId
	 * @return 返回sessionId前14为字符
	 */
	private static String getSessionId(String sessionId){
		return sessionId.substring(0,14);
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
	 * 通过SessionId 混淆生成token唯一标识
	 * 
	 * @param sessionId
	 * @return 返回混淆生成token唯一标识
	 */
	public static String getToken(String sessionId){
		String session=getSessionId(sessionId);
		String currentTimeMillis=getCurrentTimeMillis();
		currentTimeMillis=replenish(currentTimeMillis);
		String strRnd=getRandom();
		session+=getCHAR(currentTimeMillis, 0, 2);
		session+=getCHAR(strRnd, 0, 2);
		session+=getCHAR(currentTimeMillis, 2, 4);
		session+=getCHAR(strRnd, 2, 4);
		session+=getCHAR(currentTimeMillis, 4, 6);
		currentTimeMillis=getCurrentTimeMillis();
		currentTimeMillis=replenish(currentTimeMillis);
		strRnd=getRandom();
		session+=getCHAR(strRnd, 0, 2);
		session+=getCHAR(currentTimeMillis, 4, 6);
		session+=getCHAR(currentTimeMillis, 0, 2);
		session+=getCHAR(strRnd, 2, 4);
		session+=getCHAR(currentTimeMillis, 2, 4);
        Base64Utils.encode(session);
		return session;
	}

	/**
	 *
	 * UUID
	 *
	 * @return UUID(Universally Unique Identifier)全局唯一标识符
	 */
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		return 	uuid.toString();
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

	final public synchronized static String generate() {
		StringBuilder sb = new StringBuilder();// 创建一个StringBuilder
		pixFour++;
		if (pixFour == pixLen) {
			pixFour = 0;
			pixThree++;
			if (pixThree == pixLen) {
				pixThree = 0;
				pixTwo++;
				if (pixTwo == pixLen) {
					pixTwo = 0;
					pixOne++;
					if (pixOne == pixLen) {
						pixOne = 0;
					}
				}
			}
		}
		return sb.append(str.charAt(pixOne)).append(str.charAt(pixTwo)).append(str.charAt(pixThree)).append(str.charAt(pixFour)).toString();
	}
	final public synchronized static String generateBase(String str) {
		StringBuilder sb = new StringBuilder();// 创建一个StringBuilder
		int baseLength = str.length();
		base64Five++;
		if (base64Five == baseLength) {
			base64Five = 0;
			base64Four++;
			if (base64Four == baseLength) {
				base64Four = 0;
				base64Three++;
				if (base64Three == baseLength) {
					base64Three = 0;
					base64Two++;
					if (base64Two == baseLength) {
						base64Two = 0;
						base64One++;
						if (base64One == baseLength) {
							base64One = 0;
						}
					}
				}
			}
		}
		return sb.append(str.charAt(base64One)).append(str.charAt(base64Two)).append(str.charAt(base64Three)).append(str.charAt(base64Four)).append(str.charAt(base64Five)).toString();
	}
}
