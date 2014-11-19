package org.guiceside.commons.lang;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 封装了一个Date类型转换辅助工具类
 * </p>
 * 
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 $Date:200808
 */
public class DateFormatUtil {

	/** 年月日模式字符串 */
	public static final String YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd";

    public static final String YEAR_MONTH_DAY_PATTERN_SHORT = "yy-MM-dd";

	/** 年模式字符串 */
	public static final String YEAR_PATTERN = "yyyy";

	/** 日模式字符串 */
	public static final String DAY_PATTERN = "dd";

	/** 月模式字符串 */
	public static final String MONTH_PATTERN = "MM";

    public static final String MDHMS_PATTERN = "MM-dd HH:mm:ss";

    public static final String MDHM_PATTERN = "MM-dd HH:mm";

	public static final String YEAR_MONTH_DAY = "yyyy.MM.dd";

	/** 时分秒模式字符串 */
	public static final String HOUR_MINUTE_SECOND_PATTERN = "HH:mm:ss";

    /** 时分秒模式字符串 */
	public static final String HOUR_MINUTE_PATTERN = "HH:mm";
	/** 年月日时分秒模式字符串 */
	public static final String YMDHMS_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/** 年月日时分模式字符串 */
	public static final String YMDHM_PATTERN = "yyyy-MM-dd HH:mm";

	/** 年月日时模式字符串 */
	public static final String YMDH_PATTERN = "yyyy-MM-dd HH";

    /** 年月日时分秒模式字符串 */
    public static final String YMDHMS_TIMESTAMP = "yyyyMMddHHmmss";

	private static final Logger log = Logger.getLogger(DateFormatUtil.class);

	/**
	 * 
	 * 传入日期类型参数 以String形式返回<br/>
	 * bShowTimePart_in代表是否包含时分秒
	 * 
	 * @param date
	 * @param bShowTimePart_in
	 * @return 返回data的String字符串
	 */
	public static String formatDate(Date date, boolean bShowTimePart_in) {

		if (bShowTimePart_in)

			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);

		else

			return (new SimpleDateFormat("yyyy-MM-dd")).format(date);

	}

	/**
	 * 
	 * 获取当前时间<br/>
	 * bShowTimePart_in代表是否包含时分秒
	 * 
	 * @param bShowTimePart_in
	 * @return 返回当前时间
	 */
	public static Date getCurrentDate(boolean bShowTimePart_in) {

		SimpleDateFormat f = null;
		Date d = null;
		try {
			if (bShowTimePart_in) {
				f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String s = f.format(currentDate());
				d = f.parse(s);
			} else {
				d = getCurrentDate();
			}
		} catch (Exception e) {
			log.error("获得当前时间类型格式化出错");
			log.error(e.getMessage());
		}
		return d;

	}

	public static String getCurrentDateFormat(String pattern) {
		String currentDate = currentDateFormat(pattern);
		return currentDate;
	}

	public static String getCurrentDateString(boolean bShowTimePart_in) {
		String currentDate = null;
		if (bShowTimePart_in) {
			currentDate = currentDateYMDHMS();
		} else {
			currentDate = currentDateDefaultString();
		}
		return currentDate;
	}

	/**
	 * 
	 * 从服务器获取当前时间。
	 * 
	 * @return 返回当前时间
	 * @throws java.sql.SQLException
	 *             获取数据库时间时发生错误
	 */
	public static Date currentDate() {
		return new Date();
	}

	/**
	 * 
	 * 从服务器获取当前时间并转换成默认字符串形式（yyyy-MM-dd）。
	 * 
	 * @return 返回当前时间的默认字符串形式（yyyy-MM-dd）
	 * @throws java.sql.SQLException
	 *             获取数据库时间时发生错误
	 */
	public static String currentDateDefaultString() {
		return format(currentDate(), YEAR_MONTH_DAY_PATTERN);
	}

	/**
	 * 
	 * 从服务器获取当前时间并转换成字符串形式（yyyy-MM-dd HH:mm:ss）。
	 * 
	 * @return 返回当前时间的默认字符串形式（yyyy-MM-dd HH:mm:ss）
	 * @throws java.sql.SQLException
	 *             获取数据库时间时发生错误
	 */
	public static String currentDateYMDHMS() {
		return format(currentDate(), YMDHMS_PATTERN);
	}

	public static String currentDateFormat(String pattern) {
		return format(currentDate(), pattern);
	}

	/**
	 * 
	 * 根据传入的日期格式化pattern将传入的日期格式化成字符串。
	 * 
	 * @param date
	 *            要格式化的日期对象
	 * @param pattern
	 *            日期格式化pattern
	 * @return 格式化后的日期字符串
	 */
	public static String format(final Date date, final String pattern) {
		if (date == null) {
			return "";
		}
		DateFormat df = null;
		if (StringUtils.isBlank(pattern)) {
			df = new SimpleDateFormat(YEAR_MONTH_DAY_PATTERN);
		} else {
			df = new SimpleDateFormat(pattern);
		}
		return df.format(date);
	}

	/**
	 * 从服务器获取当前时间以yyyy-MM-dd形式返回
	 * 
	 * @return 当前日期
	 * @throws java.text.ParseException
	 */
	public static Date getCurrentDate() throws ParseException {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String s = f.format(currentDate());
		return f.parse(s);
	}

	/**
	 * 
	 * 根据传入的日期格式化patter将传入的字符串转换成日期对象
	 * 
	 * @param dateStr
	 *            要转换的字符串
	 * @param pattern
	 *            日期格式化pattern
	 * @return 转换后的日期对象
	 * @throws java.text.ParseException
	 *             如果传入的字符串格式不合法
	 */
	public static Date parse(final String dateStr, final String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			log.warn("日期格式化错误 不能将指定字符串转换为指定格式");
			log.warn(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 将传入的字符串按照默认格式转换为日期对象（yyyy-MM-dd）
	 * 
	 * @param dateStr
	 *            要转换的字符串
	 * @return 转换后的日期对象
	 * @throws java.text.ParseException
	 *             如果传入的字符串格式不符合默认格式（如果传入的字符串格式不合法）
	 */
	public static Date parse(final String dateStr) throws ParseException {
		return parse(dateStr, YEAR_MONTH_DAY_PATTERN);
	}

	/**
	 * 
	 * 获取给定日期对象是星期几
	 * 
	 * @param date
	 *            日期对象
	 * @return 星期几
	 */
	public static int getDayInWeek(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取给定日期对象年份
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayInYear(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	/**
	 * 获取给定日期对象月份
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayInMonth(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH);
	}
    public static int getDayInMinute(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}

    public static int getDayInSecond(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.SECOND);
	}

	/**
	 * 获取给定日期对象日
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayInDay(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DATE);
	}

	public static int getDayInHour(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 
	 * 将某个日期增加指定年数，并返回结果。如果传入负数，则为减。
	 * 
	 * @param date
	 *            要操作的日期对象
	 * @param ammount
	 *            要增加年的数目
	 * @return 结果日期对象
	 */
	public static Date addYear(final Date date, final int ammount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, ammount);
		return c.getTime();
	}

	/**
	 * 
	 * 将某个日期增加指定月数，并返回结果。如果传入负数，则为减。
	 * 
	 * @param date
	 *            要操作的日期对象
	 * @param ammount
	 *            要增加月的数目
	 * @return 结果日期对象
	 */
	public static Date addMonth(final Date date, final int ammount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, ammount);
		return c.getTime();
	}

	/**
	 * 
	 * 将某个日期增加指定天数，并返回结果。如果传入负数，则为减。
	 * 
	 * @param date
	 *            要操作的日期对象
	 * @param ammount
	 *            要增加天的数目
	 * @return 结果日期对象
	 */
	public static Date addDay(final Date date, final int ammount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, ammount);
		return c.getTime();
	}

	/**
	 * 
	 * 将某个日期增加指定小时，并返回结果。如果传入负数，则为减。
	 * 
	 * @param date
	 *            要操作的日期对象
	 * @param ammount
	 *            要增加小时的数目
	 * @return 结果日期对象
	 */
	public static Date addHours(final Date date, final int ammount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, ammount);
		return c.getTime();
	}

    	/**
	 *
	 * 将某个日期增加指定小时，并返回结果。如果传入负数，则为减。
	 *
	 * @param date
	 *            要操作的日期对象
	 * @param ammount
	 *            要增加小时的数目
	 * @return 结果日期对象
	 */
	public static Date addMinute(final Date date, final int ammount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, ammount);
		return c.getTime();
	}



	public static int calendarMonthPlus( Date date1,  Date date2) {
		if (date1.after(date2)) {
			Date t = date1;
			date1 = date2;
			date2 = t;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(date1);
		Calendar end = Calendar.getInstance();
		end.setTime(date2);
		Calendar temp = Calendar.getInstance();
		temp.setTime(date2);
		temp.add(Calendar.DATE, 1);

		int y = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
		int m = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);

		if ((start.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {// 前后都不破月
			return y * 12 + m + 1;
		} else if ((start.get(Calendar.DATE) != 1)
				&& (temp.get(Calendar.DATE) == 1)) {// 前破月后不破月
			return y * 12 + m;
		} else if ((start.get(Calendar.DATE) == 1)
				&& (temp.get(Calendar.DATE) != 1)) {// 前不破月后破月
			return y * 12 + m;
		} else {// 前破月后破月
			return (y * 12 + m - 1) < 0 ? 0 : (y * 12 + m - 1);
		}
	}

	/**
	 * 
	 * 返回日期时间差 单位为天
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回日期相差天数
	 */
	public static long calendarDayPlus(final Date date1, final Date date2) {
		long milis = calendarMilisPlus(date1, date2);
		milis = milis / 1000 / 60 / 60 / 24;
		return milis;
	}

	/**
	 * 
	 * 返回日期时间差 单位为小时
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回日期相差小时数
	 */
	public static long calendarHourPlus(final Date date1, final Date date2) {
		long milis = calendarMilisPlus(date1, date2);
		milis = milis / 1000 / 60 / 60;
		return milis;
	}

	/**
	 * 
	 * 返回日期时间差 单位为分
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回日期相差多少分钟
	 */
	public static long calendarMinutePlus(final Date date1, final Date date2) {
		long milis = calendarMilisPlus(date1, date2);
		milis = milis / 1000 / 60;
		return milis;
	}

	/**
	 * 
	 * 返回日期时间差 单位为秒
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回日期相差多少秒
	 */
	public static long calendarSecondPlus(final Date date1, final Date date2) {
		long milis = calendarMilisPlus(date1, date2);
		milis = milis / 1000;
		return milis;
	}

	/**
	 * 返回日期时间差
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回long型时间差
	 */
	public static long calendarMilisPlus(final Date date1, final Date date2) {
		return date2.getTime() - date1.getTime();
	}

    public static int getActualMaximum(final Date date,final int type){
        Calendar   cal   =   Calendar.getInstance();
        cal.setTime(date);
        int  maxDay   =   cal.getActualMaximum(type);
        return maxDay;
    }

    public static int getActualMinimum(final Date date,final int type){
        Calendar   cal   =   Calendar.getInstance();
        cal.setTime(date);
        int  maxDay   =   cal.getActualMinimum(type);
        return maxDay;
    }

    public static int getLeastMaximum(final Date date,final int type){
        Calendar   cal   =   Calendar.getInstance();
        cal.setTime(date);
        int  maxDay   =   cal.getLeastMaximum(type);
        return maxDay;
    }

}
