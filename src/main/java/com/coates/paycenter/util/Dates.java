package com.coates.paycenter.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * <p> Title: DateUtils.java </p>
 * <p> Package com.shenpinkj.utils </p>
 * <p> Description: TODO(获取时间所有操作) </p>
 * <p> Company: www.shenpinkj.cn </p> 
 * @author 牟超
 * @date 2017年10月24日下午2:14:18
 * @version 1.0
 */
public class Dates {
	private static volatile Dates dateUtils;
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String CHINESE_DATE_FORMAT_LINE = "yyyy-MM-dd";
	public static final String DATETIME_NOT_SEPARATOR = "yyyyMMddHHmmssSSS";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

	private Dates() {
	}

	public static Dates getDateUtils() {
		if (dateUtils == null) {
			synchronized (Dates.class) {
				if (dateUtils == null) {
					dateUtils = new Dates();
				}
			}
		}
		return dateUtils;
	}
	
	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2018年1月8日
	 * 方法描述：获取年
	 * @return yyyy
	 */
	public  String getCurrentYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return sdf.format(date);
	}

	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：获取当前时间
	 * 修改人：张玻
	 * @return yyyy-MM-dd
	 */
	public String getTodayDate() {
		SimpleDateFormat sdf= new SimpleDateFormat(CHINESE_DATE_FORMAT_LINE);
		Calendar c = Calendar.getInstance();
		return sdf.format(c.getTime());
	}
	public static void main(String[] args) {
		System.out.println(Dates.getDateUtils().getTodayDate());
	}
	
	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2018年1月4日
	 * 方法描述：获取当前时间戳
	 */
	public String timeStamp(){
		return String.valueOf(new Date().getTime());
	}
	
	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：获取指定时间格式的当前系统时间
	 * 修改人：张玻
	 * @param format
	 * @return
	 */
	public String getTodayDate(String format) {
		Calendar c = Calendar.getInstance();
		sdf.applyPattern(format);
		return sdf.format(c.getTime());
	}
	
	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年11月2日
	 * 方法描述：获取指定时间格式的当前系统时间  yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public  String getTodayDate1(){
		 Date d = new Date();  
		return sdf.format(d);
	}
	
	
	
	
	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：时间毫秒转时间格式
	 * 修改人：张玻
	 * @param time
	 * @param format
	 * @return
	 */
	public String longToString(long time, String format) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		sdf.applyPattern(format);
		return sdf.format(c.getTime());
	}

	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：获取指定分钟数以前的时间
	 * 修改人：张玻
	 * @param minute
	 * @param format
	 * @return
	 */
	public String getTimeBeformMinute(int minute, String format) {
		Calendar c = Calendar.getInstance();
		sdf.applyPattern(format);
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) - minute);
		return sdf.format(c.getTime());
	}

	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：获取指定分钟数以后的时间 默认格式：yyyy-MM-dd HH:mm:ss
	 * 修改人：张玻
	 * @param minute
	 * @param format
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public String getTimeAfterMinute(int minute, String format) {
		Calendar c = Calendar.getInstance();
		sdf.applyPattern(format);
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + minute);
		return sdf.format(c.getTime());
	}

	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：获取指定分钟数以后的时间
	 * 修改人：张玻
	 * @param time
	 * @param minute
	 * @param format
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public String getTimeAfterMinute(String time, int minute, String format) {
		Calendar c = Calendar.getInstance();
		sdf.applyPattern(format);
		try {
			c.setTime(sdf.parse(time));
			c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + minute);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(c.getTime());
	}

	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：获取今天以前的指定天数日期
	 * 修改人：张玻
	 * @param day
	 * @param format
	 * @return 默认格式yyyy-MM-dd
	 */
	public String getBeforeDay(int day, String format) {
		Calendar c = Calendar.getInstance();
		sdf.applyPattern(format);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - day);
		return sdf.format(c.getTime());
	}
	
	public  String getBillDate(int day){
		Date dt=new Date();
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DATE, day);
		String s=DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime()).replaceAll("-","");
		return s;
	}

	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：计算出两个时间得分钟数差值
	 * 
	 * @param c1 大
	 * @param c2 小
	 * @return
	 */
	public int getMinutePoor(String c1, String c2) {
		try {
			Calendar ca1 = Calendar.getInstance();
			ca1.setTime(sdf.parse(c1));
			Calendar ca2 = Calendar.getInstance();
			ca2.setTime(sdf.parse(c2));
			long sub = ca1.getTimeInMillis() - ca2.getTimeInMillis();
			long m = sub / 1000 / 60;
			return Math.abs(Integer.parseInt(m + ""));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：计算出两个时间得秒数差值
	 * 
	 * @param c1 大
	 * @param c2  小
	 * @return
	 */
	public int getSecondsPoor(String c1, String c2) {
		try {
			Calendar ca1 = Calendar.getInstance();
			ca1.setTime(sdf.parse(c1));
			Calendar ca2 = Calendar.getInstance();
			ca2.setTime(sdf.parse(c2));
			long sub = ca1.getTimeInMillis() - ca2.getTimeInMillis();
			long m = sub / 1000;
			return Math.abs(Integer.parseInt(m + ""));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：获取当前时间毫秒数
	 * 
	 * @param c1  大
	 * @param c2  小
	 * @return
	 */
	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：获取两个日期间隔天数
	 * 
	 * @param c1  大
	 * @param c2  小
	 * @return
	 */
	public int daysBetween(String bdate, String edate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(bdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(edate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述：获取指定天数以前的时间
	 * 
	 * @param day 天数
	 * @return
	 */
	public String getBeforeDate(int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - day);
		sdf.applyPattern(CHINESE_DATE_FORMAT_LINE);
		return sdf.format(c.getTime());
	}

	/**
	 * 
	 * 创 建 人：牟 超 
	 * 创建时间：2017年10月24日 
	 * 方法描述： 获取多少分钟数以前时间
	 * 
	 * @param minute
	 * @return
	 */
	public String getBeforeTime(int minute) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) - minute);
		return sdf.format(c.getTime());
	}
	
	
	/**
	 * 
	 * 创 建 人：张玻
	 * 创建时间：2017年11月7日 
	 * 方法描述：字符串转换时间格式
	 * 
	 * @param dateString
	 * @param pattern
	 * @return
	 */
	public Date parse(String dateString, String pattern) {
		sdf.applyPattern(pattern);
		try {
			return sdf.parse(dateString);
		} catch (Exception e) {
			throw new RuntimeException("parse String[" + dateString + "] to Date faulure with pattern[" + pattern
					+ "].");
		}
	}
	
	/**
	 * 创  建  人：张 玻
	 * 创建时间：2017年11月7日下午1:54:39
	 * 方法描述：增加多少年
	 * @param date	当前时间
	 * @param years 增加的年数
	 * @return
	 */
	public Date addYear(Date date, int years) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, years);
		return c.getTime();
	}

	/**
	 * 创  建  人：张 玻
	 * 创建时间：2017年11月7日下午1:55:07
	 * 方法描述：增加多少月
	 * @param date	当前时间
	 * @param months 增加的月数
	 * @return
	 */
	public static Date addMonth(Date date, int months) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		return c.getTime();
	}

	/**
	 * 创  建  人：张 玻
	 * 创建时间：2017年11月7日下午1:56:25
	 * 方法描述：增加多少周
	 * @param date	当前时间
	 * @param weeks	增加的周数
	 * @return
	 */
	public static Date addWeek(Date date, int weeks) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.WEEK_OF_YEAR, weeks);
		return c.getTime();
	}
	
	/**
	 * 创  建  人：张 玻
	 * 创建时间：2017年11月7日下午1:57:34
	 * 方法描述：增加多少天
	 * @param date	当前时间
	 * @param days	增加的天数
	 * @return
	 */
	public static Date addDay(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, days);
		return c.getTime();
	}
	
}
