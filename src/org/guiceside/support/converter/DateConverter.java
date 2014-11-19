package org.guiceside.support.converter;


import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.DateFormatUtil;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-10-15
 *
 **/
public class DateConverter implements Converter{
	private static final Log log = LogFactory.getLog(DateConverter.class);

	

	public DateConverter() {
		
	}

	private boolean matcher(String regEx,String value){
		Pattern p=Pattern.compile(regEx);
		Matcher m=p.matcher(value);
		boolean rs=m.matches();
		return  rs;
	}
	public Object convert(Class arg0, Object value) {
		try {
			if(value!=null){
				if (value instanceof Date) {
					value=DateFormatUtil.parse(DateFormatUtil.format((Date)value, DateFormatUtil.YEAR_MONTH_DAY_PATTERN), DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
					return value;
				}
				String dateStr = BeanUtils.convertValue(value, String.class);
				if (StringUtils.isNotBlank(dateStr)) {
					String regExY_M_D="[0-9]{1,4}-[0-12]{1,2}-[0-31]{1,2}"; //年月日 yyyy-MM-dd
					String regExY_M_D_h="[0-9]{1,4}-[0-12]{1,2}-[0-31]{1,2}:[0-23]{1,2}"; //年月日 yyyy-MM-dd:hh24
					String regExY_M_D_h_m="[0-9]{1,4}-[0-12]{1,2}-[0-31]{1,2}:[0-23]{1,2}:[0-59]{1,2}"; //年月日 yyyy-MM-dd:hh24:mm
					String regExY_M_D_h_m_s="[0-9]{1,4}-[0-12]{1,2}-[0-31]{1,2}:[0-23]{1,2}:[0-59]{1,2}:[0-59]{1,2}"; //年月日 yyyy-MM-dd:hh24:mm:ss
					String regExh_m_s="[0-23]{1,2}:[0-59]{1,2}:[0-59]{1,2}";//hh24:mm:ss
					if(matcher(regExY_M_D, dateStr)){
						value= DateFormatUtil.parse(dateStr, DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
					}else if(matcher(regExY_M_D_h_m_s, dateStr)){
						value= DateFormatUtil.parse(dateStr, DateFormatUtil.YMDHMS_PATTERN);
					}else if(matcher(regExY_M_D_h, dateStr)){
						value= DateFormatUtil.parse(dateStr, DateFormatUtil.YMDH_PATTERN);
					}else if(matcher(regExY_M_D_h_m, dateStr)){
						value= DateFormatUtil.parse(dateStr, DateFormatUtil.YMDHM_PATTERN);
					}else if(matcher(regExh_m_s, dateStr)){
						value= DateFormatUtil.parse(dateStr, DateFormatUtil.HOUR_MINUTE_SECOND_PATTERN);
					}else{
						value= DateFormatUtil.parse(dateStr, DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
					}
					regExY_M_D=null;
					regExY_M_D_h=null;
					regExY_M_D_h_m=null;
					regExY_M_D_h_m_s=null;
					regExh_m_s=null;
					return value;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
