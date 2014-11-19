package org.guiceside.commons;

import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.NumberUtils;

import java.util.Date;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @since JDK1.5
 * @version 1.0 2008-9-18
 *
 **/
public class TimeUtils {

    public static void main(String[] strs) throws Exception{
        Date dd= DateFormatUtil.parse("2014-02-12 8:21:00", DateFormatUtil.YMDHMS_PATTERN);
        Date ee=DateFormatUtil.parse("2014-02-12 22:21:00",DateFormatUtil.YMDHMS_PATTERN);
        System.out.println(dd.getTime());
        System.out.println(ee.getTime());
        System.out.println(TimeUtils.getHoursTimeDiff(dd.getTime(),ee.getTime()));
    }

    public static Double getHoursTimeDiff(long tStart, long tEnd) {
        long sec = 1000;
        long min = sec * 60;
        long hour = min * 60;
        long day = hour * 24;
        long month = day * 30;
        long year = 365 * day;
        long diffInMills = tEnd - tStart;
        Double hours=0.0d;
        if(diffInMills<sec){
            hours=0.0d;
        }else if (diffInMills < min){
            hours=0.0d;
        }else if (diffInMills < hour){
            long lmin = diffInMills / min;
            hours= NumberUtils.divide(lmin,60,1);
        }else if (diffInMills < day) {
            long lhour = diffInMills / hour;
            long lmin = (diffInMills - lhour * hour) / min;
            hours=NumberUtils.multiply(lhour,1,1);
            hours+= NumberUtils.divide(lmin,60,1);
        }
        return hours;
    }
	public static String getTimeDiff(long tStart, long tEnd) {
		long sec = 1000;
		long min = sec * 60;
		long hour = min * 60;
		long day = hour * 24;
		long month = day * 30;
		long year = 365 * day;

		// UTC is temporary realized to hold the time in miliss passed from ..
		// 1970
		long diffInMills = tEnd - tStart;
		if(diffInMills<sec){
			return String.valueOf(diffInMills) + " [ms]";
		}else if (diffInMills < min) {
			return String.valueOf(diffInMills / sec) + " [s]";
		} else if (diffInMills < hour) {
			long lmin = diffInMills / min;
			long lsec = (diffInMills - lmin * min) / sec;
			return String.valueOf(lmin) + " [min] " + String.valueOf(lsec)
					+ " [s]";
		} else if (diffInMills < day) {
			long lhour = diffInMills / hour;
			long lmin = (diffInMills - lhour * hour) / min;
			long lsec = (diffInMills - lhour * hour - lmin * min) / sec;
			return String.valueOf(lhour) + " [h] " + String.valueOf(lmin)
					+ " [min] " + String.valueOf(lsec) + " [s]";
		} else if (diffInMills < month) {
			long lday = diffInMills / day;
			long lhour = (diffInMills - lday * day) / hour;
			long lmin = (diffInMills - lday * day - lhour * hour) / min;
			long lsec = (diffInMills - lday * day - lhour * hour - lmin * min)
					/ sec;
			return String.valueOf(lday) + " [d] " + String.valueOf(lhour)
					+ " [h] " + String.valueOf(lmin) + " [min] "
					+ String.valueOf(lsec) + " [s]";
		} else if (diffInMills < year) {
			long mn = diffInMills / month;
			long lday = (diffInMills - mn * month) / day;
			long lhour = (diffInMills - mn * month - lday * day) / hour;
			long lmin = (diffInMills - mn * month - lday * day - lhour * hour)
					/ min;
			long lsec = (diffInMills - mn * month - lday * day - lhour * hour - lmin
					* min)
					/ sec;
			return String.valueOf(mn) + " [m] " + String.valueOf(lday)
					+ " [d] " + String.valueOf(lhour) + " [h] "
					+ String.valueOf(lmin) + " [min] " + String.valueOf(lsec)
					+ " [s]";
		} else { // if (diffInMills>=year)
			long lyear = diffInMills / year;
			long mn = (diffInMills - lyear * year) / month;
			long lday = (diffInMills - lyear * year - mn * month) / day;
			long lhour = (diffInMills - lyear * year - mn * month - lday * day)
					/ hour;
			long lmin = (diffInMills - lyear * year - mn * month - lday * day - lhour
					* hour)
					/ min;
			long lsec = (diffInMills - lyear * year - mn * month - lday * day
					- lhour * hour - lmin * min)
					/ sec;
			return String.valueOf(lyear) + " [y] " + String.valueOf(mn)
					+ " [m] " + String.valueOf(lday) + " [d] "
					+ String.valueOf(lhour) + " [h] " + String.valueOf(lmin)
					+ " [min] " + String.valueOf(lsec) + " [s]";
		}
	}
}
