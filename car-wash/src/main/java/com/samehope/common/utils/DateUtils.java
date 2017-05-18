package com.samehope.common.utils;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat yyyymmddhhmmsssss = new SimpleDateFormat("yyyyMMddhhmmsssss");

	public static String now() {
		return sdf.format(new Date());
	}

	public static String dateString() {
		return dateSdf.format(new Date());
	}

	public static String format(Date date) {
		if (null == date)
			return null;

		return sdf.format(date);
	}
	
	public static Date format(String dateStr) {
		if (StringUtils.isBlank(dateStr))
			return null;
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据日期格式把字符串转日期
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String dateToStrByFormat(Date date,DateFormat dateFormat) {
		return dateFormat.format(date);
	}

	/**
	 * 日期转字符串型
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date strToDate(String dateStr) {
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	 /** 
     * 两个时间相差距离多少天多少小时多少分多少秒 
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
     * @return String 返回值为：xx天xx小时xx分xx秒 
     */  
    public static String getDistanceTime(String beginDateStr, String endDateStr) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date one;  
        Date two;  
        long day = 0;  
        long hour = 0;  
        long min = 0;  
        long sec = 0;  
        try {  
            one = df.parse(beginDateStr);  
            two = df.parse(endDateStr);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }  
            day = diff / (24 * 60 * 60 * 1000);  
            hour = (diff / (60 * 60 * 1000) - day * 24);  
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }
		if (day > 0) {
			return day + "天" + hour + "小时" + min + "分" + sec + "秒";
		} else {
			return hour + "小时" + min + "分" + sec + "秒";
		}
    } 
    
    /**
     * 两个时间相差距离多少天多少小时多少分
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：1990-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分
     */
    public static String getDurationTime(String beginDateStr, String endDateStr) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date one;  
        Date two;  
        long day = 0;  
        long hour = 0;  
        long min = 0;  
        long sec = 0;  
        try {  
            one = df.parse(beginDateStr);  
            two = df.parse(endDateStr);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }  
            day = diff / (24 * 60 * 60 * 1000);  
            hour = (diff / (60 * 60 * 1000) - day * 24);  
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }
		if (day > 0) {
			if(sec > 0) {
				return day + "天" + hour + "小时" + (min+1) + "分";
			}
			else {
				return day + "天" + hour + "小时" + min + "分";
			}
		} else {
			if(hour > 0){
				if(sec > 0) {
					return hour + "小时" + (min+1) + "分";
				}
				else {
					return hour + "小时" + min + "分";
				}
			}
			else {
				if(sec > 0) {
					return (min+1) + "分";
				}
				else {
					return min + "分";
				}
			}
		}
    } 
    
	/**
	 * 统计两个日期之间包含的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDayDiff(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new InvalidParameterException("date1 and date2 cannot be null!");
		}
		long millSecondsInOneDay = 24 * 60 * 60 * 1000;
		return (int) ((date1.getTime() - date2.getTime()) / millSecondsInOneDay);
	}

	
	/** 
     * 两个时间相差多少小时
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
     */  
    public static long getDistanceTimes(String beginDateStr, String endDateStr) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date one;  
        Date two;  
        long hour = 0;  
        try {  
            one = df.parse(beginDateStr);  
            two = df.parse(endDateStr);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }  
            hour = (diff / (60 * 60 * 1000));  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return hour;  
    } 
    
	/** 
     * 两个时间相差多少秒（time2-time1）
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00 
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00 
     */  
    public static long getDistanceSecond(String beginDateStr, String endDateStr) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date one;  
        Date two;  
        long second = 0;  
        try {  
            one = df.parse(beginDateStr);  
            two = df.parse(endDateStr);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff = time2 - time1;  
            second = (diff / (1000));  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return second;  
    }  
    
	/** 
     * 某个日期加上n小时n天n月n年
     * @param date 起始时间 
     * @param year 年 
     * @param month 月 
     * @param day 日 
     * @param hour 小时 
     */  
    public static Date putOff(Date date, int year , int month , int day ,int hour ) {  
		Calendar ca=Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.YEAR,year);
		ca.add(Calendar.MONTH,month);
		ca.add(Calendar.DATE,day);
		ca.add(Calendar.HOUR,hour);
		date = ca.getTime();
        return date;  
    }
	
    /**
	 * 获取当前时间 yyyyMMddHHmmss
	 * @return String
	 */ 
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}
	
	/**
	 * 当前日历，这里用中国时间表示
	 * 
	 * @return 以当地时区表示的系统当前日历
	 */
	public static Calendar getCalendar() {
		return Calendar.getInstance();
	}
	
	/**
	 * 获取时间字符串
	 */
	public static String getDataString(SimpleDateFormat formatstr) {
		return formatstr.format(getCalendar().getTime());
	}

	
	public static void main(String[] args) throws ParseException {
		System.out.println(getDistanceSecond("2017-03-08 18:20:59", "2017-03-08 18:20:00"));
	}

}
